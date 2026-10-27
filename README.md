# mine-pack

A surreal, epic **Minecraft 1.20.1 Forge** modpack (singleplayer) built from a curated base of
exploration, tech and magic/RPG mods, tied together by a few custom mods written in this repo.

## Layout

| Path | What it is |
| --- | --- |
| `mods/ascension-core/` | Custom mod: shared progression layer (Tech / Arcane / Wanderer paths) |
| `docs/ROADMAP.md` | Plan, mod list ideas, and what is built vs. planned |
| `.github/workflows/build.yml` | Builds every custom mod in the cloud and uploads the jars |

Planned next: `rift-expeditions`, `contract-board`, a Dreamscape dimension, mutator events,
companions, a codex, plus the KubeJS/quest layer and the packwiz modpack files.

## Getting a build

Every push to `main` runs the **Build mods** workflow (Actions tab). Open the latest run and
download the `ascension-core` artifact: that is the mod jar to drop into a Forge 1.20.1 `mods` folder.

## Ascension Core (v0.1)

- Three paths: **Tech**, **Arcane**, **Wanderer**. 25 points per tier, 10 tiers.
- Right-click an *Essence* item to gain 1 point in its path (placeholder vanilla recipes for now).
- Killing hostile mobs gives Wanderer points (1 per 10 kills).
- Progress is mirrored into player tags (`ascension_tech_tier_2`, `ascension_balanced_1`, ...) so
  KubeJS scripts and quests can gate content on it.
- Commands: `/ascension status`, and op-only `/ascension add <tech|arcane|wanderer> <amount>` for testing.

## Requirements

Minecraft 1.20.1, Forge 47.x, Java 17.
