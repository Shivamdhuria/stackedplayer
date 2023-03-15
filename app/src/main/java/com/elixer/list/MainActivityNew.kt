package com.elixer.list

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ContentView(modifier: Modifier, movie: GameEntry, isActive: Boolean) {
  Card(
    modifier
      .height(400.dp)
      .padding(horizontal = 30.dp)
  ) {

    MediaPlayer(videoUri = movie.media?.url.toString(), id = movie.id, isActive)

    // The below text Composable works as expected
    Text(
      text = movie.id.toString(),
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )
  }
  DisposableEffect(Unit) {
    onDispose {
      Log.e("Swipe", "Disposed Card id -> ${movie.id}")
    }
  }
}
