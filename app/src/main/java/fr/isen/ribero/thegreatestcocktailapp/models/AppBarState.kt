package fr.isen.ribero.thegreatestcocktailapp.models

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable

class AppBarState(
    val title: String = "",
    val actions: (@Composable RowScope.() -> Unit)? = null
)

//class AppBarState{
   // val title: String = ""
   // val actions: (@Composable RowScope.()-> Unit)? = null)

       //     init(title: String, actions: @Composable RowScope.()-> Unit)? = null){
         //       this.title = title
     //   this.actions = actions
    //}
//}