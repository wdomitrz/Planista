package planista.strategies;

import java.util.List;
import java.util.Queue;
import planista.process.ProcessInformation;
import planista.process.Process;
import planista.strategies.results.ProcessResultsInformation;

/*
 * Nadklasa dla klas strategii opierających się na obsłudze jednego procesu na raz
 */
public abstract class BaseStrategy extends Strategy {

    // Aktualnie wykonywany proces
    protected Process currentWorkingProcess;

    // Kolejka pryiorytetowa elementów aktualnie oczekujących na obsłużenie
    protected Queue<planista.process.Process> queueOfAwaitingProcesses;

    public BaseStrategy(Queue<Process> queueOfAwaitingProcesses, String strategyName) {
        super(strategyName);
        currentWorkingProcess = null;
        this.queueOfAwaitingProcesses = queueOfAwaitingProcesses;
    }

    @Override
    protected boolean continueSimulation() {
        return !listOfProcesses.isEmpty()
                || currentWorkingProcess != null
                || !queueOfAwaitingProcesses.isEmpty();
    }

    @Override
    protected void prepareToSimulate(List<ProcessInformation> mainListOfProcesses) {
        super.prepareToSimulate(mainListOfProcesses);

        // Rroces aktualnie obsługiwany
        currentWorkingProcess = null;

        // Kolejka pryiorytetowa elementów aktualnie oczekujących na obsłużenie
        queueOfAwaitingProcesses.clear();
    }

    @Override
    protected void actualizeCurrentWorkingProcess() {
        if (currentWorkingProcess != null) {

            // Dodanie informacji o odpracowanym czasie
            currentWorkingProcess.workedFor(currentTime - currentWorkingProcess.getLastUpdate(), 1);

            // Sprawdzenie, czy proces już zakończył pracę
            if (currentWorkingProcess.getDemandLeft() <= 0.) {

                // Dodanie czasu obrotu danego procesu do średniego czasu
                meanRealizationTime
                        = meanRealizationTime
                        * listOfFinishedProcesses.size()
                        / (listOfFinishedProcesses.size() + 1)
                        + (currentWorkingProcess.getLatencyTime()
                        + currentWorkingProcess.getRealizationTime())
                        / (listOfFinishedProcesses.size() + 1);

                // Dodanie czasu oczekiwania danego procesu do średniego czasu
                meanLatencyTime
                        = meanLatencyTime
                        * listOfFinishedProcesses.size()
                        / (listOfFinishedProcesses.size() + 1)
                        + currentWorkingProcess.getLatencyTime()
                        / (listOfFinishedProcesses.size() + 1);

                // Dodanie procesu do listy zakończonych
                listOfFinishedProcesses.add(new ProcessResultsInformation(
                        currentWorkingProcess.getProcessInfo(),
                        currentTime));

                // Zaznaczenie, że nie ma aktualnie żadnego wykonywanego procesu
                currentWorkingProcess = null;
            }
        }
    }

    @Override
    protected void addNewProcesses() {
        while (!listOfProcesses.isEmpty()
                && listOfProcesses.peek().getBegin() <= currentTime) {

            // Pobranie procesu i usunięciu go z listy jeszcze nie oczekujących
            queueOfAwaitingProcesses.add(
                    new planista.process.Process(listOfProcesses.poll()));

        }
    }

    @Override
    protected void startToWork() {
        if (currentWorkingProcess == null && !queueOfAwaitingProcesses.isEmpty()) {

            // Pobranie pierwszego obiegktu z kolejki oczekujących usuwając go
            Process currentProcess = queueOfAwaitingProcesses.poll();

            // Zaktualizowanie informacji o czasie oczekiwania
            currentProcess.increaseLatencyTime(
                    currentTime - currentProcess.getLastUpdate());

            // Ustawienie tego procesu jako właśnie pracujący
            currentWorkingProcess = currentProcess;
        }
    }

    @Override
    protected void nextTimeMoment() {
        if (!listOfProcesses.isEmpty()
                && (currentTime == nextTime
                || listOfProcesses.peek().getBegin() < nextTime)) {
            nextTime = listOfProcesses.peek().getBegin();
        }
        if (currentWorkingProcess != null
                && (currentTime == nextTime
                || currentWorkingProcess.getDemandLeft() + currentTime < nextTime)) {
            nextTime = currentWorkingProcess.getDemandLeft() + currentTime;
        }
    }
}
