package com.elixer.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player


@Composable
fun  rememberManagedExoPlayer(): State<Player?> = rememberManagedPlayer { context ->
    val builder = ExoPlayer.Builder(context)
//    builder.setMediaSourceFactory(ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context)))
    builder.build().apply {
        playWhenReady = true
    }
}
