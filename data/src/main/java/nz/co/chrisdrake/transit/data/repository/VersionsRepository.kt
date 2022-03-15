package nz.co.chrisdrake.transit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import nz.co.chrisdrake.transit.data.Version
import nz.co.chrisdrake.transit.data.internal.aucklandtransport.api.ApiService
import nz.co.chrisdrake.transit.data.internal.database.TransitDao
import nz.co.chrisdrake.transit.data.internal.ResetStaticTransitData
import java.time.LocalDateTime
import java.time.ZoneId

class VersionsRepository internal constructor(
    private val apiService: ApiService,
    private val dao: TransitDao,
    private val resetStaticTransitData: ResetStaticTransitData,
) {
    private val mutex = Mutex()
    private var contentVersion: Version? = null

    suspend fun currentVersion(): Version = mutex.withLock {
        withContext(Dispatchers.IO) {
            contentVersion
                ?.takeIf { dao.populated(it) }
                ?.let { return@withContext it }

            dao.deleteVersions()
            dao.insertVersions(apiService.versions().data)

            val currentVersion = checkNotNull(dao.currentVersion())

            if (!dao.populated(currentVersion)) {
                resetStaticTransitData()
            }

            contentVersion = currentVersion

            currentVersion
        }
    }

    private suspend fun TransitDao.currentVersion(): Version? {
        val today = LocalDateTime.now().atZone(ZoneId.of("UTC"))

        return versions()
            .firstOrNull { it.contains(today) }
            ?.version
    }
}