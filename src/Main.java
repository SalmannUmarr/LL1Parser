import firstfollow.FirstSetCalculator;
import firstfollow.FollowSetCalculator;
import grammar.Grammar;
import grammar.GrammarReader;
import grammar.LeftFactoring;
import grammar.LeftRecursionRemover;
import table.ParsingTable;
import table.TableBuilder;
import utils.FileUtils;
import utils.Printer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String grammarPath = "input/grammar1.txt";
        String grammarLoadedPath = "output/grammar_loaded.txt";
        String grammarFactoredPath = "output/grammar_factored.txt";
        String grammarNoRecursionPath = "output/grammar_no_left_recursion.txt";
        String firstSetsPath = "output/first_sets.txt";
        String followSetsPath = "output/follow_sets.txt";
        String parsingTablePath = "output/parsing_table.txt";

        GrammarReader reader = new GrammarReader();
        LeftFactoring leftFactoring = new LeftFactoring();
        LeftRecursionRemover leftRecursionRemover = new LeftRecursionRemover();
        FirstSetCalculator firstSetCalculator = new FirstSetCalculator();
        FollowSetCalculator followSetCalculator = new FollowSetCalculator();
        TableBuilder tableBuilder = new TableBuilder();

        try {
            Grammar grammar = reader.readGrammarFromFile(grammarPath);

            Printer.printGrammar(grammar, "Original Grammar");

            System.out.println("\nStart Symbol: " + grammar.getStartSymbol());
            System.out.println("Non-Terminals: " + grammar.getNonTerminals());
            System.out.println("Terminals: " + grammar.getTerminals());

            FileUtils.writeLines(grammarLoadedPath, Printer.grammarToLines(grammar));
            System.out.println("\nGrammar saved to: " + grammarLoadedPath);

            Grammar factoredGrammar = leftFactoring.factorGrammar(grammar);
            Printer.printGrammar(factoredGrammar, "Grammar After Left Factoring");
            FileUtils.writeLines(grammarFactoredPath, Printer.grammarToLines(factoredGrammar));
            System.out.println("Factored grammar saved to: " + grammarFactoredPath);

            Grammar noLeftRecursionGrammar = leftRecursionRemover.removeLeftRecursion(factoredGrammar);
            Printer.printGrammar(noLeftRecursionGrammar, "Grammar After Left Recursion Removal");
            FileUtils.writeLines(grammarNoRecursionPath, Printer.grammarToLines(noLeftRecursionGrammar));
            System.out.println("Left recursion removed grammar saved to: " + grammarNoRecursionPath);

            Map<String, Set<String>> firstSets = firstSetCalculator.computeFirstSets(noLeftRecursionGrammar);
            Printer.printFirstFollow("FIRST Sets", firstSets);
            FileUtils.writeLines(firstSetsPath, Printer.firstFollowToLines("FIRST Sets", firstSets));
            System.out.println("FIRST sets saved to: " + firstSetsPath);

            Map<String, Set<String>> followSets =
                    followSetCalculator.computeFollowSets(noLeftRecursionGrammar, firstSets);
            Printer.printFirstFollow("FOLLOW Sets", followSets);
            FileUtils.writeLines(followSetsPath, Printer.firstFollowToLines("FOLLOW Sets", followSets));
            System.out.println("FOLLOW sets saved to: " + followSetsPath);

            ParsingTable parsingTable =
                    tableBuilder.buildParsingTable(noLeftRecursionGrammar, firstSets, followSets);
            Printer.printParsingTable(noLeftRecursionGrammar, parsingTable);
            FileUtils.writeLines(parsingTablePath, Printer.parsingTableToLines(noLeftRecursionGrammar, parsingTable));
            System.out.println("Parsing table saved to: " + parsingTablePath);

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Grammar error: " + e.getMessage());
        }
    }
}