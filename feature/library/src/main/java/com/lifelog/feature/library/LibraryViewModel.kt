package com.lifelog.feature.library

import androidx.lifecycle.ViewModel
import com.lifelog.core.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles = _articles.asStateFlow()

    init {
        loadArticles()
    }

    private fun loadArticles() {
        _articles.value = listOf(
            Article(1, "Understanding Anxiety", "Content about anxiety...", "Mental Health"),
            Article(2, "The Benefits of Mindfulness", "Content about mindfulness...", "Well-being"),
            Article(3, "Improving Your Sleep Quality", "Content about sleep...", "Sleep"),
            Article(4, "Coping with Stress", "Content about stress...", "Mental Health"),
            Article(5, "The Power of Positive Thinking", "Content about positivity...", "Well-being")
        )
    }
}
