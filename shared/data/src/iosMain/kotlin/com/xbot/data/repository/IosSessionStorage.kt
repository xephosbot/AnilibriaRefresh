@file:OptIn(ExperimentalForeignApi::class)

package com.xbot.data.repository

import com.xbot.network.client.SessionStorage
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.CoreFoundation.CFDictionaryCreate
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecCopyErrorMessageString
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecItemUpdate
import platform.Security.errSecDuplicateItem
import platform.Security.errSecItemNotFound
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.darwin.OSStatus

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal class IosSessionStorage : SessionStorage {

    private val serviceName = "com.xbot.anilibriarefresh.account"

    private val listeners = mutableListOf<(String?) -> Unit>()
    private val mutex = Mutex()

    override val tokenFlow: Flow<String?> = callbackFlow {
        val listener: (String?) -> Unit = { token ->
            trySend(token)
        }
        
        mutex.withLock { listeners.add(listener) }
        send(getTokenInternal())
        
        awaitClose {
            launch { 
                mutex.withLock { listeners.remove(listener) }
            }
        }
    }
    .distinctUntilChanged()
    .flowOn(Dispatchers.IO)

    override suspend fun saveToken(username: String, token: String) {
        clearTokenInternal()
        
        val tokenData = token.toNSString().dataUsingEncoding(NSUTF8StringEncoding)
        if (addOrUpdateKeychainItem(username, tokenData)) {
             notifyListeners(token)
        }
    }

    override suspend fun getToken(): String? = getTokenInternal()

    override suspend fun clearToken() {
        clearTokenInternal()
        notifyListeners(null)
    }
    
    private suspend fun notifyListeners(token: String?) {
        mutex.withLock {
            listeners.forEach { it(token) }
        }
    }

    private fun getTokenInternal(): String? = memScoped {
        val cfValue = alloc<CFTypeRefVar>()
        val status = keyChainOperation(
            kSecReturnData to kCFBooleanTrue,
            kSecMatchLimit to kSecMatchLimitOne
        ) { SecItemCopyMatching(it, cfValue.ptr) }

        if (status == errSecItemNotFound) {
            return@memScoped null
        }
        status.checkError()
        
        val data = CFBridgingRelease(cfValue.value) as? NSData
        return@memScoped data?.let { NSString.create(it, NSUTF8StringEncoding) }?.toKString()
    }

    private fun clearTokenInternal() {
        val status = memScoped {
             keyChainOperation { SecItemDelete(it) }
        }
        if (status != errSecItemNotFound) {
            status.checkError()
        }
    }

    private fun addOrUpdateKeychainItem(account: String, value: NSData?): Boolean {
        return if (!addKeychainItem(account, value)) {
            updateKeychainItem(account, value)
        } else {
            true
        }
    }

    private fun addKeychainItem(account: String, value: NSData?): Boolean = cfRetain(account, value) { cfAccount, cfValue ->
        val status = memScoped {
            keyChainOperation(
                kSecAttrAccount to cfAccount,
                kSecValueData to cfValue
            ) { SecItemAdd(it, null) }
        }
        
        if (status == errSecDuplicateItem) {
            return@cfRetain false
        }
        status.checkError()
        return@cfRetain true
    }

    private fun updateKeychainItem(account: String, value: NSData?): Boolean = cfRetain(account, value) { cfAccount, cfValue ->
        val status = memScoped {
            keyChainOperation(
                kSecAttrAccount to cfAccount
            ) { query ->
                val attributes = cfDictionaryOf(kSecValueData to cfValue)
                val output = SecItemUpdate(query, attributes)
                CFBridgingRelease(attributes)
                output
            }
        }
        status.checkError(errSecItemNotFound)
        return@cfRetain status == 0
    }

    private inline fun MemScope.keyChainOperation(
        vararg input: Pair<CFStringRef?, CFTypeRef?>,
        operation: (query: CFDictionaryRef?) -> OSStatus,
    ): OSStatus {
        val cfService = CFBridgingRetain(serviceName)
        return try {
            val combined = mapOf(
                kSecClass to kSecClassGenericPassword,
                kSecAttrService to cfService
            ) + mapOf(*input)

            val query = cfDictionaryOf(combined)
            val output = operation(query)
            CFBridgingRelease(query)
            output
        } finally {
            CFBridgingRelease(cfService)
        }
    }
}

private fun OSStatus.checkError(vararg expectedErrors: OSStatus) {
    if (this != 0 && this !in expectedErrors) {
        val cfMessage = SecCopyErrorMessageString(this, null)
        val nsMessage = CFBridgingRelease(cfMessage) as? NSString
        val message = nsMessage?.toKString() ?: "Unknown error"
        println("Keychain error $this: $message")
    }
}

@Suppress("CAST_NEVER_SUCCEEDS")
internal fun String.toNSString() = this as NSString

@Suppress("CAST_NEVER_SUCCEEDS")
private fun NSString.toKString() = this as String

private inline fun <T> cfRetain(value: Any?, block: (CFTypeRef?) -> T): T {
    val cfValue = CFBridgingRetain(value)
    return try {
        block(cfValue)
    } finally {
        CFBridgingRelease(cfValue)
    }
}

private inline fun <T> cfRetain(value1: Any?, value2: Any?, block: (CFTypeRef?, CFTypeRef?) -> T): T {
    val cfValue1 = CFBridgingRetain(value1)
    val cfValue2 = CFBridgingRetain(value2)
    return try {
        block(cfValue1, cfValue2)
    } finally {
        CFBridgingRelease(cfValue1)
        CFBridgingRelease(cfValue2)
    }
}

private inline fun MemScope.cfDictionaryOf(map: Map<CFStringRef?, CFTypeRef?>): CFDictionaryRef? {
    val size = map.size
    val keys = allocArrayOf(*map.keys.toTypedArray())
    val values = allocArrayOf(*map.values.toTypedArray())
    return CFDictionaryCreate(
        kCFAllocatorDefault,
        keys.reinterpret(),
        values.reinterpret(),
        size.convert(),
        null,
        null
    )
}

private inline fun MemScope.cfDictionaryOf(vararg items: Pair<CFStringRef?, CFTypeRef?>): CFDictionaryRef? =
    cfDictionaryOf(mapOf(*items))
