# Skip Game Feature

## Overview

The Stardew Fishing mod now includes a client-side command that allows players to skip the fishing minigame and automatically receive successful fishing results.

## Usage

### Command Syntax
```
/stardewfishing skip-game <on|off>
```

### Examples
- Enable skip-game: `/stardewfishing skip-game on`
- Disable skip-game: `/stardewfishing skip-game off`

## Behavior

When skip-game is **enabled**:
- The fishing minigame UI will not appear when you catch a fish
- The mod automatically sends a successful fishing result to the server
- You receive **perfect accuracy (1.0)** results, which means:
  - Maximum quality fish (if Quality Food mod is installed)
  - Maximum experience gains
  - Treasure chests are collected automatically if they were available

When skip-game is **disabled** (default):
- Normal fishing minigame behavior
- Players must complete the minigame manually
- Results depend on actual player performance

## Technical Details

### Client-Server Communication
1. **Normal Flow**: Server → Client (start minigame) → Player plays → Client → Server (results)
2. **Skip Flow**: Server → Client (start minigame) → Auto-success → Client → Server (perfect results)

### Data Preserved
- Fish type and behavior parameters from server
- Treasure chest availability and type
- All server-side reward calculations and modifiers
- Experience and quality modifiers based on perfect accuracy

### Implementation
- Client-side only feature (no server modifications needed)
- State is stored in memory (resets when client restarts)
- Uses existing networking packets (C2SCompleteMinigamePacket)
- Maintains compatibility with all existing server features

## Notes

- This is a **client-side only** command and setting
- The skip state resets when you restart Minecraft
- Server admins cannot detect or control this feature
- Works with all fish types, treasure chests, and mod integrations
- Preserves all server-side balancing and reward calculations