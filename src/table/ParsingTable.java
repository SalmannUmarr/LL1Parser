package table;

import grammar.Production;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ParsingTable {
    private final Map<String, Map<String, Production>> table;
    private boolean isLL1;

    public ParsingTable() {
        this.table = new LinkedHashMap<>();
        this.isLL1 = true;
    }

    public void addEntry(String nonTerminal, String terminal, Production production) {
        table.putIfAbsent(nonTerminal, new LinkedHashMap<>());

        Map<String, Production> row = table.get(nonTerminal);

        if (row.containsKey(terminal)) {
            Production existing = row.get(terminal);
            if (!existing.equals(production)) {
                isLL1 = false;
            }
        } else {
            row.put(terminal, production);
        }
    }

    public Production getEntry(String nonTerminal, String terminal) {
        if (!table.containsKey(nonTerminal)) {
            return null;
        }
        return table.get(nonTerminal).get(terminal);
    }

    public Map<String, Map<String, Production>> getTable() {
        return table;
    }

    public boolean isLL1() {
        return isLL1;
    }

    public Set<String> getTerminalsForRow(String nonTerminal) {
        if (!table.containsKey(nonTerminal)) {
            return Set.of();
        }
        return table.get(nonTerminal).keySet();
    }
}