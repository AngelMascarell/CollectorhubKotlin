package com.angelmascarell.collectorhub.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.composable.MyBasicTextFieldReadOnly
import com.angelmascarell.collectorhub.core.composable.MyEditableTextField
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.data.model.UserModel
import com.angelmascarell.collectorhub.viewmodel.ProfileViewModel
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = hiltViewModel()

    val userProfile by viewModel.userProfile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val navController = LocalNavController.current

    var isEditing by remember { mutableStateOf(false) }
    var editableProfile by remember { mutableStateOf(userProfile) }

    LaunchedEffect(userProfile) {
        editableProfile = userProfile
    }

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    val updateResult by viewModel.updateResult.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(updateResult) {
        if (updateResult.isNotEmpty()) {
            showToast(context, updateResult)
            viewModel.clearUpdateResult()
        }
    }



    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            CloseButton(navController)

            Spacer(modifier = Modifier.height(30.dp))
            when {
                isLoading -> LoadingView()
                errorMessage != null -> ErrorView(errorMessage ?: "Error desconocido")
                userProfile != null -> {
                    editableProfile?.let { profile ->
                        ProfileContent(
                            user = profile,
                            editableUser = editableProfile!!,
                            isEditing = isEditing,
                            onEditClick = {
                                isEditing = !isEditing
                                if (isEditing) {
                                    editableProfile = userProfile
                                }
                            },
                            onSaveClick = {
                                editableProfile?.let { updatedProfile ->
                                    viewModel.updateUserProfile(updatedProfile)
                                    isEditing = false
                                }
                            },
                            onCancelClick = {
                                editableProfile = userProfile
                                isEditing = false
                            },
                            onTextChanged = { field, newText ->
                                println("Text changed in field $field to $newText")

                                editableProfile = when (field) {
                                    "username" -> editableProfile?.copy(username = newText)
                                    "email" -> editableProfile?.copy(email = newText)

                                    else -> editableProfile
                                }
                            },
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }

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

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

}

@Composable
fun ProfileImageSection(profileImageUrl: String?, navController: NavHostController) {
    val url = "http://10.0.2.2:8080${profileImageUrl}"

    Box(
        modifier = Modifier
            .size(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .border(2.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = { navController.navigate(Routes.ProfileScreenRoute.route) }) {
                if (!profileImageUrl.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = url,
                            placeholder = painterResource(id = R.drawable.profile),
                            error = painterResource(id = R.drawable.libros)
                        ),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Ícono de perfil predeterminado",
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Green),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileContent(
    user: UserModel,
    editableUser: UserModel,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onTextChanged: (String, String) -> Unit,
    navController: NavHostController,
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        ProfileImageSection(profileImageUrl = user.profileImageUrl, navController = navController)
        PersonalInfoSection(
            user = user,
            editableUser = editableUser,
            isEditing = isEditing,
            onTextChanged = onTextChanged
        )
        AccountStatusSection(isPremium = user.isPremium)
        ActionButtonsSection(
            isEditing = isEditing,
            onEditClick = onEditClick,
            onSaveClick = onSaveClick,
            onCancelClick = onCancelClick,
            logout = { viewModel.doLogOut() },
            navigateToSignIn = { navController.navigate(Routes.SignInScreenRoute.route) }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalInfoSection(
    user: UserModel,
    editableUser: UserModel,
    isEditing: Boolean,
    onTextChanged: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MyEditableTextField(
            value = editableUser.username,
            label = "Nombre",
            readOnly = !isEditing,
            onTextChanged = { newText -> onTextChanged("username", newText) },
            imageVector = Icons.Default.Person
        )
        MyEditableTextField(
            value = editableUser.email,
            label = "Correo electrónico",
            readOnly = !isEditing,
            onTextChanged = { newText -> onTextChanged("email", newText) },
            imageVector = Icons.Default.Email,
            color = MaterialTheme.colorScheme.onBackground
        )
        MyBasicTextFieldReadOnly(
            value = (editableUser.birthdate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ?: "No disponible"),
            label = "Fecha de nacimiento",
            readOnly = true,
            onTextChanged = { newText -> onTextChanged("birthdate", newText) },
            imageVector = Icons.Default.DateRange
        )


        MyBasicTextFieldReadOnly(
            value = (user.registerDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                ?: "No disponible"),
            label = "Fecha de registro",
            readOnly = true,
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
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ActionButtonsSection(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    logout: () -> Unit,
    navigateToSignIn: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isEditing) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = CircleShape
            ) {
                Text("Guardar cambios")
            }

            Button(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary)
            }
        } else {
            Button(
                onClick = onEditClick,
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = CircleShape
            ) {
                Text("Editar perfil")
            }

            Button(
                onClick = {
                    logout()
                    navigateToSignIn()
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.onError)
            }
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
