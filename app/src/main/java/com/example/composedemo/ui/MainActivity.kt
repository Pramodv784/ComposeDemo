package com.example.composedemo.ui

import RepoDetailScreen
import HomeScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composedemo.model.GitResponse
import com.example.composedemo.model.ItemArgsType
import com.example.composedemo.model.LocalData

import com.example.composedemo.ui.theme.ComposeDemoTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    companion object{
       const val  HOME_SCREEN = "homescreen"
       const val  DETAIL_SCREEN = "detail_screen"
        const val REPO_ARGS ="repo_args"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //  var  viewModel: MainViewModel = hiltViewModel()

            //  viewModel.getMeal()
            //  val data by viewModel.mealList.observeAsState()

            //Log.d("Apoi Daata","${data?.categories?.get(0)?.strCategory}")
            UserApplication()
            // ImageScreen()
        }
    }


    @Composable
    fun UserApplication() {

        ComposeDemoTheme(darkTheme = false) {
            val composeNavController = rememberNavController()
            NavHost(navController = composeNavController, startDestination = HOME_SCREEN) {
                composable(route = HOME_SCREEN) {
                    HomeScreen(composeNavController)
                }
                composable(
                    route = "$DETAIL_SCREEN/{$REPO_ARGS}",
                    arguments = listOf(navArgument(REPO_ARGS) {
                        type = ItemArgsType()
                    })
                ) { navBackStack ->
                    val repoData = navBackStack.arguments?.getString(REPO_ARGS)
                        ?.let { Gson().fromJson(it, LocalData::class.java) }
                    RepoDetailScreen(repoData!!, composeNavController)
                }



            }

        }

    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        HomeScreen(null)
    }
}