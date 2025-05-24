package com.example.baustlecturenote.utils

import PdfViewerScreen
import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.baustlecturenote.presentation.HomeScreen
import com.example.baustlecturenote.presentation.SplashScreen
import com.example.baustlecturenote.presentation.auth.SignupScreen
import com.example.baustlecturenote.presentation.auth.SigninScreen
import com.example.baustlecturenote.presentation.create_note.CreateNoteScreen

@Composable
fun NavigationHelper(
    navController: NavHostController,
    pref: SharedPreferences
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen
    ){
        composable<Screen.SplashScreen>{
            SplashScreen(
                navController = navController,
                pref = pref
            )
        }
        composable<Screen.SignupScreen> {
            SignupScreen(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController
            )
        }
        composable<Screen.SigninScreen> {
            SigninScreen(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController
            )
        }
        composable<Screen.HomeScreen> {
            HomeScreen(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController,
                pref = pref
            )
        }

        composable<Screen.CreateNoteScreen> {
            CreateNoteScreen(
                modifier = Modifier
                    .fillMaxSize(),
                navController = navController
            )
        }

        composable<Screen.PdfViewerScreen> { backStackEntry ->
            val pdfUrl = backStackEntry.toRoute<Screen.PdfViewerScreen>().pdfUrl
            PdfViewerScreen(
                pdfUrl = pdfUrl,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}