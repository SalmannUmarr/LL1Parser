package firstfollow;

import grammar.Grammar;

import java.util.*;

public class FirstSetCalculator {

    public Map<String, Set<String>> computeFirstSets(Grammar grammar) {
        Map<String, Set<String>> firstSets = new LinkedHashMap<>();

        for (String nonTerminal : grammar.getNonTerminals()) {
            firstSets.put(nonTerminal, new LinkedHashSet<>());
        }

        boolean changed;
        do {
            changed = false;

            for (String nonTerminal : grammar.getNonTerminals()) {
                List<List<String>> productions = grammar.getAlternatives(nonTerminal);

                for (List<String> production : productions) {
                    int beforeSize = firstSets.get(nonTerminal).size();
                    addFirstOfSequence(production, grammar, firstSets, firstSets.get(nonTerminal));
                    int afterSize = firstSets.get(nonTerminal).size();

                    if (afterSize > beforeSize) {
                        changed = true;
                    }
                }
            }
        } while (changed);

        return firstSets;
    }

    public Set<String> computeFirstOfSequence(List<String> sequence,
                                              Grammar grammar,
                                              Map<String, Set<String>> firstSets) {
        Set<String> result = new LinkedHashSet<>();
        addFirstOfSequence(sequence, grammar, firstSets, result);
        return result;
    }

    private void addFirstOfSequence(List<String> sequence,
                                    Grammar grammar,
                                    Map<String, Set<String>> firstSets,
                                    Set<String> target) {
        if (sequence == null || sequence.isEmpty()) {
            target.add(Grammar.EPSILON);
            return;
        }

        boolean allCanDeriveEpsilon = true;

        for (String symbol : sequence) {
            if (symbol.equals(Grammar.EPSILON)) {
                target.add(Grammar.EPSILON);
                allCanDeriveEpsilon = false;
                break;
            }

            if (grammar.isTerminal(symbol)) {
                target.add(symbol);
                allCanDeriveEpsilon = false;
                break;
            }

            if (grammar.isNonTerminal(symbol)) {
                Set<String> firstOfSymbol = firstSets.get(symbol);

                for (String item : firstOfSymbol) {
                    if (!item.equals(Grammar.EPSILON)) {
                        target.add(item);
                    }
                }

                if (firstOfSymbol.contains(Grammar.EPSILON)) {
                    allCanDeriveEpsilon = true;
                } else {
                    allCanDeriveEpsilon = false;
                    break;
                }
            } else {
                target.add(symbol);
                allCanDeriveEpsilon = false;
                break;
            }
        }

        if (allCanDeriveEpsilon) {
            target.add(Grammar.EPSILON);
        }
    }
}