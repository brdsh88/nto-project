package ru.nto.guestmanagement.mobile.task.a3

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nto.guestmanagement.mobile.data.PassRepository
import ru.nto.guestmanagement.mobile.model.PassScreenState

/** A3. Логика загрузки и обновления данных пропуска. */
class PassLoadController(
    private val repository: PassRepository,
    private val scope: CoroutineScope,
) {
    private val mutableState = MutableStateFlow<PassScreenState>(PassScreenState.InitialLoading)
    val state: StateFlow<PassScreenState> = mutableState.asStateFlow()

    private var started = false
    private var loading = false

    fun start() {
        if (started || loading) return
        started = true
        load(initial = true)
    }

    fun refresh() {
        val current = mutableState.value
        if (loading || current !is PassScreenState.Content) return
        mutableState.value = current.copy(isRefreshing = true)
        load(initial = false)
    }

    fun retry() {
        val current = mutableState.value
        if (loading || current !is PassScreenState.InitialError) return
        mutableState.value = PassScreenState.InitialLoading
        load(initial = true)
    }

    private fun load(initial: Boolean) {
        loading = true
        scope.launch {
            try {
                val result = repository.load()
                val data = result.getOrNull()
                if (data != null) {
                    mutableState.value = PassScreenState.Content(
                        data = data,
                        isRefreshing = false,
                        isOffline = false,
                    )
                    return@launch
                }
                if (initial) {
                    val message = result.exceptionOrNull()?.message?.takeIf { it.isNotBlank() }
                        ?: DEFAULT_LOAD_ERROR
                    mutableState.value = PassScreenState.InitialError(message)
                } else {
                    val current = mutableState.value
                    if (current is PassScreenState.Content) {
                        mutableState.value = current.copy(isRefreshing = false, isOffline = true)
                    }
                }
            } finally {
                loading = false
            }
        }
    }

    private companion object {
        const val DEFAULT_LOAD_ERROR = "Не удалось загрузить пропуск"
    }
}
