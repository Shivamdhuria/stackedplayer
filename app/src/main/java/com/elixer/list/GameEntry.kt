package com.elixer.list

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.SwipeableCardState
import com.elixer.list.mediaCompose.media.Media
import com.elixer.list.mediaCompose.media.SurfaceType
import com.elixer.list.mediaCompose.media.rememberMediaState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs


data class GameEntry(

  val authorId: kotlin.Long,

  val id: kotlin.Long,

  val instanceId: kotlin.Long,

  val invitationId: kotlin.Long? = null,

  val media: Media? = null,

  val mediaId: kotlin.Long? = null,

  )


data class Media(

  val id: kotlin.Long,
  val url: kotlin.String,
)

enum class MediaType(val value: kotlin.String) {
  photo("photo"),

  video("video")
}

val mockMovies = listOf(
  GameEntry(
    authorId = 1693470030420771857, id = 0, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 0,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 1, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 1,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 2, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 2,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/ebb81dea-d559-4c25-8793-fe99f9a3a746.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 3, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 3, //
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/958656f7-949b-4b25-b4a3-9ea9ad82212b.mp4", //      url = "https://html5demos.com/assets/dizzy.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 4, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 4, //
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4", //      url = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4",

    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 5, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 5, //
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4", //      url = "https://storage.googleapis.com/downloads.webmproject.org/av1/exoplayer/bbb-av1-480p.mp4",
    ),
    mediaId = 17,
  ),

  )


@Composable
fun ContentStack(modifier: Modifier = Modifier, movieList: List<GameEntry>, onRemove: () -> Unit) {

  //  val mediaItems = remember(movieList) {
  //    movieList.map {
  //      MediaItem.Builder().setMediaId(it.media?.url.toString()).setUri(it.media?.url.toString()).build()
  //    }
  //  }

  val gameEntry = remember() { mutableStateListOf<GameEntry>() }
  Log.e("TAG GameEntry", gameEntry.map { it.id }.toString())

  var currentMovie by remember { mutableStateOf<GameEntry?>(null) }

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
    Log.e("TAG MOVIE LIST rec", gameEntry.map { it.id }.toString())

    if (gameEntry.isEmpty()) {
      gameEntry.addAll(movieList)
    } else { //
       gameEntry.removeLast()
      movieList.forEach {
        if (!gameEntry.contains(it)) {
          gameEntry.add(0, it)
        }
      }
    }
    activeOffset.snapTo(Offset(0f, 0f))

    currentMovie = movieList.last() //    activeOffset.snapTo(Offset(0f, 0f))
  }

  //  Log.e("TAG", mediaItems.map { it.mediaId.toString() }.toString())
  Box(
    modifier = modifier,
  ) {
    gameEntry.forEachIndexed { index, mediaItem ->
      Log.e("TAG", "media item -- > ${mediaItem.id}, index -> ${index},") //      key(mediaItem.mediaId) {
      ScopedView {
        key(mediaItem.id) {

          SwipeableCard(
            modifier = Modifier
              .padding(horizontal = 40.dp)
              .aspectRatio(9 / 16f)
              .pointerInput(Unit) {
                coroutineScope {
                  detectDragGestures(onDragCancel = {
                    launch { //                    state.reset()
                      //                    onSwipeCancel()
                    }
                  }, onDrag = { change, dragAmount ->
                    launch {
                      val original = activeOffset.targetValue
                      val summed = original + dragAmount
                      val newValue = Offset(
                        x = summed.x.coerceIn(-screenWidth, screenWidth), y = summed.y.coerceIn(-screenHeight, screenHeight)
                      )
                      if (change.positionChange() != Offset.Zero) change.consume()
                      activeOffset.animateTo(Offset(newValue.x, newValue.y))
                    }
                  }, onDragEnd = {
                    launch {
                      val coercedOffset = activeOffset.targetValue.coerceIn(
                        listOf(Direction.Up, Direction.Down), maxHeight = screenHeight, maxWidth = screenWidth
                      )

                      if (hasNotTravelledEnoughNew(
                          screenWidth, screenHeight, coercedOffset
                        )
                      ) {
                        activeOffset.animateTo(Offset.Zero, tween(400))
                      } else {
                        val horizontalTravel = abs(activeOffset.targetValue.x)
                        val verticalTravel = abs(activeOffset.targetValue.y)

                        if (horizontalTravel > verticalTravel) {
                          if (activeOffset.targetValue.x > 0) {
                            swipeNew(Direction.Right) //                          onSwiped(Direction.Right)
                          } else {
                            swipeNew(Direction.Left) //                          onSwiped(Direction.Left)
                          }
                        } else {
                          if (activeOffset.targetValue.y < 0) {
                            swipeNew(Direction.Up) //                          onSwiped(Direction.Up)
                          } else {
                            swipeNew(Direction.Down) //                          onSwiped(Direction.Down)
                          }
                        }
                      }
                    }
                  })
                }
              }
              .graphicsLayer {
                if (mediaItem == currentMovie) {
                  translationX = activeOffset.value.x
                  translationY = activeOffset.value.y
                  rotationZ = (activeOffset.value.x / 60).coerceIn(-40f, 40f)
                } else {
                  translationX = 0f
                  translationY = 0f
                  rotationZ = 0f
                }

              },
            showVideo = mediaItem == currentMovie,
            mediaItem = mediaItem,
          ) {}
        }
      } //      }
    }
  }

  //      movieList.forEachIndexed { index, movie ->
  ////      key(movie.id) {
  //        Log.e("TAG", " id ->> ${movie.id}, index--> ${index}")
  //        ContentView(movie, true)
  ////      }
  //      }
}

@Composable
fun SwipeableCard(
  modifier: Modifier, showVideo: Boolean, mediaItem: GameEntry, video: @Composable BoxScope.() -> Unit
) {

  val mediaSourceUrl = MediaItem.Builder().setMediaId(mediaItem.media?.url.toString()).setUri(mediaItem.media?.url.toString()).build()

  val player by rememberManagedExoPlayer()
  player?.apply {
    val httpDataSourceFactory: DefaultHttpDataSource.Factory = DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
    val defaultDataSourceFactory: DefaultDataSource.Factory = DefaultDataSource.Factory(application, httpDataSourceFactory)
    val cacheDataSourceFactory: CacheDataSource.Factory? = application.simpleCache?.let {
      CacheDataSource.Factory().setCache(it).setUpstreamDataSourceFactory(defaultDataSourceFactory).setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
    val mediaSource: ProgressiveMediaSource? = cacheDataSourceFactory?.let {
      mediaSourceUrl.let { it1 ->
        ProgressiveMediaSource.Factory(it).createMediaSource(it1)
      }
    }
    mediaSource?.let { (player as? ExoPlayer)?.setMediaSource(it, true) }
    repeatMode = REPEAT_MODE_ONE
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
        modifier = Modifier.fillMaxSize(),
        surfaceType = SurfaceType.TextureView,
      )
    }
  }
}

class Ref(var value: Int)

// Note the inline function below which ensures that this function is essentially
// copied at the call site to ensure that its logging only recompositions from the
// original call site.
@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun LogCompositions(tag: String) {
  val ref = remember { Ref(0) }
  SideEffect { ref.value++ }
  Log.d("TAG", "Compositions: $tag: ${ref.value}")
}

@Composable
fun ScopedView(content: @Composable () -> Unit) {
  content()
}

@Composable
fun ListItem(
  showVideo: Boolean, video: @Composable BoxScope.() -> Unit
) {
  Card(
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp), shape = RoundedCornerShape(12.dp)
  ) {
    Box(modifier = Modifier.aspectRatio(1f)) {
      if (showVideo) {
        video()
      }
    }
  }
}

public fun Offset.coerceIn(
  blockedDirections: List<Direction>,
  maxHeight: Float,
  maxWidth: Float,
): Offset {
  return copy(
    x = x.coerceIn(
      if (blockedDirections.contains(Direction.Left)) {
        0f
      } else {
        -maxWidth
      }, if (blockedDirections.contains(Direction.Right)) {
        0f
      } else {
        maxWidth
      }
    ), y = y.coerceIn(
      if (blockedDirections.contains(Direction.Up)) {
        0f
      } else {
        -maxHeight
      }, if (blockedDirections.contains(Direction.Down)) {
        0f
      } else {
        maxHeight
      }
    )
  )
}

public fun hasNotTravelledEnoughNew(
  maxHeight: Float,
  maxWidth: Float,
  offset: Offset,
): Boolean {
  return abs(offset.x) < maxWidth / 4 && abs(offset.y) < maxHeight / 4
}