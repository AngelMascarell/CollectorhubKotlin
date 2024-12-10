package com.angelmascarell.collectorhub.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.viewmodel.DesiredMangasViewModel
import com.angelmascarell.collectorhub.viewmodel.GetMangaViewModel


@Composable
fun DesiredMangasScreen() {
    val navController = LocalNavController.current
    val desiredMangasViewModel: DesiredMangasViewModel = hiltViewModel()
    val mangaViewModel: GetMangaViewModel = hiltViewModel()

    val averageRate by mangaViewModel.averageRate


    LaunchedEffect(Unit) {
        desiredMangasViewModel.loadUserDesiredCollection()
    }

    val userCollection by desiredMangasViewModel.userCollection.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        CloseButton(navController)

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "Resumen de tu lista de deseados",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
            )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tus deseados: ",
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground
            )
        Spacer(modifier = Modifier.height(16.dp))
        DesiredMangaGrid(
            mangaList = userCollection,
            onItemClick = { manga ->
                navController.navigate("GetMangaScreen/${manga.id}")
            },
            averageRating = averageRate
        )
    }
}

@Composable
fun DesiredMangaGrid(mangaList: List<MangaModel>, onItemClick: (MangaModel) -> Unit, averageRating: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mangaList) { manga ->
            DesiredMangaItem(
                manga = manga,
                onClick = { onItemClick(manga) },
                averageRating = averageRating
            )
        }
    }
}

@Composable
fun DesiredMangaItem(manga: MangaModel, onClick: () -> Unit, averageRating: Int) {
    val imageUrl = "http://10.0.2.2:8080${manga.imageUrl}"

    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = manga.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = manga.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (averageRating > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.ic_star),
                    contentDescription = "Star Icon",
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "%.1f".format(averageRating),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Yellow,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}