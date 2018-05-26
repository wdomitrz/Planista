package planista.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import planista.process.ProcessInformation;
import planista.strategies.*;
import planista.strategies.Strategy;

public class InputReader {

    private final Scanner scanner;
    private List<ProcessInformation> processes;
    private List<Strategy> strategies;

    private int lineNumber;

    public List<ProcessInformation> getProcesses() {
        // Sortuję dane na wypadek ich nieposortowania na wejściu
        Collections.sort(processes, new InputSortComparator());
        return processes;
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public InputReader(Scanner scanner) {
        this.scanner = scanner;
        prepare();
    }

    private int readIntFromLine() throws ErrorInLine {

        // Sprawdzenie istnienia kolejnej linii
        if (!scanner.hasNextLine()) {
            throw new ErrorInLine("Brak linii.", lineNumber);
        }

        // Wczytanie linii
        String line = scanner.nextLine();

        // Sprawdzenie poprawności wejścia w tej linii
        if (line.matches("^\\d+$")) {
            lineNumber++;

            // Zwrócenie wartości w tej linii
            return Integer.parseInt(line);
        } else {
            throw new ErrorInLine("Niepoprawny format - użyto niedozowlonych znaków, lub brak dancyh.", lineNumber);
        }
    }

    private PairOfInts readPairFromLine() throws ErrorInLine {

        // Sprawdzenie istnienia kolejnej linii
        if (!scanner.hasNextLine()) {
            throw new ErrorInLine("Brak linii.", lineNumber);
        }

        // Wczytanie linii
        String line = scanner.nextLine();

        // Sprawdzenie poprawności formatu
        if (line.matches("^\\d+\\s\\d+$")) {
            lineNumber++;

            // Wyznaczenie i zwrócenie wartości z tej linii
            String[] numbers = line.split(" ");
            return new PairOfInts(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
        } else {

            // Sprawdzenie na której liczbie jest błąd
            if (line.matches("^\\d+\\s?$")) {
                throw new ErrorInLine("Niepoprawny format - użyto niedozowlonych znaków, lub brak dancyh dla drugiej liczby.", lineNumber);
            } else {
                throw new ErrorInLine("Niepoprawny format - użyto niedozowlonych znaków, lub brak dancyh dla pierwszej liczby.", lineNumber);
            }
        }
    }

    private List<Integer> readArrayFromLine(int size) throws ErrorInLine {

        // Sprawdzenie istnienia kolejnej linii
        if (!scanner.hasNextLine()) {
            throw new ErrorInLine("Brak linii.", lineNumber);
        }

        // Wczytanie linii
        String line = scanner.nextLine();

        String pattern = "^[\\d+\\s]*\\d+$";

        // Sprawdzenie poprawności formatu
        if (line.matches(pattern)) {

            // Przekonwertowanie wejścia
            String[] numbers = line.split(" ");

            // Sprawdzenie zgodności liczby argumentów i wymaganej liczby argumentów
            if (numbers.length == size) {

                // Przygotowanie listy liczb
                List<Integer> outputList = new LinkedList<>();
                for (String number : numbers) {
                    outputList.add(Integer.parseInt(number));
                }
                lineNumber++;
                // Zwrócenie wartości z tej linii
                return outputList;
            } else {
                throw new ErrorInLine("Niepoprawna liczba argumentów.", lineNumber);
            }
        } else {
            throw new ErrorInLine("Niepoprawny format wejścia - znaki, które nie powinne się znaleźć na wejściu.", lineNumber);
        }
    }

    public void readFromInput() throws ErrorInLine {

        prepare();

        int numberOfProcesses;
        PairOfInts pairRepresentaionOfProcess;

        // Wczytanie liczby procesów
        numberOfProcesses = readIntFromLine();

        // Wczytanie każdego z procesów
        for (int i = 1; i <= numberOfProcesses; i++) {
            pairRepresentaionOfProcess = readPairFromLine();
            processes.add(new ProcessInformation(i, pairRepresentaionOfProcess.getFirst(), pairRepresentaionOfProcess.getSecond()));
        }

        int numberOfRRStrategies;

        // Wczytanie liczby strategii RR
        numberOfRRStrategies = readIntFromLine();

        if (numberOfRRStrategies == 0) {
            throw new ErrorInLine("\"Możemy założyc, że liczba strategii RR do wywołania >0 \"", lineNumber - 1);
        }

        // Wczytanie listy strategii RR
        List<Integer> parametrs = readArrayFromLine(numberOfRRStrategies);

        // Dodanie strategii RR do listy strategii
        parametrs.forEach((q) -> {
            strategies.add(new RR(q));
        });

        // Spawdzenie czy wczytywanie nie jest za długie
        if (scanner.hasNextLine()) {
            throw new ErrorInLine("Za dużo linii wejścia - każde wejście musi się kończyć znakiem EOF (ctrl+d)", lineNumber);
        }
    }

    private void prepare() {
        lineNumber = 1;
        processes = new LinkedList<>();
        strategies = new ArrayList<>();
        // Inicjalizacja listy strategii strategiami zawsze przez nią zawieranymi
        strategies.add(new FCFS());
        strategies.add(new SJF());
        strategies.add(new SRT());
        strategies.add(new PS());
    }
}
