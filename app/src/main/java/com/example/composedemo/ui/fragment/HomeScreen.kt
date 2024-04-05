import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composedemo.model.GitResponse
import com.example.composedemo.model.LocalData
import com.example.composedemo.ui.MainActivity
import com.example.composedemo.utils.LinkText.capitalized
import com.example.composedemo.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.Locale


@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun HomeScreen(navController: NavHostController?) {

    val viewModel: MainViewModel = hiltViewModel()

    val searchText = viewModel.searchText.observeAsState()
    val state = viewModel.state
    var isSearchingRepo by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Github Repositories", color = Color.White,
                    style = MaterialTheme.typography.titleLarge) },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        BoxWithConstraints(Modifier.padding(it), contentAlignment = Alignment.TopCenter) {
            Surface(
                modifier = Modifier.fillMaxSize(),

                ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = searchText.value.toString(),
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            cursorColor = Color.Black,
                            disabledLabelColor = Color.Blue,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            IconButton(onClick = {}) {
                                Icon(
                                    Icons.Filled.Search,
                                    "backIcon",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },

                        placeholder = { Text("Search", style = MaterialTheme.typography.titleMedium) },
                        onValueChange = { text ->
                            viewModel.getData(text)
                            isSearchingRepo = true
                        })



                    if (state.data.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(state.data.size) { i ->

                                val data = state.data[i]
                                if (i >= state.data.size - 1 && !state.endReached && !state.loading) {
                                    viewModel.onLoadNextData()
                                }
                                CardItem(data) {
                                    navController?.navigate("${MainActivity.DETAIL_SCREEN}/${data}")
                                }
                            }
                            if (state.loading) {
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }


                    } else {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            if (isSearchingRepo) CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun CardItem(itemData: LocalData, onClick: (() -> Unit)) {
    val name: String? = itemData.name
    val description: String? = itemData.description
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick

    ) {
        Row {
            Image(
                painter = rememberImagePainter(data = itemData.image),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,

                ) {
                if (!itemData.name.isNullOrEmpty())
                    Text(
                        text = name?.capitalized() ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = description ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    color = Color.DarkGray
                )
            }
        }
    }

}



