package logicfix.instance;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import logicfix.LStatement;
import logicfix.LStatementEntry;
import org.jetbrains.annotations.NotNull;

public class MultiLStatementEntry extends LStatementEntry {
    public Seq<LStatementEntry> handle() {
        return Seq.with();
    }

    public void pre(String[] args) {
    }

    @Override
    public void build(LStatement statement, Table table) {
        handle().each(entry -> entry.build(statement, table));
    }

    @Override
    public void read(LStatement statement, int id, String @NotNull [] args) {
        String data = args[id];
        data = data.substring(1, data.length()-2);
        Seq<String> seq = new Seq<>();
        StringBuilder cont = new StringBuilder();
        boolean opened = false;
        for(char ch : data.toCharArray()) {
            if(ch == ' ' && !opened) {
                seq.add(cont.toString());
                cont = new StringBuilder();
                continue;
            }

            if(ch == '\"') {
                opened = !opened;
                continue;
            }

            cont.append(ch);
        }
        seq.add(cont.toString());
        String[] argCopy = new String[seq.size];
        for(int i = 0; i < argCopy.length; i++) {
            argCopy[i] = seq.get(i);
        }
        pre(argCopy);
        Seq<LStatementEntry> handle = handle();
        int i = 0;
        for(LStatementEntry entry : handle) {
            if(!entry.doSkip(statement)) {
                entry.read(statement, i, argCopy);
                i++;
            }
        }
    }

    @Override
    public void write(@NotNull LStatement statement, @NotNull StringBuilder builder) {
        StringBuilder builder1 = new StringBuilder();
        handle().each(h -> {
            if(!h.doSkip(statement)) {
                h.write(statement, builder1);
                builder1.append(" ");
            }
        });
        builder.append("\"").append(builder1).append("\"");
    }
}