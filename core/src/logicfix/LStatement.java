package logicfix;

import arc.func.Cons;
import arc.func.Cons2;
import arc.scene.ui.Button;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import mindustry.logic.LAssembler;

import mindustry.logic.LCategory;
import mindustry.logic.LExecutor;
import mindustry.logic.LExecutor.*;
import org.jetbrains.annotations.NotNull;

public class LStatement extends mindustry.logic.LStatement {
    public static final NoopI INSTRUCTION_INVALID_NOOP = new NoopI();
    protected ObjectMap<String, Object> keyMap = new ObjectMap<>();
    protected ObjectMap<String, Integer> valueMap = new ObjectMap<>();
    private Cons2<LStatement, LExecutor> executor;
    private LStatementData data = new LStatementData();
    private LCategory category = LCategory.unknown;
    private boolean nonPrivileged = false;
    private String localizedName = null;
    private boolean privileged = false;
    private String name = null;

    public boolean[] _func_195101() {
        return new boolean[] {name == null, localizedName == null};
    }

    public LStatement executor(LInstruction instruction) {
        if(instruction == null) {
            instruction = INSTRUCTION_INVALID_NOOP;
        }
        LInstruction finalInstruction = instruction;
        return executor((ignored, exec) -> finalInstruction.run(exec));
    }

    public LStatement executor(Cons2<LStatement, LExecutor> executor) {
        if(executor == null) {
            executor = (ignored1, ignored2) -> {};
        }
        this.executor = executor;
        return this;
    }

    public LStatement name(String name) {
        this.name = name;
        return this;
    }

    public LStatement localizedName(String name) {
        this.localizedName = name;
        return this;
    }

    public LStatement nonPrivileged(boolean nonPrivileged) {
        this.nonPrivileged = nonPrivileged;
        return this;
    }

    public LStatement privileged(boolean privileged) {
        this.privileged = privileged;
        return this;
    }

    public LStatement category(LCategory category) {
        this.category = category;
        return this;
    }

    public LStatement data(LStatementData data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean nonPrivileged() {
        return nonPrivileged;
    }

    @Override
    public boolean privileged() {
        return privileged;
    }

    @Override
    public String name() {
        return localizedName;
    }

    @Override
    public LCategory category() {
        return category;
    }

    @Override
    public void build(Table table) {
        data.build(this, table);
    }

    @Override
    public LInstruction build(LAssembler builder) {
        valueMap.clear();
        keyMap.each((key, value) -> {
            valueMap.put(key, builder.var(String.valueOf(value)));
        });
        return executor == null ? INSTRUCTION_INVALID_NOOP : (exec) -> executor.get(this, exec);
    }

    @Override
    public void param(Cell<Label> label) {
        super.param(label);
    }

    @Override
    public String sanitize(String value) {
        return super.sanitize(value);
    }

    @Override
    public Cell<TextField> field(Table table, String value, Cons<String> setter) {
        return super.field(table, value, setter);
    }

    @Override
    public Cell<TextField> fields(Table table, String desc, String value, Cons<String> setter) {
        return super.fields(table, desc, value, setter);
    }

    @Override
    public Cell<TextField> fields(Table table, String value, Cons<String> setter) {
        return super.fields(table, value, setter);
    }

    @Override
    public void row(Table table) {
        super.row(table);
    }

    @Override
    public <T> void showSelect(Button b, T[] values, T current, Cons<T> getter, int cols, Cons<Cell> sizer) {
        super.showSelect(b, values, current, getter, cols, sizer);
    }

    @Override
    public <T> void showSelect(Button b, T[] values, T current, Cons<T> getter) {
        super.showSelect(b, values, current, getter);
    }

    @Override
    public void showSelectTable(Button b, Cons2<Table, Runnable> hideCons) {
        super.showSelectTable(b, hideCons);
    }

    @Override
    public void write(@NotNull StringBuilder builder) {
        data.write(this, builder.append(name).append(" "));
    }

    public void read(String[] args) {
        data.read(this, args);
    }
}