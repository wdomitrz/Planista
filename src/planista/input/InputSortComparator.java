package planista.input;

import java.util.Comparator;
import planista.process.ProcessInformation;

public class InputSortComparator implements Comparator<ProcessInformation> {

    public InputSortComparator() {
    }

    /*
     * -1 -> o1 <  o2
     *  0 -> o1 == o2
     *  1 -> o1 >  o2
     */
    @Override
    public int compare(ProcessInformation o1, ProcessInformation o2) {
        if (o1.getId() == o2.getId()) {
            return 0;
        }
        if (o1.getBegin() < o2.getBegin() || (o1.getBegin() == o2.getBegin() && o1.getId() < o2.getId())) {
            return -1;
        }
        return 1;
    }

}
