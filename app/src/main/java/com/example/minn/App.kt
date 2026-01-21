package com.example.minn

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.minn.domain.usecase.userUseCase.SetOnlineUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App: Application(), DefaultLifecycleObserver {

    @Inject lateinit var setOnlineUseCase: SetOnlineUseCase

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        CoroutineScope(Dispatchers.IO).launch {
            setOnlineUseCase(true)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        CoroutineScope(Dispatchers.IO).launch {
            setOnlineUseCase(false)
        }
    }
}