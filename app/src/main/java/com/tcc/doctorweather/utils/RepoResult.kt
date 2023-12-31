package com.tcc.doctorweather.utils


/**
 * Used to pass a result from a repository to a viewModel
 */
sealed class RepoResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : RepoResult<T>(data)
    class Error<T>(message: String, data: T? = null) : RepoResult<T>(data, message)
}