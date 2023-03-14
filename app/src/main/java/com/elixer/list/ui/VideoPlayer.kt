package com.elixer.list.ui


import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.elixer.list.LogCompositions

@androidx.media3.common.util.UnstableApi
@Composable
fun VideoPlayer(
  modifier: Modifier = Modifier,
  playerWrapper: PlayerWrapper,
  onPlayerClick: () -> Unit
) {
  val context = LocalContext.current

  Box(
    modifier = modifier
      .testTag("VideoPlayerParent")

  ) {
    LogCompositions(tag = "Swipe", msg = "VideoPlayer")

    AndroidView(
      modifier = modifier
        .testTag("VideoPlayer"),
      factory = {
        PlayerView(context).apply {
          hideController()
          useController = false
          resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
          player = playerWrapper.exoPlayer
          layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )

        }
      })
  }
}

@Immutable
@Stable
data class PlayerWrapper(
  val exoPlayer: Player
)
