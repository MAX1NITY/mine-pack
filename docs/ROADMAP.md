# Roadmap

Target: Minecraft 1.20.1, Forge, singleplayer, about 150-200 mods, 6-8 GB RAM.

## Custom mods

| Mod | Status | Purpose |
| --- | --- | --- |
| Ascension Core | v0.1 in repo | Shared Tech/Arcane/Wanderer progression, tags for KubeJS and quests |
| Rift Expeditions | planned | Procedural, scaling-difficulty dungeon dimension feeding all three paths |
| Contract Board | planned | Bounty and daily-challenge block spanning all playstyles |
| Dreamscape | planned | Surreal dimension: floating islands, shifting biomes, bent physics |
| Mutator Events | planned | Random world events to keep long sessions unpredictable |
| Companions | planned | Tameable allies that level differently per path |
| Codex | planned | In-game journal tying discoveries and lore together |

## Curated base (verify each mod has a 1.20.1 Forge build before adding)

- Foundation: Embeddium, Oculus, ModernFix, FerriteCore, Entity Culling, JEI, Jade, JourneyMap,
  Sophisticated Backpacks, AppleSkin, Waystones, KubeJS, FTB Quests
- Exploration: Terralith, Biomes O' Plenty, YUNG's structure mods, Twilight Forest, The Aether,
  Alex's Mobs, L_Ender's Cataclysm, Mowzie's Mobs, Epic Fight, Better Combat, Simply Swords
- Tech: Create, Mekanism, Applied Energistics 2
- Magic/RPG: Iron's Spells 'n Spellbooks, Ars Nouveau, Botania, Apotheosis, Passive Skill Tree,
  Artifacts, Curios
- Cozy: Farmer's Delight, Supplementaries, Macaw's mods

## Status

- Done: Ascension Core v0.1, packwiz pack (79 mods incl. dependencies, all resolved by exact Modrinth slug),
  pack export with custom mods included, and a real Forge server boot test that passes in CI.
- Not covered by the server test: client-only code (rendering, shaders, UI). Launch the pack on a PC to check.
- Known gaps: FTB Quests and Twilight Forest are not available for Forge 1.20.1 on Modrinth, so they are out.
  Two Iron's Spells 'n Spellbooks loot tables log parse warnings on startup (some dungeon chest loot may be affected).

## Next steps

1. Launch the pack on a real PC, report crashes or anything that feels off.
2. KubeJS layer: replace placeholder essence recipes with mod-aware ones, gate content on tier tags.
3. Build Rift Expeditions, then Contract Board, Dreamscape, Mutator Events, Companions, Codex.
4. Re-run the server test after every change and keep the pack stable.
