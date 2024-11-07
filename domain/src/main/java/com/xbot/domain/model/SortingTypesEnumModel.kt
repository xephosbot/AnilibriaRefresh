package com.xbot.domain.model

//TODO: поле value здесь ненужно, имя модели в ед. числе (SortingType...)
enum class SortingTypesEnumModel(val value: String) {
    FRESH_AT_DESC("FRESH_AT_DESC"),
    FRESH_AT_ASC("FRESH_AT_ASC"),
    RATING_DESC("RATING_DESC"),
    RATING_ASC("RATING_ASC"),
    YEAR_DESC("YEAR_DESC"),
    YEAR_ASC("YEAR_ASC");
}