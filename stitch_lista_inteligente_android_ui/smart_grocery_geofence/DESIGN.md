---
name: Smart Grocery Geofence
colors:
  surface: '#faf8ff'
  surface-dim: '#d2d9f4'
  surface-bright: '#faf8ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f3ff'
  surface-container: '#eaedff'
  surface-container-high: '#e2e7ff'
  surface-container-highest: '#dae2fd'
  on-surface: '#131b2e'
  on-surface-variant: '#3e4a3d'
  inverse-surface: '#283044'
  inverse-on-surface: '#eef0ff'
  outline: '#6e7b6c'
  outline-variant: '#bdcaba'
  surface-tint: '#006e2d'
  primary: '#006b2c'
  on-primary: '#ffffff'
  primary-container: '#00873a'
  on-primary-container: '#f7fff2'
  inverse-primary: '#62df7d'
  secondary: '#0051d5'
  on-secondary: '#ffffff'
  secondary-container: '#316bf3'
  on-secondary-container: '#fefcff'
  tertiary: '#825100'
  on-tertiary: '#ffffff'
  tertiary-container: '#a36700'
  on-tertiary-container: '#fffbff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#7ffc97'
  primary-fixed-dim: '#62df7d'
  on-primary-fixed: '#002109'
  on-primary-fixed-variant: '#005320'
  secondary-fixed: '#dbe1ff'
  secondary-fixed-dim: '#b4c5ff'
  on-secondary-fixed: '#00174b'
  on-secondary-fixed-variant: '#003ea8'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#faf8ff'
  on-background: '#131b2e'
  surface-variant: '#dae2fd'
typography:
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 32px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '700'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-mobile: 0.75rem
  margin: 1rem
  margin-tablet: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system establishes a refined, tactile, and frictionless utility environment tailored for on-the-go routine management. Engineered specifically for a native Android context, it combines the clarity of modern utilitarian minimalism with subtle organic warmth. The core intent is cognitive relief: grocery shopping and proximity-triggered reminders must feel immediate, predictable, and stress-free.

### Brand Personality & Emotional Tone
- **Effortless & Reliable:** High affordance and zero cognitive clutter. Interactions feel instantaneous, prioritizing one-handed ergonomics on mobile devices.
- **Friendly & Attentive:** A vibrant botanical emerald green signals completion, productivity, and vitality, while warm slate neutrals ground the interface in premium clarity.
- **Context-Aware:** Subtle ambient cues (geofence indicators, georadius rings, residential context chips) provide situational awareness without distracting visual noise.

### Design Movement
**Modern Native Utility (Refined Material Minimalism):** Rooted in contemporary Android Material 3 ergonomics, avoiding decorative embellishment in favor of surface-driven hierarchy, pristine typographic contrast, deliberate whitespace, and precise, crisp micro-borders.

## Colors

The palette relies on high-luminance canvas tones paired with slate typography to ensure WCAG AAA legibility under direct sunlight. Emerald green anchors primary actions and completed states, cobalt blue clarifies geofence telemetry, and warm amber highlights sensitive device permission prompts.

### Functional Roles & Tone Distribution
- **Canvas Base (`#F8FAFC` - Slate 50):** The default off-white backdrop provides low glare and subtle warmth.
- **Surface Layer (`#FFFFFF`):** Pure white used strictly for elevated cards, bottom sheets, search inputs, and modal dialogs to establish tactile differentiation.
- **Primary Emerald (`#16A34A` / Hover & Glow `#22C55E`):** Represents positive state progression—checked items, primary Floating Action Buttons (FAB), and confirmed configuration toggles.
- **Geofence Cobalt (`#2563EB`):** Dedicated to residential geofencing indicators, monitoring radius badges, and location pinpoint interactions.
- **Alert Amber (`#F59E0B`):** Reserved for contextual friction warnings, notably missing background location or battery optimization permissions.
- **Typography & Structural Neutrals:**
  - `Primary Text (#0F172A)`: Maximum contrast deep slate.
  - `Secondary Text (#64748B)`: Balanced mid-slate for categories, metadata, timestamps, and subtitles.
  - `Subtle Borders (#E2E8F0)`: Delicate 1px structural hairpins that organize cards and interactive chips without visual weight.
  - `Surface Subdued (#F1F5F9)`: Background fill for unselected filter pills, radius chips, and disabled input states.

## Typography

The type hierarchy uses `Inter` across all platforms to leverage its tall x-height, clear numerical distinction, and robust optical legibility in rapid glance scenarios.

### Hierarchy & Application
- **Headline Large (`headline-lg-mobile`):** Used for top app bar titles and major view headings (e.g., "Minha Lista", "Configurações").
- **Headline Medium (`headline-md`):** Modal sheet titles, section dividers, and geofence state cards.
- **Body Large (`body-lg`):** Active grocery list item titles; weighted for immediate recognition while navigating supermarket aisles.
- **Body Medium (`body-md`):** Secondary metadata (e.g., "Adicionado por Maria", quantity indicators, and geofence residential descriptions).
- **Body Small (`body-sm`):** Timestamps, micro-copy, and tertiary helper notes.
- **Label Tokens (`label-lg`, `label-md`, `label-sm`):** Reserved for interactive items, button text, radius selector chips, and permission status pills.
- **Strike-through Rule:** Checked grocery items transition from `body-lg` in `#0F172A` to `body-lg` with `line-through` style in `#94A3B8`, visually retiring completed tasks.

## Layout & Spacing

The layout is built on an adaptable 4px/8px incremental grid, calibrated around Android hand zones and natural thumb reach.

### Layout Model
- **Mobile First Canvas (Single Column):** Default 16px (`1rem`) outer lateral margins preserve content density while accommodating bezel-less edge gestures and Android system navigation bars.
- **Item Rhythm:** Vertical stacks between grocery rows maintain an 8px (`0.5rem`) gap, avoiding crowded touch targets and eliminating accidental mis-taps.
- **Touch Affirmation Zone:** All primary interactive elements (checkboxes, drag handles, chips, action buttons) adhere to a minimum bounding box of 48x48dp to satisfy accessibility standards.
- **FAB Clearance:** The bottom container includes a dynamic bottom inset of at least 88px (`space-xl` + 24px) to ensure items in long scrollable lists are never obscured behind the Floating Action Button or Android home bar.

## Elevation & Depth

Visual hierarchy combines tonal surface differentiation with ultra-diffused, ambient micro-shadows, ensuring pure surfaces lift subtly off the off-white canvas without dense, murky drop-shadows.

### Layer Tiers & Shadows
- **Base Canvas (Level 0):** `#F8FAFC` flat surface.
- **Surface Cards & Inactive Banners (Level 1):** `#FFFFFF` with a 1px border in `#E2E8F0` and an ambient shadow: `0px 1px 3px rgba(15, 23, 42, 0.04), 0px 1px 2px rgba(15, 23, 42, 0.02)`.
- **Active Geofence Banner & Selected Cards (Level 2):** `#FFFFFF` with a primary tinted micro-shadow: `0px 4px 12px -2px rgba(22, 163, 74, 0.08), 0px 2px 6px rgba(15, 23, 42, 0.04)`.
- **Floating Action Button (FAB) & Bottom Sheets (Level 3):**
  - FAB: Elevated with an emerald bloom: `0px 8px 20px -4px rgba(22, 163, 74, 0.35)`.
  - Bottom Sheets: Elevated with an ambient sheet curtain shadow: `0px -8px 24px rgba(15, 23, 42, 0.08)`.

## Shapes

The design uses balanced, modern curves. A base radius of 8px (`rounded-md`), large radii of 16px (`rounded-xl`), and extra-large radii of 24px (`rounded-2xl`) match the friendly yet efficient persona of the application.

### Geometry Breakdown
- **Item Cards & Surface Panels:** `rounded-xl` (16px) for approachable boundaries that fit mobile viewports cleanly.
- **Bottom Sheets:** Top-left and top-right corners styled with `rounded-2xl` (24px) to ground modal overlays natively.
- **Pills, FAB, and Badges:** Fully circular or pill-shaped (`rounded-full` / 9999px) for radius selectors, permission status indicators, and the circular Floating Action Button.
- **Checkboxes:** `rounded-md` (8px) for an organic, tactile tap area.

## Components

### 1. Item Cards & Grocery List Rows
- **Structure:** White (`#FFFFFF`) background, 16px padding, 1px border (`#E2E8F0`), `rounded-xl`. Contains a 48px leading touch target for the checkbox, item title (`body-lg`), and optional category or quantity tag (`label-sm`).
- **Completed State:** Checkbox fills with `#16A34A`, showing a white checkmark. The title transitions to `#94A3B8` with `line-through`. The card background shifts to `#F8FAFC` with zero elevation.

### 2. Checkboxes
- **Specs:** 24x24dp box centered within a 48x48dp interactive frame.
- **Unchecked:** 2px border in `#CBD5E1`, transparent fill.
- **Checked:** Solid `#16A34A` fill, `#16A34A` border, high-contrast white glyph (`#FFFFFF`).

### 3. Floating Action Button (FAB)
- **Specs:** 56x56dp circular container (`rounded-full`), positioned 16px from the bottom-right viewport boundary.
- **Styling:** Solid `#16A34A` background with `#FFFFFF` plus icon (28px). Applies emerald shadow elevation (Level 3). On press, scales slightly (0.96) with an Android ripple effect.

### 4. Geofencing Monitoring Status Banner
- **Placement:** Anchored to the top of the main list immediately below the app bar.
- **Active State:** Light blue surface (`#EFF6FF`), 1px border (`#BFDBFE`), `rounded-xl`, 12px 16px padding. Displays a pulsating cobalt indicator dot (`#2563EB`), text "Monitoramento Ativo: Casa", and current trigger status.
- **Warning/Pending State:** Light amber surface (`#FFFBEB`), 1px border (`#FDE68A`), alert icon (`#F59E0B`), and a "Permissão Necessária" text CTA.

### 5. Radius Selector Chips (100m, 150m, 200m, 300m)
- **Specs:** Height 36px, `rounded-full`, 8px 16px horizontal padding.
- **Default State:** Background `#F1F5F9`, text `#64748B` (`label-md`).
- **Active State:** Background `#2563EB`, text `#FFFFFF` (`label-md`), subtle shadow.

### 6. Permission Badges
- **Granted:** Background `#DCFCE7`, text `#15803D`, dot `#16A34A`, `label-sm`.
- **Pending:** Background `#FEF3C7`, text `#B45309`, dot `#F59E0B`, `label-sm`.

### 7. Bottom Sheets
- **Styling:** Background `#FFFFFF`, top corners `rounded-2xl`, top handle centered (36x4dp, `#CBD5E1`, `rounded-full`). Houses the quick-add item input, category chips, and geofence radius adjustments.

### 8. Native Switches & Input Fields
- **Switches:** Android native toggle track with `#E2E8F0` when off, turning `#16A34A` when on, with a white thumb (`#FFFFFF`).
- **Input Fields:** 48px height, `#FFFFFF` background, 1px border in `#CBD5E1`, focusing to a 2px `#16A34A` border without layout shift.