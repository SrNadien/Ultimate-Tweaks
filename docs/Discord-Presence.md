# Discord Rich Presence

Ultimate Tweaks muestra en Discord a qué pack estás jugando. Es solo del cliente: en un servidor dedicado no hace nada.

Si Discord no está abierto, el mod no hace nada y no da error. Se vuelve a conectar solo en cuanto abras Discord.

## Qué se ve

| Línea | Contenido |
|---|---|
| Título | Nombre del pack |
| Línea 1 | `{mods} Mods, {minecraft}, {version}` (cantidad de mods, versión de Minecraft y versión del pack) |
| Línea 2 | `{dimension}, Misiones: hechas/totales` (las misiones solo si **FTB Quests** está instalado) |
| Tiempo | Tiempo transcurrido desde que abriste el juego |
| Imagen grande | Logo del mod, o la imagen del pack si pones `largeImage = "pack"` |
| Imagen pequeña | Icono de la dimensión: Overworld, Nether, End u otra |
| Botones | `CurseForge` (página del pack) y `Discord` (invitación), si hay enlace |

Las líneas no rotan ni se cortan: si un texto no entra en el ancho de la tarjeta, se usa una variante más corta con los mismos datos.

> **Los botones no se ven en tu propia tarjeta.** Discord no muestra los botones de una presencia a quien la genera. Los ven los demás usuarios. Para comprobarlos, míralo desde otra cuenta.

## De dónde sale la información del pack

Con `source = "curseforge"` (por defecto) el mod lee `minecraftinstance.json` de la instancia de la app de CurseForge: nombre, versión, autor y enlace del pack. No hay que configurar nada.

Para cualquier otro launcher, o para forzar valores, rellena la sección `[discord.manual]`. Lo que pongas ahí manda sobre lo que lea de CurseForge. Con `source = "manual"` se usan solo esos valores.

Si no hay ningún dato, el título es `Minecraft <versión>` y solo se muestra lo que se conozca, así que la presencia nunca queda vacía.

## Configuración

Archivo: `config/ultimatetweaks-client.toml`. Los cambios se aplican con el juego abierto en unos segundos, sin reiniciar.

```toml
[discord]
    enabled = true
    applicationId = "1559158264170553364"
    source = "curseforge"
    details = "{mods} Mods, {minecraft}, {version}"
    states = ["{dimension}, {quests}", "{dimension}, Quests {questsShort}", "{dimension}, {questsShort}", "{quests}", "{status}"]
    rotationSeconds = 0
    largeImage = ""
    largeImageText = "{pack} - Minecraft {minecraft}"
    smallImage = ""
    showElapsedTime = true
    curseforgeButton = true
    maxLineLength = 26
    inviteUrl = ""

    [discord.status]
        starting = "Starting Minecraft"
        menu = "Main Menu"
        singleplayer = "Dimension: {dimension}"
        multiplayer = "Dimension: {dimension}"

    [discord.manual]
        packName = ""
        packVersion = ""
        packAuthor = ""
        packUrl = ""
```

| Opción | Qué hace |
|---|---|
| `enabled` | Activa o desactiva la presencia. |
| `applicationId` | ID de la aplicación de Discord. Vacío o inválido usa el del mod. |
| `source` | `curseforge` o `manual`. |
| `details` | Primera línea. Vacío usa el valor por defecto. |
| `states` | Segunda línea. Se muestra el primer texto que tenga todos sus datos. |
| `rotationSeconds` | `0` muestra siempre el primero que tenga datos. Mayor que `0` pasa de uno en uno cada esos segundos. |
| `largeImage` | Vacío: logo del mod. `pack`: imagen del pack en CurseForge. O el nombre de un asset de la aplicación, o una URL `https`. |
| `largeImageText` | Texto al pasar el cursor sobre la imagen grande. |
| `smallImage` | Vacío: icono de la dimensión. O un asset, o una URL `https`. |
| `showElapsedTime` | Muestra el tiempo transcurrido. |
| `curseforgeButton` | Botón hacia la página del pack, si se conoce su enlace. |
| `maxLineLength` | Caracteres que caben por línea (unos 26 en el popout estrecho). |
| `inviteUrl` | Enlace de invitación para el botón `Discord`. Vacío no lo muestra. |
| `[discord.status]` | Textos de `{status}` según estés cargando, en el menú, en un mundo o en un servidor. |
| `[discord.manual]` | Nombre, versión, autor y enlace del pack a mano. |

### Variables

| Variable | Valor |
|---|---|
| `{pack}` | Nombre del pack |
| `{version}` | Versión del pack |
| `{author}` | Autor del pack |
| `{mods}` | Cantidad de mods cargados |
| `{minecraft}` | Versión de Minecraft |
| `{dimension}` | Dimensión actual |
| `{status}` | Texto de `[discord.status]` según dónde estés |
| `{quests}` | `Misiones: hechas/totales`. Vacío si FTB Quests no está |
| `{questsShort}` | `hechas/totales` |

Una línea que usa una variable sin valor se descarta y pasa a la siguiente variante de la lista, por eso `states` trae varias.

## Ejemplo: pack propio sin CurseForge

```toml
[discord]
    source = "manual"
    inviteUrl = "https://discord.gg/WzH7ZudYQ9"

    [discord.manual]
        packName = "Mi Pack"
        packVersion = "1.4.0"
        packAuthor = "NadienDev"
        packUrl = "https://www.curseforge.com/minecraft/modpacks/mi-pack"
```

## Usar tu propia aplicación de Discord

1. Crea una aplicación en <https://discord.com/developers/applications>. El **nombre** de la aplicación es lo que Discord muestra como "Jugando a …".
2. Copia su **Application ID** en `applicationId`.
3. En *Rich Presence → Art Assets* sube las imágenes con estos nombres para que se usen en vez de las del repositorio: `overworld`, `nether`, `end` y `other`.

Las imágenes pequeñas solo se envían si existen, ya sea como asset de la aplicación o como archivo accesible en GitHub. Si no, no se muestra ninguna en lugar de un icono roto con `?`.

## Problemas frecuentes

| Síntoma | Causa |
|---|---|
| No aparece nada | Discord tiene que estar abierto antes o después del juego, y la opción *Mostrar el juego en curso* activada en Discord. |
| Faltan los botones | Es normal en tu propia cuenta, míralo desde otra. |
| Se ve un `?` como imagen pequeña | La imagen no existe como asset de la aplicación ni en el repositorio. |
| El pack aparece como `Minecraft 1.21.1` | No se encontró `minecraftinstance.json`: usa `[discord.manual]`. |
| Quieres desactivarlo | `enabled = false`. |
