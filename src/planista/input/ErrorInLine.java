package planista.input;

public class ErrorInLine extends Exception {

    final private String additionalInformation;
    final private int errorLine;

    public ErrorInLine(String additionalInformation, int errorLine) {
        this.additionalInformation = additionalInformation;
        this.errorLine = errorLine;
    }

    @Override
    public String toString() {
        return "Błąd w wierszu " + errorLine + " : " + additionalInformation;
    }
}
