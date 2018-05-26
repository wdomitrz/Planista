package planista.strategies;

import java.util.PriorityQueue;
import planista.process.Process;
import planista.strategies.comparators.FirstComeFirstServedComparator;

public class FCFS extends BaseStrategy {

    public FCFS() {
        // Strategia FCFS to BaseStrategy z odpowiednią kolejką
        super(new PriorityQueue<Process>(1, new FirstComeFirstServedComparator()), "FCFS");
    }

}
