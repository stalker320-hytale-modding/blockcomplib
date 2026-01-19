# Hytale plugin template

Minimal plugin template, containing `/hello`-command.

# Build

Just run

```sh
./gradlew build
```

And copy `build/libs/<pluginname>-1.0-SNAPSHOT.jar` to your `Saves/<savename>/mods` or `Server/mods` folder.

# Grant OP

Plugin applies to Dedicated Server, where you need to execute next command from server console, after joining server as player.

```sh
/op add <username>
```

To add player `username` to op list. Now open game window, press `Return` and type `/hello`

# Credits

Based on [this plugin](https://github.com/noel-lang/hytale-example-plugin)
