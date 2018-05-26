package planista.strategies.results;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;

/*
 * Klasa przechowująca informacje o wynikach symulacji
 */
public class SimulationResults {

    private final String strategyName;
    private final List<ProcessResultsInformation> listOfProcesses;
    private final double meanRealizationTime;
    private final double meanLatencyTime;

    public SimulationResults(String strategyName, List<ProcessResultsInformation> listOfProcesses, double meanRealizationTime, double meanLatencyTime) {
        this.strategyName = strategyName;
        this.listOfProcesses = listOfProcesses;
        this.meanRealizationTime = meanRealizationTime;
        this.meanLatencyTime = meanLatencyTime;
    }

    @Override
    public String toString() {
        String listString = "";
        listString = listOfProcesses.stream().map((process) -> process.toString()).reduce(listString, String::concat);

        StringBuilder result = new StringBuilder();
        Formatter formatter = new Formatter(result, Locale.US);
        formatter.format("Strategia: %s\n", strategyName);
        formatter.format("%s\n", listString);
        formatter.format("Średni czas obrotu: %.2f\n", meanRealizationTime);
        formatter.format("Średni czas oczekiwania: %.2f\n", meanLatencyTime);

        return result.toString();
    }

}
