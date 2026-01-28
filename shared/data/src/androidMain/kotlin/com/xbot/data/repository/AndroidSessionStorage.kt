package com.xbot.data.repository

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.OnAccountsUpdateListener
import android.content.Context
import android.os.Bundle
import com.xbot.network.client.SessionStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class AndroidSessionStorage(
    private val context: Context
) : SessionStorage {

    private val accountManager by lazy { AccountManager.get(context) }
    private val accountType = "com.xbot.anilibriarefresh.account"
    private val authTokenType = "full_access"

    override val tokenFlow: Flow<String?> = callbackFlow {
        val listener = OnAccountsUpdateListener {
            trySend(getAccount())
        }
        accountManager.addOnAccountsUpdatedListener(listener, null, true)
        
        trySend(getAccount())
        
        awaitClose {
            accountManager.removeOnAccountsUpdatedListener(listener)
        }
    }.map { account ->
        account?.let { getAuthToken(it) }
    }.distinctUntilChanged()

    override suspend fun saveToken(username: String, token: String) {
        val accounts = accountManager.getAccountsByType(accountType)
        for (account in accounts) {
            removeAccount(account)
        }
        
        val account = Account(username, accountType)
        accountManager.addAccountExplicitly(account, null, null)
        accountManager.setAuthToken(account, authTokenType, token)
    }

    override suspend fun getToken(): String? {
        val account = getAccount() ?: return null
        return getAuthToken(account)
    }

    override suspend fun clearToken() {
        val accounts = accountManager.getAccountsByType(accountType)
        for (account in accounts) {
            removeAccount(account)
        }
    }

    private fun getAccount(): Account? {
        return accountManager.getAccountsByType(accountType).firstOrNull()
    }

    private suspend fun getAuthToken(account: Account): String? = suspendCancellableCoroutine { cont ->
        val accountManagerCallback = AccountManagerCallback<Bundle> { future ->
            try {
                val result = future.result
                val token = result.getString(AccountManager.KEY_AUTHTOKEN)
                cont.resume(token)
            } catch (e: Exception) {
                cont.resume(null)
            }
        }
        val future = accountManager.getAuthToken(account, authTokenType, null, false, accountManagerCallback, null)
        cont.invokeOnCancellation { future.cancel(true) }
    }

    private suspend fun removeAccount(account: Account): Boolean = suspendCancellableCoroutine { cont ->
        val accountManagerCallback = AccountManagerCallback<Bundle> { future ->
            try {
                val result = future.result
                val removed = result.getBoolean(AccountManager.KEY_BOOLEAN_RESULT)
                cont.resume(removed)
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
        val future = accountManager.removeAccount(account, null, accountManagerCallback, null)
        cont.invokeOnCancellation { future.cancel(true) }
    }
}
