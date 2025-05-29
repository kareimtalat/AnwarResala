package com.kareimt.anwarresala.ui.theme.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R



// The password field with a toggle button to show/hide the password
@Composable
fun PasswordFieldWithToggle(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: String = "Next",
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val imeAction = if (imeAction == "Next") ImeAction.Next else ImeAction.Go
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier.widthIn(max = 300.dp),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
            )
        },
        textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Ltr),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                keyboardController?.hide()
                focusManager.moveFocus(FocusDirection.Down)
            },
            onGo = {
                keyboardController?.hide()
            }
        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(24.dp),
                )
            }
        },
    )
}

// The Prompt
@Composable
fun ThePrompt(context: Context, onClickFun: () -> Unit,preText:String,clickableText: String) {
    Row {
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF2144DA))) {
                    append(clickableText)
                }
            },
            onClick = { offset ->
                // You can add logic here to check if the click was on the "login" text
                //  (though in this simple case, it's always the entire text)
                onClickFun()
            }
        )
        Text(" ")
        Text(
            text = preText,
        )

    }
}

// The Input Field
@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    rtl: Boolean=true,
    imeAction: String="Next",
    keyboardType: String="Text",
    visualTransformation: VisualTransformation = VisualTransformation.None
){
    val textDirection=if (rtl){TextDirection.Rtl}else{TextDirection.Ltr}
    val textAlign=if (rtl){TextAlign.Start}else{TextAlign.End}
    val imeAction=if (imeAction=="Next"){ImeAction.Next}else{ImeAction.Go}
    val keyboardType=if (keyboardType=="Email"){KeyboardType.Email} else {
        if (keyboardType=="Password"){KeyboardType.Password}else{KeyboardType.Text}
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
        )},
        modifier = Modifier.widthIn(max = 300.dp),
        textStyle = LocalTextStyle.current.copy(textDirection = textDirection,textAlign = textAlign),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                keyboardController?.hide()
                focusManager.moveFocus(FocusDirection.Down)
            },
            onGo = {
                keyboardController?.hide()
            }
        ),
        visualTransformation = visualTransformation,
    )
}

// The Dropdown Menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReusableDropdown(
    label: String,
    options: List<String> /*= emptyList()*/,
    value: String/*=options.first()*/,
    onOptionSelected: (String) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded=expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)//The only solution after a lot of suffering trying (Modifier.onClickable { expanded = !expanded })
                .widthIn(max = 300.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            label={Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = label)
                Spacer(Modifier.width(3.dp))
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Open Dropdown",
                )
            }
            },
            value = value,
            /*Just must call it*/onValueChange = {},
            readOnly = true,
            /*placeholder = {
                Text(
                    text = value,
                    modifier = Modifier.padding(end = 30.dp),
                    textAlign = TextAlign.End,
                )
            }*/
        )

        // Dropdown menu items
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { optionName ->
                DropdownMenuItem(
                    text = { Text(
                        text = optionName,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End)},
                    onClick = {
                        onOptionSelected(optionName)
                        expanded = false
                    }
                )
            }
        }
    }
}
