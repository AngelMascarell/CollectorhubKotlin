package com.angelmascarell.collectorhub.home.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.data.model.MangaModel
import com.angelmascarell.collectorhub.viewmodel.ThemeViewModel

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current

    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    MaterialTheme(
        colorScheme = if (isDarkTheme) customDarkColorScheme() else customLightColorScheme()
    ) {
        val homeViewModel: HomeViewModel = hiltViewModel()

        var searchQuery by remember { mutableStateOf("") }
        val mangas = homeViewModel.mangas.value
        val personalizedMangas = homeViewModel.personalizedMangas.value
        val isLoading = homeViewModel.isLoading.value
        val errorMessage = homeViewModel.errorMessage.value

        // Llamada para cargar mangas personalizados
        LaunchedEffect(true) {
            homeViewModel.loadPersonalizedMangas()
            homeViewModel.loadMangas()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(if (isDarkTheme) Color(0xFF121212) else Color.White)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { themeViewModel.toggleTheme() },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color(0xFF444444) else Color(0xFFFFA500)
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = "Cambiar tema",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                HeaderRow()
            }

            Spacer(modifier = Modifier.height(10.dp))
            FirstRowButtons()
            Spacer(modifier = Modifier.height(10.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    println("Buscando: $searchQuery")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Text(
                text = "Mangas personalizados",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (!isLoading && personalizedMangas.isNotEmpty()) {
                MangaSlider(mangas = personalizedMangas, navController = navController, mangaViewModel = homeViewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            PremiumAdBanner(onClick = {
                navController.navigate(Routes.PremiumScreenRoute.route)
            })

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Mangas generales",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (!isLoading && mangas.isNotEmpty()) {
                MangaSlider(mangas = mangas, navController = navController, mangaViewModel = homeViewModel
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AdBanner()
        }
    }
}

@Composable
fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "CollectorHub",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground // Asegúrate de usar el color adecuado
        )
        IconButton(onClick = { /* Navega al perfil */ }) {
            Icon(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Perfil",
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground // Color del icono de perfil
            )
        }
    }
}

@Composable
fun FirstRowButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeButton("Colección", R.drawable.libros, Modifier.weight(1f)) { }
        HomeButton("Futuros", R.drawable.anadir, Modifier.weight(1f)) { }
        HomeButton("Novedades", R.drawable.novedoso, Modifier.weight(1f)) { }
    }
}

@Composable
fun HomeButton(text: String, iconRes: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1.4f)
            .padding(horizontal = 2.dp, vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.Gray, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 0.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Buscar",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Buscar manga...") },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch() })
        )
        if (query.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Borrar texto",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun MangaSlider(
    mangas: List<MangaModel>,
    modifier: Modifier = Modifier,
    navController: NavHostController,mangaViewModel: HomeViewModel
) {
    // Asegúrate de que la lista no esté vacía antes de mostrar el slider
    if (mangas.isNotEmpty()) {
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre las imágenes
        ) {
            items(mangas) { manga ->
                MangaImage(
                    manga = manga,
                    onClick = {
                        Log.d("MangaSlider", "Selected manga ID: ${manga.id}")
                        mangaViewModel.setMangaId(manga.id)
                        navController.navigate("GetMangaScreen/${manga.id}")  // Pasamos el mangaId en la ruta
                    }
                )
            }
        }
    } else {
        // Mostrar un mensaje si no hay mangas disponibles
        Text(
            text = "No hay mangas disponibles",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.background,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun MangaImage(manga: MangaModel, onClick: () -> Unit) {
    val imageUrl = "http://10.0.2.2:8080${manga.imageUrl}"

    Log.d("Image URL", "URL: $imageUrl")

    Box(
        modifier = Modifier
            .width(170.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() } // Detectar el clic y ejecutar la navegación
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl), // Usando Coil o Glide para cargar imágenes
            contentDescription = manga.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // La imagen se recortará para llenar el área sin deformarse
        )
    }
}


@Composable
fun PremiumAdBanner(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho de la pantalla
            .aspectRatio(3f) // Relación de aspecto ancho:alto
            .clip(RoundedCornerShape(10.dp)) // Sin bordes redondeados (opcional, puedes cambiarlos)
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFDBB2D), // Amarillo oro
                        Color(0xFFE85D04), // Naranja intenso
                    )
                )
            )
    ) {
        // Contenido del banner
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hazte Premium",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Disfruta contenido exclusivo y sin anuncios!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A0572) // Botón violeta intenso
                ),
                modifier = Modifier
                    .weight(1.2f) // Ajusta el ancho relativo
                    .height(48.dp), // Altura fija más pequeña
                shape = RoundedCornerShape(10.dp), // Esquinas completamente rectas
                contentPadding = PaddingValues(
                    horizontal = 10.dp,
                    vertical = 8.dp
                ) // Reduce el padding interno
            ) {
                Text(
                    "Suscribirse",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium // Asegúrate de usar un estilo adecuado
                )
            }
        }
    }
}

@Composable
fun AdBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Asegura que ocupe todo el ancho
            .padding(8.dp)
            .background(
                Color(0xFFFFA500),
                RoundedCornerShape(8.dp)
            ) // Color de fondo y bordes redondeados
            .height(60.dp) // Altura del banner
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Texto o contenido del anuncio
            Text(
                text = "¡Anuncio Premium!",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Aquí va la imagen del anuncio
            Image(
                painter = painterResource(id = R.drawable.libros), // Reemplaza con el ícono de tu anuncio
                contentDescription = "Publicidad",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


@Composable
fun customDarkColorScheme(): ColorScheme {
    return darkColorScheme(
        primary = Color(0xFFBB86FC), // Personaliza el color principal
        onPrimary = Color.White, // Color del texto en el color principal
        secondary = Color(0xFF03DAC6), // Personaliza el color secundario
        onSecondary = Color.Black, // Color del texto en el color secundario
        background = Color(0xFF121212), // Fondo oscuro personalizado
        onBackground = Color.White, // Color del texto en el fondo oscuro
        surface = Color(0xFF121212), // Fondo de las superficies
        onSurface = Color.White, // Color del texto en las superficies
        error = Color(0xFFCF6679), // Error color
        onError = Color.Black, // Texto del error
    )
}

@Composable
fun customLightColorScheme(): ColorScheme {
    return lightColorScheme(
        primary = Color(0xFF6200EE), // Color primario
        onPrimary = Color.White, // Texto sobre el color primario
        secondary = Color(0xFF03DAC6), // Color secundario
        onSecondary = Color.Black, // Texto sobre el color secundario
        background = Color.White, // Fondo claro
        onBackground = Color.Black, // Texto sobre el fondo claro
        surface = Color(0xFFFFFFFF), // Fondo de las superficies
        onSurface = Color.Black, // Texto sobre las superficies
        error = Color(0xFFB00020), // Color de error
        onError = Color.White, // Texto sobre el error
    )
}
