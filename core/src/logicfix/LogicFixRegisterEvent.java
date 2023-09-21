package logicfix;

import arc.Core;
import arc.func.Func;
import arc.func.Prov;
import arc.struct.ObjectMap;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.logic.LStatement;
import org.jetbrains.annotations.NotNull;

public class LogicFixRegisterEvent {
    private final ObjectMap<String, Func<String[], ? extends LStatement>> registry = new ObjectMap<>();
    private final ObjectMap<String, Prov<? extends LStatement>> registry2 = new ObjectMap<>();

    public<T extends LStatement> void register(String string, Func<String[], T> handler, Prov<T> statement) {
        registry.put(string, handler);
        registry2.put(string, statement);
    }

    public<T extends logicfix.LStatement> void register(String string, Prov<T> statement) {
        register(string, (args) -> {
            T l = procession(string, statement);
            l.read(args);
            return l;
        }, statement);
    }

    public void process() {
        registry.each((key, value) -> {
            LAssembler.customParsers.put(key, value::get);
        });
        registry2.each((key, prov) -> {
            LogicIO.allStatements.add(() -> procession(key, prov));
        });
    }

    public static<T extends LStatement> T procession(String key, @NotNull Prov<T> prov) {
        T statement = prov.get();
        if(statement instanceof logicfix.LStatement) {
            logicfix.LStatement statement1 = (logicfix.LStatement) statement;
            boolean[] bools = statement1._func_195101();
            if(bools[0]) {
                statement1.name(key);
            }
            if(bools[1]) {
                String prefix = "logicfix.statements." + key;
                statement1.localizedName(Core.bundle.get(prefix));
            }
        }
        return statement;
    }
}