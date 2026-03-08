package fr.isen.ribero.thegreatestcocktailapp.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.ribero.thegreatestcocktailapp.DetailCocktailActivity
import fr.isen.ribero.thegreatestcocktailapp.R
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.DrinkPreview
import fr.isen.ribero.thegreatestcocktailapp.network.ApiClient
import retrofit2.Call
import retrofit2.Response

@Composable
fun DrinksScreen(modifier: Modifier, category: String) {
    val drinks = remember { mutableStateOf<List<DrinkPreview>?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val call = ApiClient.retrofit.getDrinksPreview(category)
        call.enqueue(object : retrofit2.Callback<DrinkFilterResponse> {
            override fun onResponse(
                call: Call<DrinkFilterResponse?>?,
                response: Response<DrinkFilterResponse?>?
            ) {
                drinks.value = response?.body()?.drinks
            }

            override fun onFailure(call: Call<DrinkFilterResponse?>, t: Throwable) {
            }
        })
    }

    drinks.value?.let { list ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { drink ->
                DrinkPreviewItem(drink) {
                    val intent = Intent(context, DetailCocktailActivity::class.java).apply {
                        putExtra(DetailCocktailActivity.DRINKID, drink.idDrink)
                        putExtra("DRINK_NAME", drink.strDrink)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun DrinkPreviewItem(drink: DrinkPreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.cocktail_orange).copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = drink.strDrinkThumb,
                contentDescription = drink.strDrink,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = drink.strDrink ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = colorResource(R.color.white)
                )
            }

            Text(
                text = "→",
                fontSize = 20.sp,
                color = colorResource(R.color.white),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}