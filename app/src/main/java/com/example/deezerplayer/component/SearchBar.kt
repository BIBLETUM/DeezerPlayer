package com.example.deezerplayer.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deezerplayer.R
import com.example.deezerplayer.theme.PlayerTheme
import com.example.deezerplayer.theme.nunitoFontFamily

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle(
        fontFamily = nunitoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        letterSpacing = TextUnit(0.001F, TextUnitType.Sp)
    ),
    isError: Boolean,
    onQueryChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextInputField(
            modifier = Modifier.weight(1f),
            text = text,
            style = style,
            isError = isError,
            placeholderTextResId = R.string.search_tracks,
            onTextChanged = onQueryChange,
            leadingIcon = {
                when (text.isEmpty()) {
                    true -> {
                        SearchIcon()
                    }

                    else -> {}
                }
            },
            trailingIcon = {
                when (text.isEmpty()) {
                    false -> {
                        ClearIcon(onClear = onQueryChange)
                    }

                    else -> {}
                }
            }
        )
        when (text.isEmpty()) {
            true -> {}

            else -> {
                CancelText {
                    onQueryChange("")
                    focusManager.clearFocus()
                }
            }
        }
    }
}

@Composable
private fun CancelText(
    onCancel: () -> Unit,
) {
    Text(
        modifier = Modifier.clickable(onClick = onCancel),
        text = stringResource(R.string.cancel),
        style = TextStyle(
            fontFamily = nunitoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 17.sp,
            letterSpacing = TextUnit(0.001F, TextUnitType.Sp)
        ),
        color = PlayerTheme.colors.primary
    )
}

@Composable
private fun RowScope.ClearIcon(
    onClear: (String) -> Unit,
) {
    Icon(
        modifier = Modifier
            .size(18.dp)
            .align(Alignment.CenterVertically)
            .clickable { onClear("") },
        imageVector = ImageVector.vectorResource(id = R.drawable.search_cross),
        tint = PlayerTheme.colors.gray,
        contentDescription = null,
    )
}

@Composable
private fun RowScope.SearchIcon() {
    Icon(
        modifier = Modifier
            .size(22.dp)
            .align(Alignment.CenterVertically),
        imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
        tint = PlayerTheme.colors.gray,
        contentDescription = null,
    )
}