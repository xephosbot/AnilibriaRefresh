package com.xbot.domain.model

//TODO: поле value здесь ненужно, имя модели в ед. числе (ProductionStatus...)
enum class ProductionStatusesEnumModel(val value: String) {
    IS_IN_PRODUCTION("IS_IN_PRODUCTION"),
    IS_NOT_IN_PRODUCTION("IS_NOT_IN_PRODUCTION")
}
