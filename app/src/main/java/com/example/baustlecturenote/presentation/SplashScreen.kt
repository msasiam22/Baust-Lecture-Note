package com.example.baustlecturenote.presentation

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.baustlecturenote.R
import com.example.baustlecturenote.presentation.auth.SigninScreen
import com.example.baustlecturenote.presentation.create_note.CreateNoteScreen
import com.example.baustlecturenote.ui.theme.LightGreen
import com.example.baustlecturenote.utils.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    pref: SharedPreferences
) {
    val token = pref.getString("token", "")
    LaunchedEffect(Unit) {
        delay(3000)
        if (!token.isNullOrEmpty()) {
            navController.navigate(Screen.HomeScreen) {
                popUpTo(Screen.SplashScreen) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(Screen.SigninScreen) {
                popUpTo(Screen.SplashScreen) {
                    inclusive = true
                }
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painterResource(id = R.drawable.logo),
            contentDescription = "BaustLectureNote Logo",
            modifier = modifier
                .size(200.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "BAUST Lecture Note",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}