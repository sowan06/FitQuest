package com.fitquest.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.fitquest.app.ui.theme.ColorAccent
import com.fitquest.app.ui.theme.ColorBorder
import com.fitquest.app.ui.theme.ColorBgDeep
import com.fitquest.app.ui.theme.ColorText

/** Top app header — pixel logo, notification + account icons. */
@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorBgDeep)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "FITQUEST",
            color = ColorAccent,
            style = MaterialTheme.typography.headlineMedium,
        )
        Row {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications",
                tint = ColorText,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = 4.dp),
            )
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Account",
                tint = ColorText,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Composable
fun ScreenTitleRow(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, ColorBorder, RectangleShape)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = ColorAccent,
        )
    }
}
