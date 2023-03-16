package com.elixer.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elixer.list.mediaCompose.media.Media
import com.elixer.list.mediaCompose.media.SurfaceType
import com.elixer.list.mediaCompose.media.rememberMediaState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.Player.RepeatMode
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource


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
      id = 3, //      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/958656f7-949b-4b25-b4a3-9ea9ad82212b.mp4",
      url = "https://html5demos.com/assets/dizzy.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 4, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 4, //      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4",
      url = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4",

      ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 5, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 6, //      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4",
      url = "https://storage.googleapis.com/downloads.webmproject.org/av1/exoplayer/bbb-av1-480p.mp4",
    ),
    mediaId = 17,
  ),

  )


@Composable
fun ContentList(modifier: Modifier = Modifier, movieList: List<GameEntry>) {

  val mediaItems = remember(movieList) {
    movieList.map {
      MediaItem.Builder().setMediaId(it.media?.url.toString()).setUri(it.media?.url.toString()).build()
    }
  }
  Box(
    modifier = modifier,
  ) {
    mediaItems.forEachIndexed { index, mediaItem ->
      Log.e("TAG", "media item -- > ${mediaItem}, index -> ${index},")
//      key(mediaItem.mediaId) {
        ListItemNew(
          showVideo = index == 1,
          mediaItem = mediaItem,
        ) {}
//      }
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
fun ListItemNew(
  showVideo: Boolean, mediaItem: MediaItem, video: @Composable BoxScope.() -> Unit
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
    modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp), shape = RoundedCornerShape(12.dp)
  ) {
    Box(modifier = Modifier.aspectRatio(1f)) {
      Media(
        state = rememberMediaState(player = player),
        modifier = Modifier
          .matchParentSize()
          .background(Color.Black),
        surfaceType = SurfaceType.TextureView
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
