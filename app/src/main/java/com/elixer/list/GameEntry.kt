package com.elixer.list

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.media3.common.util.UnstableApi
import com.elixer.list.ui.theme.ListTheme


data class GameEntry(

  val authorId: kotlin.Long,

  val id: kotlin.Long,

  val instanceId: kotlin.Long,

  val invitationId: kotlin.Long? = null,

  val media: Media? = null,

  val mediaId: kotlin.Long? = null,

  )


data class Media(

  val id: kotlin.Long,
  val url: kotlin.String,
)

enum class MediaType(val value: kotlin.String) {
  photo("photo"),

  video("video")
}

val mockMovies = listOf(
  GameEntry(
    authorId = 1693470030420771857, id = 0, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 0,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 1, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 1,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 2, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 2,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/ebb81dea-d559-4c25-8793-fe99f9a3a746.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 3, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 3,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/958656f7-949b-4b25-b4a3-9ea9ad82212b.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 4, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 4,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 5, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 6,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4",
    ),
    mediaId = 17,
  ),

  )


@Composable
fun ContentList(movieList: List<GameEntry>) {
  Column() {
    LogCompositions("ContentList Box ")
    ScopedView {
      movieList.forEachIndexed { index, movie ->
//      key(movie.id) {
        Log.e("TAG", " id ->> ${movie.id}, index--> ${index}")
        ContentView(movie, index == 1)
//      }
      }
    }
  }
}

class Ref(var value: Int)

// Note the inline function below which ensures that this function is essentially
// copied at the call site to ensure that its logging only recompositions from the
// original call site.
@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun LogCompositions(tag: String) {
  val ref = remember { Ref(0) }
  SideEffect { ref.value++ }
  Log.d("TAG", "Compositions: $tag: ${ref.value}")
}

@Composable
fun ScopedView(content: @Composable () -> Unit) {
  content()
}