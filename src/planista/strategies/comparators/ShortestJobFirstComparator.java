package planista.strategies.comparators;

import java.util.Comparator;

import planista.process.Process;

public class ShortestJobFirstComparator implements Comparator<Process> {

    public ShortestJobFirstComparator() {
    }

    /*
     * -1 -> o1 <  o2
     *  0 -> o1 == o2
     *  1 -> o1 >  o2
     */
    @Override
    public int compare(Process o1, Process o2) {
        if (o1.getId() == o2.getId()) {
            return 0;
        }
        if (o1.getDemand() < o2.getDemand() || (o1.getDemand() == o2.getDemand() && o1.getId() < o2.getId())) {
            return -1;
        }
        return 1;
    }
}
