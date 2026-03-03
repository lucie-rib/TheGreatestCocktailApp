package fr.isen.ribero.thegreatestcocktailapp.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.ribero.thegreatestcocktailapp.DrinksActivity
import fr.isen.ribero.thegreatestcocktailapp.R
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.CategoryListResponse
import fr.isen.ribero.thegreatestcocktailapp.dataClasses.DrinkCategory
import fr.isen.ribero.thegreatestcocktailapp.models.AppBarState
import fr.isen.ribero.thegreatestcocktailapp.network.ApiClient
import retrofit2.Call
import retrofit2.Response

@Composable
fun CategoriesScreen(modifier: Modifier, onComposing: (AppBarState) -> Unit) {
    val context = LocalContext.current
    val categories = remember { mutableStateOf<List<DrinkCategory>?>(null) }

    LaunchedEffect(Unit) {
        onComposing(AppBarState("Categories"))
        val call = ApiClient.retrofit.getCategories()
        call.enqueue(object : retrofit2.Callback<CategoryListResponse> {
            override fun onResponse(
                call: Call<CategoryListResponse>,
                response: Response<CategoryListResponse>
            ) {
                categories.value = response.body()?.drinks
            }

            override fun onFailure(call: Call<CategoryListResponse>, t: Throwable) {
                Log.e("request", "getCategories failed ${t.message}")
            }
        })
    }

    categories.value?.let { list ->
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(context, DrinksActivity::class.java)
                            intent.putExtra(DrinksActivity.CATEGORY, category.strCategory)
                            context.startActivity(intent)
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.cocktail_yellow).copy(alpha = 0.7f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.strCategory ?: "",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = colorResource(R.color.black)
                        )

                        Text(
                            text = "→",
                            fontSize = 20.sp,
                            color = colorResource(R.color.black)
                        )
                    }
                }
            }
        }
    }
}