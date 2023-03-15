package com.elixer.list.ui

import android.util.Log
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.elixer.list.CultApplication
import com.elixer.list.LogCompositions

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MediaPlayer(videoUri: String, id: Long, isActive:Boolean) {
  val context = LocalContext.current
  val application = context.applicationContext as CultApplication

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
        playWhenReady = true
        videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        repeatMode = Player.REPEAT_MODE_ONE
        prepare()
      }
  }

  DisposableEffect(
    AndroidView(
      factory = {
        PlayerView(context).apply {
          hideController()
          useController = false
          resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
          player = exoPlayer
          layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
          videoSurfaceView?.alpha = 0.5f
          videoSurfaceView?.translationZ = 40f
          (videoSurfaceView as? SurfaceView)?.let {
            Log.d("!!!!Surface view", "id ${id}")
            it.setZOrderOnTop(true)
            it.setZOrderMediaOverlay(false)
            Log.d("!!!!Surface view", "id ${id} hasOverlappingRendering " +
                    "${hasOverlappingRendering}")

          }
        }
      },
    )
  ) {
    onDispose {
      exoPlayer.release()
    }
  }
}