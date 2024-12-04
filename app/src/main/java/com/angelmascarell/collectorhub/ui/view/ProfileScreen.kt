package com.angelmascarell.collectorhub.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.composable.MyBasicTextFieldReadOnly
import com.angelmascarell.collectorhub.data.model.UserModel
import com.angelmascarell.collectorhub.viewmodel.ProfileViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = hiltViewModel()
    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var isEditing by remember { mutableStateOf(false) } // Estado para controlar la edición

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> LoadingView()
                errorMessage != null -> ErrorView(errorMessage ?: "Error desconocido")
                userProfile != null -> ProfileContent(userProfile!!, isEditing, onEditClick = {
                    isEditing = !isEditing // Cambiar el estado de edición
                })
                else -> Text(
                    text = "No hay datos disponibles.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ProfileImageSection(profileImageUrl: String?) {
    Box(
        modifier = Modifier.size(150.dp),
        contentAlignment = Alignment.Center
    ) {
        if (profileImageUrl.isNullOrEmpty()) {
            // Imagen de perfil por defecto si no existe
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Imagen de perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Imagen de perfil del usuario
            AsyncImage(
                model = profileImageUrl,
                contentDescription = "Imagen de perfil del usuario",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ProfileContent(
    user: UserModel,
    isEditing: Boolean,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        ProfileImageSection(profileImageUrl = user.profileImageUrl)
        PersonalInfoSection(user, isEditing)
        AccountStatusSection(isPremium = user.isPremium)
        ActionButtonsSection(onEditClick)
    }
}


@Composable
fun PersonalInfoSection(user: UserModel, isEditing: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MyBasicTextFieldReadOnly(
            value = user.username,
            label = "Nombre",
            readOnly = !isEditing,
            onTextChanged = { /* Manejar cambios */ },
            imageVector = Icons.Default.Person
        )
        MyBasicTextFieldReadOnly(
            value = user.email,
            label = "Correo electrónico",
            readOnly = !isEditing,
            onTextChanged = { /* Manejar cambios */ },
            imageVector = Icons.Default.Email
        )
        MyBasicTextFieldReadOnly(
            value = (user.birthdate ?: "No disponible").toString(),
            label = "Fecha de nacimiento",
            readOnly = !isEditing,
            onTextChanged = { /* Manejar cambios */ },
            imageVector = Icons.Default.DateRange
        )
        MyBasicTextFieldReadOnly(
            value = (user.registerDate ?: "No disponible").toString(),
            label = "Fecha de registro",
            readOnly = true, // Este siempre es de solo lectura
            onTextChanged = { /* Sin acción */ },
            imageVector = Icons.Default.DateRange
        )
    }
}

@Composable
fun AccountStatusSection(isPremium: Boolean) {
    Text(
        text = if (isPremium) "Cuenta Premium Activa" else "Cuenta Estándar",
        style = MaterialTheme.typography.bodyLarge,
        color = if (isPremium) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ActionButtonsSection(onEditClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón de editar perfil
        Button(
            onClick = onEditClick,
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = CircleShape
        ) {
            Text("Editar perfil")
        }

        // Botón de cerrar sesión
        Button(
            onClick = { /* Acción de cerrar sesión */ },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión", color = MaterialTheme.colorScheme.onError)
        }
    }
}


@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(errorMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
    }
}
