package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lazyTitlesItems = viewModel.titles.collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            items(
                count = lazyTitlesItems.itemCount,
                key = lazyTitlesItems.itemKey()
            ) { index ->
                when (val title = lazyTitlesItems[index]) {
                    null -> {
                        ListItem(
                            modifier = Modifier.clickable {

                            },
                            headlineContent = {
                                Text("Загрузка")
                            },
                            supportingContent = {
                                Text("Загрузка")
                            }
                        )
                    }
                    else -> {
                        ListItem(
                            modifier = Modifier.clickable {

                            },
                            headlineContent = {
                                Text(title.name)
                            },
                            supportingContent = {
                                Text(
                                    text = title.description,
                                    maxLines = 3
                                )
                            }
                        )
                    }
                }
            }

            with(lazyTitlesItems) {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        item {
                            ErrorItem(e.error.localizedMessage ?: "Неизвестная ошибка")
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = loadState.append as LoadState.Error
                        item {
                            ErrorItem(e.error.localizedMessage ?: "Ошибка при загрузке следующей страницы")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingItem() {
    Box(modifier = Modifier.fillMaxWidth()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ErrorItem(message: String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Red
        )
    }
}