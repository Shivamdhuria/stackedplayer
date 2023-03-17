package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.alexstyl.swipeablecard.Direction
import com.elixer.list.mediaCompose.media.Media
import com.elixer.list.mediaCompose.media.SurfaceType
import com.elixer.list.mediaCompose.media.rememberMediaState
import com.elixer.list.ui.theme.ListTheme
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import java.lang.Exception

class MainActivityNew : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {

//      InsideListContent(Modifier.fillMaxSize())

      ListTheme {
        // A surface container using the 'background' color from the theme

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

          var movieList = remember {
            mutableStateListOf<GameEntry>(

            )
          }
          LaunchedEffect(Unit ){
            movieList.addAll(mockMovies)
          }

          fun onClick() {
            try {
              movieList.removeLast()
            } catch (ex:Exception){

            }

          }

          Column {
            Button(
              onClick = ::onClick
            ) {
              Text(text = "remove last")
            }
            Stack(Modifier, movieList.takeLast(2)){
              onClick()
            }
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

@Composable
fun Stack(modifier: Modifier = Modifier, movieList: List<GameEntry>, onRemove: () -> Unit) {

  val mediaItems = remember() {
    mutableStateListOf<MediaItem>()
  }
  val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
  val screenHeight = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
  val scope = rememberCoroutineScope() //    var activeCardState = rememberSwipeableCardState() //    val queuedCardState = rememberSwipeableCardState()
  val activeOffset by remember {
    mutableStateOf(
      Animatable(
        Offset.Zero, Offset.VectorConverter
      )
    )
  }

  fun onSwipedNew(direction: Direction) {
    onRemove()
  }


  suspend fun swipeNew(direction: Direction, animationSpec: AnimationSpec<Offset> = tween(400)) {
    val endX = screenWidth * 1.5f
    val endY = screenHeight
    when (direction) {
      Direction.Left -> activeOffset.animateTo(Offset(x = -endX, 0f), animationSpec)
      Direction.Right -> activeOffset.animateTo(Offset(x = endX, 0f), animationSpec)
      Direction.Up -> activeOffset.animateTo(Offset(x = 0f, y = -endY), animationSpec)
      Direction.Down -> activeOffset.animateTo(Offset(x = 0f, y = endY), animationSpec)
    }
    onSwipedNew(direction)
  }

  LaunchedEffect(movieList) { //
    if (mediaItems.isEmpty()) {
      mediaItems.addAll(movieList.map {
        MediaItem.Builder().setMediaId(it.media?.url.toString()).setUri(it.media?.url.toString()).build()
      })
    } else { //
      mediaItems.removeLast()
      movieList.forEachIndexed { index, item ->

        if (index == 0) {
          val media = MediaItem.Builder().setMediaId(item.media?.url.toString()).setUri(item.media?.url.toString()).build()
          mediaItems.add(0, media)
        }
      }
    } //    activeOffset.snapTo(Offset(0f, 0f))
    //
    //    currentMovie = movieList.last() //    activeOffset.snapTo(Offset(0f, 0f))
  }



  Log.e("TAG", mediaItems.map { it.mediaId.toString() }.toString())
  Box(
    modifier = modifier,
  ) {
    mediaItems.forEachIndexed { index, mediaItem ->
      Log.e("TAG", "media item -- > ${mediaItem.mediaId}, index -> ${index},") //      key(mediaItem.mediaId) {
      Item(
        modifier = Modifier
          .clickable(onClick = { onRemove() })
          .padding(horizontal = 40.dp)
          .aspectRatio(9 / 16f),
        showVideo = mediaItem == mediaItems.last(),
        mediaItem = mediaItem,
      ) {} //      }
    }
  }

  //      movieList.forEachIndexed { index, movie ->
  ////      key(movie.id) {
  //        Log.e("TAG", " id ->> ${movie.id}, index--> ${index}")
  //        ContentView(movie, true)
  ////      }
  //      }
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


@Composable
fun Item(
  modifier: Modifier, showVideo: Boolean, mediaItem: MediaItem, video: @Composable BoxScope.() -> Unit
) {
  val player by rememberManagedExoPlayer()
  player?.apply {
    val httpDataSourceFactory: DefaultHttpDataSource.Factory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
    val defaultDataSourceFactory: DefaultDataSource.Factory = DefaultDataSource.Factory(application, httpDataSourceFactory)
    val cacheDataSourceFactory: CacheDataSource.Factory? = application.simpleCache?.let {
      CacheDataSource.Factory().setCache(it).setUpstreamDataSourceFactory(defaultDataSourceFactory).setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
    val mediaSource: ProgressiveMediaSource? = cacheDataSourceFactory?.let {
      mediaItem.let { it1 ->
        ProgressiveMediaSource.Factory(it).createMediaSource(it1)
      }
    }
    mediaSource?.let { (player as? ExoPlayer)?.setMediaSource(it, true) }
    repeatMode = Player.REPEAT_MODE_ONE
    prepare()
  }
  LaunchedEffect(showVideo) {
    if (showVideo) {
      player?.playWhenReady = true
    } else {
      player?.pause()
    }
  }
  Card(
    modifier = modifier, shape = RoundedCornerShape(12.dp)
  ) {
    Box(modifier = Modifier) {
      Media(
        state = rememberMediaState(player = player),
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black),
        surfaceType = SurfaceType.TextureView,
      )
    }
  }
}
