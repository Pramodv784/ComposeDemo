import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.composedemo.model.GitResponse
import com.example.composedemo.model.LocalData
import com.example.composedemo.utils.LinkText.LinkifyText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RepoDetailScreen(data: LocalData, navController: NavHostController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(data.name, color = Color.White,style = MaterialTheme.typography.titleLarge)},
            backgroundColor = MaterialTheme.colorScheme.primary,
            navigationIcon = {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(Icons.Filled.ArrowBack, "backIcon", tint = Color.White)
                }
            },
        )
    }, ) {
        BoxWithConstraints(Modifier.padding(it), contentAlignment = Alignment.TopCenter) {
            Surface(
                modifier = Modifier.fillMaxSize(),

                ) {

                Column (
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ){
                    Spacer(modifier = Modifier.heightIn(20.dp))
                    Image(painter = rememberImagePainter(data = data.image),
                        contentDescription = ""
                        ,  modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,)
                    Spacer(modifier = Modifier.heightIn(20.dp))



                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(5.dp)

                        ) {
                            Text(
                                text = "Repository Name",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = " ${data.name}",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = " ${data.description}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Contributors URL",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            LinkifyText(
                                text = " ${data.contributors}",
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Repository URL",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            LinkifyText(
                                text = " ${data.projectLink}",
                            )


                        }


                }
            }
        }

        
    }




}

