package alessandro.vendramini.hackernews.presentation.viewmodel

import alessandro.vendramini.hackernews.presentation.viewmodel.event.DashboardViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodel.state.DashboardViewModelState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel() : ViewModel() {

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
}
