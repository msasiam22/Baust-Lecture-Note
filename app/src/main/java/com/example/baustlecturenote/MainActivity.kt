package com.example.baustlecturenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.baustlecturenote.ui.theme.BaustLectureNoteTheme
import com.example.baustlecturenote.utils.NavigationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaustLectureNoteTheme {
                val pref = this.getSharedPreferences("baustlecturenote", MODE_PRIVATE)
                val navController = rememberNavController()
                NavigationHelper(
                    navController = navController,
                    pref = pref
                )
            }
        }
    }
}