package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.NewStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.NewStoriesViewModelState
import alessandro.vendramini.hackernews.util.PaginationState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewStoriesViewModel(private val repository: StoriesRepository): ViewModel() {

    private val hitsPerPage = 30

    private val _uiState = MutableStateFlow(value = NewStoriesViewModelState())
    val uiState: StateFlow<NewStoriesViewModelState> = _uiState.asStateFlow()

    var paginationState by mutableStateOf(PaginationState())
        private set

    fun onEvent(event: NewStoriesViewModelEvent) {
        when (event) {
            is NewStoriesViewModelEvent.FetchNewStoriesIds -> {
                fetchNewStoriesIds()
            }
            is NewStoriesViewModelEvent.FetchStoriesByIds -> {
                val startHit = paginationState.page * hitsPerPage
                val isEndReached = startHit > event.listOfIds.size

                if (startHit != 0) {
                    paginationState = paginationState.copy(
                        isLoading = !isEndReached,
                        endReached = isEndReached
                    )
                }

                if (!isEndReached) {
                    fetchStoryDetail(
                        listOfIds = event.listOfIds.subList(
                            startHit,
                            minOf(startHit + hitsPerPage, event.listOfIds.size),
                        )
                    )
                }
            }
        }
    }

    private fun fetchNewStoriesIds() {
        viewModelScope.launch {
            repository.getNewStoriesIds(
                onCallbackResource = { response ->
                    when (response) {
                        is ApiResource.Success -> {
                            _uiState.update { state ->
                                state.copy(newStoriesIds = response.data)
                            }
                        }
                        else -> {

                        }
                    }
                }
            )
        }
    }

    private fun fetchStoryDetail(listOfIds: List<Long>) {
        viewModelScope.launch {
            val storyArrayList: ArrayList<StoryModel> =
                uiState.value.newStories?.toCollection(ArrayList()) ?: arrayListOf()

            val deferredItems = listOfIds.map { id ->
                async {
                    repository.getStoryDetail(
                        id = id,
                        onCallbackResource = { response ->
                            when (response) {
                                is ApiResource.Success -> {
                                    response.data?.let { storyArrayList.add(it) }
                                }
                                else -> {

                                }
                            }
                        }
                    )
                }
            }
            deferredItems.awaitAll()

            _uiState.update { state ->
                state.copy(newStories = storyArrayList)
            }

            paginationState = paginationState.copy(
                isLoading = false,
                page = paginationState.page + 1,
            )
        }
    }
}