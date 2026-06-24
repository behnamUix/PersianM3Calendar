package com.behnamuix.persianM3Calendar.ui.persianCalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CalendarHeader(
    title: String,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {

    val spring = listOf("فروردین", "اردیبهشت", "خرداد")
    val summer = listOf("تیر", "مرداد", "شهریور")
    val autumn = listOf("مهر", "آبان", "آذر")
    val winter = listOf("دی", "بهمن", "اسفند")

    val monthName = title.split(" ")[0]

    val imageUrl = when (monthName) {
        in spring -> "https://images.pexels.com/photos/36764/marguerite-daisy-beautiful-beauty.jpg"
        in summer -> "https://images.pexels.com/photos/457882/pexels-photo-457882.jpeg"
        in autumn -> "https://images.pexels.com/photos/33109/fall-autumn-red-season.jpg"
        in winter -> "https://images.pexels.com/photos/688660/pexels-photo-688660.jpeg"
        else -> ""
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {

        // 🖼 Background Image
        androidx.compose.foundation.Image(
            painter = coil.compose.rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        // 🌫 Overlay برای خوانایی متن
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        // UI Content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onPrev) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}