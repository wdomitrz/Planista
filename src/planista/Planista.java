package planista;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import planista.input.InputReader;
import planista.input.ErrorInLine;
import planista.process.ProcessInformation;
import planista.strategies.Strategy;

public class Planista {

    public static void main(String[] args) {
        InputStream stream = null;

        // Sprawdzenie obecności pliku z danymi
        if (args.length >= 1) {
            // Próba otwarcia pliku z danymi
            try {
                stream = new FileInputStream(args[0]);
            } catch (FileNotFoundException | SecurityException fileReadException) {
                System.out.println("Plik z danymi nie jest dostępny.");
                System.exit(1);
            }
        } else {
            // W przypadku braku pliku wejściowego ustawiam strumień wejściowy na standardowe wejście
            stream = System.in;
        }

        // Stworzenie "czytacza" wejścia
        InputReader input = new InputReader(new Scanner(stream));

        // Próba odczytania wejścia
        try {
            input.readFromInput();
        } catch (ErrorInLine readingException) {
            System.out.println(readingException);
            // Kończymy z kodem 1, żeby zasygnalizować nieudane wykonanie
            System.exit(1);
        }

        // Pobranie odczytanych procesów
        List<ProcessInformation> listOfProcesses = input.getProcesses();
        // Pobranie odczytanych strategii
        List<Strategy> listOfStrategies = input.getStrategies();

        // Przetestowanie każdej ze strategii i wypisanie wyniku jej symulacji
        listOfStrategies.forEach((Strategy strategy) -> {
            strategy.simulate(listOfProcesses);
            System.out.println(strategy.getResultsOfLastSimulation());
        });

    }

}
