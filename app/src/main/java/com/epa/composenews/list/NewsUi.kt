package com.epa.composenews.list

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epa.composenews.R
import com.epa.composenews.common.News
import com.epa.composenews.ui.common.FullScreenError
import com.epa.composenews.ui.theme.Neutral300
import com.epa.composenews.ui.theme.Neutral600
import com.epa.composenews.ui.theme.Neutral900
import com.squareup.picasso.Picasso
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private val publishedAtFormatter = SimpleDateFormat("dd MMMM, HH:mm", Locale.getDefault())

@Composable
fun NewsScreen(viewModel: NewsListViewModel) {
    val state by viewModel.uiState.collectAsState()
    NewsScreen(
        uiState = state,
        onRetryClick = { viewModel.retry() },
        loadMore = { viewModel.loadMore() },
        retryLoadMore = { viewModel.retryLoadMore() }
    )
}

@Composable
private fun NewsScreen(
    uiState: NewsUiState,
    onRetryClick: () -> Unit,
    loadMore: () -> Unit,
    retryLoadMore: () -> Unit
) {
    val activity = (LocalContext.current as Activity)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "News", modifier = Modifier.wrapContentHeight())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                backgroundColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { activity.finish() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back_black),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        when (uiState) {
            NewsUiState.FullScreenProgress -> Shimmer()
            is NewsUiState.FullScreenError -> FullScreenError(text = uiState.text, onRetryClick)
            is NewsUiState.Content -> NewsItemList(newsList = uiState.items, loadMore, retryLoadMore)
        }
    }
}

@Composable
private fun NewsItemList(
    newsList: List<NewsListItem>,
    loadMore: () -> Unit,
    retryLoadMore: () -> Unit
) {
    val currentNewsList by rememberUpdatedState(newsList)
    val listState = rememberLazyListState()
    listState.OnBottomReached {
        if (currentNewsList.lastOrNull() is NewsListItem.Progress) {
            loadMore()
        }
    }
    LazyColumn(state = listState) {
        items(newsList) {
            when (it) {
                is NewsListItem.Item -> NewsItem(it.news)
                is NewsListItem.Progress -> PaginationProgress()
                is NewsListItem.Error -> PaginationErrorItem(retryLoadMore)
            }
        }
    }
}

@Composable
private fun NewsItem(news: News) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { }
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = news.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutral900,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 16.dp, start = 20.dp, end = 16.dp)
                )
                Text(
                    text = publishedAtFormatter.format(news.date),
                    fontSize = 12.sp,
                    color = Neutral600,
                    modifier = Modifier.padding(bottom = 16.dp, start = 20.dp)
                )
            }
            RemoteImage(
                news.urlToImage,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically),
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Divider(
            color = Neutral300,
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun PaginationProgress() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun PaginationErrorItem(retryLoadMore: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                text = "Не удалось загрузить данные",
                fontSize = 16.sp,
                color = Neutral600,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Button(
                onClick = retryLoadMore,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            ) {
                Text(text = "Повторить")
            }
        }
    }

}

@Composable
private fun Shimmer() {
    val shape = RoundedCornerShape(4.dp)
    val shimmerColor = Color(0xFFF1EFFF)
    CompositionLocalProvider(
        LocalShimmerTheme provides defaultShimmerTheme.copy(
            blendMode = BlendMode.DstOut,
        )
    ) {
        Column(modifier = Modifier.shimmer()) {
            repeat(6) {
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .width(256.dp)
                            .height(18.dp)
                            .clip(shape)
                            .background(shimmerColor),
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .width(128.dp)
                            .height(16.dp)
                            .clip(shape)
                            .background(shimmerColor),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun RemoteImage(url: String, modifier: Modifier) {
    val bitmapState = remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(url) {
        bitmapState.value = withContext(Dispatchers.IO) {
            runCatching {
                Picasso.get()
                    .load(url)
                    .get()
            }.getOrNull()
        }
    }
    val bitmap = bitmapState.value
    if (bitmap == null) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.LightGray)
        )
    } else {
        Image(
            modifier = modifier.clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop,
            painter = BitmapPainter(bitmap.asImageBitmap()),
            contentDescription = null
        )
    }
}

@Composable
private fun LazyListState.OnBottomReached(action: () -> Unit) {
    val more by remember {
        derivedStateOf {
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex == totalItemsNumber
        }
    }
    LaunchedEffect(more) {
        if (more) {
            action()
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NewsCardPreview() {
    PaginationErrorItem {

    }
}