package com.lifelog.feature.library

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.lifelog.core.domain.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val articles by viewModel.articles.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Library") })
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(articles) { article ->
                ListItem(
                    headlineContent = { Text(article.title) },
                    supportingContent = { Text(article.category) }
                )
            }
        }
    }
}
