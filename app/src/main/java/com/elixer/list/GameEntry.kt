package com.elixer.list


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

val mock4 = listOf(
  GameEntry(
    authorId = 1693470030420771857, id = 1, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 1,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 2, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 2,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 3, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 3,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/ebb81dea-d559-4c25-8793-fe99f9a3a746.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 4, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 4,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/958656f7-949b-4b25-b4a3-9ea9ad82212b.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 5, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 5,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/a5aa932d-d631-4a77-ad5a-15f916efcb89.mp4",
    ),
    mediaId = 17,
  ),
  GameEntry(
    authorId = 1693470030420771857, id = 6, instanceId = 1697052293175706907, invitationId = null,
    media = Media(
      id = 6,
      url = "https://cdn-cult.s3.us-west-2.amazonaws.com/media/b71bb799-4fd4-4c24-b9c0-fd064de3a22d.mp4",
    ),
    mediaId = 17,
  ),

  )