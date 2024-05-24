package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.NewStoriesRepository
import alessandro.vendramini.hackernews.data.api.services.NewStoriesService
import alessandro.vendramini.hackernews.presentation.viewmodels.events.DashboardViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.DashboardViewModelState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(private val newStoriesRepository: NewStoriesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(value = DashboardViewModelState())
    val uiState: StateFlow<DashboardViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: DashboardViewModelEvent) {
        when (event) {
            is DashboardViewModelEvent.UpdateSelectedDestinationState -> {
                _uiState.update { state ->
                    state.copy(selectedDestination = event.selectedDestination)
                }
            }
        }
    }

    fun aaa() {
        viewModelScope.launch {
            newStoriesRepository.getNewStoriesIds { response ->
                when (response) {
                    is ApiResource.Success -> {
                       response.data?.map { id ->
                           fetchDetail()
                       }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun fetchDetail() {

    }
}
