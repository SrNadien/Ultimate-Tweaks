# FTB Teams en modo offline

En un servidor con `online-mode=false` (jugadores no premium), FTB Teams no encontraba al jugador: comparaba el UUID de la cuenta del cliente con el que le da el servidor, y en modo offline no coinciden. El resultado era que los equipos, las partys y las invitaciones no funcionaban y el registro mostraba `Local player id ... was not found in the known players list`.

Ultimate Tweaks usa en el cliente el UUID que recibe del servidor, así que FTB Teams vuelve a funcionar.

- Hay que instalar el mod en el cliente de cada jugador offline, no solo en el servidor.
- En servidores premium no cambia nada: los dos UUID ya son el mismo.
- Se puede desactivar con `ftbTeamsOfflineFix = false` en `ultimatetweaks-client.toml`.
