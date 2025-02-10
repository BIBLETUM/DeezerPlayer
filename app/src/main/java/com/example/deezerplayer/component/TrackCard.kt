package com.example.deezerplayer.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.deezerplayer.R
import com.example.deezerplayer.model.TrackUi
import com.example.deezerplayer.theme.nunitoFontFamily

@Composable
fun TrackCard(
    modifier: Modifier = Modifier,
    track: TrackUi,
    onTrackClick: (TrackUi) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onTrackClick(track)
            },
        horizontalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        AsyncImage(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(8.dp)),
            model = track.coverUrl,
            placeholder = painterResource(id = R.drawable.track_image_placeholder),
            error = painterResource(id = R.drawable.track_image_placeholder),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .padding(top = 11.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = track.title,
                maxLines = 1,
                fontFamily = nunitoFontFamily,
                color = Color.Black,
                fontSize = 17.sp,
                lineHeight = 22.sp,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = track.artistName,
                maxLines = 1,
                fontFamily = nunitoFontFamily,
                color = Color.Black,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}