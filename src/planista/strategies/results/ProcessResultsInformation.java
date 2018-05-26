package planista.strategies.results;

import java.util.Formatter;
import java.util.Locale;
import planista.process.ProcessInformation;

/*
 * Klasa przechowująca informacje o przetworzonych już procesach
 */
public class ProcessResultsInformation {

    // Informacje bazowe o procesie
    private final ProcessInformation processInformation;

    // Informacja o czasie zakończenia
    private final double end;

    public ProcessResultsInformation(ProcessInformation processInformation, double end) {
        this.processInformation = processInformation;
        this.end = end;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Formatter formatter = new Formatter(result, Locale.US);
        formatter.format("[%d %d %.2f]", processInformation.getId(), processInformation.getBegin(), end);
        return result.toString();
    }
}
