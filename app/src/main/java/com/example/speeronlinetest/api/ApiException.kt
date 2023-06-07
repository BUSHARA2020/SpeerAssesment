package com.example.speeronlinetest.api


class ApiException(val code: Int, message: String?) : Exception(message)
