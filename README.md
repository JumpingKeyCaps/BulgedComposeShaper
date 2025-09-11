# BulgedComposeShaper 

![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-%2300C853.svg?style=for-the-badge&logo=android&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSDK-33+-orange?style=for-the-badge)
![Kotlin](https://img.shields.io/badge/Kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)


**BulgedComposeShaper** is a tiny UI building block for Jetpack Compose:  
it gives you a custom **curved shape** and a **Composable** that reacts to touch and clicks.

Use it to create more organic, dynamic, and visually expressive layouts with minimal setup.

This shape can be applied to any `Composable` (like `Card`, `Surface`, or `Box`) to give your UI a soft, modern, and elegant visual identity.

---

## 🚀 Features

- 🎯 **Custom Bulged Shape**  
  `BulgedRoundedRectangleShape` lets you control both the **corner radius** and **edge curvature** (`bulgeAmount`) independently.  
  Use positive values to **bulge** outward, or negative ones to **cave** inward.

- 🧩 **Seamless Compose Integration**  
  Works with all `shape`-enabled composables like `Card`, `Surface`, `Box`, and custom layouts.

- ✋ **Perfect Clipping for Images**  
  Scale your bitmap (1.05–1.25x) with `ContentScale.Crop` for flawless clipping inside the bulged shape.

- 🖼️ **Ready-to-use ClippedImage Composable**  
  Handles scaling and clipping automatically.

---

## 📸 Screenshots

| Bulged Shape Morphing | Shape Preview 1 | Shape Preview 2 | Touch Interaction |
|:---:|:---:|:---:|:---:|
| ![BSM](screenshots/bulgedShapeDemo1.gif) | ![SP1](screenshots/bulgedShapeDemo2.gif) |  ![SP2](screenshots/bulgedShapeDemo3.gif) |  ![TI](screenshots/bulgedShapeDemo4.gif) |

---


## 🔮 Roadmap (WIP)

- 🖐️ **Touch-Based Shape Deformation**  
  Animate and morph the shape in response to user interactions, such as touch position or pressure.  
  Goal: create an organic, responsive surface.

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

---

## ✨ Why this project?

Default shapes in Compose are flat and rigid.  
**BulgedComposeShaper** gives your UI a new visual language: one that’s soft, tactile, and full of subtle character.

It’s a small detail — but it **feels** different.  
And that’s the point.

---

Made with ❤️
