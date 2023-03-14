package com.elixer.list

import android.util.Log
import android.view.ViewGroup.LayoutParams.*
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContentProviderCompat.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.*
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView


@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun VideoPlayerCard(videoUri: String, shouldPlayBack: Boolean = false) {
  val context = LocalContext.current
  val application = context.applicationContext as CultApplication
  val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

  val exoPlayer = remember {
    ExoPlayer.Builder(context).build()
      .apply {
        val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
        val httpDataSourceFactory: DefaultHttpDataSource.Factory =
          DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
        val defaultDataSourceFactory: DefaultDataSource.Factory =
          DefaultDataSource.Factory(context, httpDataSourceFactory)
        val cacheDataSourceFactory: CacheDataSource.Factory? = application.simpleCache?.let {
          CacheDataSource.Factory()
            .setCache(it)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        }

        val mediaSource: ProgressiveMediaSource? = cacheDataSourceFactory?.let {
          ProgressiveMediaSource.Factory(it)
            .createMediaSource(mediaItem)
        }
        mediaSource?.let { setMediaSource(it, true) }
        playWhenReady = false
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        repeatMode = Player.REPEAT_MODE_ONE
        prepare()
      }
  }


  LogCompositions(tag = "Swipe", msg = "Video Card -> ${videoUri} ..")
  exoPlayer.addListener(object : Player.Listener {

    override fun onPlaybackStateChanged(state: Int) {
      when (state) {
        STATE_ENDED -> {
          exoPlayer.seekTo(0L)
          Log.e("onPlaybackStateChanged", "ended")
        }

        STATE_IDLE -> {
//                    exoPlayer.pause()
          Log.e("onPlaybackStateChanged", "idle")

        }

        STATE_READY -> {
//                    exoPlayer.pause()
          Log.e("onPlaybackStateChanged", "ready")

        }
      }
    }
  })
  LaunchedEffect(shouldPlayBack) {
    exoPlayer.playWhenReady = shouldPlayBack
  }



  DisposableEffect(
    AndroidView(
      factory = {
        PlayerView(context).apply {
          hideController()
          useController = false
          resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
          player = exoPlayer
          layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
      },
    )
  ) {
    Log.d("Swipe", "exoplayer created ${videoUri}")

    val observer = LifecycleEventObserver { owner, event ->
      when (event) {
        Lifecycle.Event.ON_PAUSE -> {
//                    exoPlayer.pause()
        }

        Lifecycle.Event.ON_RESUME -> {
//                    exoPlayer.play()
        }

        else -> {}
      }
    }
    val lifecycle = lifecycleOwner.value.lifecycle
    lifecycle.addObserver(observer)
    onDispose {
      Log.d("Swipe", "exoplayer disposed ${videoUri}")
      exoPlayer.release()
      lifecycle.removeObserver(observer)
    }
  }
}