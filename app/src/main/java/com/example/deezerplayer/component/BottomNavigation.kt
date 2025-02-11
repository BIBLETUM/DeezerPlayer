package com.example.deezerplayer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deezerplayer.R
import com.example.deezerplayer.navigation.NavigationItem
import com.example.deezerplayer.theme.PlayerTheme
import com.example.deezerplayer.theme.nunitoFontFamily

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .shadow(20.dp)
            .fillMaxWidth()
            .height(64.dp)
            .background(PlayerTheme.colors.neutralWhite),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

@Composable
fun RowScope.BottomNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    navigationItem: NavigationItem,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .weight(1f),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = true) {
                onClick()
            }) {
            when (selected) {
                true -> {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 13.dp, vertical = 6.dp)
                            .height(58.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = navigationItem.titleOnSelectedResId),
                            color = PlayerTheme.colors.neutralActive,
                            style = TextStyle(
                                fontFamily = nunitoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                        )
                        Icon(
                            modifier = Modifier
                                .size(4.dp),
                            painter = painterResource(
                                id = R.drawable.dot_icon
                            ),
                            contentDescription = stringResource(id = navigationItem.titleOnSelectedResId)
                        )
                    }
                }

                false -> {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 13.dp, vertical = 6.dp)
                            .size(32.dp),
                        painter = painterResource(id = navigationItem.iconResId),
                        contentDescription = stringResource(id = navigationItem.titleOnSelectedResId)
                    )
                }
            }
        }
    }
}