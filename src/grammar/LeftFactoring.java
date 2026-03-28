package grammar;

import java.util.*;

public class LeftFactoring {

    public Grammar factorGrammar(Grammar originalGrammar) {
        Grammar factoredGrammar = deepCopyGrammar(originalGrammar);

        boolean changed;
        do {
            changed = false;
            List<String> nonTerminals = new ArrayList<>(factoredGrammar.getNonTerminals());

            for (String nonTerminal : nonTerminals) {
                boolean factored = factorNonTerminal(factoredGrammar, nonTerminal);
                if (factored) {
                    changed = true;
                    break;
                }
            }
        } while (changed);

        factoredGrammar.finalizeGrammarSymbols();
        return factoredGrammar;
    }

    private boolean factorNonTerminal(Grammar grammar, String lhs) {
        List<List<String>> alternatives = grammar.getAlternatives(lhs);
        if (alternatives.size() < 2) {
            return false;
        }

        Map<String, List<List<String>>> prefixGroups = new LinkedHashMap<>();

        for (List<String> alternative : alternatives) {
            String key = alternative.isEmpty() ? Grammar.EPSILON : alternative.get(0);
            prefixGroups.putIfAbsent(key, new ArrayList<>());
            prefixGroups.get(key).add(alternative);
        }

        for (Map.Entry<String, List<List<String>>> entry : prefixGroups.entrySet()) {
            List<List<String>> group = entry.getValue();

            if (group.size() < 2) {
                continue;
            }

            List<String> commonPrefix = findLongestCommonPrefix(group);

            if (commonPrefix.isEmpty()) {
                continue;
            }

            String newNonTerminal = generateNewNonTerminal(grammar, lhs);

            List<List<String>> updatedAlternatives = new ArrayList<>();
            List<List<String>> newNonTerminalAlternatives = new ArrayList<>();

            for (List<String> alternative : alternatives) {
                if (group.contains(alternative)) {
                    List<String> suffix = new ArrayList<>(
                            alternative.subList(commonPrefix.size(), alternative.size())
                    );

                    if (suffix.isEmpty()) {
                        suffix.add(Grammar.EPSILON);
                    }

                    newNonTerminalAlternatives.add(suffix);
                } else {
                    updatedAlternatives.add(new ArrayList<>(alternative));
                }
            }

            List<String> newProduction = new ArrayList<>(commonPrefix);
            newProduction.add(newNonTerminal);
            updatedAlternatives.add(newProduction);

            grammar.getProductions().put(lhs, updatedAlternatives);
            grammar.getProductions().put(newNonTerminal, newNonTerminalAlternatives);
            grammar.getNonTerminals().add(newNonTerminal);

            return true;
        }

        return false;
    }

    private List<String> findLongestCommonPrefix(List<List<String>> group) {
        if (group == null || group.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> prefix = new ArrayList<>(group.get(0));

        for (int i = 1; i < group.size(); i++) {
            prefix = commonPrefix(prefix, group.get(i));
            if (prefix.isEmpty()) {
                break;
            }
        }

        if (prefix.size() == 1 && prefix.get(0).equals(Grammar.EPSILON)) {
            return new ArrayList<>();
        }

        return prefix;
    }

    private List<String> commonPrefix(List<String> first, List<String> second) {
        List<String> result = new ArrayList<>();
        int minLength = Math.min(first.size(), second.size());

        for (int i = 0; i < minLength; i++) {
            if (first.get(i).equals(second.get(i))) {
                result.add(first.get(i));
            } else {
                break;
            }
        }

        return result;
    }

    private String generateNewNonTerminal(Grammar grammar, String base) {
        String candidate = base + "Prime";
        int counter = 1;

        while (grammar.getNonTerminals().contains(candidate)) {
            candidate = base + "Prime" + counter;
            counter++;
        }

        return candidate;
    }

    private Grammar deepCopyGrammar(Grammar original) {
        Grammar copy = new Grammar();
        copy.setStartSymbol(original.getStartSymbol());

        for (Map.Entry<String, List<List<String>>> entry : original.getProductions().entrySet()) {
            String lhs = entry.getKey();
            for (List<String> rhs : entry.getValue()) {
                copy.addProduction(lhs, rhs);
            }
        }

        copy.finalizeGrammarSymbols();
        return copy;
    }
}