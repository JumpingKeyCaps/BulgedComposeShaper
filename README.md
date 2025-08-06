# BulgedComposeShaper 

![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-%2300C853.svg?style=for-the-badge&logo=android&logoColor=white)
![AGSL Ready](https://img.shields.io/badge/AGSL%20Shaders-Android%2013%2B-blueviolet?style=for-the-badge)
![Min SDK](https://img.shields.io/badge/minSDK-33+-orange?style=for-the-badge)
![Kotlin](https://img.shields.io/badge/Kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)


**BulgedComposeShaper** is a tiny UI building block for Jetpack Compose:  
it gives you a custom **curved shape** and a **Composable** that reacts to touch, clicks, and AGSL shaders.

Use it to create more organic, dynamic, and visually expressive layouts — with minimal setup.

This shape can be applied to any `Composable` (like `Card`, `Surface`, or `Box`) to give your UI a soft, modern, and elegant visual identity.

---

## 🚀 Features

- 🎯 **Custom Bulged Shape**  
  `BulgedRoundedRectangleShape` lets you control both the **corner radius** and **edge curvature** (`bulgeAmount`) independently.  
  Use positive values to **bulge** outward, or negative ones to **cave** inward.

- 🧩 **Seamless Compose Integration**  
  Works with all `shape`-enabled composables like `Card`, `Surface`, `Box`, and custom layouts.

- 🌀 **AGSL-Ready**  
  Built with support for Android 13+ **AGSL shaders** (Android Graphics Shading Language) to create interactive visual effects like ripple, glow, and morphing.

- ✋ **Touch & Clipping Support**  
  Ensures perfect clipping around the curved shape, even when using `RenderEffect` or custom AGSL shaders.

- 🖼️ **Interactive ClippedImage Composable**  
  A ready-to-use `Composable` that displays an image clipped with the custom shape,  
  and handles touch events to trigger shape morphing or shader animations.  

---

## 🧪 Usage Example

```kotlin
val customShape = remember {
    BulgedRoundedRectangleShape(
        cornerRadius = 25.dp,
        bulgeAmount = 0.03f // value between 1.0 and -1.0 : Positive = bulge, Negative = cave   
    )
}

Card(
    modifier = Modifier
        .width(310.dp)
        .height(310.dp),
    shape = customShape,
    colors = CardDefaults.cardColors(containerColor = Color(0xFFBABABA)),
    elevation = CardDefaults.cardElevation(6.dp)
)
```

## Notes on Complex Shape Clipping

 - It's important to note a `specific behavior within Jetpack Compose's rendering engine` concerning the clipping of complex custom shapes (`Outline.Generic`, such as BulgedRoundedRectangleShape).
   
 - While components like Card apply these shapes perfectly to their backgrounds, clipping a simple Image or other content using Modifier.graphicsLayer(clip = true, shape = someShape) might sometimes fail to fully respect the intricate curves of the edges, only clipping the corners.

 - This behavior is due to Compose's `internal rendering optimizations`.

 - The presence of a RenderEffect (even a "passthrough" shader that visually does nothing to the image) appears to force Compose to utilize a more comprehensive and precise rendering pipeline.

 - This pipeline accurately rasterizes (converts to pixels) the graphic layer's content, fully adhering to the complex Path of the custom shape.
   
 - The BulgedImage composable encapsulates this workaround by `employing a minimalist AGSL shader` to activate this robust rendering , ensuring your image is always perfectly contained within the bulged shape.

---

## 🔮 Roadmap (WIP)

- 🖐️ **Touch-Based Shape Deformation**  
  Animate and morph the shape in response to user interactions, such as touch position or pressure.  
  Goal: create an organic, responsive surface.

- 💧 **AGSL Ripple Effects**  
  Integrate a water-like ripple shader using AGSL (Android 13+), triggered directly at the user's touch point — clipped precisely to the custom shape.

- 🎛️ **Interactive Playground UI**  
  A live preview screen with sliders to tweak `cornerRadius` and `bulgeAmount` values in real time, helping designers/devs quickly prototype the shape.

- 🎨 **Multiple Shape Variants**  
  Explore variations like fully circular bulged shapes or asymmetric curves per edge.

- ⚙️ **Composable Modifier Extensions**  
  Provide utility functions like `.bulgedClip()` or `.bulgedBackground()` for even easier integration.

---

## 📦 Requirements

- **Android Studio** (Giraffe or newer recommended)  
- **Kotlin**  
- **Jetpack Compose**  
- **SDK 33+ (Android 13+)** — required for AGSL shader support

---

## 📸 Preview (coming soon)

A visual example or animated demo will be added here soon.  
Stay tuned 👀

---

## 📜 License

MIT — Free and open source. No restrictions.

---

## ✨ Why this project?

Default shapes in Compose are flat and rigid.  
**BulgedComposeShaper** gives your UI a new visual language: one that’s soft, tactile, and full of subtle character.

It’s a small detail — but it **feels** different.  
And that’s the point.

---

Made with ❤️
