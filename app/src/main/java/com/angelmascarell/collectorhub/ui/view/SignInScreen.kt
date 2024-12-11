package com.angelmascarell.collectorhub.ui.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.composable.MyButton
import com.angelmascarell.collectorhub.ui.theme.CollectorHubTheme
import com.angelmascarell.collectorhub.ui.theme.MyUltraBlue
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.core.composable.MyBasicTextField
import com.angelmascarell.collectorhub.viewmodel.SignInViewModel


@Composable
fun SignInScreen() {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderSignIn()

        Spacer(modifier = Modifier.height(40.dp))
        BodySignIn(navController)

        Spacer(modifier = Modifier.height(16.dp))
        FooterSignIn(navController)
    }
}

@Composable
fun HeaderSignIn() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 0.dp)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    append("Welcome ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MyUltraBlue
                    )
                ) {
                    append("Back")
                }
            },
            fontSize = 30.sp
        )
        Text(
            text = "Login to proceed with us",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 57.dp, end = 57.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Gray
        )
    }
}

@Composable
fun BodySignIn(navController: NavController, signInViewModel: SignInViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val username by signInViewModel.username.observeAsState("")
        val password by signInViewModel.password.observeAsState("")

        EmailTextField(value = username) {
            signInViewModel.onSignInChanged(username = it, password = password)
        }
        PasswordTextField(value = password) {
            signInViewModel.onSignInChanged(username = username, password = it)
        }

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MyUltraBlue
                    )
                ) {
                    append("Forgot password?")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable {
                    navController.navigate(Routes.SignInScreenRoute.route)
                },
            color = Color.Gray
        )

        val context = LocalContext.current
        MyButton(text = "Login") {
            signInViewModel.login { loginResult ->
                if (loginResult) {
                    navController.navigate(Routes.HomeScreenRoute.route)
                } else {
                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }

        }

        Text(
            text = "--- Or continue with ---",
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(painter = painterResource(id = R.drawable.google), contentDescription = null)
            Image(painter = painterResource(id = R.drawable.facebook), contentDescription = null)

        }
    }
}

@Composable
fun FooterSignIn(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                ) {
                    append("Does not have an account? ")
                }
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MyUltraBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp
                    )
                ) {
                    append("Sign Up")
                }
            },
            modifier = Modifier.clickable { navController.navigate(Routes.SignUpScreenRoute.route) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    CollectorHubTheme {

    }
}

@Composable
fun EmailTextField(value: String, onTextChanged: (String) -> Unit) {
    MyBasicTextField(
        value = value,
        label = "Email",
        onTextChanged = onTextChanged,
        imageVector = Icons.Default.Email
    )
}

@Composable
fun UsernameTextField(value: String, onTextChanged: (String) -> Unit) {
    MyBasicTextField(
        value = value,
        label = "Email",
        onTextChanged = onTextChanged,
        imageVector = Icons.Default.AccountCircle
    )
}

@Composable
fun PasswordTextField(value: String, onTextChanged: (String) -> Unit) {
    MyBasicTextField(
        value = value,
        label = "Password",
        onTextChanged = onTextChanged,
        imageVector = Icons.Default.Lock,
        password = true
    )
}