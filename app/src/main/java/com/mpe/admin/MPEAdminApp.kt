package com.mpe.admin

import android.app.Application
import com.google.firebase.FirebaseApp

class MPEAdminApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
