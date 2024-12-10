package com.angelmascarell.collectorhub.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.viewmodel.GetMangaViewModel
import com.angelmascarell.collectorhub.viewmodel.NewMangasViewModel


@Composable
fun NewMangasScreen() {

    val navController = LocalNavController.current
    val newMangasViewModel: NewMangasViewModel = hiltViewModel()
    val mangaViewModel: GetMangaViewModel = hiltViewModel()

    val averageRate by mangaViewModel.averageRate


    LaunchedEffect(Unit) {
        newMangasViewModel.loadNewMangas()
    }

    val newMangas by newMangasViewModel.newMangas.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        CloseButton(navController)

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "Resumen de tu lista de novedades",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
            )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tus novedades: ",
            style = TextStyle(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground
            )
        Spacer(modifier = Modifier.height(16.dp))
        DesiredMangaGrid(
            mangaList = newMangas,
            onItemClick = { manga ->
                navController.navigate("GetMangaScreen/${manga.id}")
            },
            averageRating = averageRate
        )
    }
}