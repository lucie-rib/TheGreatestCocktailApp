package fr.isen.ribero.thegreatestcocktailapp

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.material3.Card
import androidx.compose.ui.res.painterResource

@Composable
fun DetailCocktailScreen() {
    Column {
        Image(painter = painterResource(id = R.drawable.yoghurt_cooler), contentDescription = "Cocktail picture")
        Text(text = "Yoghurt Cooler")
        Row{
            Text(text = "Other/Unknown")
            Text(text = "Non alcoholic")
        }
        Text(text = "Highball Glass")
        Card() {
            Text(text = "Ingredients")
            Text(text = "Yoghurt: 1 cup")
            Text(text = "Fruit: 1 cup")
        }
        Card() {
            Text(text = "Recipe")
        }
    }
}