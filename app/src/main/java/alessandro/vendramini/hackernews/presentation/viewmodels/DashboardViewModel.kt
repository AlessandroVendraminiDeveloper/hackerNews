package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.store.InternalDatastore
import alessandro.vendramini.hackernews.presentation.viewmodels.events.DashboardViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.DashboardViewModelState
import alessandro.vendramini.hackernews.util.gson
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val datastore: InternalDatastore
) : ViewModel() {

    companion object {
        var preferredIds: List<Long> by mutableStateOf(listOf())
            private set
    }

    private val _uiState = MutableStateFlow(value = DashboardViewModelState())
    val uiState: StateFlow<DashboardViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: DashboardViewModelEvent) {
        when (event) {
            is DashboardViewModelEvent.UpdateSelectedDestinationState -> {
                _uiState.update { state ->
                    state.copy(selectedDestination = event.selectedDestination)
                }
            }
            is DashboardViewModelEvent.UpdateIsNavigationBarVisibleState -> {
                _uiState.update { state ->
                    state.copy(isNavigationBarVisible = event.isVisible)
                }
            }
            is DashboardViewModelEvent.FetchPreferredStoriesIds -> {
                fetchPreferredStoriesIds()
            }
        }
    }

    private fun fetchPreferredStoriesIds() {
        viewModelScope.launch {
            datastore.getPreferredStories.collect { json ->
                try {
                    val typeToken = object : TypeToken<List<Long>>() {}.type
                    val list = gson.fromJson<List<Long>>(json, typeToken)
                    preferredIds = list
                } catch (e: Exception) { }
            }
        }
    }
}
