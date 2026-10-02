package com.exertia.wikingo.domain.model

data class HeartState(
    val count: Int,
    val nextRechargeAtMillis: Long?
)
