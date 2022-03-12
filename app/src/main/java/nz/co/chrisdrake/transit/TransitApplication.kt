package nz.co.chrisdrake.transit

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import nz.co.chrisdrake.transit.data.dataModules
import nz.co.chrisdrake.transit.domain.domainModule
import nz.co.chrisdrake.transit.ui.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class TransitApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Firebase.crashlytics.setCustomKey("git_sha", BuildConfig.GIT_SHA)

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {
            Firebase.crashlytics.setCustomKey("map_render", it.name)
        }

        startKoin {
            androidContext(this@TransitApplication)
            modules(uiModule + domainModule + dataModules)
        }
    }
}