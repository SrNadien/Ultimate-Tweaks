# Changelog

## [2.0.15] — 2026-10-26 — Minecraft 26.1.2

### ✨ Added
- **Advancement optimization** (server side). Checking every player's inventory on every change makes the tick rate drop with many mods and players. Now the checks of one player are grouped and run once every few ticks (5 by default); changes that arrive in between are kept, one per item type, and checked all together, so no advancement is lost. Advancements that need several items are also checked with a single pass over the inventory.
- **FTB Quests optimization** (server side, only active when FTB Quests is installed). Quests are no longer checked again when a slot changes without changing its item or amount (tools with changing energy or durability), and the list of valid items of every item task is kept instead of being calculated each time.
- Everything can be turned off from the new `optimization` section of `ultimatetweaks-common.toml`. See `docs/Optimizacion.md`.

## [2.0.14] — 2026-10-25 — Minecraft 26.1.2

### ✨ Added
- **Zone flags for FTB Chunks**, in the style of WorldGuard and ProtectionStones, with the same names: 72 settings in the team properties of every team with claimed chunks, covering protection (block break and place, interact, chests, doors and levers, PvP, riding, sleeping, vehicles...), mobs and explosions (mob spawning, denied spawns, creeper, TNT, ghast, wither, wind charge explosions, endermen, ravagers...), lightning, crop growth, entry and exit, entry and exit messages and titles, ender pearls and chorus fruit, respawn location, invincibility, fall damage, hunger and regeneration, timed healing and feeding, game mode, time and weather lock, item pickup and drop, chat, blocked and allowed commands and potion splashes. Like ProtectionStones, PvP and creeper explosions start disabled and entering or leaving shows a message, and only the flags ProtectionStones enables by default (PvP, entry and exit messages, creeper explosions, mob spawning and use) exist in the game until the rest are added to `enabledFlags` in `ultimatetweaks-common.toml`. By default they only apply to the chunks of server teams (admin zones); `ultimatetweaks-common.toml` can extend them to players and parties, change the blocked spawn types and set the permission level that skips them. See `docs/FTBChunks-Flags.md`.

### 🐛 Fixed
- FTB Teams on offline-mode servers (`online-mode=false`): teams, parties and invitations work again. FTB Teams looked the player up with the UUID of their account, which does not match the one the server gives in offline mode. Install the mod on the client of every offline player. It can be turned off with `ftbTeamsOfflineFix` in `ultimatetweaks-client.toml`. See `docs/FTB-Teams-Offline.md`. This is an unofficial 26.1.2 port of the original version by ThetaDev. Since the mod is licensed under MIT, I am allowed to fork and update it.

## [2.0.13] — 2026-10-16 — Minecraft 26.1.2

### ✨ Added
- **Transformation**, in-world recipes for KubeJS (only active when KubeJS is installed): an item in a fluid becomes other items, a fluid becomes another fluid when catalysts fall into it, an item that burns, an item that is hit by an explosion and a block that explodes can all turn into the items you choose. See `docs/Transformation.md`.
- Transformation recipes show up in JEI and REI, one category per type. Lava, flint and steel, fire charge and TNT open the categories where they are used.

### ⚔️ Changed
- The translations are generated with datagen. Argentine and Mexican Spanish (`es_ar` and `es_mx`) are now included, so their text no longer falls back to English.

## [2.0.12] — 2026-10-12 — Minecraft 26.1.2

### ✨ Added
- A trash can. Open it with the button next to the inventory or with `/ultimatetweaks trashcan`: it is a 4-row container and everything left inside is destroyed when it is closed. A **Clear** button in its window empties it without closing it.
- The button goes into the FTB Library sidebar when FTB Library is installed. Otherwise it is drawn in the top-left corner of the inventory screens; the `trashCanButton` option of `ultimatetweaks-client.toml` turns that one off. When another mod already provides a trash can button, this one is not added.

## [2.0.11] — 2026-10-12 — Minecraft 26.1.2

### ✨ Added
- Discord Rich Presence, client side. By default it reads the modpack name, version, author, CurseForge page and logo from the CurseForge app (`minecraftinstance.json` of the instance) and shows the pack as the title, the number of mods, the Minecraft version and the pack version on the first line, and the FTB Quests progress (completed/total) on the second line when FTB Quests is installed, or the game status with the current dimension otherwise. It also shows the elapsed time, a small icon per dimension and buttons to the pack on CurseForge and to your Discord.
- A client config file, `ultimatetweaks-client.toml`, to turn it off, change the Discord application, the texts, the image, the buttons and the invite, or set the pack by hand. The second line can also rotate through several texts. Anything left empty falls back to a default, so there is always a status to show.

## [2.0.10] — 2026-10-10 — Minecraft 26.1.2

First release as a standalone mod. Ultimate Tweaks was split out of Ultimate Foods.

Requires **Java 25** and NeoForge `26.1.2.100`.

### ⚠️ Breaking
- The mod id is now `ultimatetweaks`; it was `ultimatefoods`. Items from worlds made with Ultimate Foods 2.0.9 or earlier do not carry over.
- **Requires UltimateArmoury: Tools And Weapons.** The mod does not start without it.

### ✨ Added
- A mod logo.

### ⚔️ Changed
- Holds the 21 utility items and the 2 music discs that used to live in Ultimate Foods, with the sounds and jukebox songs of the discs. The food sounds stayed with Ultimate Foods.
- The utility items still have no crafting recipes by default, so they can be configured with KubeJS.
- The Ex Deorum, AllTheModium and compressed-block integrations and their config file moved to UltimateArmoury.
