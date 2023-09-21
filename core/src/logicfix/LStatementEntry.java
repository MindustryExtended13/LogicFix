package logicfix;

import arc.Core;
import arc.func.Cons2;
import arc.scene.ui.layout.Table;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LStatementEntry {
    public String id;

    public static @NotNull LStatementEntry translatable(String text) {
        return literal(Core.bundle.get(text));
    }

    public static @NotNull LStatementEntry literal(String text) {
        return table((ignored, table) -> table.add(text));
    }

    public static @NotNull LStatementEntry table(Cons2<LStatement, Table> cons2) {
        LStatementEntry entry = new LStatementEntry() {
            @Override
            public void build(LStatement statement, Table table) {
                cons2.get(statement, table);
            }

            @Override
            @Contract(pure = true)
            public boolean doSkip(LStatement statement) {
                return true;
            }

            @Override
            @Contract(pure = true)
            public void read(LStatement statement, int id, String @NotNull [] args) {
            }

            @Override
            @Contract(pure = true)
            public void write(@NotNull LStatement statement, @NotNull StringBuilder builder) {
            }
        };
        entry.id = "invalid";
        return entry;
    }

    public static @NotNull LStatementEntry largeField(String id) {
        LStatementEntry entry = new LStatementEntry() {
            @Override
            public void build(LStatement statement, Table table) {
                statement.field(table, String.valueOf(get(statement)), str -> {
                    set(statement, str);
                }).width(0f).growX().padRight(3);
            }
        };
        entry.id = id;
        return entry;
    }

    public boolean doSkip(LStatement statement) {
        return false;
    }

    public void build(LStatement statement, Table table) {
    }

    public void read(LStatement statement, int id, String @NotNull [] args) {
        set(statement, args[id]);
    }

    public void write(@NotNull LStatement statement, @NotNull StringBuilder builder) {
        builder.append(get(statement));
    }

    protected Object get(@NotNull LStatement statement) {
        return statement.keyMap.get(id);
    }

    protected void set(@NotNull LStatement statement, Object value) {
        statement.keyMap.put(id, value);
    }
}