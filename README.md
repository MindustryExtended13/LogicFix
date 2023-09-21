# Logic fix Mod
Mindustry java mod that gives utils to add custom logic and works on Android and PC.

## Documentation
 
### How add log example

```java
public class Example extends Mod {
    public static final ILogger LOGGER = LoggerFactory.build(MOD_ID);
    
    public ExampleMod() {
        final LogBinder TEST_BINDER = LOGGER.atInfo().setPrefix("test");
        Events.on(LogicFixRegisterEvent.class, (e) -> {
            e.register("test", () -> new LStatement().category(LCategory.io).data(new LStatementData()
                .add(LStatementEntry.literal("Message:")).add(LStatementEntry.largeField("message"))
            ).executor((s, exec) -> {
                TEST_BINDER.log(String.valueOf(exec.obj(s.valueMap.get("message"))));
            }));
        });
    }
}
```

### Registering statement

Okay, let's analyse code by step
When client loads, mod calls `LogicFixRegisterEvent` to register all statements
Event calls only 1 times and do not call `process` method!
`register` method register a statement with given name and statement

### logicfix.LStatement

`logicfix.LStatement` it\`s modified version of default `mindustry.logic.LStatement`.
`logicfix.LStatement` can storage and use variables by `java.lang.String` id as Object and when statement
runs this values will be parsed to the valueMap as int and with the same id. To get variable name
you can use not parsed keyMap with variables that written in the fields.
`logicfix.LStatement` returns this class this means this can easy setup and don\`t requires
to extend, all can be setup with using new.
variables:
- category (by default `LCategory.unknown`)
- name (`null` if use registered name, by default `null`)
- localized name (`null` if use registered name, by default `null`)
- executor (with `Cons2` or `LInstruction`, by default noop)
- privileged (by default `false`)
- nonPrivileged (by default `true`)
- data (by default `new LStatementData()`)

### About `logicfix.LStatementData`

This class that used in `logicfix.LStatement` for building ui, reading and writing values
LStatementData (LSD) stores LSE (`logicfix.LStatementEntry`) that used to read and write and building ui
LSE have by default many static method that can be used for building ui or fields.
Some LSE don\`t stores data (example: `logicfix.LStatementEntry.literal(String)`).
In this statements `doSkip` method was always be `true` (!!!)

## Building for Desktop Testing

1. Install JDK **17**.
2. Run `gradlew jar` [1].
3. Your mod jar will be in the `build/libs` directory. **Only use this version for testing on desktop. It will not work with Android.**
To build an Android-compatible version, you need the Android SDK. You can either let Github Actions handle this, or set it up yourself. See steps below.

## Building Locally

Building locally takes more time to set up, but shouldn't be a problem if you've done Android development before.
1. Download the Android SDK, unzip it and set the `ANDROID_HOME` environment variable to its location.
2. Make sure you have API level 30 installed, as well as any recent version of build tools (e.g. 30.0.1)
3. Add a build-tools folder to your PATH. For example, if you have `30.0.1` installed, that would be `$ANDROID_HOME/build-tools/30.0.1`.
4. Run `gradlew deploy`. If you did everything correctlly, this will create a jar file in the `build/libs` directory that can be run on both Android and desktop.