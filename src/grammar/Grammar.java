package grammar;

import java.util.*;

public class Grammar {
    public static final String EPSILON = "epsilon";

    private final Map<String, List<List<String>>> productions;
    private final Set<String> nonTerminals;
    private final Set<String> terminals;
    private String startSymbol;

    public Grammar() {
        this.productions = new LinkedHashMap<>();
        this.nonTerminals = new LinkedHashSet<>();
        this.terminals = new LinkedHashSet<>();
        this.startSymbol = null;
    }

    public void addProduction(String lhs, List<String> rhs) {
        if (startSymbol == null) {
            startSymbol = lhs;
        }

        nonTerminals.add(lhs);
        productions.putIfAbsent(lhs, new ArrayList<>());
        productions.get(lhs).add(new ArrayList<>(rhs));
    }

    public void finalizeGrammarSymbols() {
        terminals.clear();

        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            for (List<String> rhs : entry.getValue()) {
                for (String symbol : rhs) {
                    if (symbol.equals(EPSILON)) {
                        continue;
                    }
                    if (!nonTerminals.contains(symbol)) {
                        terminals.add(symbol);
                    }
                }
            }
        }
    }

    public Map<String, List<List<String>>> getProductions() {
        return productions;
    }

    public List<List<String>> getAlternatives(String nonTerminal) {
        return productions.getOrDefault(nonTerminal, new ArrayList<>());
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public boolean isNonTerminal(String symbol) {
        return nonTerminals.contains(symbol);
    }

    public boolean isTerminal(String symbol) {
        return terminals.contains(symbol);
    }

    public void printGrammar() {
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            String lhs = entry.getKey();
            List<String> formattedAlternatives = new ArrayList<>();

            for (List<String> rhs : entry.getValue()) {
                formattedAlternatives.add(String.join(" ", rhs));
            }

            System.out.println(lhs + " -> " + String.join(" | ", formattedAlternatives));
        }
    }
}