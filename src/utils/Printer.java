package utils;

import grammar.Grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Printer {

    public static List<String> grammarToLines(Grammar grammar) {
        List<String> lines = new ArrayList<>();

        for (Map.Entry<String, List<List<String>>> entry : grammar.getProductions().entrySet()) {
            String lhs = entry.getKey();
            List<String> alternatives = new ArrayList<>();

            for (List<String> rhs : entry.getValue()) {
                alternatives.add(String.join(" ", rhs));
            }

            lines.add(lhs + " -> " + String.join(" | ", alternatives));
        }

        return lines;
    }

    public static void printGrammar(Grammar grammar, String title) {
        System.out.println("\n=== " + title + " ===");
        for (String line : grammarToLines(grammar)) {
            System.out.println(line);
        }
    }
}