import grammar.Grammar;
import grammar.GrammarReader;
import grammar.LeftFactoring;
import utils.FileUtils;
import utils.Printer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        //change the file to make it run on different grammars
        String grammarPath = "input/grammar2.txt";
        String grammarLoadedPath = "output/grammar_loaded.txt";
        String grammarFactoredPath = "output/grammar_factored.txt";

        GrammarReader reader = new GrammarReader();
        LeftFactoring leftFactoring = new LeftFactoring();

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

        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Grammar error: " + e.getMessage());
        }
    }
}