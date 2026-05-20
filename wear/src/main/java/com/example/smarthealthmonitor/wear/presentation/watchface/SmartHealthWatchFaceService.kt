package com.example.smarthealthmonitor.wear.watchface

import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import com.example.smarthealthmonitor.wear.presentation.watchface.SmartHealthRenderer

class SmartHealthWatchFaceService : WatchFaceService() {

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {

        return WatchFace(
            WatchFaceType.DIGITAL,
            SmartHealthRenderer(
                applicationContext,
                surfaceHolder,
                watchState,
                complicationSlotsManager,
                currentUserStyleRepository
            )
        )
    }
}