package fr.isen.ribero.thegreatestcocktailapp.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import fr.isen.ribero.thegreatestcocktailapp.R
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.ribero.thegreatestcocktailapp.managers.FavoritesManager
import fr.isen.ribero.thegreatestcocktailapp.models.AppBarState
import fr.isen.ribero.thegreatestcocktailapp.network.ApiClient
import retrofit2.Call
import retrofit2.Response

@Composable
fun RandomCocktailScreen(modifier: Modifier, onComposing: (AppBarState) -> Unit) {
    var drink = remember { mutableStateOf<Drink?>(null) }

    LaunchedEffect(drink.value) {
        onComposing(
            AppBarState(
                title = "Random Cocktail",
                actions = { DetailCocktailTopButton(drink.value) }
            )
        )
    }

    LaunchedEffect(Unit) {
        val call = ApiClient.retrofit.getRandomCocktail()
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            override fun onResponse(call: Call<CocktailResponse?>, response: Response<CocktailResponse?>) {
                drink.value = response.body()?.drinks?.firstOrNull()
            }
            override fun onFailure(call: Call<CocktailResponse?>, t: Throwable) {
                Log.e("request", "getrandom failed ${t.message}")
            }
        })
    }

    drink.value?.let { currentDrink ->
        DetailCocktailScreen(modifier, currentDrink)
    } ?: run {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    }
}

@Composable
fun DetailCocktailScreen(drinkId: String,
                         onComposing: (AppBarState) -> Unit,
                         modifier: Modifier) {
    var drink = remember { mutableStateOf<Drink?>(null) }
    val context = LocalContext.current
    val intentName = (context as? android.app.Activity)?.intent?.getStringExtra("DRINK_NAME") ?: "Cocktail Detail"

    LaunchedEffect(drink.value) {
        onComposing(
            AppBarState(
                title = drink.value?.strDrink ?: intentName,
                actions = { DetailCocktailTopButton(drink.value) }
            )
        )
    }

    LaunchedEffect(drinkId) {
        val call = ApiClient.retrofit.getDetailCocktail(drinkId)
        call.enqueue(object : retrofit2.Callback<CocktailResponse> {
            override fun onResponse(call: Call<CocktailResponse?>, response: Response<CocktailResponse?>) {
                drink.value = response.body()?.drinks?.firstOrNull()
            }
            override fun onFailure(call: Call<CocktailResponse?>, t: Throwable) {
                Log.e("request", "getdetail failed ${t.message}")
            }
        })
    }

    drink.value?.let { drinkItem ->
        DetailCocktailScreen(modifier, drinkItem)
    } ?: run {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    }
}

@Composable
fun DetailCocktailScreen(modifier: Modifier, drink: Drink) {
    Box(
        Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.cocktail_orange),
                        colorResource(R.color.cocktail_pink)
                    )
                )
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(1.dp, colorResource(R.color.cocktail_yellow), CircleShape)
            )

            Text(
                text = drink.strDrink ?: "",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white),
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                drink.strCategory?.let { InfoBadge(it) }
                drink.strAlcoholic?.let { InfoBadge(it) }
            }

            Text(
                drink.strGlass ?: "",
                color = colorResource(R.color.white)
            )

            Card {
                Column(Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(stringResource(R.string.ingredient), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    val ingredients = drink.ingredientList()
                    if (ingredients.isEmpty()) {
                        Text("No ingredients specified")
                    } else {
                        ingredients.forEach { (ingredient, measure) ->
                            Text(if (measure.isNotBlank()) "$measure $ingredient" else ingredient)
                        }
                    }
                }
            }

            Card {
                Column(Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(stringResource(R.string.preparation), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(drink.strInstructions ?: "No instructions available.")
                }
            }
        }
    }
}

@Composable
fun InfoBadge(text: String) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(
                Brush.horizontalGradient(
                    listOf(colorResource(R.color.cocktail_yellow), colorResource(R.color.cocktail_orange))
                )
            )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = colorResource(R.color.black),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun DetailCocktailTopButton(drink: Drink?) {
    val context = LocalContext.current
    val favoritesManager = FavoritesManager()

    var isFavorite by remember(drink?.idDrink) {
        mutableStateOf(drink?.let { favoritesManager.isFavorite(it, context) } ?: false)
    }

    drink?.let { currentDrink ->
        IconButton(onClick = {
            favoritesManager.toggleFavorite(currentDrink, context)
            isFavorite = favoritesManager.isFavorite(currentDrink, context)
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite icon",
                tint = if (isFavorite) colorResource(R.color.cocktail_pink) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}