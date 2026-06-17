package util;

import java.util.Scanner;

public class ConsoleUtil {

    private ConsoleUtil() {
        // Construtor privado para evitar instanciação de classe utilitária
    }

    public static int lerInteiro(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
            }
        }
    }

    public static int lerInteiroNoIntervalo(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            int valor = lerInteiro(scanner, prompt);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.printf("Valor fora do intervalo. Deve estar entre %d e %d.%n", min, max);
        }
    }

    public static String lerString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Entrada inválida. O campo não pode ficar vazio.");
        }
    }
}
