package alessandro.vendramini.hackernews.presentation.viewmodels

import alessandro.vendramini.hackernews.data.api.ApiResource
import alessandro.vendramini.hackernews.data.api.repositories.CommentsRepository
import alessandro.vendramini.hackernews.data.models.CommentModel
import alessandro.vendramini.hackernews.data.models.StoryModel
import alessandro.vendramini.hackernews.presentation.viewmodels.events.CommentsViewModelEvent
import alessandro.vendramini.hackernews.presentation.viewmodels.states.CommentsViewModelState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CommentsViewModel(private val repository: CommentsRepository): ViewModel() {

    private val _uiState = MutableStateFlow(value = CommentsViewModelState())
    val uiState: StateFlow<CommentsViewModelState> = _uiState.asStateFlow()

    fun onEvent(event: CommentsViewModelEvent) {
        when (event) {
            is CommentsViewModelEvent.FetchCommentsByIds -> {
                fetchCommentsByIds(listOfIds = event.listOfIds)
            }
            is CommentsViewModelEvent.FetchAnswersByParents -> {
                fetchCommentsByIdsAndParentId(
                    parentId = event.parent,
                    listOfIds = event.listOfIds,
                )
            }
        }
    }

    private fun fetchCommentsByIds(listOfIds: List<Long>) {
        viewModelScope.launch {
            val commentArrayList: ArrayList<CommentModel> = arrayListOf()

            val deferredItems = mutableListOf<Deferred<CommentModel?>>()
            listOfIds.forEachIndexed { index, id ->
                val deferredItem = async {
                    var comment: CommentModel? = null
                    repository.getCommentDetail(
                        id = id,
                        onCallbackResource = { response ->
                            when (response) {
                                is ApiResource.Success -> {
                                    comment = response.data
                                }
                                else -> {
                                    // There is an error
                                    comment = null
                                }
                            }
                        }
                    )
                    comment
                }
                deferredItems.add(index, deferredItem)
            }

            deferredItems.forEach { deferredItem ->
                val comment = deferredItem.await()
                comment?.let {
                    if (commentArrayList.none { it.id == comment.id }) {
                        commentArrayList.add(comment)
                    }
                }
            }

            _uiState.update { state ->
                state.copy(comments = commentArrayList)
            }
        }
    }

    private fun fetchCommentsByIdsAndParentId(
        parentId: Long,
        listOfIds: List<Long>,
    ) {
        viewModelScope.launch {
            val commentArrayList: ArrayList<CommentModel> = arrayListOf()

            val deferredItems = mutableListOf<Deferred<CommentModel?>>()
            listOfIds.forEachIndexed { index, id ->
                val deferredItem = async {
                    var story: CommentModel? = null
                    repository.getCommentDetail(
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
                    if (commentArrayList.none { it.id == story.id }) {
                        commentArrayList.add(story)
                    }
                }
            }

            _uiState.update { state ->
                val updatedComments = findAndReplaceParentComment(
                    state.comments!!,
                    parentId = parentId,
                    newAnswers = commentArrayList,
                )
                state.copy(comments = updatedComments)
            }
        }
    }
}

private fun findAndReplaceParentComment(
    comments: List<CommentModel>,
    parentId: Long,
    newAnswers: List<CommentModel>,
): List<CommentModel> {
    return comments.map { comment ->
        if (comment.id == parentId) {
            comment.copy(answers = newAnswers)
        } else if (comment.answers.isNotEmpty()) {
            comment.copy(
                answers = findAndReplaceParentComment(
                    comments = comment.answers,
                    parentId = parentId,
                    newAnswers = newAnswers,
                )
            )
        } else {
            comment
        }
    }
}