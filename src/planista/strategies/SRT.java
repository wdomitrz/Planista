package planista.strategies;

import java.util.PriorityQueue;
import planista.process.Process;
import planista.strategies.comparators.ShortestRemainingTimeComparator;

public class SRT extends BaseStrategy {

    public SRT() {
        // SRT to strategia bazowa z odpowiednią koleją i zmienioną jedną metodą
        super(new PriorityQueue<Process>(1, new ShortestRemainingTimeComparator()), "SRT");
    }

    @Override
    protected void addNewProcesses() {
        while (!listOfProcesses.isEmpty()
                && listOfProcesses.peek().getBegin() <= currentTime) {

            // Pobranie procesu i usunięciu go z listy jeszcze nie oczekujących
            Process newProcess = new planista.process.Process(listOfProcesses.poll());

            // Sprawdzenie, czy nowy proces wybije stary i czy może to zrobić
            if (currentWorkingProcess != null && newProcess.getDemandLeft() < currentWorkingProcess.getDemandLeft()) {
                queueOfAwaitingProcesses.add(currentWorkingProcess);
                currentWorkingProcess = newProcess;
            } else {
                queueOfAwaitingProcesses.add(newProcess);
            }
        }
    }

}
