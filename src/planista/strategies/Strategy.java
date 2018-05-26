package planista.strategies;

import java.util.LinkedList;
import java.util.List;
import planista.process.ProcessInformation;
import planista.strategies.results.ProcessResultsInformation;
import planista.strategies.results.SimulationResults;

public abstract class Strategy {

    // Wyniki ostatniej symulacji
    private SimulationResults resultsOfLastSimulation;

    // Nazwa strategii
    private final String strategyName;

    // Lista porcesów służąca do wyniku
    protected LinkedList<ProcessResultsInformation> listOfFinishedProcesses;

    // Lista porcesów jeszcze nie obsłużonych
    protected LinkedList<ProcessInformation> listOfProcesses;

    // Aktualny czas i czas następnego obrotu
    protected double currentTime;
    protected double nextTime;

    // Informacje potrzebne do szukanej statystyki
    protected double meanRealizationTime;
    protected double meanLatencyTime;

    public Strategy(String strategyName) {
        this.strategyName = strategyName;

        resultsOfLastSimulation = null;
        listOfFinishedProcesses = null;
        listOfProcesses = null;
        currentTime = 0.;
        nextTime = 0.;
        meanRealizationTime = 0.;
        meanLatencyTime = 0.;
    }

    @Override
    public String toString() {
        return strategyName;
    }

    public void simulate(List<ProcessInformation> mainListOfProcesses) {

        // Przygotowanie danych do symulacji
        prepareToSimulate(mainListOfProcesses);

        // Wykonywanie symulacji do czasu istnienia jej sensu
        while (continueSimulation()) {

            // Ustawienie czasu do rozpatrzenia
            currentTime = nextTime;

            // Zaktualizowanie informacji w aktualnie rozpatrywanym procesie
            actualizeCurrentWorkingProcess();

            // Dodanie procesów rozpoczynających
            addNewProcesses();

            // Jeśli jest wolne miejsce, to wstawienie nowego pracującego procesu (jeśli taki istnieje)
            startToWork();

            // Znalezienie kolejnej jednostki czasu, którą musimy rozpatrywać
            nextTimeMoment();
        }
        setResultsOfLastSimulation(
                new SimulationResults(
                        toString(),
                        listOfFinishedProcesses,
                        meanRealizationTime,
                        meanLatencyTime));
    }

    public SimulationResults getResultsOfLastSimulation() {
        return resultsOfLastSimulation;
    }

    private void setResultsOfLastSimulation(SimulationResults resultsOfLastSimulation) {
        this.resultsOfLastSimulation = resultsOfLastSimulation;
    }

    protected void prepareToSimulate(List<ProcessInformation> mainListOfProcesses) {

        // Lista porcesów jeszcze nie obsłużonych
        listOfProcesses = new LinkedList<>(mainListOfProcesses);

        // Lista porcesów służąca do wyniku
        listOfFinishedProcesses = new LinkedList<>();

        // Aktualny czas i czas następnego obrotu
        currentTime = 0;
        nextTime = 0;

        // Informacje potrzebne do szukanej statystyki
        meanRealizationTime = 0.;
        meanLatencyTime = 0.;
    }

    // Sprawdzanie sensu kontynuowania symulacji
    protected abstract boolean continueSimulation();

    // Zaktualizowanie informacji w aktualnie rozpatrywanym procesie
    protected abstract void actualizeCurrentWorkingProcess();

    // Dodanie procesów rozpoczynających
    protected abstract void addNewProcesses();

    // Jeśli jest wolne miejsce, to wstawienie nowego pracującego procesu (jeśli taki istnieje)
    protected abstract void startToWork();

    // Znalezienie kolejnej jednostki czasu, którą musimy rozpatrywać
    protected abstract void nextTimeMoment();

}
