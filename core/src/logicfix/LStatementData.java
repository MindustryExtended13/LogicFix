package logicfix;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;

public class LStatementData {
    public final Seq<LStatementEntry> entries = new Seq<>();

    public LStatementData add(LStatementEntry entry) {
        entries.add(entry);
        return this;
    }

    public void build(LStatement statement, Table table) {
        entries.each(entry -> entry.build(statement, table));
    }

    public void read(LStatement statement, String[] args) {
        final int[] i = {1};
        entries.each(entry -> {
            if(!entry.doSkip(statement)) {
                entry.read(statement, i[0], args);
                i[0]++;
            }
        });
    }

    public void write(LStatement statement, StringBuilder builder) {
        entries.each(entry -> entry.write(statement, builder));
    }
}