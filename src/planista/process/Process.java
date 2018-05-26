package planista.process;


/*
 * Klasa procesów, które są obsługiwane w symulacji. 
 * Zawiera dodatkowe informacje o wykonaniu.
 */
public class Process {

    // Stałe informacje o procesie (dane na wejściu)
    private final ProcessInformation processInfo;

    // Ostatnie uaktualnienie wiadomości o procesie
    private double lastUpdate;

    // Pozostałe zapotrzebowanie
    private double demandLeft;

    // Czas realizacji do aktualnego momentu
    private double realizationTime;

    // Czas oczekiwania do aktualnego momentu
    private double latencyTime;

    // Czas pracy w aktualnym cyklu pracy (używane w RR)
    private double currentWorkTime;

    // Ustawienei danych wejściowych na podstawie processInfo
    public Process(ProcessInformation processInfo) {
        this.processInfo = processInfo;
        this.lastUpdate = processInfo.getBegin();
        this.demandLeft = processInfo.getDemand();
        this.realizationTime = 0.;
        this.latencyTime = 0.;
        this.currentWorkTime = 0.;
    }

    public ProcessInformation getProcessInfo() {
        return processInfo;
    }

    public int getId() {
        return processInfo.getId();
    }

    public double getLastUpdate() {
        return lastUpdate;
    }

    public int getBegin() {
        return processInfo.getBegin();
    }

    public int getDemand() {
        return processInfo.getDemand();
    }

    public double getDemandLeft() {
        return demandLeft;
    }

    public double getRealizationTime() {
        return realizationTime;
    }

    public double getLatencyTime() {
        return latencyTime;
    }

    public void workedFor(double workingTime, double efficiency) {
        realizationTime += workingTime;
        demandLeft -= workingTime * efficiency;
        currentWorkTime += workingTime;
        setLastUpdate(getLastUpdate() + workingTime);
    }

    public void setCurrentWorkTime(double currentWorkTime) {
        this.currentWorkTime = currentWorkTime;
    }

    public double getCurrentWorkTime() {
        return currentWorkTime;
    }

    public void increaseLatencyTime(double latencyTime) {
        this.latencyTime += latencyTime;
        setLastUpdate(getLastUpdate() + latencyTime);
    }

    private void setLastUpdate(double lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
