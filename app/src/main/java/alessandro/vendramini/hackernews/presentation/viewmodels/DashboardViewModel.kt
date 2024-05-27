package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import alessandro.vendramini.hackernews.presentation.viewmodels.events.DashboardViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.DashboardViewModelState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(private val newStoriesRepository: StoriesRepository) : ViewModel() {

    companion object {
        var newStoriesIds: List<Long>? by mutableStateOf(null)
            private set

        var topStoriesIds: List<Long>? by mutableStateOf(null)
            private set

        var bestStoriesIds: List<Long>? by mutableStateOf(null)
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
            is DashboardViewModelEvent.FetchNewStoriesIds -> {
                fetchNewStoriesIds()
            }
            is DashboardViewModelEvent.FetchTopStoriesIds -> {
                fetchTopStoriesIds()
            }
            is DashboardViewModelEvent.FetchBestStoriesIds -> {
                fetchBestStoriesIds()
            }
        }
    }

    private fun fetchNewStoriesIds() {
        viewModelScope.launch {
            newStoriesRepository.getNewStoriesIds { response ->
                when (response) {
                    is ApiResource.Success -> {
                       response.data?.let {
                           newStoriesIds = it
                       }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun fetchTopStoriesIds() {
        viewModelScope.launch {
            newStoriesRepository.getNewStoriesIds { response ->
                when (response) {
                    is ApiResource.Success -> {
                        response.data?.let {
                            topStoriesIds = it
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun fetchBestStoriesIds() {
        viewModelScope.launch {
            newStoriesRepository.getNewStoriesIds { response ->
                when (response) {
                    is ApiResource.Success -> {
                        response.data?.let {
                            bestStoriesIds = it
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }
}
