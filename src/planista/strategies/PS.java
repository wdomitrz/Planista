package planista.strategies;

import java.util.List;
import java.util.PriorityQueue;
import planista.process.Process;
import planista.process.ProcessInformation;
import planista.strategies.comparators.ShortestRemainingTimeComparator;
import planista.strategies.results.ProcessResultsInformation;

public class PS extends Strategy {

    // Aktualna ilość zasobów procesora otrzymywana przez jeden proces
    private double currentEfficiency;

    private PriorityQueue<Process> queueOfCurrentProcesses;

    // Dopuszczana przeze mnie niedokładność powstała przez double
    private static final double EPS = 0.0000000001;

    public PS() {
        super("PS");
        currentEfficiency = 0.;
        queueOfCurrentProcesses = null;
    }

    @Override
    protected void prepareToSimulate(List<ProcessInformation> mainListOfProcesses) {
        super.prepareToSimulate(mainListOfProcesses);
        queueOfCurrentProcesses = new PriorityQueue<>(1, new ShortestRemainingTimeComparator());

    }

    @Override
    protected boolean continueSimulation() {
        return !listOfProcesses.isEmpty()
                || !queueOfCurrentProcesses.isEmpty();
    }

    @Override
    protected void actualizeCurrentWorkingProcess() {
        // Ustawienie mocy obliczeniowej na jeden proces w jednej jednosctce czasu
        if (queueOfCurrentProcesses.isEmpty()) {
            currentEfficiency = 0.;
        } else {
            currentEfficiency = 1. / queueOfCurrentProcesses.size();
        }

        // Zaktualizowanie informacji w aktualnie rozpatrywanych procesach
        queueOfCurrentProcesses.forEach((currentProcess) -> {
            // Dodanie informacji o odpracowanym czasie
            currentProcess.workedFor((currentTime - currentProcess.getLastUpdate()), currentEfficiency);
        });

        // Usunięcie procesów już zrealizowanych
        while (!queueOfCurrentProcesses.isEmpty() && queueOfCurrentProcesses.peek().getDemandLeft() <= EPS) {

            // Usunięcie procesu z kolejki wykonywanych procesów
            Process currentProcess = queueOfCurrentProcesses.poll();

            // Dodanie czasu obrotu danego procesu do średniego czasu
            meanRealizationTime
                    = meanRealizationTime
                    * listOfFinishedProcesses.size()
                    / (listOfFinishedProcesses.size() + 1)
                    + (currentProcess.getLatencyTime()
                    + currentProcess.getRealizationTime())
                    / (listOfFinishedProcesses.size() + 1);

            // Dodanie czasu oczekiwania danego procesu do średniego czasu
            meanLatencyTime
                    = meanLatencyTime
                    * listOfFinishedProcesses.size()
                    / (listOfFinishedProcesses.size() + 1)
                    + currentProcess.getLatencyTime()
                    / (listOfFinishedProcesses.size() + 1);

            // Dodanie procesu do listy zakończonych
            listOfFinishedProcesses.add(
                    new ProcessResultsInformation(
                            currentProcess.getProcessInfo(),
                            currentTime));
        }
    }

    @Override
    protected void addNewProcesses() {
        // Dodanie procesów rozpoczynających
        while (!listOfProcesses.isEmpty()
                && listOfProcesses.peek().getBegin() <= currentTime) {
            queueOfCurrentProcesses.add(new Process(listOfProcesses.poll()));
        }
    }

    @Override
    protected void startToWork() {
        // Procesy rozpoczynające od razu idą pracować, więc nie muszę tu nic robić
    }

    @Override
    protected void nextTimeMoment() {
        /* Ustawienie mocy obliczeniowej na jeden proces w jednej jednosctce 
             * czasu w kolejnym okresie czasu
         */
        if (queueOfCurrentProcesses.isEmpty()) {
            currentEfficiency = 0.;
        } else {
            currentEfficiency = 1. / queueOfCurrentProcesses.size();
        }

        // Znalezienie kolejnej jednostki czasu, którą musimy rozpatrywać
        if (!listOfProcesses.isEmpty()
                && (currentTime == nextTime
                || listOfProcesses.peek().getBegin() < nextTime)) {
            nextTime = listOfProcesses.peek().getBegin();
        }
        if (currentEfficiency != 0.
                && (currentTime == nextTime
                || queueOfCurrentProcesses.peek().getDemandLeft() / currentEfficiency
                + currentTime < nextTime)) {
            nextTime = queueOfCurrentProcesses.peek().getDemandLeft() / currentEfficiency
                    + currentTime;
        }
    }
}
