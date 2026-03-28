package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GrammarReader {

    public Grammar readGrammarFromFile(String filePath) throws IOException {
        Grammar grammar = new Grammar();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (!line.contains("->")) {
                    throw new IllegalArgumentException("Invalid production format at line " + lineNumber + ": " + line);
                }

                String[] parts = line.split("->");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid production format at line " + lineNumber + ": " + line);
                }

                String lhs = parts[0].trim();
                String rhsPart = parts[1].trim();

                validateNonTerminal(lhs, lineNumber);

                String[] alternatives = rhsPart.split("\\|");
                for (String alt : alternatives) {
                    List<String> rhsSymbols = tokenizeAlternative(alt.trim());
                    grammar.addProduction(lhs, rhsSymbols);
                }
            }
        }

        grammar.finalizeGrammarSymbols();
        return grammar;
    }

    private List<String> tokenizeAlternative(String alternative) {
        List<String> tokens = new ArrayList<>();

        if (alternative.isEmpty()) {
            tokens.add(Grammar.EPSILON);
            return tokens;
        }

        String[] parts = alternative.split("\\s+");
        for (String part : parts) {
            if (part.equals("@")) {
                tokens.add(Grammar.EPSILON);
            } else {
                tokens.add(part);
            }
        }

        return tokens;
    }

    private void validateNonTerminal(String symbol, int lineNumber) {
        if (symbol.length() < 1) {
            throw new IllegalArgumentException("Empty non-terminal at line " + lineNumber);
        }

        if (!Character.isUpperCase(symbol.charAt(0))) {
            throw new IllegalArgumentException("Non-terminal must start with uppercase at line " + lineNumber + ": " + symbol);
        }
    }
}