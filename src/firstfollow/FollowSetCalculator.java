package firstfollow;

import grammar.Grammar;

import java.util.*;

public class FollowSetCalculator {

    private final FirstSetCalculator firstSetCalculator;

    public FollowSetCalculator() {
        this.firstSetCalculator = new FirstSetCalculator();
    }

    public Map<String, Set<String>> computeFollowSets(Grammar grammar,
                                                      Map<String, Set<String>> firstSets) {
        Map<String, Set<String>> followSets = new LinkedHashMap<>();

        for (String nonTerminal : grammar.getNonTerminals()) {
            followSets.put(nonTerminal, new LinkedHashSet<>());
        }

        followSets.get(grammar.getStartSymbol()).add("$");

        boolean changed;
        do {
            changed = false;

            for (Map.Entry<String, List<List<String>>> entry : grammar.getProductions().entrySet()) {
                String lhs = entry.getKey();
                List<List<String>> productions = entry.getValue();

                for (List<String> production : productions) {
                    for (int i = 0; i < production.size(); i++) {
                        String currentSymbol = production.get(i);

                        if (!grammar.isNonTerminal(currentSymbol)) {
                            continue;
                        }

                        List<String> beta = new ArrayList<>();
                        for (int j = i + 1; j < production.size(); j++) {
                            beta.add(production.get(j));
                        }

                        int beforeSize = followSets.get(currentSymbol).size();

                        Set<String> firstOfBeta = firstSetCalculator.computeFirstOfSequence(beta, grammar, firstSets);

                        for (String symbol : firstOfBeta) {
                            if (!symbol.equals(Grammar.EPSILON)) {
                                followSets.get(currentSymbol).add(symbol);
                            }
                        }

                        if (beta.isEmpty() || firstOfBeta.contains(Grammar.EPSILON)) {
                            followSets.get(currentSymbol).addAll(followSets.get(lhs));
                        }

                        int afterSize = followSets.get(currentSymbol).size();
                        if (afterSize > beforeSize) {
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        return followSets;
    }
}