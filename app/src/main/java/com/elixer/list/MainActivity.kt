package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.elixer.list.ui.theme.ListTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.elixer.list.ui.PlayerWrapper
import com.elixer.list.ui.VideoPlayer

import com.github.theapache64.twyper.rememberTwyperController
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@UnstableApi
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ListTheme {
        // A surface container using the 'background' color from the theme

        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

          var movieList = remember {
            mutableStateListOf(
              mock4.get(0),
              mock4.get(1),
              mock4.get(2),
              mock4.get(3),
              mock4.get(4),
              mock4.get(5),
            )
          }

          var toggle by remember { mutableStateOf(false) }
          Column {
//            MoviesScreenStackWithKey(movieList.toList().takeLast(2)) {
//              val index = movieList.indexOf(it)
//              movieList.removeAt(index)
//            }
//          }
            Button(onClick = { movieList.removeLast() }) {
              Text(text = "take index ")
            }
            LogCompositions(tag = "Swipe", msg = "Column 0")


            ListNewLatest(movieList.takeLast(2))


//
//              Box(modifier = Modifier.width(100.dp)) {
//                VideoPlayerCard(movieList.get(2).media?.url.toString(), true)
//
//              }
//
//
//              Box(
//                Modifier
//                  .height(100.dp)
//                  .width(500.dp)
//              ) {
//                VideoPlayerCard(movieList.get(0).media?.url.toString(), true)
//
//              }
//
//              Column (){
//                Spacer(modifier = Modifier.padding(top = 50.dp))
//                Column (
//                  Modifier
//                    .height(100.dp)
//                    .width(100.dp)){
//                  VideoPlayerCard(movieList.get(1).media?.url.toString(), true)
//
//                }
//
//              }

          }


//

        }
      }
    }

    @Composable
    fun List(myList: SnapshotStateList<Pair<Int, Boolean>>, onClick: () -> Unit) {
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
  private fun ListNewLatest(movieList: List<GameEntry>) {
    val context = LocalContext.current
    val application = context.applicationContext as CultApplication
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val exoPlayer = remember {
      ExoPlayer.Builder(context).build()
//        .apply {
//          val mediaItem: MediaItem = MediaItem.fromUri(mock4.get(0).media?.url.toString())
//          val httpDataSourceFactory: DefaultHttpDataSource.Factory =
//            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
//          val defaultDataSourceFactory: DefaultDataSource.Factory =
//            DefaultDataSource.Factory(context, httpDataSourceFactory)
//          val cacheDataSourceFactory: CacheDataSource.Factory? = application.simpleCache?.let {
//            CacheDataSource.Factory()
//              .setCache(it)
//              .setUpstreamDataSourceFactory(defaultDataSourceFactory)
//              .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
//          }
//
//          val mediaSource: ProgressiveMediaSource? = cacheDataSourceFactory?.let {
//            ProgressiveMediaSource.Factory(it)
//              .createMediaSource(mediaItem)
//          }
//          mediaSource?.let { setMediaSource(it, true) }
//          playWhenReady = true
//          videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//          repeatMode = Player.REPEAT_MODE_ONE
//          prepare()
//        }
    }

    val exoPlayer2 = remember {
      ExoPlayer.Builder(context).build()
//        .apply {
//          val mediaItem: MediaItem = MediaItem.fromUri(com.elixer.list.mock4.get(2).media?.url
//            .toString())
//          val httpDataSourceFactory: DefaultHttpDataSource.Factory =
//            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
//          val defaultDataSourceFactory: DefaultDataSource.Factory =
//            DefaultDataSource.Factory(context, httpDataSourceFactory)
//          val cacheDataSourceFactory: CacheDataSource.Factory? = application.simpleCache?.let {
//            CacheDataSource.Factory()
//              .setCache(it)
//              .setUpstreamDataSourceFactory(defaultDataSourceFactory)
//              .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
//          }
//
//          val mediaSource: ProgressiveMediaSource? = cacheDataSourceFactory?.let {
//            ProgressiveMediaSource.Factory(it)
//              .createMediaSource(mediaItem)
//          }
//          mediaSource?.let { setMediaSource(it, true) }
//          playWhenReady = true
//          videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//          repeatMode = Player.REPEAT_MODE_ONE
//          prepare()
//        }
    }

    val originList = remember { mutableStateListOf<GameEntry>() }
    Log.d("Swipe", "Origin list ${originList.map { it.id }.toString()}")
    Box() {
      LogCompositions(tag = "Swipe", msg = "Box")
      ScopedView {


        originList.reversed().forEachIndexed { index, gameEntry ->
          
          Column() {
            if (index == 1) Spacer(modifier = Modifier.height(200.dp))
            VideoPlayer(
              modifier = Modifier
                .width(200.dp)
                .height(300.dp),
              playerWrapper = if (index == 0) PlayerWrapper(exoPlayer) else PlayerWrapper(exoPlayer2)
            ) {

            }
          }

        
        }
      }
    }

    LaunchedEffect(movieList) {
      try {
        originList.removeAt(0)
      } catch (ex: Exception) {

      }
      Log.d("Swipe", movieList.toString())
      movieList.getOrNull(0)?.media?.url?.let { url ->
        val mediaItem: MediaItem = MediaItem.fromUri(url.orEmpty())
        exoPlayer.apply {
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

      movieList.getOrNull(1)?.media?.url?.let {
        val mediaItem2: MediaItem = MediaItem.fromUri(it.orEmpty())
        exoPlayer2.apply {
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
              .createMediaSource(mediaItem2)
          }
          mediaSource?.let { setMediaSource(it, true) }
          playWhenReady = true
          videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
          repeatMode = Player.REPEAT_MODE_ONE
          prepare()
        }
      }
      movieList.reversed().forEach {
        if (!originList.contains(it)) originList.add(it)
      }
    }
//    Column() {
//      LogCompositions(tag = "Swipe", msg = "Box -1")
//
//      movieList.forEachIndexed { index, gameEntry ->
//        Box(
//          modifier = Modifier
//            .padding(horizontal = 40.dp)
//            .aspectRatio(9 / 16f)
//        ) {
//          Spacer(modifier = Modifier.height((index * 40).dp))
//          LogCompositions(tag = "Swipe", msg = "Box")
//          key(gameEntry.id) {
////            VideoPlayerCard(gameEntry.media?.url.toString(), true)
//            VideoPlayer(
//              modifier = Modifier.width(100.dp), playerWrapper = PlayerWrapper(exoPlayer)
//            ) {
//
//            }
//
//          }
//
//        }
//      }
//    }
    DisposableEffect(key1 = Unit) {
      onDispose {
        Log.d("Swipe", "ListNewLatest disposed")

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
  fun MoviesScreenStackWithKey(movies: List<GameEntry>, onSwiped: (GameEntry) -> Unit) {
    Log.d("onCreate", movies.toString())
    Column {
      LogCompositions(tag = "onCreate", msg = "MoviesScreenWithKey Column")

      val currentMovie by remember(movies.last()) {
        mutableStateOf(movies.last())
      }
      val onNameEnteredClick: (value: GameEntry) -> Unit = remember { return@remember onSwiped }

      fun isCurrent(movie: GameEntry): Boolean {
        return currentMovie.id == movie.id
      }
//    val isActive: (value: Movie) -> Boolean = remember { return@remember (movies.last() == value) }

      ScopedView {
        Box() {
          movies.forEachIndexed { index, it ->
            key(it.id) {
//            ScopedView {
              MovieOverview(
                it,
                onSwiped = { onNameEnteredClick(it) }
              ) {
                isCurrent(it)
//              ::onSwiped(it)
              }
            }
          }
        }
//      LazyColumn() {
//        items(
//          items = movies,
//          key = { message ->
//            // Return a stable + unique key for the item
//            message.id
//          }
//        ) { it ->
//          MovieOverviewNew(it)
//        }
//      }

//      Box() {
//        movies.forEach {
//          key(it.id) {
//            MovieOverviewNew(it)
//          }
//        }
//      }
      }
    }
  }
//  Button(onClick = {
//    val movie = movies.lastOrNull()
//    movie?.let { onSwiped(it) }
//  }) {
//    Text(text = "onclick")
//  }
}


@Composable
fun MovieOverview(
  movie: GameEntry,
//  onDrag: (PointerInputChange, Offset) -> Unit,
  onSwiped: (GameEntry) -> Unit,
  isActiveProvider: () -> Boolean
) {

//  Log.d("oncreate", "is Active ${isActiveProvider()}, movie ${movie.id}")
  val scope = rememberCoroutineScope()
//  val screenWidth =
//    with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
//  val screenHeight =
//    with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
  val screenWidth = 1000f
  val screenHeight = 2000f

  val activeOffset by remember() {
    mutableStateOf(Animatable(Offset.Zero, Offset.VectorConverter))
  }
  var activeS by remember {
    mutableStateOf(false)
  }

  fun onSwipedNew(direction: Direction) {
    onSwiped(movie)
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
  LaunchedEffect(Unit) {
//    while (true) {
//      if (isActiveProvider()) {
//        Log.d("oncreate", "is Active ${isActiveProvider()}, movie ${movie.id}")
//      }
//      delay(1000)
//    }
  }


  LogCompositions(tag = "Oncreate", msg = "Movie Overview -> ${movie.id} comp")
  val random = remember { getRandomColor() }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 30.dp)
      .height(300.dp)
      .clickable { onSwiped(movie) }
      .pointerInput(Unit) {
        coroutineScope {
          detectDragGestures(
            onDragCancel = {
              launch {
                activeOffset.animateTo(Offset(0f, 0f))

//                    state.reset()
//                    onSwipeCancel()
              }
            },
            onDrag = { change, dragAmount ->
//              onDrag(change, dragAmount)
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
//                              swipeNew(Direction.Up)
//                          onSwiped(Direction.Up)
                    } else {
//                              swipeNew(Direction.Down)
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
        translationX = activeOffset.value.x
        translationY = activeOffset.value.y
        rotationZ = (activeOffset.value.x / 60).coerceIn(-40f, 40f)
      }, colors = CardDefaults.cardColors(containerColor = random)
  ) {
    key(movie.id) {
      LogCompositions(tag = "Oncreate", msg = "Movie Overview CARD -> ${movie.id} comp")
//          Text("${movie.id}", color = Color.Black)
//      Box(){
      VideoPlayerCard(movie.media?.url.toString(), false)
//
//      }
    }

//    Text("${activeS}", color = Color.Black)
  }
}

//@Stable
//data class Movie(
//  val id: String
//)

fun getRandomColor(): Color {
  val clr = listOf(Color.Yellow, Color.Red, Color.Cyan, Color.Green)
  return clr.random()
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

//@Composable
//fun MovieOverviewNew(movie: Movie) {
//  Column {
//    LogCompositions(tag = "Oncreate", msg = "Text MovieOverviewNew  ${movie.id} -- ")
//
//    // Side effect explained later in the docs. If MovieOverview
//    // recomposes, while fetching the image is in progress,
//    // it is cancelled and restarted.
//    MovieHeader(movie)
//
//    /* ... */
//  }
//}

// [START_EXCLUDE silent]
//@Composable
//fun MovieHeader(image: Movie) {
//  Text(image.id)
//}

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