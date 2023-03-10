package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elixer.list.ui.theme.ListTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.github.theapache64.twyper.rememberTwyperController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ListTheme {
        // A surface container using the 'background' color from the theme

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

          var movieList = remember {
            mutableStateListOf(
              Movie("1"), Movie("2"), Movie("3"),
              Movie("4"),
            )

          }
          Column {
            MoviesScreenStackWithKey(movies = movieList.takeLast(2)) {
              movieList.remove(it)
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
        mutableStateOf("100")
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
fun MoviesScreenStackWithKey(movies: List<Movie>, onSwiped: (Movie) -> Unit) {
  Column {
    LogCompositions(tag = "onCreate", msg = "MoviesScreenWithKey COlumn")
//    val state = rememberSwipeableCardState()
//    Log.d("MoviesScreenWithKey", "state ${state}")
//    val
    val screenWidth =
      with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeight =
      with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val scope = rememberCoroutineScope()
//    var activeCardState = rememberSwipeableCardState()
//    val queuedCardState = rememberSwipeableCardState()
    val activeOffset by remember {
      mutableStateOf(
        Animatable(
          Offset.Zero, Offset
            .VectorConverter
        )
      )
    }

//    val mutaList = remember { mutableStateListOf<Movie>() }

    var currentMovie by remember { mutableStateOf<Movie?>(movies.last()) }

//    fun moveLastEntryToCurrent(movies: List<Movie>) {
//      currentMovie = movies.last()
//      scope.launch {
//        delay(200)
//        activeOffset.snapTo(Offset.Zero)
//      }
//    }

    LaunchedEffect(movies) {
      currentMovie = movies.last()
    }

    fun onSwipedNew(direction: Direction) {
      currentMovie?.let {
        onSwiped(it)
      }
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

    LaunchedEffect(movies) {
      currentMovie = movies.last()
      activeOffset.snapTo(Offset(0f,0f))
    }

    Box() {
      movies.forEach {
        ScopedView {
          key(it.id) {
            MovieOverview(modifier = Modifier
              .pointerInput(Unit) {
                coroutineScope {
                  detectDragGestures(
                    onDragCancel = {
                      launch {
//                    state.reset()
//                    onSwipeCancel()
                      }
                    },
                    onDrag = { change, dragAmount ->
                      launch {
                        val original = activeOffset.targetValue
                        val summed = original + dragAmount
                        val newValue = Offset(
                          x = summed.x.coerceIn(-screenWidth, screenWidth),
                          y = summed.y.coerceIn(-screenHeight, screenHeight)
                        )
                        if (change.positionChange() != Offset.Zero) change.consume()
                        activeOffset.animateTo(Offset(newValue.x, newValue.y))
                      }
                    },
                    onDragEnd = {
                      launch {
                        val coercedOffset = activeOffset.targetValue
                          .coerceIn(
                            listOf(Direction.Up, Direction.Down),
                            maxHeight = screenHeight,
                            maxWidth = screenWidth
                          )

                        if (hasNotTravelledEnoughNew(
                            screenWidth,
                            screenHeight,
                            coercedOffset
                          )
                        ) {
                          activeOffset.animateTo(Offset.Zero, tween(400))
                        } else {
                          val horizontalTravel = abs(activeOffset.targetValue.x)
                          val verticalTravel = abs(activeOffset.targetValue.y)

                          if (horizontalTravel > verticalTravel) {
                            if (activeOffset.targetValue.x > 0) {
                              swipeNew(Direction.Right)
//                          onSwiped(Direction.Right)
                            } else {
                              swipeNew(Direction.Left)
//                          onSwiped(Direction.Left)
                            }
                          } else {
                            if (activeOffset.targetValue.y < 0) {
                              swipeNew(Direction.Up)
//                          onSwiped(Direction.Up)
                            } else {
                              swipeNew(Direction.Down)
//                          onSwiped(Direction.Down)
                            }
                          }
                        }
                      }
                    }
                  )
                }
              }
              .graphicsLayer {
                if (it == currentMovie) {
                  translationX = activeOffset.value.x
                  translationY = activeOffset.value.y
                  rotationZ = (activeOffset.value.x / 60).coerceIn(-40f, 40f)
                } else {
                  translationX = 0f
                  translationY = 0f
                  rotationZ = 0f
                }
              }, movie = it
            )
          }
        }
      }
    }
  }
}


@Composable
fun MovieOverview(modifier: Modifier, movie: Movie) {
  LogCompositions(tag = "Oncreate", msg = "Movie Overview -> ${movie.id} comp")
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 30.dp)
      .height(250.dp),
    colors = CardDefaults.cardColors(containerColor = getRandomColor())
  ) {
    Text(movie.id, color = Color.Black)
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

fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
  clear()
  addAll(newList)
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