package logicfix;

import arc.Core;
import arc.func.Cons;
import arc.func.Cons2;
import arc.func.Func;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import logicfix.instance.MultiLStatementEntry;
import mindustry.ui.Styles;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class LStatementEntry {
    public static final String INVALID = "invalid";
    public static final String FROG = "\"frog\"";
    public String id = INVALID;

    @Contract(value = " -> new", pure = true)
    public static @NotNull LStatementEntry row() {
        return table((ignored, table) -> table.row());
    }

    @Contract("_ -> new")
    public static @NotNull LStatementEntry translatable(String text) {
        return literal(Core.bundle.get(text));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull LStatementEntry literal(String text) {
        return table((ignored, table) -> table.add(text));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull LStatementEntry table(Cons2<LStatement, Table> cons2) {
        return new LStatementEntry() {
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
    }

    @Contract("_, _, _, _ -> new")
    public static<T> @NotNull MultiLStatementEntry multi(T[] values,
                                                         T def,
                                                         String id32,
                                                         Func<T, Seq<LStatementEntry>> func) {
        return multi(values, def, id32, (ignored) -> {}, func);
    }

    @Contract("_, _, _, _, _ -> new")
    public static<T> @NotNull MultiLStatementEntry multi(T[] values,
                                                         T def,
                                                         String id32,
                                                         Cons<T> setter,
                                                         Func<T, Seq<LStatementEntry>> func) {
        return new MultiLStatementEntry() {
            public LStatement lastStatement;
            public Table table;
            public T value = def;

            @Override
            public Seq<LStatementEntry> handle() {
                Seq<LStatementEntry> entries = new Seq<>();
                entries.add(select(values, value, id32, (t) -> {
                    value = t;
                    table.clearChildren();
                    super.build(lastStatement, table);
                    setter.get(t);
                }));
                entries.add(func.get(value));
                return entries;
            }

            @Override
            public void build(LStatement statement, Table table) {
                lastStatement = statement;
                table.table(cont -> {
                    cont.setColor(table.color);
                    super.build(statement, cont);
                    this.table = cont;
                });
            }

            @Override
            public void pre(String[] args) {
                for(T value : values) {
                    if(String.valueOf(value).equals(args[0])) {
                        this.value = value;
                    }
                }
            }
        };
    }

    @Contract("_ -> new")
    public static @NotNull MultiLStatementEntry multi(Seq<LStatementEntry> entries) {
        return new MultiLStatementEntry() {
            @Override
            public Seq<LStatementEntry> handle() {
                return entries;
            }
        };
    }

    @Contract("_ -> new")
    public static @NotNull MultiLStatementEntry multi(LStatementEntry... entries) {
        return new MultiLStatementEntry() {
            @Override
            public Seq<LStatementEntry> handle() {
                return Seq.with(entries);
            }
        };
    }

    public static<T> @NotNull LStatementEntry select(T[] values, T def, String id) {
        return select(values, def, id, 100);
    }

    public static<T> @NotNull LStatementEntry select(T[] values, T def, String id, Cons<T> setter) {
        return select(values, def, id, setter, 100);
    }

    public static<T> @NotNull LStatementEntry select(T[] values, T def, String id, float width) {
        return select(values, def, id, (ignored) -> {}, width);
    }

    public static<T> @NotNull LStatementEntry select(T[] values, T def, String id, Cons<T> setter, float width) {
        LStatementEntry entry = new LStatementEntry() {
            @Override
            public void build(LStatement statement, Table table) {
                if(!has(statement)) set(statement, def);
                table.button(b -> {
                    final T obj = (T) get(statement);
                    b.label(() -> String.valueOf(obj));
                    b.clicked(() -> statement.showSelect(b, values, obj, t -> {
                        set(statement, t);
                        b.clearChildren();
                        b.label(() -> String.valueOf(t));
                        setter.get(t);
                    }, 2, cell -> cell.size(100, 50)));
                }, Styles.logict, () -> {}).color(table.color).left().size(width, 40).pad(2).padBottom(0);
            }
        };
        entry.id = id;
        return entry;
    }

    public static @NotNull LStatementEntry field(String def, String id) {
        LStatementEntry entry = new LStatementEntry() {
            @Override
            public void build(@NotNull LStatement statement, Table table) {
                if(!has(statement)) set(statement, def);
                statement.field(table, String.valueOf(get(statement)), str -> {
                    set(statement, str);
                });
            }
        };
        entry.id = id;
        return entry;
    }

    public static @NotNull LStatementEntry largeField(String id) {
        return largeField(FROG, id);
    }

    public static @NotNull LStatementEntry largeField(String def, String id) {
        LStatementEntry entry = new LStatementEntry() {
            @Override
            public void build(@NotNull LStatement statement, Table table) {
                if(!has(statement)) set(statement, def);
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

    protected boolean has(@NotNull LStatement statement) {
        return statement.keyMap.containsKey(id);
    }

    protected Object get(@NotNull LStatement statement) {
        return statement.keyMap.get(id);
    }

    protected void set(@NotNull LStatement statement, Object value) {
        statement.keyMap.put(id, value);
    }
}