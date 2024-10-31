package com.xbot.anilibriarefresh.ui.feature.title.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.components.ButtonComponent

@Composable
fun DescriptionBox(
    modifier: Modifier = Modifier,
    text: String,
    scrollState: ScrollState,
    flowRowSize: MutableIntState
) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Column(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        //TODO: переделать
//        if (expanded) {
//            LaunchedEffect(true) {
//                coroutineScope.launch {
//                    textLayoutResult.value?.let { layoutResult ->
//                        val scrollTo = layoutResult.size.height + flowRowSize.intValue
//                        scrollState.animateScrollTo(scrollTo)
//                    }
//                }
//            }
//        }
        Text(text = text,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            onTextLayout = { layoutResult ->
                textLayoutResult.value = layoutResult
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.LightGray
                ),
            onClick = {
                expanded = !expanded
            },
            text = stringResource(
                if (expanded) R.string.hide_text_button else R.string.show_text_button
            ),
        )
        Spacer(Modifier.padding(bottom = 16.dp))
    }
}