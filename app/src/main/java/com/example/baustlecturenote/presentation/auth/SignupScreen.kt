package com.example.baustlecturenote.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.baustlecturenote.presentation.auth.components.Banner
import com.example.baustlecturenote.presentation.auth.components.CustomOutlinedTextField
import com.example.baustlecturenote.utils.Resource
import android.widget.Toast
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.baustlecturenote.utils.Screen

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavController
) {
    val signupState by viewModel.signupState.collectAsState()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Handle signup state changes
    signupState?.let { state ->
        when (state) {
            is Resource.Loading -> {
                if (state.isLoading) {
                    Toast.makeText(context, "Signing up...", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Signup completed", Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Success -> {
                Toast.makeText(
                    context,
                    "Signup successful!",
                    Toast.LENGTH_SHORT
                ).show()
                // Save token to SharedPreferences
                state.data?.token?.let { token ->
                    context.getSharedPreferences("baustlecturenote", android.content.Context.MODE_PRIVATE)
                        .edit()
                        .putString("token", token)
                        .putString("userId", state.data.id)
                        .apply()
                }
                navController.navigate(Screen.SigninScreen){
                    popUpTo(Screen.SignupScreen) {
                        inclusive = true
                    }
                }
            }
            is Resource.Error -> {
                Toast.makeText(context, "Signup failed: please try again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Banner(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Create an account",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            placeholder = "Enter your email",
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomOutlinedTextField(
            placeholder = "Enter your password",
            value = password,
            isPassword = true,
            onValueChange = { password = it },
            label = "Password",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signup(email, password)
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "SIGN UP",
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Already have an account? ",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.SigninScreen) {
                        popUpTo(Screen.SignupScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}