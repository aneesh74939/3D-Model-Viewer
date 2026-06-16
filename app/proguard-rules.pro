# ProGuard rules for 3D Model Viewer

# SceneView / Filament
-keep class com.google.android.filament.** { *; }
-keep class io.github.sceneview.** { *; }

# Keep Filament native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
