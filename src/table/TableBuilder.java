package table;

import firstfollow.FirstSetCalculator;
import grammar.Grammar;
import grammar.Production;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableBuilder {

    private final FirstSetCalculator firstSetCalculator;

    public TableBuilder() {
        this.firstSetCalculator = new FirstSetCalculator();
    }

    public ParsingTable buildParsingTable(Grammar grammar,
                                          Map<String, Set<String>> firstSets,
                                          Map<String, Set<String>> followSets) {
        ParsingTable parsingTable = new ParsingTable();

        for (String nonTerminal : grammar.getNonTerminals()) {
            List<List<String>> productions = grammar.getAlternatives(nonTerminal);

            for (List<String> rhs : productions) {
                Production production = new Production(nonTerminal, rhs);

                Set<String> firstOfRhs = firstSetCalculator.computeFirstOfSequence(rhs, grammar, firstSets);

                for (String terminal : firstOfRhs) {
                    if (!terminal.equals(Grammar.EPSILON)) {
                        parsingTable.addEntry(nonTerminal, terminal, production);
                    }
                }

                if (firstOfRhs.contains(Grammar.EPSILON)) {
                    for (String followSymbol : followSets.get(nonTerminal)) {
                        parsingTable.addEntry(nonTerminal, followSymbol, production);
                    }
                }
            }
        }

        return parsingTable;
    }
}