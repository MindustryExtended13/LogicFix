package logicfix.registry;

import arc.func.Prov;
import logicfix.LStatement;
import org.jetbrains.annotations.Contract;

public class RegistryObject<T extends LStatement> {
    private final Prov<T> prov;
    private final String name;

    @Contract(pure = true)
    public RegistryObject(String name, Prov<T> prov) {
        this.name = name;
        this.prov = prov;
    }

    public String getName() {
        return name;
    }

    public Prov<T> getProv() {
        return prov;
    }

    public T get() {
        return prov.get();
    }
}