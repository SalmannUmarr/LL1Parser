package utils;

import grammar.Grammar;

import java.util.*;

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

    public static List<String> firstFollowToLines(String title, Map<String, Set<String>> sets) {
        List<String> lines = new ArrayList<>();
        lines.add("=== " + title + " ===");

        for (Map.Entry<String, Set<String>> entry : sets.entrySet()) {
            lines.add(entry.getKey() + " = { " + String.join(", ", entry.getValue()) + " }");
        }

        return lines;
    }

    public static void printFirstFollow(String title, Map<String, Set<String>> sets) {
        System.out.println("\n=== " + title + " ===");
        for (Map.Entry<String, Set<String>> entry : sets.entrySet()) {
            System.out.println(entry.getKey() + " = { " + String.join(", ", entry.getValue()) + " }");
        }
    }
}