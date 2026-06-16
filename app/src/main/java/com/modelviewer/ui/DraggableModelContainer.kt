package com.modelviewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.modelviewer.model.ModelState
import io.github.sceneview.loaders.ModelLoader
import kotlin.math.roundToInt

@Composable
fun DraggableModelContainer(
    engine: Engine,
    modelLoader: ModelLoader,
    state: ModelState,
    onMove: (String, Float, Float) -> Unit,
    onResize: (String, Float) -> Unit,
    onToggleInteraction: (String) -> Unit,
    onClose: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .offset { IntOffset(state.offsetX.roundToInt(), state.offsetY.roundToInt()) }
            .size(state.containerWidth.dp, state.containerHeight.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.15f))
            .border(
                width = 3.dp,
                color = if (state.isInteracting) Color(0xFF4CAF50) else Color(0x66FFFFFF),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        // 3D Viewport Rendering
        ModelSceneView(
            engine = engine,
            modelLoader = modelLoader,
            modelPath = state.modelPath,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay transparent Box to consume touches when in normal mode
        // This prevents touches from passing down to the SceneView's camera manipulator
        if (!state.isInteracting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(state.id) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            onMove(state.id, pan.x, pan.y)
                            onResize(state.id, zoom)
                        }
                    }
            )
        }

        // Mode and Control buttons overlay
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Interact button (Toggles between Normal Mode and 3D Rotation/Zoom Mode)
            Button(
                onClick = { onToggleInteraction(state.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isInteracting) Color(0xFF4CAF50) else Color(0xAA000000)
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(36.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = "Interact Mode",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (state.isInteracting) "3D Mode" else "UI Mode",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }

            // Close Button
            IconButton(
                onClick = { onClose(state.id) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xCCD32F2F)
                ),
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Model",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
