package com.example.baustlecturenote.utils
import  kotlinx.serialization.Serializable

@Serializable
sealed interface Screen{
    @Serializable
    data object SplashScreen: Screen
    @Serializable
    data object SignupScreen: Screen
    @Serializable
    data object SigninScreen: Screen
    @Serializable
    data object HomeScreen: Screen
    @Serializable
    data object CreateNoteScreen: Screen
    @Serializable
    data class PdfViewerScreen(val pdfUrl: String) : Screen
}