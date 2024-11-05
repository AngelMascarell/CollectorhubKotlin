package com.angelmascarell.collectorhub.core.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.angelmascarell.collectorhub.ui.theme.MyUltraBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBasicTextField(
    value: String,
    placeholder: String = "",
    label: String = "",
    onTextChanged: (String) -> Unit,
    imageVector: ImageVector,
    password: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        onValueChange = onTextChanged,
        colors = getOutlinedTextFieldDefaultsColors(),
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = label)
        },
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
fun MyBasicTextFieldReadOnly(
    value: String,
    placeholder: String = "",
    label: String = "",
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit,
    imageVector: ImageVector,
    password: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        onValueChange = onTextChanged,
        colors = getOutlinedTextFieldDefaultsColors(),
        maxLines = 1,
        singleLine = true,
        readOnly = readOnly,
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = label)
        },
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None
    )
}


@Composable
fun getOutlinedTextFieldDefaultsColors(): TextFieldColors{
    return OutlinedTextFieldDefaults.colors().copy(
        focusedTextColor = Color.Black,
        focusedLabelColor = MyUltraBlue,
        focusedLeadingIconColor = MyUltraBlue,
        focusedSupportingTextColor = MyUltraBlue,
        focusedIndicatorColor = MyUltraBlue,
        focusedPlaceholderColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedLabelColor = Color.Gray,
        unfocusedLeadingIconColor = Color.Gray,
        unfocusedSupportingTextColor = MyUltraBlue,
        unfocusedTextColor = Color.Black,
        unfocusedContainerColor = Color.White,
        errorIndicatorColor = Color.Red,
        errorTextColor = Color.Black,
        errorLabelColor = Color.Black,
        errorSupportingTextColor = Color.Red
    )
}