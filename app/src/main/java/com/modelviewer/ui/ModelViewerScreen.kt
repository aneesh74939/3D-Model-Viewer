package com.modelviewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.modelviewer.model.ModelState
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelViewerScreen() {
    val context = LocalContext.current

    // Share a single Filament Engine and ModelLoader across all SceneViews for high performance
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    // Use mutableStateListOf for high-performance state updates (avoids list copying during gestures)
    val models = remember { mutableStateListOf<ModelState>() }
    var showPickerDialog by remember { mutableStateOf(false) }

    // Offset count to staggered spawn positions
    var spawnCount by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A), // Slate 900
                        Color(0xFF1E293B)  // Slate 800
                    )
                )
            )
    ) {
        // Render all active 3D model containers
        models.forEach { modelState ->
            key(modelState.id) {
                DraggableModelContainer(
                    engine = engine,
                    modelLoader = modelLoader,
                    state = modelState,
                    onMove = { id, dx, dy ->
                        val index = models.indexOfFirst { it.id == id }
                        if (index != -1) {
                            models[index] = models[index].copy(
                                offsetX = models[index].offsetX + dx,
                                offsetY = models[index].offsetY + dy
                            )
                        }
                    },
                    onResize = { id, scale ->
                        val index = models.indexOfFirst { it.id == id }
                        if (index != -1) {
                            val state = models[index]
                            val newWidth = (state.containerWidth * scale).coerceIn(120f, 600f)
                            val newHeight = (state.containerHeight * scale).coerceIn(120f, 600f)
                            models[index] = state.copy(
                                containerWidth = newWidth,
                                containerHeight = newHeight
                            )
                        }
                    },
                    onToggleInteraction = { id ->
                        models.forEachIndexed { index, state ->
                            if (state.id == id) {
                                models[index] = state.copy(isInteracting = !state.isInteracting)
                            } else {
                                models[index] = state.copy(isInteracting = false)
                            }
                        }
                    },
                    onClose = { id ->
                        models.removeIf { it.id == id }
                    }
                )
            }
        }

        // Empty state overlay
        if (models.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Models Loaded",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap the '+' button below to place interactive 3D models on screen.",
                    color = Color.LightGray.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Beautiful glassmorphic top header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x22FFFFFF)
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "3D Model Viewer",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Active models: ${models.size}",
                        color = Color(0xFF94A3B8), // Slate 400
                        fontSize = 13.sp
                    )
                }

                if (models.isNotEmpty()) {
                    IconButton(
                        onClick = { models.clear() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0x33EF4444)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear All",
                            tint = Color(0xFFEF4444)
                        )
                    }
                }
            }
        }

        // Floating Action Button to Add Model
        LargeFloatingActionButton(
            onClick = { showPickerDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp),
            containerColor = Color(0xFF3B82F6), // Blue 500
            contentColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Model",
                modifier = Modifier.size(36.dp)
            )
        }

        // Bottom picker dialog
        if (showPickerDialog) {
            ModelPickerDialog(
                onDismissRequest = { showPickerDialog = false },
                onModelSelected = { option ->
                    showPickerDialog = false
                    // Stagger the spawn positions so models don't spawn on top of each other
                    val staggerOffset = (spawnCount % 4) * 80f
                    spawnCount++

                    val newModel = ModelState(
                        modelPath = option.path,
                        displayName = option.name,
                        offsetX = 100f + staggerOffset,
                        offsetY = 300f + staggerOffset,
                        containerWidth = 260f,
                        containerHeight = 260f
                    )
                    models.add(newModel)
                }
            )
        }
    }
}
