package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.common.util.UnstableApi
import com.elixer.list.ui.theme.ListTheme

@UnstableApi
class MainActivityNew : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
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
}

@Composable
fun ContentView( movie: GameEntry, isActive: Boolean) {
  Card(
    Modifier
      .fillMaxWidth()
      .height(300.dp)
      .padding(horizontal = 50.dp)
  ) {
    LogCompositions("ContentView ${movie.id}")
////    key(movie.media?.url) {
//      MediaPlayer(Modifier, videoUri = movie.media?.url
//        .toString
//        (), id = movie.id,
//        isActive)
//
////    }

    // The below text Composable works as expected
    Text(
      text = movie.id.toString(),
      textAlign = TextAlign.Center,
      modifier = Modifier.fillMaxWidth()
    )
  }
  DisposableEffect(Unit) {
    onDispose {
      Log.e("TAG", "Disposed Card id -> ${movie.id}")
    }
  }
}
