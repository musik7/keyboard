package com.coding.keyboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.coding.keyboard.core.Constants
import com.coding.keyboard.ui.theme.KeyboardTheme

@Composable
fun KeyContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = KeyboardTheme.colors.keyBackground,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val view = LocalView.current
    var boxModifier = modifier
        .padding(
            horizontal = KeyboardTheme.dimens.keyHorizontalPadding,
            vertical = KeyboardTheme.dimens.keyVerticalPadding
        )
        .height(KeyboardTheme.dimens.keyHeight)
        .clip(RoundedCornerShape(KeyboardTheme.dimens.keyCornerRadius))
        .background(backgroundColor)

    if (onClick != null) {
        boxModifier = boxModifier.clickable {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            onClick()
        }
    }

    Box(
        modifier = boxModifier,
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun TextKey(
    label: String,
    modifier: Modifier = Modifier,
    isAction: Boolean = false,
    textStyle: TextStyle = KeyboardTheme.typography.mainChar,
    onClick: () -> Unit
) {
    KeyContainer(
        modifier = modifier,
        backgroundColor = if (isAction) KeyboardTheme.colors.keyBackgroundAction else KeyboardTheme.colors.keyBackground,
        onClick = onClick
    ) {
        Text(
            text = label,
            color = if (isAction) KeyboardTheme.colors.keyTextAction else KeyboardTheme.colors.keyText,
            style = textStyle
        )
    }
}

@Composable
fun IconKey(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isAction: Boolean = true,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    KeyContainer(
        modifier = modifier,
        backgroundColor = if (isActive) KeyboardTheme.colors.accent else if (isAction) KeyboardTheme.colors.keyBackgroundAction else KeyboardTheme.colors.keyBackground,
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) KeyboardTheme.colors.accentText else if (isAction) KeyboardTheme.colors.keyTextAction else KeyboardTheme.colors.keyText,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ModifierKey(
    label: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    KeyContainer(
        modifier = modifier,
        backgroundColor = if (isActive) KeyboardTheme.colors.accent else KeyboardTheme.colors.keyBackgroundAction,
        onClick = onClick
    ) {
        Text(
            text = label,
            color = if (isActive) KeyboardTheme.colors.accentText else KeyboardTheme.colors.keyTextAction,
            style = KeyboardTheme.typography.label
        )
    }
}

@Composable
fun SpacebarKey(
    modifier: Modifier = Modifier,
    onTap: () -> Unit,
    onMoveCursorLeftRight: (Int) -> Unit,
    onMoveCursorUpDown: (Int) -> Unit
) {
    val view = LocalView.current
    var accumulatedDragX by remember { mutableStateOf(0f) }
    var accumulatedDragY by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .padding(
                horizontal = KeyboardTheme.dimens.keyHorizontalPadding,
                vertical = KeyboardTheme.dimens.keyVerticalPadding
            )
            .height(KeyboardTheme.dimens.keyHeight)
            .clip(RoundedCornerShape(KeyboardTheme.dimens.keyCornerRadius))
            .background(KeyboardTheme.colors.keyBackgroundAction)
            .clickable { 
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                onTap() 
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { 
                         accumulatedDragX = 0f 
                         accumulatedDragY = 0f
                    },
                    onDragEnd = { 
                         accumulatedDragX = 0f
                        accumulatedDragY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        accumulatedDragX += dragAmount.x
                        accumulatedDragY += dragAmount.y

                        if (kotlin.math.abs(accumulatedDragX) > Constants.SWIPE_CURSOR_THRESHOLD_PX) {
                            view.performHapticFeedback(9) 
                            if (accumulatedDragX > 0) onMoveCursorLeftRight(1)
                            else onMoveCursorLeftRight(-1)
                            accumulatedDragX = 0f
                            accumulatedDragY = 0f
                        } else if (kotlin.math.abs(accumulatedDragY) > Constants.SWIPE_CURSOR_THRESHOLD_PX) {
                            view.performHapticFeedback(9)
                            if (accumulatedDragY > 0) onMoveCursorUpDown(1)
                            else onMoveCursorUpDown(-1)
                            accumulatedDragY = 0f
                            accumulatedDragX = 0f
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Constants.LABEL_SPACE,
            color = KeyboardTheme.colors.keyTextAction,
            style = KeyboardTheme.typography.label
        )
    }
}
