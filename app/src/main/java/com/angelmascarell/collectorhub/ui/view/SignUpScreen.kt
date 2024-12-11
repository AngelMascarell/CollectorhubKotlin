package com.angelmascarell.collectorhub.ui.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.angelmascarell.collectorhub.R
import com.angelmascarell.collectorhub.core.composable.MyButton
import com.angelmascarell.collectorhub.core.routes.LocalNavController
import com.angelmascarell.collectorhub.core.routes.Routes
import com.angelmascarell.collectorhub.ui.theme.MyUltraBlue
import com.angelmascarell.collectorhub.viewmodel.SignUpViewModel
import java.time.LocalDate
import java.util.Calendar

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen() {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(MySoftBlue)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Header()

        Spacer(modifier = Modifier.height(40.dp))
        Body(navController)

        Spacer(modifier = Modifier.height(16.dp))
        Footer(navController)
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp, bottom = 0.dp)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,             color = MaterialTheme.colorScheme.onBackground
                )) {
                    append("Create ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = MyUltraBlue
                    )
                ) {
                    append("Account")
                }
            },
            fontSize = 30.sp
        )
        Text(
            text = "Sign up to get started",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 71.dp, end = 57.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.Gray
        )

    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Body(navController: NavController) {

    val signUpViewModel: SignUpViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val username by signUpViewModel.username.observeAsState("")
        val email by signUpViewModel.email.observeAsState("")
        val password by signUpViewModel.password.observeAsState("")
        val birthdate by signUpViewModel.birthdate.observeAsState(null)

        UsernameTextField(value = username) {
            signUpViewModel.onSignUpChanged(username = it, email = email, password = password, birthdate = birthdate)
        }

        EmailTextField(value = email) {
            signUpViewModel.onSignUpChanged(username = username, email = it, password = password, birthdate = birthdate)
        }

        PasswordTextField(value = password) {
            signUpViewModel.onSignUpChanged(username = username, email = email, password = it, birthdate = birthdate)
        }

        BirthdatePicker(
            birthdate = birthdate,
            onBirthdateChanged = {
                signUpViewModel.onSignUpChanged(username = username, email = email, password = password, birthdate = it)
            }
        )

        Spacer(modifier = Modifier.padding(vertical = 10.dp))

        val context = LocalContext.current
        MyButton(text = "Sign Up") {
            signUpViewModel.signUp { isSuccess, errorMessage ->
                if (isSuccess) {
                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.SignInScreenRoute.route)
                } else {
                    errorMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        Text(
            text = "--- Or sign up with ---",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center, color = Color.Gray
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BirthdatePicker(
    birthdate: LocalDate?,
    onBirthdateChanged: (LocalDate?) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year = birthdate?.year ?: calendar.get(Calendar.YEAR)
    val month = birthdate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
    val day = birthdate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            onBirthdateChanged(LocalDate.of(selectedYear, selectedMonth + 1, selectedDay))
        },
        year,
        month,
        day
    )

    OutlinedButton(onClick = { datePickerDialog.show() }) {
        Text(text = birthdate?.toString() ?: "Select Birthdate")
    }
}


@Composable
fun Footer(navController: NavController) {
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
                    append("Already have an account? ")
                }
                withStyle(
                    style = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = MyUltraBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                ) {
                    append("Login")
                }
            },
            modifier = Modifier.clickable { navController.navigate(Routes.SignInScreenRoute.route) }
        )
    }
}