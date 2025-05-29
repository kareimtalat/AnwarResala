package com.kareimt.anwarresala.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import com.kareimt.anwarresala.R

@Composable
fun SearchRow(
    query: String="",
    onQueryChange: (String) -> Unit={},
    onSearch: () -> Unit={}
) {
    // This function is a placeholder for the SearchRow component.
    // You can implement the UI and functionality for the search row here.
    // For example, you might use a TextField for input and a Button for triggering the search.


    // Search Row
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            //label = { Text(text = "") },
            modifier = Modifier
                .widthIn(max = 300.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    // TODO: Handel what will happen when focus changing
                    // in search bar at Beneficiary Screen
                },
            textStyle = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl),
            trailingIcon = {
                if (!isFocused) {
                    Text(
                        text = context.getString(R.string.search_for_course),
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        )
        Button(onClick = onSearch) {
            Icon(
                painter = painterResource(android.R.drawable.ic_menu_search),
                contentDescription = "Search Icon"
            )
        }
    }
}