# Changelog

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
