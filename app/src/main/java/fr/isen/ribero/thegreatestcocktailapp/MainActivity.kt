package fr.isen.ribero.thegreatestcocktailapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.ribero.thegreatestcocktailapp.screens.CategoriesScreen
import fr.isen.ribero.thegreatestcocktailapp.screens.BottomAppBar
import fr.isen.ribero.thegreatestcocktailapp.screens.DetailCocktailScreen
import fr.isen.ribero.thegreatestcocktailapp.screens.DrinksScreen
import fr.isen.ribero.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

data class TabBarItem(
    var title : String,
    var selectedIcon : ImageVector,
    var unselectedIcon : ImageVector
)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var context = LocalContext.current
            val navController = rememberNavController()

            val tabItems = listOf(
            TabBarItem("Random", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home),
            TabBarItem("Category", selectedIcon = Icons.Filled.Menu, unselectedIcon = Icons.Outlined.Menu),
            TabBarItem("Favorite", selectedIcon = Icons.Filled.Favorite, unselectedIcon = Icons.Outlined.Favorite))

            TheGreatestCocktailAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "TopApp Bar"
                                )
                            },
                            actions = {
                                IconButton({
                                    Toast.makeText(context, "Add to favorite", Toast.LENGTH_LONG).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.FavoriteBorder,
                                        contentDescription = "Localized description"
                                    )
                                }
                            })
                    }, bottomBar = {BottomAppBar(tabItems, navController)}
                ){ innerPadding ->
                    NavHost(navController, startDestination = tabItems[0].title){
                        composable(tabItems[0].title){
                            CategoriesScreen(
                                Modifier.padding(innerPadding))
                        }
                        composable(tabItems[1].title){
                            DrinksScreen(
                                Modifier.padding(innerPadding))
                        }
                        composable(tabItems[2].title){
                            DetailCocktailScreen(
                                Modifier.padding(innerPadding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text("Hello $name!")
        Text("Hello Isen")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheGreatestCocktailAppTheme {
        Greeting("Android")
    }
}

