package com.fitquest.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorMuted
import com.fitquest.app.ui.theme.ColorText
import androidx.compose.foundation.layout.Box

/** Inset-look pixel input. Sharp corners, 2dp border, mono font. */
@Composable
fun PixelTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = ColorMuted,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(ColorBgDeep)
                .border(BorderStroke(2.dp, ColorBorder), RectangleShape)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = singleLine,
                visualTransformation = if (isPassword) VisualTransformation.None.let {
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                } else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = LocalTextStyle.current.copy(
                    color = ColorText,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                ),
                cursorBrush = SolidColor(ColorAccent),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { inner ->
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = ColorMuted,
                        )
                    }
                    inner()
                },
            )
        }
    }
}
