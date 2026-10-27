# mine-pack

A surreal, epic **Minecraft 1.20.1 Forge** modpack (singleplayer) built from a curated base of
exploration, tech and magic/RPG mods, tied together by custom mods written in this repo.

## Play it

1. Open the **Actions** tab, click the latest **Build and test pack** run.
2. Download the `mine-pack` artifact and unzip it. Inside is `Mine Pack-<version>.mrpack`.
3. In **Prism Launcher**: *Add Instance* -> *Import* -> pick the `.mrpack` file. Prism installs
   Minecraft 1.20.1, Forge, and every mod for you.
4. In the instance settings give it **6-8 GB RAM**, then launch.

## What's in the repo

| Path | What it is |
| --- | --- |
| `mods/ascension-core/` | Custom mod: shared progression layer (Tech / Arcane / Wanderer paths) |
| `pack/` | packwiz pack. `mods.txt` is the human-edited mod list, `mods/*.pw.toml` are generated |
| `scripts/` | Server boot test and pack tooling |
| `reports/server-test.txt` | Latest result of booting a real Forge server with the whole pack |
| `docs/ROADMAP.md` | Plan and status |

## How it is built and checked

Every relevant push runs **Build and test pack**: it resolves `pack/mods.txt` against Modrinth by exact
project slug, exports the pack, adds the custom mod jars, and then boots a real Forge dedicated server
with everything installed. The result is committed to `reports/server-test.txt`.

The server test catches missing dependencies, wrong Forge versions and mod-loading crashes. It does
**not** run client-only code (rendering, shaders, UI), so launch the pack on your PC to check those.

## Ascension Core (v0.1)

- Three paths: **Tech**, **Arcane**, **Wanderer**. 25 points per tier, 10 tiers.
- Right-click an *Essence* item to gain 1 point in its path (placeholder vanilla recipes for now).
- Killing hostile mobs gives Wanderer points (1 per 10 kills).
- Progress is mirrored into player tags (`ascension_tech_tier_2`, `ascension_balanced_1`, ...) so
  KubeJS scripts can gate content on it.
- Commands: `/ascension status`, and op-only `/ascension add <tech|arcane|wanderer> <amount>`.
