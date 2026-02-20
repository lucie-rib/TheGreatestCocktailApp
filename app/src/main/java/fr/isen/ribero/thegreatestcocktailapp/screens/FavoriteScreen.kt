package fr.isen.ribero.thegreatestcocktailapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import fr.isen.ribero.thegreatestcocktailapp.models.AppBarState

@Composable
fun FavoritesScreen(modifier: Modifier, onComposing: (AppBarState) -> Unit) {
    LaunchedEffect(Unit) {
        onComposing(
            AppBarState("Favorites")
        )
    }
    Column(modifier) {
        Text("Favorite View")
    }
}