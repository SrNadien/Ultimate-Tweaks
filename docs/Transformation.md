# Transformation

Transformation añade recetas que ocurren **en el mundo**: un ítem tirado en un fluido se convierte en otro, un ítem que arde se transforma, un ítem o un bloque que explota da otra cosa. Se definen con **KubeJS** (o con un datapack) y se ven en JEI y REI.

Solo está activo si **KubeJS** está instalado. Sin él, los tipos de receta no se registran y no hace nada.

## Los cinco tipos

| Tipo | Qué hace |
|---|---|
| `item_in_fluid` | Un ítem dentro de un bloque de fluido se convierte en otros ítems. |
| `fluid_to_fluid` | Un bloque de fluido (fuente) se convierte en otro fluido al caer ítems catalizadores. |
| `burning` | Un ítem que arde (fuego o lava) se convierte en otros ítems. |
| `item_explosion` | Un ítem alcanzado por una explosión se convierte en otros ítems en vez de destruirse. |
| `block_explosion` | Un bloque que explota suelta ítems definidos por ti. |

Los ids de receta son `ultimatetweaks:item_in_fluid`, `ultimatetweaks:fluid_to_fluid`, `ultimatetweaks:burning`, `ultimatetweaks:item_explosion` y `ultimatetweaks:block_explosion`.

## Con KubeJS

En un script de servidor (`kubejs/server_scripts/`). Siempre va primero el resultado, como en las recetas de KubeJS.

```js
ServerEvents.recipes(event => {
  event.recipes.ultimatetweaks.item_in_fluid('minecraft:diamond', '2x minecraft:coal', 'minecraft:water')

  event.recipes.ultimatetweaks.fluid_to_fluid('minecraft:lava', 'minecraft:water', ['minecraft:redstone'])

  event.recipes.ultimatetweaks.burning('minecraft:emerald', 'minecraft:stick')

  event.recipes.ultimatetweaks.item_explosion('minecraft:gold_ingot', 'minecraft:sand')

  event.recipes.ultimatetweaks.block_explosion('minecraft:clay_ball', 'minecraft:dirt')
})
```

Cada una de esas llamadas tiene también un nombre corto con prefijo: `transformationItemInFluid`, `transformationFluidToFluid`, `transformationBurning`, `transformationItemExplosion` y `transformationBlockExplosion`.

### Argumentos

| Tipo | Argumentos (en orden) | Opcionales |
|---|---|---|
| `item_in_fluid` | resultados, ingrediente, fluido | `duration` (ticks, 20 por defecto), `consumeFluidChance` (0 a 1, 0 por defecto) |
| `fluid_to_fluid` | fluido resultante, fluido de entrada, catalizadores | — |
| `burning` | resultados, ingrediente | — |
| `item_explosion` | resultados, ingrediente | — |
| `block_explosion` | resultados, bloque | `replaceDrops` (`true` por defecto) |

- Los **ingredientes** aceptan cantidad: `'3x minecraft:coal'`.
- El **bloque** de `block_explosion` es un id (`'minecraft:dirt'`) o un tag con `#` (`'#minecraft:logs'`).
- Los **opcionales** se encadenan: `.duration(40)`, `.consumeFluidChance(0.25)`, `.replaceDrops(false)`.

## Cómo funciona cada tipo

**`item_in_fluid`**
- El ítem tiene que estar dentro del bloque de fluido (fuente o corriente).
- Se comprueba cada 5 ticks y la receta se completa cuando el ítem lleva `duration` ticks en el fluido.
- Convierte tantas veces como quepa en la pila: `2x coal` con 5 de carbón da 2 diamantes y deja 1.
- Con `consumeFluidChance`, cada conversión puede gastar el bloque de fluido (solo si es una fuente).

**`fluid_to_fluid`**
- Solo actúa sobre bloques de fluido **fuente**.
- Los catalizadores son ítems tirados dentro de ese mismo bloque; se gastan todos a la vez.
- El bloque pasa a ser una fuente del fluido resultante, al instante.

**`burning`**
- Se aplica a ítems que estén ardiendo o en lava (fuego, lava, mechero, etc.).
- El ítem resultante nace invulnerable, así que no se quema ni se destruye.

**`item_explosion`**
- Cualquier explosión (TNT, creeper...) con el ítem en su radio. El ítem no se destruye: se convierte.
- Lo que no alcance la cantidad del ingrediente sigue su suerte normal con la explosión.

**`block_explosion`**
- Si el bloque entra en la explosión, suelta los resultados.
- Con `replaceDrops` en `true` el bloque se elimina sin sus drops normales; con `false` suelta también los suyos.

## Con datapack

Las recetas también funcionan como JSON en `data/<namespace>/recipe/`, con `type` igual al id del tipo. Los campos son los mismos que en KubeJS:

| Tipo | Campos |
|---|---|
| `item_in_fluid` | `ingredient`, `fluid`, `results`, `duration`, `consume_fluid_chance` |
| `fluid_to_fluid` | `fluid`, `catalysts`, `result` |
| `burning` | `ingredient`, `results` |
| `item_explosion` | `ingredient`, `results` |
| `block_explosion` | `block`, `results`, `replace_drops` |

Los resultados son `{"id": "...", "count": n}`. Ejemplo de `block_explosion`, que no lleva ingredientes:

```json
{
  "type": "ultimatetweaks:block_explosion",
  "block": "minecraft:dirt",
  "results": [ { "id": "minecraft:clay_ball", "count": 2 } ],
  "replace_drops": true
}
```

El formato de ingredientes y fluidos es el de esta versión de Minecraft. Lo más cómodo es definir las recetas con KubeJS, que escribe el JSON por ti.
## En el visor de recetas

Cada tipo tiene su categoría en JEI y REI, con los ítems y fluidos de entrada a la izquierda, los resultados a la derecha y una línea de texto debajo con lo que hay que hacer (por ejemplo "Tíralo al fuego o a la lava").

Pulsar **U** sobre un objeto muestra dónde sirve:

| Categoría | Objetos que la abren |
|---|---|
| Quemado | lava, cubo de lava, mechero y bola de fuego |
| Explosión de ítem | TNT |
| Explosión de bloque | TNT |

## Notas

- Las recetas se recargan con `/reload` como cualquier receta de KubeJS.
- Si dos recetas del mismo tipo aceptan el mismo ítem, gana la primera que se cargue.
- Todo ocurre en el servidor, pero el mod tiene que estar instalado también en el cliente, como cualquier mod que añade tipos de receta.
