package com.elixer.list.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.elixer.list.ui.theme.ListTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.elixer.list.GameEntry
import com.elixer.list.LogCompositions
import com.elixer.list.ScopedView
import com.elixer.list.mock4

@UnstableApi
class MainActivityNew : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ListTheme {
        // A surface container using the 'background' color from the theme

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

          var movieList = remember { mutableStateListOf(mock4.get(0), mock4.get(1), mock4.get(2), mock4.get(3), mock4.get(4), mock4.get(5),) }
          fun onClick() {
            movieList.removeLast()
          }

          Column {
            ContentList(movieList.takeLast(2))
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

  @Composable
  private fun ContentList(movieList: List<GameEntry>) {
    Box {
      LogCompositions(tag = "", msg = "ContentList Box ")
      ScopedView {
        movieList.forEachIndexed { index, movie ->
          key(movie.id) {
            ContentView(
              Modifier.fillMaxWidth(), movie, index == 1
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ContentView(modifier: Modifier, movie: GameEntry, isActive: Boolean) {
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
