package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elixer.list.ui.theme.ListTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.theapache64.twyper.rememberTwyperController

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ListTheme {
        // A surface container using the 'background' color from the theme

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          var numbers = remember {
            mutableStateListOf(Pair(1, false), Pair(2, false), Pair(3, false), Pair(4, false))
          }
//          val myList = remember {
//            mutableStateListOf<Int>().apply {
//              add(1)
//              add(2)
//              add(3)
//            }
//          }


          val myList = remember {
            mutableStateListOf<Pair<Int, Boolean>>().apply {
              add(Pair(1, false))
              add(Pair(2, false))
              add(Pair(3, false))

            }
          }

          var movieList = remember {
            mutableStateListOf(
              Movie("Sasaasa"),
              Movie("Sasaasa"),
              Movie("Sasaasa"),
              Movie("Sasaasa"),
            )

          }

//          var takeIndex by remember { mutableStateOf(2) }

          //          LaunchedEffect(takeIndex) {
//            myList.swapList(getDailyItemList()) // Returns a List<DailyItem> with latest values and uses mutable list internally
//            numbers.add(5)
//            myList.
//          }
          fun addElem() {
            myList.add(Pair(9, false))

          }

//          List(myList) {
//            addElem()
//          }
//          TwyperPreview()

          Column {
            MoviesScreenWithKey(movies = movieList)
            Button(onClick =
            {
              movieList.add(Movie(id = "${Math.random()}"))
            }) {
              Text(text = "Add")
            }
          }
        }
      }
    }
  }

  @Composable
  private fun List(myList: SnapshotStateList<Pair<Int, Boolean>>, onClick: () -> Unit) {
    Column() {
      val text = remember {
        mutableStateOf("ajdkajds")
      }

      Greeting("Android")
      ScopedView {
        Greeting(text.value)
      }

      LogCompositions(tag = "onCreate", msg = "Coloumn")
      ScopedView {
        myList.forEach {
          ScopedViewRow {

            Text(text = it.first.toString())
            Text(text = it.second.toString())
          }
        }
      }
//      myList.forEach { one ->
//        ScopedViewRow {
//
//          Text(text = one.toString())
//        }
//        ScopedView {
//          Row() {
//            ScopedView {
//              Text(text = one.toString())
//            }
//          }
//        }
//      }

//      ScopedView {
//        Button(onClick = {
//          text.value = "${Math.random()}"
//          onClick()
//        }) {
//          Text(text = "take index ")
//        }
//      }


    }
  }


}

@Composable
private fun MyText(one: Int) {
  Text(text = one.toString())
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Composable
fun MoviesScreenWithKey(movies: List<Movie>) {
  Box {
    LogCompositions(tag = "onCreate", msg = "MoviesScreenWithKey Box")

    for (movie in movies) {
      key(movie.id) { // Unique ID for this movie
        MovieOverview(movie)
      }
    }
  }
}

@Composable
fun MovieOverview(movie: Movie) {
  Column() {

    Card(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp).height(50.dp),
      colors = CardDefaults.cardColors(containerColor = getRandomColor())
    ) {
      LogCompositions(tag = "onCreate", msg = "MovieOverview Column")

      // Side effect explained later in the docs. If MovieOverview
      // recomposes, while fetching the image is in progress,
      // it is cancelled and restarted.
      Text(movie.id, color = Color.Black)
    }


    /* ... */
  }
}

data class Movie(
  val id: String
)

fun getRandomColor(): Color {
  val clr = listOf(Color.Yellow, Color.Red, Color.Cyan, Color.Green)
  return clr.random()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  ListTheme {
    Greeting("Android")
  }
}

class Ref(var value: Int)

// Note the inline function below which ensures that this function is essentially
// copied at the call site to ensure that its logging only recompositions from the
// original call site.
@Composable
inline fun LogCompositions(tag: String, msg: String) {

  val ref = remember { Ref(0) }
  SideEffect { ref.value++ }
  Log.d(tag, "Compositions: $msg ${ref.value}")

}

@Composable
fun ScopedView(content: @Composable () -> Unit) {
  content()
}

@Composable
fun ScopedViewRow(content: @Composable () -> Unit) {
  LogCompositions(tag = "onCreate", msg = "Row (index -}) -- comp")

  Row() {
    content()
  }
}


@Preview
@Composable
fun TwyperPreview() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val items = remember { mutableStateListOf(*('A'..'Z').toList().toTypedArray()) }
    val twyperController = rememberTwyperController()
//    LogCompositions(tag = "TwyperPreview", msg = "Column")
//    ScopedView {
//      Twyper(
//        items = items,
//        twyperController = twyperController,
//        onItemRemoved = { item, direction ->
//          println("Item removed: $item -> $direction")
//          items.remove(item)
//        },
//        onEmpty = {
//          println("End reached")
//        }
//      ) { item ->
//        Box(
//          modifier = Modifier
//            .size(300.dp)
//            .background(
//              brush = Brush.horizontalGradient(
//                listOf(
//                  Color.Red,
//                  Color.Blue,
//                )
//              )
//            ),
//          contentAlignment = Alignment.Center
//        ) {
//          LogCompositions(tag = "TwyperPreview", msg = "Box")
//          ScopedView {
//            Text(text = "$item", fontSize = 200.sp, color = Color.White)
//          }
//
//        }
//      }
//    }


    Spacer(modifier = Modifier.height(50.dp))

    Row(
      horizontalArrangement = Arrangement.spacedBy(30.dp),
    ) {

      IconButton(onClick = {
        twyperController.swipeLeft()
      }) {
        Text(text = "❌", fontSize = 30.sp)
      }

      IconButton(onClick = {
        twyperController.swipeRight()
      }) {
        Text(text = "✅", fontSize = 30.sp)
      }
    }
  }
}