package com.example.deezerplayer.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.deezerplayer.theme.PlayerTheme

@Composable
internal fun TextInputField(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    isError: Boolean = false,
    @StringRes placeholderTextResId: Int,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onTextChanged: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    val backgroundErrorColor =
        if (isError) PlayerTheme.colors.error.copy(alpha = 0.1f) else Color.Transparent

    BasicTextField(modifier = modifier
        .clip(RoundedCornerShape(16.dp))
        .background(PlayerTheme.colors.backgroundDisabled)
        .background(backgroundErrorColor)
        .heightIn(56.dp)
        .onFocusChanged { focus -> isFocused = focus.isFocused }
        .fillMaxWidth()
        .border(
            width = 1.5.dp,
            color = if (isFocused) PlayerTheme.colors.primaryStroke else Color.Transparent,
            shape = RoundedCornerShape(16.dp)
        ),
        value = text,
        onValueChange = { newText ->
            onTextChanged(newText)
        },
        textStyle = style,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            DecorationTextRow(
                text = text,
                style = style,
                placeholderTextResId = placeholderTextResId,
                innerTextField = innerTextField
            )
        },
        cursorBrush = SolidColor(PlayerTheme.colors.primary)
    )
}

@Composable
private fun DecorationTextRow(
    text: String,
    style: TextStyle,
    @StringRes placeholderTextResId: Int,
    leadingIcon: @Composable() (RowScope.(String) -> Unit)? = null,
    trailingIcon: @Composable() (RowScope.(String) -> Unit)? = null,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        Modifier.padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        leadingIcon?.invoke(this, text)

        Box(
            Modifier
                .padding(vertical = 16.dp)
                .weight(1f)
                .align(Alignment.CenterVertically),
        ) {
            when (text.isEmpty()) {
                true -> {
                    PlaceHolderText(placeholderTextResId, style = style)
                }

                else -> {}
            }
            innerTextField()
        }


        trailingIcon?.invoke(this, text)
    }
}

@Composable
private fun PlaceHolderText(
    @StringRes placeholderTextResId: Int,
    style: TextStyle,
) {
    Text(
        text = stringResource(id = placeholderTextResId),
        style = style,
        color = PlayerTheme.colors.placeholder
    )
}