package com.angelmascarell.collectorhub.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.Routes
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
    val navController = LocalNavController.current

    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    MaterialTheme(
        colorScheme = if (isDarkTheme) customDarkColorScheme() else customLightColorScheme()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Imagen en la parte inferior
                /*Image(
                    painter = painterResource(id = R.drawable.fondolibros),
                    contentDescription = "Fondo de pantalla",
                    modifier = Modifier
                        .fillMaxWidth() // O puedes usar .size(200.dp) si prefieres tamaño fijo
                        .height(200.dp) // Ajusta la altura según lo que necesites
                        .alpha(0.6f)
                        .align(Alignment.BottomCenter) // Posiciona la imagen en la parte inferior
                )
                 */
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CloseButton(navController)

                Spacer(modifier = Modifier.height(16.dp))

                //Title()

                //Spacer(modifier = Modifier.height(16.dp))

                PremiumText()

                Spacer(modifier = Modifier.height(20.dp))

                AdvantagesList()

                Spacer(modifier = Modifier.height(16.dp))

                PricingSection()

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
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun PremiumText() {
    Text(
        text = "Ventajas de ser Premium",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        style = TextStyle(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFDBB2D),
                    Color(0xFFE85D04)
                )
            )
        ),
        modifier = Modifier.graphicsLayer(alpha = 0.99f)
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFFD6A6F1),
                        Color(0xFFF2D4FF)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color(0xFFf8f8f5))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            advantages.forEach { (description, iconRes) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 5.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = description,
                        modifier = Modifier
                            .size(26.dp)
                            .padding(2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
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
        PriceOptionCard(
            price = "$1.19",
            title = "Mensual",
            description = "Pago mensual, cancelable en cualquier momento",
            modifier = Modifier
                .weight(1f)
                .height(200.dp)
        )

        PriceOptionCard(
            price = "$11.99",
            title = "Anual",
            description = "Pago anual con descuento, acceso continuo",
            modifier = Modifier
                .weight(1f)
                .height(200.dp)
        )
    }
}

@Composable
fun PriceOptionCard(
    price: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFFDBB2D),
                            Color(0xFFE85D04)
                        )
                    )
                )
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
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = price,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = description,
                    color = Color.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun CloseButton(navController: NavHostController) {
    IconButton(
        onClick = { navController.navigate(Routes.HomeScreenRoute.route) },
        modifier = Modifier
            .padding(top = 16.dp, end = 16.dp)
            .size(36.dp)
            .clip(RoundedCornerShape(50))
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
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFFFA500), RoundedCornerShape(8.dp))
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡COLLECTORHUB PREMIUM!",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.libros),
                contentDescription = "Publicidad",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

