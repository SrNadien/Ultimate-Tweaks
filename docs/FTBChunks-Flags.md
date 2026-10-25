# Flags de zonas (FTB Chunks)

Ultimate Tweaks añade a FTB Chunks flags por zona, con los mismos nombres y la misma idea que los de WorldGuard y ProtectionStones. Cada equipo con chunks reclamados los tiene entre sus ajustes (botón **Settings** del equipo) y se cambian como cualquier otra propiedad del equipo.

Hace falta **FTB Chunks** (y FTB Teams). Sin ellos no hace nada. Los flags se aplican por defecto a los equipos de servidor, es decir, a las zonas de admin (ver Configuración).

**Por defecto solo salen en el juego los que ProtectionStones activa de serie**: `pvp`, `greeting`, `farewell`, `entry`, `creeper_explosion`, `mob_spawning` y `use`. El resto no existe hasta que lo añadas a `enabledFlags` en `ultimatetweaks-common.toml` (ver Configuración) y reinicies.

## Cómo se cambian

Con comandos, para un equipo de servidor:

```
/ftbteams server create <nombre>
/ftbchunks admin claim_as <equipo> <radio>
/ftbteams server settings <equipo> ultimatetweaks:flag_invincible true
/ftbteams server settings <equipo> ultimatetweaks:flag_greeting &aBienvenido a {team}
```

Para una party: `/ftbteams party settings <propiedad> <valor>`, o `/ftbteams party settings_for <party> <propiedad> <valor>` siendo admin.

Los flags de lista (`deny_spawn`, `blocked_cmds`, `allowed_cmds`) se editan desde la pantalla de ajustes del equipo, una entrada por línea.

## Cómo funcionan

- Los flags de **sí/no** vienen en "permitido" salvo `invincible`, `pvp` y `creeper_explosion`. Poner uno en "no" prohíbe esa acción dentro de la zona.
- Los valores de partida son los de ProtectionStones: `pvp` y `creeper_explosion` desactivados, `entry` permitido, y mensajes de entrada y salida ("Entrando al rancho de ..." y "Saliendo del rancho de ..."). Cada equipo los puede cambiar después.
- Los flags de acciones del jugador (`block_break`, `block_place`, `interact`, `chest_access`, `use`, `ride`, `sleep`, `lighter`, `pvp`...) no afectan a los miembros del equipo ni a los jugadores con permisos de operador.
- Cuando se impide una acción se muestra `deny_message` en la barra de acciones.
- En los mensajes (`greeting`, `farewell`, `deny_message`...) se pueden usar colores con `&` y `{team}` para el nombre del equipo. En los títulos, `\n` separa el título del subtítulo.

## Los flags

### Protección

| Flag | Qué hace |
|---|---|
| `block_break`, `block_place` | Romper y colocar bloques. |
| `interact` | Usar bloques y entidades (sin inventarios). |
| `chest_access` | Abrir cofres y otros inventarios. |
| `use` | Puertas, trampillas, portones, botones y palancas. |
| `use_anvil` | Yunques. |
| `ride`, `sleep` | Montar y dormir. |
| `lighter` | Mechero y bolas de fuego. |
| `pvp` | Combate entre jugadores. |
| `damage_animals` | Dañar animales. |
| `respawn_anchors` | Anclas de reaparición. |
| `vehicle_place`, `vehicle_destroy` | Colocar y destruir botes y vagonetas. |
| `block_trampling` | Pisotear la tierra de cultivo. |
| `item_frame_rotation` | Girar el ítem de un marco. |
| `potion_splash` | Pociones arrojadizas. |
| `firework_damage` | Daño de fuegos artificiales. |
| `pistons` | Pistones. |

### Mobs, fuego y explosiones

| Flag | Qué hace |
|---|---|
| `mob_spawning` | Aparición de mobs. Los tipos de aparición que bloquea se eligen en la configuración. |
| `deny_spawn` | Tipos de entidad que nunca aparecen (`minecraft:zombie`). |
| `mob_damage` | Mobs dañando a jugadores. |
| `creeper_explosion`, `tnt`, `ghast_fireball`, `wither_damage`, `other_explosion` | Daño a bloques y entidades de cada tipo de explosión. |
| `wind_charge_burst`, `breeze_charge_explosion` | Ráfagas de cargas de viento, de jugador y de brisa. |
| `enderdragon_block_damage` | El dragón destruyendo bloques. |
| `enderman_grief`, `snowman_trails`, `ravager_grief` | Enderman, golem de nieve y devastador alterando bloques. |
| `entity_painting_destroy`, `entity_item_frame_destroy` | Cuadros y marcos destruidos por algo que no es un jugador. |
| `lightning` | Rayos. Desactivado, son solo visuales. |
| `crop_growth` | Crecimiento de cultivos y árboles jóvenes. |
| `lava_harden` | Lava endureciéndose al tocar agua. |

### Movimiento y mensajes

| Flag | Qué hace |
|---|---|
| `entry`, `exit` | Entrar y salir. Solo afecta a quien no es del equipo. |
| `exit_via_teleport` | Con `exit` desactivado, permite salir teletransportándose. |
| `entry_deny_message`, `exit_deny_message` | Mensajes cuando se impide entrar o salir. |
| `greeting`, `farewell` | Mensaje de chat al entrar y al salir. |
| `greeting_title`, `farewell_title` | Título al entrar y al salir. |
| `enderpearl`, `chorus_fruit_teleport` | Teletransportes con perlas de ender y fruta de chorus. |
| `spawn` | Dónde reaparece quien muere en la zona (`x y z`). |

### Jugador y mundo

| Flag | Qué hace |
|---|---|
| `invincible` | Los jugadores no reciben daño. |
| `fall_damage` | Daño por caída. |
| `natural_health_regen` | Regeneración de vida con la comida. Desactivado, impide cualquier curación normal. |
| `natural_hunger_drain` | Pérdida de hambre. |
| `heal_delay`, `heal_amount`, `heal_min_health`, `heal_max_health` | Cura o daña a los jugadores cada tantos segundos, dentro de esos límites de vida. |
| `feed_delay`, `feed_amount`, `feed_min_hunger`, `feed_max_hunger` | Lo mismo con la comida. |
| `game_mode` | Modo de juego mientras están en la zona; al salir recuperan el suyo. |
| `time_lock`, `weather_lock` | Hora (`0` a `24000`, o `day`, `noon`, `night`, `midnight`) y clima (`clear` o `rain`) que ve el jugador dentro de la zona. |
| `item_pickup`, `item_drop`, `exp_drops` | Recoger ítems, tirarlos y experiencia al morir los mobs. |
| `send_chat` | Escribir en el chat. |
| `blocked_cmds`, `allowed_cmds` | Comandos prohibidos, o únicos permitidos, dentro de la zona. |
| `deny_message` | Mensaje cuando se impide una acción. |

## Flags de WorldGuard que no están

`build`, `passthrough`, `nonplayer-protection-domains`, `exit-override`, `receive-chat`, `teleport`, `notify-enter`, `notify-leave`, `use-dripleaf` y los que dependen de eventos naturales del terreno que Minecraft no ofrece sin modificar su código: `fire-spread`, `lava-fire`, `snow-fall`, `snow-melt`, `ice-form`, `ice-melt`, `frosted-ice-form`, `frosted-ice-melt`, `mushroom-growth`, `leaf-decay`, `grass-growth`, `mycelium-spread`, `vine-growth`, `rock-growth`, `sculk-growth`, `soil-dry`, `coral-fade`, `copper-fade`, `water-flow`, `lava-flow` y `moisture-change`.

`build`, `block-break`, `block-place`, `interact`, `chest-access` y `pvp` ya los cubre también FTB Chunks con sus propias opciones de protección; los de aquí se suman a esas.

## Configuración

Está en la sección `zoneFlags` de `ultimatetweaks-common.toml`:

| Opción | Qué hace |
|---|---|
| `enabled` | `false` desactiva todos los flags. |
| `enabledFlags` | Lista de flags que existen en el juego. Por defecto `pvp`, `greeting`, `farewell`, `entry`, `creeper_explosion`, `mob_spawning` y `use`. Un flag que no esté en la lista no sale en los ajustes del equipo y no hace nada. |
| `onlyServerTeams` | `true` (por defecto) aplica los flags solo a los chunks de equipos de servidor. `false` los aplica también a jugadores y partys. |
| `blockedSpawnTypes` | Tipos de aparición que bloquea `mob_spawning`. Por defecto `NATURAL`, `CHUNK_GENERATION`, `PATROL` y `REINFORCEMENT`; los spawners, huevos y comandos no se bloquean. |
| `bypassPermissionLevel` | Nivel de permisos (0 a 4) a partir del cual un jugador se salta los flags que le limitan. 2 son los operadores; 5 lo desactiva. |

## Notas

- Las propiedades de un equipo que ya existía aparecen con su valor de partida la primera vez que se cargan.
- El mod tiene que estar también en el cliente para que los flags salgan en la pantalla de ajustes del equipo.
