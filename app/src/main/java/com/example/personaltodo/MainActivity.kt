package com.example.personaltodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import com.example.personaltodo.featureevents.presentation.ui.view.EventListComponent
import com.example.personaltodo.ui.theme.PersonalToDoTheme
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appSecret = "dbe8aa27-777b-4df0-8ea3-d13e85567366"

        AppCenter.start(
            application,
            appSecret,
            Analytics::class.java,
            Crashes::class.java
        )

        setContent {
            PersonalToDoTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) {
                    EventListComponent(it, snackbarHostState)
                }
            }
        }
    }
}