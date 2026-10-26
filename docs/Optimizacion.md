# Optimización de logros y FTB Quests

Con muchos mods y jugadores, comprobar el inventario en cada cambio hace que el servidor baje de tick rate. Ultimate Tweaks reduce ese trabajo sin cambiar el resultado de los logros ni de las misiones. Todo es del lado del servidor y se configura en la sección `optimization` de `ultimatetweaks-common.toml`.

## Logros de Minecraft

| Opción | Qué hace |
|---|---|
| `advancements.enabled` | `false` desactiva todo lo de logros. |
| `advancements.intervalTicks` | Ticks mínimos entre dos comprobaciones de logros de un mismo jugador (5 por defecto). `0` comprueba en cada cambio, como Minecraft. |
| `advancements.ignoreEmptyStacks` | `true` ignora los cambios en que el ítem es vacío. Puede impedir logros que piden huecos libres, por eso viene en `false`. |
| `advancements.fastInventoryMatch` | `true` comprueba los logros con varios ítems recorriendo el inventario una sola vez y sin crear listas. |

Los cambios que llegan antes de cumplirse el intervalo **no se pierden**: se guardan, uno por tipo de ítem, y se comprueban todos cuando toca. Así un logro de un ítem concreto sigue saliendo aunque se recojan otros ítems justo antes.

## FTB Quests

Hace falta FTB Quests; sin él esta parte no hace nada.

| Opción | Qué hace |
|---|---|
| `ftbQuests.enabled` | `false` desactiva todo lo de FTB Quests. |
| `ftbQuests.ignoreUnchangedSlots` | `true` no vuelve a comprobar las misiones cuando un slot cambia sin cambiar de ítem ni de cantidad (herramientas con energía o durabilidad que cambia todo el rato, por ejemplo). |
| `ftbQuests.cacheValidItems` | `true` guarda la lista de ítems válidos de cada tarea de ítem en vez de calcularla en cada consulta. La lista se descarta al cambiar la tarea y al recargar las tags. |
