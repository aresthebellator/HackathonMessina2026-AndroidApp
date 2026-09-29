package com.hackaton.wikitrainer.domain.model

data class HeartState(
    val count: Int,
    val nextRechargeAtMillis: Long?
)
