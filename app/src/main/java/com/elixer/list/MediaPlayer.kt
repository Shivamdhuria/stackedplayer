package com.elixer.list

import android.util.Log
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
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

@OptIn(ExperimentalComposeUiApi::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MediaPlayer(modifier: Modifier, videoUri: String, id: Long, isActive: Boolean) {
  val context = LocalContext.current
  val application = context.applicationContext as CultApplication
  LogCompositions("Exoplayer created  ${id}")

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

  Box(modifier = modifier){
    AndroidView(
      modifier = modifier,
      factory = {
        StyledPla(context).apply {
          hideController()
          useController = false
          resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
          player = exoPlayer
          layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
//          videoSurfaceView?.alpha = 0.5f
//          videoSurfaceView?.translationZ = 40f
//          (videoSurfaceView as? SurfaceView)?.let {
//            Log.d("!!!!Surface view", "id ${id}")
//            it.setZOrderOnTop(true)
//            it.setZOrderMediaOverlay(false)
//            Log.d("!!!!Surface view", "id ${id} hasOverlappingRendering " +
//                    "${hasOverlappingRendering}")
//
//          }
        }
      },
      update = {
//        it.videoSurfaceView?.z = if (isActive) 1f else 0f
//        it.z = if (isActive) 1f else 0f
        Log.e("TAG", "update Exoplayer id -> ${id}, isActive ${isActive}")
      },
      onReset = {
        Log.e("TAG", "clear Exoplayer id -> ${id}, isActive ${isActive}")


      },
      onRelease = {
        Log.e("TAG", "reset Exoplayer id -> ${id}, isActive ${isActive}")

      }
    )
  }
//  LaunchedEffect(isActive) {
////    if (isActive) exoPlayer.play()
//  }

  DisposableEffect(Unit) {
    onDispose {
      Log.e("TAG", "Disposed Exoplayer id -> ${id}")
      exoPlayer.release()
    }
  }
}