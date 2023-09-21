package logicfix.registry;

import arc.func.Prov;
import arc.struct.Seq;
import logicfix.LStatement;
import logicfix.LogicFixRegisterEvent;

public class LStatementRegistry {
    private final Seq<RegistryObject<?>> objectSeq = new Seq<>();
    private boolean prefixEnabled = false;
    private String prefix = "invalid";

    public LStatementRegistry prefixEnabled(boolean prefixEnabled) {
        this.prefixEnabled = prefixEnabled;
        return this;
    }

    public LStatementRegistry prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public Seq<RegistryObject<?>> getEntries() {
        return objectSeq;
    }

    public String handleName(String name) {
        return prefixEnabled ? prefix + "." + name : name;
    }

    public<T extends LStatement> RegistryObject<T> register(String name, Prov<T> prov) {
        RegistryObject<T> object = new RegistryObject<>(handleName(name), prov);
        objectSeq.add(object);
        return object;
    }

    public void register(LogicFixRegisterEvent logicFixRegisterEvent) {
        objectSeq.each(obj -> logicFixRegisterEvent.register(obj.getName(), obj.getProv()));
    }
}