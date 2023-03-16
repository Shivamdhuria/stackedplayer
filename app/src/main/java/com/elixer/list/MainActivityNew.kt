package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.elixer.list.mediaCompose.media.Media
import com.elixer.list.mediaCompose.media.rememberMediaState
import com.elixer.list.ui.theme.ListTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

class MainActivityNew : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

//      InsideListContent(Modifier.fillMaxSize())

      ListTheme {
        // A surface container using the 'background' color from the theme

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

          var movieList = remember {
            mutableStateListOf(
              mockMovies.get(0),
              mockMovies.get(1),
              mockMovies.get(2),
              mockMovies.get(3),
              mockMovies.get(4),
              mockMovies.get(5),
            )
          }

          fun onClick() {
            movieList.removeLast()
          }

          Column {
            Button(
              onClick = ::onClick
            ) {
              Text(text = "remove last")
            }
            ContentList(Modifier, movieList.takeLast(2))
            Button(
              onClick = ::onClick
            ) {
              Text(text = "remove last")
            }
          }
        }
      }
    }
  }


}
val Urls = listOf(
  "https://storage.googleapis.com/downloads.webmproject.org/av1/exoplayer/bbb-av1-480p.mp4",
  "https://storage.googleapis.com/exoplayer-test-media-1/mkv/android-screens-lavf-56.36.100-aac-avc-main-1280x720.mkv",
  "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4",
  "https://html5demos.com/assets/dizzy.mp4",
)
@Composable
fun InsideListContent(
  modifier: Modifier = Modifier,
) {
  val mediaItems = remember { Urls.map { MediaItem.Builder().setMediaId(it).setUri(it).build() } }
  val player by rememberManagedExoPlayer()

  val listState = rememberLazyListState()
  val focusedIndex by remember(listState) {
    derivedStateOf {
      val firstVisibleItemIndex = listState.firstVisibleItemIndex
      val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
      if (firstVisibleItemScrollOffset == 0) {
        firstVisibleItemIndex
      } else if (firstVisibleItemIndex + 1 <= listState.layoutInfo.totalItemsCount - 1) {
        firstVisibleItemIndex + 1
      } else -1
    }
  }
  LazyColumn(
    modifier = modifier,
    state = listState,
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    itemsIndexed(mediaItems) { index, mediaItem ->
      ListItem(
        showVideo = focusedIndex == index
      ) {
        LaunchedEffect(mediaItem, player) {
          player?.run {
            val httpDataSourceFactory: DefaultHttpDataSource.Factory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
            val defaultDataSourceFactory: DefaultDataSource.Factory = DefaultDataSource.Factory(application, httpDataSourceFactory)
            val cacheDataSourceFactory: CacheDataSource.Factory? = application.simpleCache?.let {
              CacheDataSource.Factory()
                .setCache(it)
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
            }
            val mediaSource: ProgressiveMediaSource? = cacheDataSourceFactory?.let {
              mediaItem.let { it1 ->
                ProgressiveMediaSource.Factory(it)
                  .createMediaSource(it1)
              }
            }
            mediaSource?.let { (this as? ExoPlayer)?.setMediaSource(it, true) }
            prepare()
          }
        }
        Media(
          state = rememberMediaState(player = player),
          modifier = Modifier
            .matchParentSize()
            .background(Color.Black)
        )
      }
    }
  }
}
@Composable
fun ContentView(movie: GameEntry, isActive: Boolean) {

  Card(
    Modifier
      .fillMaxWidth()
      .padding(horizontal = 50.dp)
      .padding(vertical = 30.dp)
  ) {
    LogCompositions("ContentView ${movie.id}")
////    key(movie.media?.url) {
//    MediaPlayer(
//      Modifier.height(200.dp), videoUri = movie.media?.url
//        .toString
//          (), id = movie.id,
//      isActive
//    )
//
////    }

    // The below text Composable works as expected
    Text(
      text = movie.id.toString(),
      textAlign = TextAlign.Center,
      color = Color.White,
      fontSize = 20.sp,
      modifier = Modifier.fillMaxWidth()
    )
  }
  DisposableEffect(Unit) {
    Log.e("TAG", "Created Card id -> ${movie.id}")

    onDispose {
      Log.e("TAG", "Disposed Card id -> ${movie.id}")
    }
  }
}
