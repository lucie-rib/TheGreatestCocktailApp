package fr.isen.ribero.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.ribero.thegreatestcocktailapp.screens.DetailCocktailScreen
import fr.isen.ribero.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class DetailCocktailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkId = intent.getStringExtra(DRINKID) ?: ""
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DetailCocktailScreen( drinkId, Modifier.padding(innerPadding))
                }
            }
        }
    }

    companion object {
        const val DRINKID = "drinkid"
    }
}