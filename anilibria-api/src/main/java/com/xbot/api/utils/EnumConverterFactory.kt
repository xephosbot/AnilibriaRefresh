package com.xbot.api.utils

import kotlinx.serialization.SerialName
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EnumConverterFactory private constructor() : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<Enum<*>, String>? =
        if (type is Class<*> && type.isEnum) {
            Converter { enum ->
                try {
                    enum.javaClass.getField(enum.name)
                        .getAnnotation(SerialName::class.java)?.value
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    null
                } ?: enum.toString()
            }
        } else {
            null
        }

    companion object {
        @JvmStatic
        fun create(): EnumConverterFactory = EnumConverterFactory()
    }
}