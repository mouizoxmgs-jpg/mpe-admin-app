package com.mpe.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mpe.admin.ui.navigation.NavGraph
import com.mpe.admin.ui.theme.MPEAdminTheme
import com.mpe.admin.viewmodel.AdminViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MPEAdminTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AdminViewModel = viewModel()
                    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

                    LaunchedEffect(toastMessage) {
                        toastMessage?.let {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                            viewModel.clearToast()
                        }
                    }

                    NavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
