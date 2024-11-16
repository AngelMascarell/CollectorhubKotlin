package com.angelmascarell.collectorhub.ui.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.home.presentation.HomeViewModel
import com.angelmascarell.collectorhub.viewmodel.GetMangaViewModel
import com.angelmascarell.collectorhub.viewmodel.MangaDetailState

@Composable
fun GetMangaScreen() {
    val navController = LocalNavController.current

    val mangaId = navController.currentBackStackEntry?.arguments?.getString("mangaId")?.toLong()

    Log.d("GetMangaScreen", "Current Manga ID: $mangaId")

    // Usar mangaId para hacer la llamada a la API o lógica de la pantalla
    val mangaViewModel: GetMangaViewModel = hiltViewModel()
    val state = mangaViewModel.state.collectAsState()

    LaunchedEffect(mangaId) {
        mangaId?.let {
            mangaViewModel.fetchMangaDetails(it)
        }
    }

    // Muestra el estado actual del detalle del manga
    when (val currentState = state.value) {
        is MangaDetailState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is MangaDetailState.Success -> {
            MangaDetailView(mangaDetail = currentState.mangaDetail)
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
fun MangaDetailView(mangaDetail: MangaModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        //TODO: NO RECUPERA LA IMAGEN PARECE SER

        Image(
            painter = rememberImagePainter(mangaDetail.imageUrl),
            contentDescription = mangaDetail.title,
            modifier = Modifier.fillMaxWidth().height(250.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = mangaDetail.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mangaDetail.author,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
