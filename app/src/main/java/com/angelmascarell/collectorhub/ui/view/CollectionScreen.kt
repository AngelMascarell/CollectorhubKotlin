package com.angelmascarell.collectorhub.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.home.presentation.PremiumAdBanner
import com.angelmascarell.collectorhub.viewmodel.CollectionViewModel

@Composable
fun CollectionScreen() {
    val navController = LocalNavController.current
    val collectionViewModel: CollectionViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        collectionViewModel.loadUserCollection()
    }

    val userCollection by collectionViewModel.userCollection.observeAsState(emptyList())
    val genresCount by collectionViewModel.genresCount.observeAsState(emptyMap())
    val totalMangas by collectionViewModel.totalMangas.observeAsState(0)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        CloseButton(navController)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Resumen de tu colección",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoCard(
                title = "Total de Géneros",
                value = genresCount.size.toString(),
            )
            InfoCard(
                title = "Total de Mangas",
                value = totalMangas.toString(),
            )
        }

        PremiumAdBanner (onClick = {
            navController.navigate(Routes.PremiumScreenRoute.route)
        })

        Spacer(modifier = Modifier.height(16.dp))

        GenreDistributionScreen(
            genresCount = genresCount
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu colección",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))
        MangaGrid(
            mangaList = userCollection,
            onItemClick = { manga ->
                navController.navigate("GetMangaScreen/${manga.id}")
            }
        )
    }
}


@Composable
fun InfoCard(title: String, value: String) {

    Card(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White),
                textAlign = TextAlign.Center
            )
        }
    }
}



@Composable
fun MangaGrid(mangaList: List<MangaModel>, onItemClick: (MangaModel) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(mangaList) { manga ->
            MangaItem(
                manga = manga,
                onClick = { onItemClick(manga) }
            )
        }
    }
}


@Composable
fun MangaItem(manga: MangaModel, onClick: () -> Unit) {
    val imageUrl = "http://10.0.2.2:8080${manga.imageUrl}"

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = manga.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun GenreDistributionScreen(genresCount: Map<String, Int>) {
    var showChart by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Button(
            onClick = { showChart = !showChart },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = if (showChart) "Ocultar distribución" else "Distribución por género")
        }

        if (showChart) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))

                        GenreDoughnutChart(
                            genresCount = genresCount,
                            modifier = Modifier
                                .size(120.dp)
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))

                        GenreLegend(
                            genresCount = genresCount,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenreDoughnutChart(genresCount: Map<String, Int>, modifier: Modifier = Modifier) {
    val total = genresCount.values.sum().toFloat()
    val colors = listOf(
        Color(0xFFBB86FC),
        Color(0xFF03DAC5),
        Color(0xFFFF5722),
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFFEB3B),
        Color(0xFF9C27B0)
    )

    Canvas(modifier = modifier) {
        var startAngle = 0f
        val outerRadius = size.minDimension / 2
        val innerRadius = outerRadius * 0.60f

        genresCount.entries.forEachIndexed { index, (genre, count) ->
            val sweepAngle = (count / total) * 360f
            val color = colors[index % colors.size]

            // Dibuja el arco de la dona
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = center - Offset(outerRadius, outerRadius),
                size = Size(outerRadius * 2, outerRadius * 2),
                style = Stroke(width = outerRadius - innerRadius)
            )

            startAngle += sweepAngle
        }
    }
}


@Composable
fun GenreLegend(genresCount: Map<String, Int>, modifier: Modifier = Modifier) {
    val colors = listOf(
        Color(0xFFBB86FC),
        Color(0xFF03DAC5),
        Color(0xFFFF5722),
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFFEB3B),
        Color(0xFF9C27B0)
    )

    Column(modifier = modifier) {
        val total = genresCount.values.sum().toFloat()
        genresCount.entries.forEachIndexed { index, (genre, count) ->
            val percentage = (count / total * 100).toInt()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(17.dp)
                        .background(colors[index % colors.size], shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$genre - $percentage%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
}



