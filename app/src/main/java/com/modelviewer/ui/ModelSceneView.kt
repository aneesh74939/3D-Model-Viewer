package com.modelviewer.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.sceneview.SceneView
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.RenderQuality
import com.google.android.filament.Engine

@Composable
fun ModelSceneView(
    engine: Engine,
    modelLoader: ModelLoader,
    modelPath: String,
    modifier: Modifier = Modifier
) {
    val modelInstance = rememberModelInstance(modelLoader, modelPath)

    SceneView(
        modifier = modifier.fillMaxSize(),
        engine = engine,
        modelLoader = modelLoader,
        cameraManipulator = rememberCameraManipulator(),
        renderQuality = RenderQuality.Performance, // Optimize rendering quality for performance
        mainLightNode = null, // Disable main light node to optimize rendering and draw calls
        fillLightNode = null, // Disable fill light to save on lighting computations
        autoCenterContent = true
    ) {
        modelInstance?.let { instance ->
            // Render the 3D model node inside the SceneView
            ModelNode(
                modelInstance = instance,
                scaleToUnits = 1.0f,
                isEditable = false
            )
        }
    }
}
