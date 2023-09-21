package logicfix;

import arc.Events;
import me13.core.logger.ILogger;
import me13.core.logger.LoggerFactory;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class LogicFix extends Mod {
    public static final String MOD_ID = "logicfix";
    public static final String MOD_NAME = "Logic Fix";
    public static final ILogger LOGGER = LoggerFactory.build(MOD_ID);

    public LogicFix() {
        LOGGER.info("Loaded {} constructor", (Object) MOD_NAME);

        Events.on(EventType.ClientLoadEvent.class, (ignored) -> {
            final var event = new LogicFixRegisterEvent();
            Events.fire(event);
            event.process();
            LOGGER.info("Registered statements");
        });
    }
}
