# Lootr

Con Lootr instalado, los cofres y barriles de las estructuras pasan a ser de Lootr aunque la estructura los ponga sin pasar por el aviso que usa Lootr para convertirlos. Esto pasa con las estructuras de datapacks y de KubeJS (`kubejs/data`), que suelen traer los cofres ya hechos dentro de una plantilla.

Cuando se carga un chunk con estructuras, un segundo después se convierten sus cofres. Funciona también con chunks ya generados.

## Reglas por estructura

Para que los cofres de una estructura salgan con una tabla de botín concreta, se crea un archivo en `data/<namespace>/ultimatetweaks/lootr_structures/<nombre>.json` (en un datapack, o en `kubejs/data/<namespace>/ultimatetweaks/lootr_structures/` con KubeJS):

```json
{
  "structures": ["mipack:santuario", "#mipack:santuarios"],
  "loot_table": "mipack:chests/santuario",
  "replace_items": true
}
```

| Campo | Qué hace |
|---|---|
| `structures` | Ids de estructura, o tags con `#`. |
| `loot_table` | Tabla de botín que usarán los cofres. |
| `replace_items` | `true` (por defecto) vacía el cofre y usa la tabla. `false` solo la pone a los cofres que no tenían ya una tabla. |

Los cofres con tabla de botín propia que no entren en ninguna regla se pasan a Lootr tal cual.

## Configuración

Sección `lootr` de `ultimatetweaks-common.toml`:

| Opción | Qué hace |
|---|---|
| `enabled` | `false` desactiva toda la integración. |
| `forceStructureChests` | `true` avisa a Lootr de todos los cofres con tabla de botín de los chunks con estructuras. |
| `delayTicks` | Ticks que se espera tras cargar el chunk antes de convertir. |
| `logConversions` | `true` escribe en el log un mensaje por cada contenedor que se pasa a Lootr, con su posición y su tabla de botín. |
