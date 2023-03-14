package com.elixer.list

import android.app.Application
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache


// Application context is safe to make Global without memory leakages
lateinit var application: CultApplication

// Application class is initialized before activity.
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
class CultApplication : Application() {
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
