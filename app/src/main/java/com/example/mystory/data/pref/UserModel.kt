package com.example.mystory.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)