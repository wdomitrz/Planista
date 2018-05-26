package planista.strategies;

import java.util.PriorityQueue;
import planista.process.Process;
import planista.strategies.comparators.ShortestJobFirstComparator;

public class SJF extends BaseStrategy {

    public SJF() {
        // Strategia SJF to BaseStrategy z odpowiednią kolejką
        super(new PriorityQueue<Process>(1, new ShortestJobFirstComparator()), "SJF");
    }
}
