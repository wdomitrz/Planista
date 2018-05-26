package planista.strategies;

import java.util.LinkedList;
import planista.process.Process;

public class RR extends BaseStrategy {

    private final int q;

    public RR(int q) {
        /*
         * RR-q to strategia bazowa z odpowiednią koleją i zamienionymi dowma
         * medtodami.
         */
        super(new LinkedList<Process>(), "RR-" + q);
        this.q = q;
    }

    @Override
    protected void actualizeCurrentWorkingProcess() {
        super.actualizeCurrentWorkingProcess();
        // Sprawdzenie czy aktualnie pracujący proces powinien już zakończyć pracę, bo robi to za długo
        if (currentWorkingProcess != null && currentWorkingProcess.getCurrentWorkTime() >= q) {
            currentWorkingProcess.setCurrentWorkTime(0.);
            queueOfAwaitingProcesses.add(currentWorkingProcess);
            currentWorkingProcess = null;
        }
    }

    @Override
    protected void nextTimeMoment() {
        // Znalezienie kolejnej jednostki czasu, którą musimy rozpatrywać
        super.nextTimeMoment();
        if (currentWorkingProcess != null
                && q - currentWorkingProcess.getCurrentWorkTime() + currentTime < nextTime
                && q - currentWorkingProcess.getCurrentWorkTime() > 0) {
            if (q - currentWorkingProcess.getCurrentWorkTime() > 0.) {
                nextTime = q - currentWorkingProcess.getCurrentWorkTime() + currentTime;
            }
        }
    }
}
