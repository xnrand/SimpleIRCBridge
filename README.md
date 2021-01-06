SimpleIRCBridge
===============

Building
--------

```
./gradlew build
```

The output will be located at `build/libs/SimpleIRCBridge-VERSION.jar`.

Developing
----------

Setup for IDEs is covered in the [Forge documentation](https://mcforge.readthedocs.io/en/latest/gettingstarted/#from-zero-to-modding).

The mod can easily be tested using `./gradlew runServer`. A `run` directory will be created, holding server files.


Usage
-----

Drop the jar file in your `mods` directory, start the server once, edit `world/serverconfig/simpleircbridge-server.toml` and restart the server.
