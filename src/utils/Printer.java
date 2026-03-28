package utils;

import grammar.Grammar;
import grammar.Production;
import table.ParsingTable;

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

    public static List<String> parsingTableToLines(Grammar grammar, ParsingTable parsingTable) {
        List<String> lines = new ArrayList<>();

        List<String> columns = new ArrayList<>(grammar.getTerminals());
        columns.add("$");

        lines.add("=== LL(1) Parsing Table ===");
        lines.add("Grammar is LL(1): " + (parsingTable.isLL1() ? "YES" : "NO"));
        lines.add("");

        StringBuilder header = new StringBuilder(String.format("%-15s", ""));
        for (String terminal : columns) {
            header.append(String.format("%-30s", terminal));
        }
        lines.add(header.toString());

        for (String nonTerminal : grammar.getNonTerminals()) {
            StringBuilder row = new StringBuilder(String.format("%-15s", nonTerminal));

            for (String terminal : columns) {
                Production production = parsingTable.getEntry(nonTerminal, terminal);
                String cell = (production == null) ? "" : production.toString();
                row.append(String.format("%-30s", cell));
            }

            lines.add(row.toString());
        }

        return lines;
    }

    public static void printParsingTable(Grammar grammar, ParsingTable parsingTable) {
        System.out.println("\n=== LL(1) Parsing Table ===");
        System.out.println("Grammar is LL(1): " + (parsingTable.isLL1() ? "YES" : "NO"));

        List<String> columns = new ArrayList<>(grammar.getTerminals());
        columns.add("$");

        int colWidth = 25;

        // Print top border
        printSeparator(columns.size() + 1, colWidth);

        // Print header row
        System.out.printf("|%-" + colWidth + "s", "");
        for (String terminal : columns) {
            System.out.printf("|%-" + colWidth + "s", terminal);
        }
        System.out.println("|");

        // Header separator
        printSeparator(columns.size() + 1, colWidth);

        // Rows
        for (String nonTerminal : grammar.getNonTerminals()) {
            System.out.printf("|%-" + colWidth + "s", nonTerminal);

            for (String terminal : columns) {
                Production production = parsingTable.getEntry(nonTerminal, terminal);
                String cell = (production == null) ? "" : production.toString();

                // Trim if too long
                if (cell.length() > colWidth - 1) {
                    cell = cell.substring(0, colWidth - 4) + "...";
                }

                System.out.printf("|%-" + colWidth + "s", cell);
            }
            System.out.println("|");

            printSeparator(columns.size() + 1, colWidth);
        }
    }

    private static void printSeparator(int columns, int width) {
        for (int i = 0; i < columns; i++) {
            System.out.print("+");
            for (int j = 0; j < width; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
    }}