package com.xbot.anilibriarefresh.ui.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.xbot.domain.model.TitleModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lazyTitlesItems = viewModel.titles.collectAsLazyPagingItems()

    HomeScreenContent(
        modifier = modifier,
        items = lazyTitlesItems
    )
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<TitleModel>
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ) {
            items(
                count = items.itemCount,
                key = items.itemKey(),
                contentType = items.itemContentType { "TitleItem" }
            ) { index ->
                TitleItem(
                    title = items[index],
                    onClick = { index ->
                        //TODO: действие по клику
                    }
                )
            }

            //TODO: переделать
            with(items) {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        val e = loadState.refresh as LoadState.Error
                        scope.launch {
                            when (snackbarHostState.showSnackbar(e.error.localizedMessage ?: "Ошибка", "Retry")) {
                                SnackbarResult.Dismissed -> {
                                    //TODO: On dismiss action
                                }
                                SnackbarResult.ActionPerformed -> {
                                    retry()
                                }
                            }
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = loadState.append as LoadState.Error
                        scope.launch {
                            when (snackbarHostState.showSnackbar(e.error.localizedMessage ?: "Ошибка", "Retry")) {
                                SnackbarResult.Dismissed -> {
                                    //TODO: On dismiss action
                                }
                                SnackbarResult.ActionPerformed -> {
                                    retry()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingItem() {
    Box(modifier = Modifier.fillMaxWidth()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
