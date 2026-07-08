package com.chaos.mangaties.core.component.text

sealed class TextState {
    object DisplayLarge : TextState()   // Hero / splash
    object DisplaySmall : TextState()   // Section hero
    object HeadingLarge : TextState()   // Screen titles
    object HeadingMedium : TextState()  // Card titles
    object HeadingSmall : TextState()   // Sub-sections
    object BodyLarge : TextState()      // Primary readable text
    object BodyMedium : TextState()     // Secondary body
    object BodySmall : TextState()      // Captions / hints
    object Label : TextState()          // Form labels, tags
    object Overline : TextState()       // Uppercase category label
    object Link : TextState()           // Clickable text
    object Code : TextState()           // Monospace snippets
}