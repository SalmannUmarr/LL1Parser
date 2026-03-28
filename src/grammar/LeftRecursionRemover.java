package grammar;

import java.util.*;

public class LeftRecursionRemover {

    public Grammar removeLeftRecursion(Grammar originalGrammar) {
        Grammar grammar = deepCopyGrammar(originalGrammar);

        List<String> nonTerminals = new ArrayList<>(grammar.getNonTerminals());

        for (int i = 0; i < nonTerminals.size(); i++) {
            String ai = nonTerminals.get(i);

            for (int j = 0; j < i; j++) {
                String aj = nonTerminals.get(j);
                replaceIndirectRecursion(grammar, ai, aj);
            }

            eliminateDirectLeftRecursion(grammar, ai);

            nonTerminals = new ArrayList<>(grammar.getNonTerminals());
        }

        grammar.finalizeGrammarSymbols();
        return grammar;
    }

    private void replaceIndirectRecursion(Grammar grammar, String ai, String aj) {
        List<List<String>> aiProductions = grammar.getAlternatives(ai);
        List<List<String>> updatedProductions = new ArrayList<>();

        boolean changed = false;

        for (List<String> production : aiProductions) {
            if (!production.isEmpty() && production.get(0).equals(aj)) {
                changed = true;

                List<List<String>> ajProductions = grammar.getAlternatives(aj);
                List<String> suffix = new ArrayList<>(production.subList(1, production.size()));

                for (List<String> ajProd : ajProductions) {
                    List<String> newProduction = new ArrayList<>();

                    if (!(ajProd.size() == 1 && ajProd.get(0).equals(Grammar.EPSILON))) {
                        newProduction.addAll(ajProd);
                    }

                    if (!suffix.isEmpty()) {
                        newProduction.addAll(suffix);
                    }

                    if (newProduction.isEmpty()) {
                        newProduction.add(Grammar.EPSILON);
                    }

                    updatedProductions.add(newProduction);
                }
            } else {
                updatedProductions.add(new ArrayList<>(production));
            }
        }

        if (changed) {
            grammar.getProductions().put(ai, updatedProductions);
        }
    }

    private void eliminateDirectLeftRecursion(Grammar grammar, String nonTerminal) {
        List<List<String>> productions = grammar.getAlternatives(nonTerminal);

        List<List<String>> recursive = new ArrayList<>();
        List<List<String>> nonRecursive = new ArrayList<>();

        for (List<String> production : productions) {
            if (!production.isEmpty() && production.get(0).equals(nonTerminal)) {
                recursive.add(new ArrayList<>(production.subList(1, production.size())));
            } else {
                nonRecursive.add(new ArrayList<>(production));
            }
        }

        if (recursive.isEmpty()) {
            return;
        }

        String newNonTerminal = generateNewNonTerminal(grammar, nonTerminal);

        List<List<String>> newAiProductions = new ArrayList<>();
        List<List<String>> newAiPrimeProductions = new ArrayList<>();

        for (List<String> beta : nonRecursive) {
            List<String> newProduction = new ArrayList<>();

            if (!(beta.size() == 1 && beta.get(0).equals(Grammar.EPSILON))) {
                newProduction.addAll(beta);
            }

            newProduction.add(newNonTerminal);
            newAiProductions.add(newProduction);
        }

        for (List<String> alpha : recursive) {
            List<String> newProduction = new ArrayList<>(alpha);
            newProduction.add(newNonTerminal);
            newAiPrimeProductions.add(newProduction);
        }

        newAiPrimeProductions.add(Collections.singletonList(Grammar.EPSILON));

        grammar.getProductions().put(nonTerminal, newAiProductions);
        grammar.getProductions().put(newNonTerminal, newAiPrimeProductions);
        grammar.getNonTerminals().add(newNonTerminal);
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