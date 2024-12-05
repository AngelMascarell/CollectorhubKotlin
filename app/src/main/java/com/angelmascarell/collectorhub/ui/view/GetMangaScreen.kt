package com.angelmascarell.collectorhub.ui.view

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.livedata.observeAsState
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
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.data.model.RateCreateModel
import com.angelmascarell.collectorhub.data.model.RateModel
import com.angelmascarell.collectorhub.viewmodel.GetMangaViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

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
        is GetMangaViewModel.MangaDetailState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is GetMangaViewModel.MangaDetailState.Success -> {
            MangaDetailView(
                mangaDetail = currentState.mangaDetail,
                navController = navController,
                averageRate = averageRate,
                rates = rates
            )
        }

        is GetMangaViewModel.MangaDetailState.Error -> {
            Text(
                text = "Error: ${currentState.message}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MangaDetailView(
    mangaDetail: MangaModel,
    navController: NavHostController,
    averageRate: Int,
    rates: List<RateModel>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CloseButton(navController)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            MangaImage(
                imageUrl = "http://10.0.2.2:8080${mangaDetail.imageUrl}",
                modifier = Modifier
                    .weight(0.4f)
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.width((-16).dp))

            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .align(Alignment.CenterVertically)
            ) {
                MangaAuthorAndReleaseDate(mangaDetail.author, mangaDetail.releaseDate)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        MangaTitle(title = mangaDetail.title)
        Spacer(modifier = Modifier.height(12.dp))
        MangaSynopsis(synopsis = mangaDetail.synopsis ?: "Sinopsis no disponible.")
        Spacer(modifier = Modifier.height(16.dp))
        AverageRating(averageRate = averageRate)
        Spacer(modifier = Modifier.height(12.dp))
        ActionButtons(mangaId = mangaDetail.id)
        Spacer(modifier = Modifier.height(12.dp))
        MangaRatings(rates = rates)
    }
}

@Composable
fun MangaImage(imageUrl: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
        //.background(MaterialTheme.colorScheme.surface)
        //.shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
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
fun MangaAuthorAndReleaseDate(author: String, releaseDate: LocalDate) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Autor:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = author,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Ícono de Calendario",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = releaseDate.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )
        }
    }
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
        style = MaterialTheme.typography.bodyLarge,
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
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
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
    var isInDesiredCollection by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(mangaId) {
        isInCollection = viewModel.isMangaInCollection(mangaId)
        isInDesiredCollection = viewModel.isMangaInDesiredCollection(mangaId)
        initialized = true
    }

    if (!initialized) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        return
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    scope.launch {
                        if (!isInCollection) {
                            val response = viewModel.addMangaToUser(mangaId)
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Manga añadido con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isInCollection = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error al añadir el manga",
                                    Toast.LENGTH_SHORT
                                ).show()
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

            Button(
                onClick = {
                    scope.launch {
                        if (!isInCollection  || !isInDesiredCollection) {
                            val response = viewModel.addDesiredMangaToUser(mangaId)
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Manga añadido a la lista de deseados con éxito",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isInDesiredCollection = true
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error. El manga ya está presente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInDesiredCollection) Color.Magenta else MaterialTheme.colorScheme.secondary
                )            ) {
                Text(
                    text = if (isInDesiredCollection) "✓ Deseado" else "Lo quiero",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        if (isInCollection) {
            RateAndCommentSection(
                mangaId = mangaId,
                onSuccess = {
                    Toast.makeText(context, "Reseña enviada con éxito", Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    Toast.makeText(
                        context,
                        "Error al enviar la reseña: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

@Composable
fun RateAndCommentSection(
    mangaId: Long,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: GetMangaViewModel = viewModel()

    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    val reviewState by viewModel.reviewState.observeAsState()

    val hasReviewed by viewModel.hasReviewed.collectAsState()

    LaunchedEffect(mangaId) {
        viewModel.checkUserReview(mangaId)
    }

    if (hasReviewed == true) {
        Text(
            text = "¡Ya has realizado una reseña!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    if (hasReviewed == false || hasReviewed == null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Califica este manga",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Calificación $i",
                        tint = if (i <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { rating = i }
                    )
                }
            }

            TextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Escribe tu comentario") },
                placeholder = { Text("¿Qué opinas de este manga?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            when (val state = reviewState) {
                is GetMangaViewModel.ReviewState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }

                is GetMangaViewModel.ReviewState.Success -> {
                    Toast.makeText(
                        context,
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    onSuccess()
                    viewModel.checkUserReview(mangaId)
                }

                is GetMangaViewModel.ReviewState.Error -> {
                    Toast.makeText(
                        context,
                        state.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    onError(state.error)
                }

                else -> {
                    Button(
                        onClick = {
                            if (rating > 0) {
                                viewModel.submitReview(
                                    RateCreateModel(
                                        mangaId = mangaId,
                                        rate = rating,
                                        comment = comment
                                    )
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor, selecciona una calificación",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        enabled = rating > 0
                    ) {
                        Text("Enviar")
                    }
                }
            }
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




