package com.modelviewer.model

import java.util.UUID

data class ModelState(
    val id: String = UUID.randomUUID().toString(),
    val modelPath: String,         // e.g., "models/duck.glb"
    val displayName: String,       // e.g., "Duck"
    val offsetX: Float = 100f,     // screen X position in pixels or dp (we will use dp or pixels)
    val offsetY: Float = 200f,     // screen Y position
    val containerWidth: Float = 280f,  // in dp
    val containerHeight: Float = 280f, // in dp
    val isInteracting: Boolean = false // false = normal mode (drag/resize), true = interaction mode (orbit/zoom)
)
