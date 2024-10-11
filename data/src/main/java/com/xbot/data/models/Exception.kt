package com.xbot.data.models

class NetworkException(message: String?) : Exception(message)
class SerializationException(message: String?, cause: Throwable) : Exception(message, cause)