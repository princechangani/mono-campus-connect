# Campus Connect Theme (Flutter Ready)

This file captures the current frontend theme so you can re-create it in a Flutter mobile app.

## Brand & Visual Direction
- Clean academic SaaS look
- Blue primary with soft neutrals
- Subtle elevation, rounded corners
- Light UI with strong primary accents

## Colors
### Primary (from `tailwind.config.js`)
- `primary-50`  `#eff6ff`
- `primary-100` `#dbeafe`
- `primary-200` `#bfdbfe`
- `primary-300` `#93c5fd`
- `primary-400` `#60a5fa`
- `primary-500` `#3b82f6`
- `primary-600` `#2563eb` (main)
- `primary-700` `#1d4ed8`
- `primary-800` `#1e40af`
- `primary-900` `#1e3a8a`

### Neutral (Tailwind defaults used in UI)
- `white` `#ffffff`
- `gray-50` `#f9fafb` (app background)
- `gray-100` `#f3f4f6` (card borders)
- `gray-300` `#d1d5db` (inputs, borders)
- `gray-400` `#9ca3af` (placeholders)
- `gray-700` `#374151` (secondary text)
- `gray-900` `#111827` (primary text)

### Semantic
- Success `green-500` `#22c55e`
- Danger  `red-600`   `#dc2626`
- Danger  `red-700`   `#b91c1c`
- Warning `yellow-500` `#eab308`

### Sidebar
- `sidebar` `#1e293b`

### Gradients
- Auth screens: `primary-600` → `primary-900`

## Typography
- Font family: `Inter`
- Base text color: `gray-900`
- Subtle text: `gray-700` / `gray-500`

Recommended Flutter text sizes:
- Title Large: 22
- Title Medium: 18
- Body: 14–16
- Caption: 12

## Spacing (Tailwind scale reference)
- `p-2` = 8px
- `p-3` = 12px
- `p-4` = 16px
- `p-6` = 24px

## Radius
- `rounded-lg` = 8px (buttons, inputs)
- `rounded-xl` = 12px (cards)
- `rounded-full` (badges, avatars)

## Elevation / Shadows
- Cards: subtle shadow (Tailwind `shadow-sm`)

## Component Tokens (from `globals.css`)
- Primary button: `primary-600` bg, hover `primary-700`, white text
- Secondary button: white bg, gray-300 border, gray-700 text
- Danger button: red-600 bg, hover red-700
- Input: gray-300 border, focus ring `primary-500`
- Card: white bg, border gray-100, radius 12px
- Badge: small pill, text-xs, radius full
- Sidebar link: slate-300 text, hover slate-700 bg, active `primary-600`

## Flutter Theme Mapping (suggested)
- `ColorScheme.primary` = `#2563eb`
- `ColorScheme.secondary` = `#3b82f6`
- `ColorScheme.error` = `#dc2626`
- `ScaffoldBackgroundColor` = `#f9fafb`
- `CardTheme` = white, radius 12, elevation 1–2
- `InputDecorationTheme` = outline, border `#d1d5db`, focus `#3b82f6`
- `ElevatedButtonTheme` = bg `#2563eb`, radius 8
- `OutlinedButtonTheme` = border `#d1d5db`, text `#374151`
- `ChipTheme` (badges) = radius 999, small font, light primary background
