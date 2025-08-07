package com.lebaillyapp.bulgedcomposeshaper.bulgedShape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp

/**
 * A custom [Shape] that renders a rounded rectangle with gently bulging sides.
 *
 * Each side of the rectangle can curve outward (convex) based on a configurable bulge factor.
 * This creates a more organic and fluid look compared to traditional sharp or flat-edged rectangles.
 *
 * Designed to be used in Jetpack Compose UIs where subtle visual personality is desired —
 * like interactive surfaces, image clipping, or animated shader effects.
 *
 * @param cornerRadius The corner radius in [Dp]. Ensures rounded corners that blend smoothly with the bulged sides.
 * @param bulgeAmount The relative strength of the bulge on each side.
 * - `0f` means flat edges (classic rounded rectangle)
 * - Positive values (e.g., `0.05f`) create outward bulges.
 *
 * The bulge is computed as a proportion of the smallest dimension (width or height),
 * so it scales responsively with different layout sizes.
 *
 * Example usage:
 * ```
 * shape = BulgedRoundedRectangleShape(cornerRadius = 24.dp, bulgeAmount = 0.07f)
 * ```
 *
 * Note: Bulging is applied uniformly to all four sides.
 */

class BulgedRoundedRectangleShape(
    private val cornerRadius: Dp,
    private val bulgeAmount: Float = 0f // Bulge amount as a normalized factor (0 to 1, or more)
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val width = size.width
        val height = size.height
        val cornerPx = with(density) { cornerRadius.toPx() }
        // Assure que le rayon ne dépasse pas la moitié de la plus petite dimension
        val radius = cornerPx.coerceAtMost(minOf(width, height) / 2f)

        // Calcule l'amplitude du bombement en pixels
        // Le 0.5f ici est une base, à ajuster pour un bulge qui s'harmonise avec le radius
        val bulgePx = minOf(width, height) * bulgeAmount * 0.5f

        // Constante pour approximer un quart de cercle avec une courbe de Bézier cubique
        val C = 0.5522847498f

        // Coordonnées pour faciliter la lecture
        val left = 0f
        val top = 0f
        val right = width
        val bottom = height

        // Points où les arcs de cercle "normaux" commenceraient/finiraient
        val xRadius = radius
        val yRadius = radius

        // --- Construction du chemin ---

        // Commence juste après le coin supérieur gauche (sur le segment supérieur)
        path.moveTo(left + xRadius, top)

        // TOP-RIGHT corner (including bulge)
        // Courbe de Bézier pour aller du point (left + xRadius, top) à (right - xRadius, top)
        // tout en bombant vers le haut.
        // Les points de contrôle sont calculés pour "tirer" la courbe et se raccorder en douceur.
        path.cubicTo(
            left + xRadius + (width - 2 * xRadius) * 0.25f, top - bulgePx, // CP1
            right - xRadius - (width - 2 * xRadius) * 0.25f, top - bulgePx, // CP2
            right - xRadius, top // End point
        )

        // TOP-RIGHT ARC (partie arrondie "normale" du coin)
        path.cubicTo(
            right - xRadius + xRadius * C, top, // CP1 for arc
            right, top + yRadius - yRadius * C, // CP2 for arc
            right, top + yRadius // End point of arc
        )

        // RIGHT-BOTTOM corner (including bulge)
        // Courbe de Bézier pour aller de (right, top + yRadius) à (right, bottom - yRadius)
        // tout en bombant vers la droite.
        path.cubicTo(
            right + bulgePx, top + yRadius + (height - 2 * yRadius) * 0.25f, // CP1
            right + bulgePx, bottom - yRadius - (height - 2 * yRadius) * 0.25f, // CP2
            right, bottom - yRadius // End point
        )

        // BOTTOM-RIGHT ARC
        path.cubicTo(
            right, bottom - yRadius + yRadius * C,
            right - xRadius + xRadius * C, bottom,
            right - xRadius, bottom
        )

        // BOTTOM-LEFT corner (including bulge)
        // Courbe de Bézier pour aller de (right - xRadius, bottom) à (left + xRadius, bottom)
        // tout en bombant vers le bas.
        path.cubicTo(
            right - xRadius - (width - 2 * xRadius) * 0.25f, bottom + bulgePx,
            left + xRadius + (width - 2 * xRadius) * 0.25f, bottom + bulgePx,
            left + xRadius, bottom
        )

        // BOTTOM-LEFT ARC
        path.cubicTo(
            left + xRadius - xRadius * C, bottom,
            left, bottom - yRadius + yRadius * C,
            left, bottom - yRadius
        )

        // LEFT-TOP corner (including bulge)
        // Courbe de Bézier pour aller de (left, bottom - yRadius) à (left, top + yRadius)
        // tout en bombant vers la gauche.
        path.cubicTo(
            left - bulgePx, bottom - yRadius - (height - 2 * yRadius) * 0.25f,
            left - bulgePx, top + yRadius + (height - 2 * yRadius) * 0.25f,
            left, top + yRadius
        )

        // TOP-LEFT ARC (ferme le chemin)
        path.cubicTo(
            left, top + yRadius - yRadius * C,
            left + xRadius - xRadius * C, top,
            left + xRadius, top
        )

        path.close()
        return Outline.Generic(path)
    }
}



