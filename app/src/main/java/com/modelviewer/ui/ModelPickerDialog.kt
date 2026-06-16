package com.modelviewer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ModelOption(
    val path: String,
    val name: String,
    val description: String
)

val availableModels = listOf(
    ModelOption("models/duck.glb", "Duck", "Simple low-poly yellow duck (~118KB)"),
    ModelOption("models/box_textured.glb", "Textured Box", "Simple box with textures (~6KB)"),
    ModelOption("models/fox.glb", "Fox", "Low-poly running fox (~159KB)"),
    ModelOption("models/milk_truck.glb", "Cesium Milk Truck", "Animated toy delivery truck (~361KB)"),
    ModelOption("models/damaged_helmet.glb", "Damaged Helmet", "Detailed sci-fi helmet (~3.6MB)")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelPickerDialog(
    onDismissRequest: () -> Unit,
    onModelSelected: (ModelOption) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        title = {
            Text("Select a 3D Model")
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableModels) { option ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onModelSelected(option) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = option.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = option.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    )
}
