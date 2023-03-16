package com.elixer.list

import android.app.Application
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache


// Application context is safe to make Global without memory leakages
lateinit var application: CultApplication

// Application class is initialized before activity.
class CultApplication : Application() {

//  companion object {
//    var simpleCache: SimpleCache? = null
//    var leastRecentlyUsedCacheEvictor: LeastRecentlyUsedCacheEvictor? = null
//    var exoDatabaseProvider: StandaloneDatabaseProvider? = null
//    var exoPlayerCacheSize: Long = 90 * 1024 * 1024
//
//
//  }
  var simpleCache: Cache? = null

  override fun onCreate() {
    // Init prefs
    application = this
    val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
    if (simpleCache == null) {
      simpleCache =
        SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, StandaloneDatabaseProvider(this))
    }



    super.onCreate()
  }
}
