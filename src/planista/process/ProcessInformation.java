package planista.process;

/*
 * Klasa informacji o procsie
 */
public class ProcessInformation {

    private final int id;
    private final int begin;
    private final int demand;

    public ProcessInformation(int id, int begin, int demand) {
        this.id = id;
        this.begin = begin;
        this.demand = demand;
    }

    public int getId() {
        return id;
    }

    public int getBegin() {
        return begin;
    }

    public int getDemand() {
        return demand;
    }
}
