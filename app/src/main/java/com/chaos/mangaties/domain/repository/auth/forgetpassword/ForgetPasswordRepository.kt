package com.chaos.mangaties.domain.repository.auth.forgetpassword

interface ForgetPasswordRepository {
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}