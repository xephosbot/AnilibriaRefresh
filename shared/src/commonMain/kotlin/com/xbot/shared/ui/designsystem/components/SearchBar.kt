@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package com.xbot.shared.ui.designsystem.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputFieldHeight
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchInputField(
    state: TextFieldState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    colors: TextFieldColors = inputFieldColors(),
    windowInsets: WindowInsets = SearchBarDefaults.windowInsets,
    interactionSource: MutableInteractionSource? = null
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val focused = interactionSource.collectIsFocusedAsState().value
    val focusRequester = remember { FocusRequester() }

    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError = false, focused = focused)
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    BasicTextField(
        state = state,
        modifier = modifier
            .sizeIn(minHeight = InputFieldHeight)
            .focusRequester(focusRequester)
            .windowInsetsPadding(windowInsets),
        enabled = enabled,
        readOnly = readOnly,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError = false)),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onKeyboardAction = { onSearch(state.text.toString()) },
        interactionSource = interactionSource,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        scrollState = scrollState,
        decorator = TextFieldDefaults.decorator(
            state = state,
            enabled = enabled,
            lineLimits = TextFieldLineLimits.SingleLine,
            outputTransformation = outputTransformation,
            interactionSource = interactionSource,
            placeholder = placeholder,
            leadingIcon =
            leadingIcon?.let { leading ->
                { Box(Modifier.offset(x = SearchBarIconOffsetX)) { leading() } }
            },
            trailingIcon =
            trailingIcon?.let { trailing ->
                { Box(Modifier.offset(x = -SearchBarIconOffsetX)) { trailing() } }
            },
            prefix = prefix,
            suffix = suffix,
            colors = colors,
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
            container = {
                Box(modifier = Modifier.fillMaxSize())
            }
        )
    )
}

private val SearchBarIconOffsetX: Dp = 4.dp