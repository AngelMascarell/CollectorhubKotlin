package com.angelmascarell.collectorhub.ui.view

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.home.presentation.HomeViewModel
import com.angelmascarell.collectorhub.viewmodel.GetMangaViewModel
import com.angelmascarell.collectorhub.viewmodel.MangaDetailState
import kotlinx.coroutines.launch

@Composable
fun GetMangaScreen() {
    val navController = LocalNavController.current
    val mangaId = navController.currentBackStackEntry?.arguments?.getString("mangaId")?.toLong()

    Log.d("GetMangaScreen", "Current Manga ID: $mangaId")

    val mangaViewModel: GetMangaViewModel = hiltViewModel()
    val state = mangaViewModel.state.collectAsState()
    val averageRate by mangaViewModel.averageRate
    val rates by mangaViewModel.rates.collectAsState()

    LaunchedEffect(mangaId) {
        mangaId?.let {
            mangaViewModel.fetchMangaDetails(it)
            mangaViewModel.fetchAverageRate(it)
            mangaViewModel.fetchRatesByMangaId(it)
        }
    }

    when (val currentState = state.value) {
        is MangaDetailState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is MangaDetailState.Success -> {
            MangaDetailView(
                mangaDetail = currentState.mangaDetail,
                navController = navController,
                averageRate = averageRate,
                rates = rates
            )
        }
        is MangaDetailState.Error -> {
            Text(
                text = "Error: ${currentState.message}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun MangaDetailView(mangaDetail: MangaModel, navController: NavHostController, averageRate: Int, rates: List<RateModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CloseButton(navController)

        MangaImage(imageUrl = "http://10.0.2.2:8080${mangaDetail.imageUrl}")

        MangaTitle(title = mangaDetail.title)
        MangaAuthor(author = mangaDetail.author)
        MangaSynopsis(synopsis = mangaDetail.imageUrl ?: "Sinopsis no disponible.")
        AverageRating(averageRate = averageRate)
        ActionButtons(mangaId = mangaDetail.id)
        MangaRatings(rates = rates)
    }
}

@Composable
fun MangaImage(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = "Manga Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun MangaTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun MangaAuthor(author: String) {
    Text(
        text = "Autor: $author",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun MangaSynopsis(synopsis: String) {
    Text(
        text = "Sinopsis:",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = synopsis,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        lineHeight = 20.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AverageRating(averageRate: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Valoración media: ",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        repeat(averageRate) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ActionButtons(mangaId: Long) {
    val viewModel: GetMangaViewModel = viewModel()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isInCollection by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    var initialized by remember { mutableStateOf(false) }

    // Verificar si el manga ya está en la colección al cargar
    LaunchedEffect(mangaId) {
        isInCollection = viewModel.isMangaInCollection(mangaId)
        initialized = true
    }

    if (!initialized) {
        // Mostrar un indicador de carga mientras se verifica el estado inicial
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        return
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón para "Lo tengo" o "Añadido"
            Button(
                onClick = {
                    scope.launch {
                        if (!isInCollection) {
                            val response = viewModel.addMangaToUser(mangaId)
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Manga añadido con éxito", Toast.LENGTH_SHORT).show()
                                isInCollection = true
                            } else {
                                Toast.makeText(context, "Error al añadir el manga", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInCollection) Color.Green else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isInCollection) "✓ Añadido" else "Lo tengo",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Botón para "Lo quiero"
            Button(
                onClick = { /* Lógica para "Lo quiero" */ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(text = "Lo quiero", color = MaterialTheme.colorScheme.onSecondary)
            }
        }

        // Campo para escribir la review si el manga ya está en la colección
        if (isInCollection) {
            TextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Escribe una review") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}



@Composable
fun ReviewBox(onSubmit: (String) -> Unit) {
    var reviewText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Escribe tu reseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onSubmit(reviewText) }) {
            Text("Guardar reseña")
        }
    }
}

@Composable
fun MangaRatings(rates: List<RateModel>) {
    Text(
        text = "Valoraciones",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(rates) { rate ->
            RateItem(rate = rate)
        }
    }
}


@Composable
fun RateItem(rate: RateModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = 2.dp)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            val maxStars = 5
            for (i in 1..maxStars) {
                val icon = if (i <= rate.rate) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.Star
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "Star rating",
                    tint = if (i <= rate.rate) Color(0xFFFFD700) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = " (${rate.rate}/5)",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 4.dp, top = 5.dp)
            )
            Text(
                text = rate.date.toString(),
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                modifier = Modifier.padding(start = 8.dp, top = 5.dp)
            )
        }

        Text(
            text = rate.comment,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(5.dp)
        )
    }
}




