package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.StoriesRepository
import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.data.store.InternalDatastore
import alessandro.vendramini.hackernews.presentation.viewmodels.events.BestStoriesViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.BestStoriesViewModelState
import alessandro.vendramini.hackernews.util.gson
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BestStoriesViewModel(
    private val repository: StoriesRepository,
    private val datastore: InternalDatastore,
): ViewModel() {

    private val hitsPerPage = 30

    private val _uiState = MutableStateFlow(value = BestStoriesViewModelState())
    val uiState: StateFlow<BestStoriesViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: BestStoriesViewModelEvent) {
        when (event) {
            is BestStoriesViewModelEvent.FetchBestStoriesIds -> {
                _uiState.update { state ->
                    state.copy(isRefreshing = event.isRefreshing)
                }
                fetchBestStoriesIds()
            }
            is BestStoriesViewModelEvent.FetchStoriesDetailByIds -> {
                val startHit = uiState.value.paginationState.page * hitsPerPage
                val isEndReached = startHit > event.listOfIds.size

                if (startHit != 0) {
                    _uiState.update { state ->
                        state.copy(
                            paginationState = state.paginationState.copy(
                                isLoading = !isEndReached,
                                endReached = isEndReached,
                            )
                        )
                    }
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
            is BestStoriesViewModelEvent.UpdatePreferredList -> {
                updatePreferredList(
                    id = event.id,
                    isAddedFromList = event.isAddFromList,
                )
            }
        }
    }

    private fun fetchBestStoriesIds() {
        viewModelScope.launch {
            repository.getBestStoriesIds(
                onCallbackResource = { response ->
                    when (response) {
                        is ApiResource.Success -> {
                            _uiState.update { state ->
                                state.copy(bestStoriesIds = response.data)
                            }

                            if (uiState.value.isRefreshing) {
                                uiState.value.bestStoriesIds?.let { ids ->
                                    // Reset pagination
                                    _uiState.update { state ->
                                        state.copy(
                                            paginationState = state.paginationState.copy(
                                                isLoading = false,
                                                page = 0,
                                                endReached = false
                                            )
                                        )
                                    }
                                    fetchStoryDetail(
                                        listOfIds = ids.subList(
                                            0,
                                            minOf(hitsPerPage, ids.size),
                                        )
                                    )
                                }
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
                uiState.value.bestStories?.takeIf { !uiState.value.isRefreshing }?.toCollection(ArrayList())
                    ?: arrayListOf()

            val deferredItems = mutableListOf<Deferred<StoryModel?>>()
            listOfIds.forEachIndexed { index, id ->
                val deferredItem = async {
                    var story: StoryModel? = null
                    repository.getStoryDetail(
                        id = id,
                        onCallbackResource = { response ->
                            when (response) {
                                is ApiResource.Success -> {
                                    story = response.data
                                }
                                else -> {
                                    // There is an error
                                    story = null
                                }
                            }
                        }
                    )
                    story
                }
                deferredItems.add(index, deferredItem)
            }

            deferredItems.forEach { deferredItem ->
                val story = deferredItem.await()
                story?.let {
                    if (storyArrayList.none { it.id == story.id }) {
                        storyArrayList.add(story)
                    }
                }
            }

            _uiState.update { state ->
                state.copy(
                    bestStories = storyArrayList,
                    isRefreshing = false,
                    paginationState = state.paginationState.copy(
                        isLoading = false,
                        page = state.paginationState.page + 1,
                    )
                )
            }
        }
    }

    private fun updatePreferredList(
        id: Long,
        isAddedFromList: Boolean,
    ) {
        viewModelScope.launch {
            try {
                val arrayOfIds = DashboardViewModel.preferredIds.toCollection(ArrayList())
                when (isAddedFromList) {
                    true -> {
                        if (!arrayOfIds.contains(id)) {
                            arrayOfIds.add(id)
                        }
                    }

                    false -> {
                        if (arrayOfIds.contains(id)) {
                            arrayOfIds.remove(id)
                        }
                    }
                }

                val typeToken = object : TypeToken<List<Long>>() {}.type
                val json = gson.toJson(arrayOfIds.toList(), typeToken)
                datastore.savePreferredStories(json = json)
            } catch (e: Exception) { }
        }
    }
}