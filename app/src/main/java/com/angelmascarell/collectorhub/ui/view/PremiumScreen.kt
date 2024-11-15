package com.angelmascarell.collectorhub.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.home.presentation.customDarkColorScheme
import com.angelmascarell.collectorhub.home.presentation.customLightColorScheme
import com.angelmascarell.collectorhub.viewmodel.ThemeViewModel


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    PremiumScreen()
}

@Composable
fun PremiumScreen() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    MaterialTheme(
        colorScheme = if (isDarkTheme) customDarkColorScheme() else customLightColorScheme()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Fondo de pantalla",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.6f)
                    .graphicsLayer(
                        scaleX = -1f,
                    )
                    .offset(y = (-60).dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CloseButton()

                Title()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ventajas de ser Premium",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                AdvantagesList()

                Spacer(modifier = Modifier.height(16.dp))

                //TODO: AJUSTAR EL TAMAÑO DE LOS OBJETOS PARA QUE ENTRE TODO
                //PricingSection()

                AdBanner()
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "Beneficios Premium",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun AdvantagesList() {
    val advantages = listOf(
        Pair("Acceso a contenido exclusivo", R.drawable.ic_star),
        Pair("Sin anuncios", R.drawable.ic_star),
        Pair("Soporte prioritario", R.drawable.ic_star),
        Pair("Actualizaciones rápidas", R.drawable.ic_star)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        advantages.forEach { (description, iconRes) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFFD6A6F1), // Morado claro
                                Color(0xFFF2D4FF)  // Morado muy claro
                            )
                        ),
                        shape = RoundedCornerShape(16.dp) // Esquinas redondeadas para un diseño moderno
                    )
                    .padding(16.dp) // Agregar padding para no dejar el contenido pegado
                    .shadow(4.dp, shape = RoundedCornerShape(16.dp)) // Sombra para darle profundidad
                    .clickable { /* Acción de clic, si es necesario */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = description,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp)
                        .background(Color.White, shape = CircleShape) // Fondo blanco redondeado para el ícono
                        .padding(4.dp) // Espacio entre el ícono y el fondo
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black), // Color del texto
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun PricingSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Precio mensual
        PriceOptionCard(
            price = "$1.19",
            title = "Mensual",
            description = "Pago mensual, cancelable en cualquier momento",
            modifier = Modifier.weight(1f)
        )

        // Precio anual
        PriceOptionCard(
            price = "$11.99",
            title = "Anual",
            description = "Pago anual con descuento, acceso continuo",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PriceOptionCard(price: String, title: String, description: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD1C4E9)) // Un tono lavanda más oscuro
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A0572),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = price,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A0572),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CloseButton() {
    IconButton(
        onClick = { /* Implementar acción de volver */ },
        modifier = Modifier
            .padding(16.dp)
            .background(color = Color.LightGray),
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cerrar",
            tint = Color.White
        )
    }
}

@Composable
fun AdBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Asegura que ocupe todo el ancho
            .padding(8.dp)
            .background(Color(0xFFFFA500), RoundedCornerShape(8.dp)) // Color de fondo y bordes redondeados
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
                text = "¡COLLECTORHUB PREMIUM!",
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

