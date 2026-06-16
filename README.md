# 3D Model Viewer (Jetpack Compose)

A high-performance Android application designed to view multiple interactive 3D models simultaneously. This project is heavily optimized for **low-end devices** with limited CPU, GPU, and RAM.

## 🚀 3D Library & Architecture

The app is built on **[SceneView](https://github.com/SceneView/sceneview-android)**, which leverages Google's **Filament** PBR engine.

### Why SceneView/Filament?
- **Efficiency:** Filament is designed for mobile-first PBR, offering superior performance compared to heavier engines like Unity or Unreal.
- **Compose Native:** Seamless integration with Jetpack Compose prevents unnecessary UI overhead.

## ⚡ Performance Optimizations (Low-End Focus)

To ensure smooth 60fps on weak hardware, the following strategies were implemented:

1.  **Shared Global Engine (RAM Optimization):**
    - The Filament `Engine` and `ModelLoader` are initialized **once** at the screen level and shared across all instances. This prevents massive memory bloat and reduces the startup time for each new window.
2.  **Aggressive Light Culling (GPU Optimization):**
    - Individual scenes have `mainLightNode` and `fillLightNode` **disabled**. This reduces draw calls and eliminates expensive shadow map calculations, which are the primary bottleneck on older GPUs.
3.  **Render Quality Scaling:**
    - `RenderQuality.Performance` is forced, prioritizing fill rate and frame stability over high-end anti-aliasing.
4.  **Zero-Allocation State Management (CPU Optimization):**
    - Using `SnapshotStateList` and in-place updates instead of list copying during gestures. This minimizes Garbage Collection (GC) pressure, preventing "jank" during drags and resizes on weak CPUs.
5.  **Recomposition Isolation:**
    - Use of `key(modelState.id)` ensures that moving one model doesn't trigger a recomposition or re-render of other active 3D viewports.
6.  **Direct GPU Composition:**
    - SceneView uses `SurfaceView` internally, which allows the 3D content to be composed by the System Compositor (Hardware Overlays) rather than the standard Android View hierarchy, saving significant GPU cycles.

## ⚖️ Trade-offs

-   **Manual Mode Toggle:** To prevent touch event conflicts between the Compose "Window" gestures and the 3D "Camera" gestures, a manual toggle is used. This ensures reliable performance without complex gesture disambiguation logic.
-   **Static Lighting:** By disabling dynamic lights per-scene, models rely on their internal materials and global environment settings. This was a necessary compromise to support 4+ simultaneous windows on entry-level hardware.

## 🛠️ Future Improvements

-   **VRAM Pooling:** Implementing a `ModelInstance` pool to reduce GPU memory fragmentation.
-   **Level of Detail (LOD):** Automatically swapping to lower-poly models when a window is scaled down.

## 🐞 Known Bugs & Limitations

-   **Overdraw:** Overlapping multiple 3D windows can cause overdraw. On very old devices, it is recommended to keep models spread apart.
-   **Gesture Ambiguity:** In "UI Mode," edge-swiping can sometimes be misinterpreted by the system as a "Back" gesture if the container is near the screen edge.

---
*Optimized for performance. Verified on low-mid range Android hardware.*
