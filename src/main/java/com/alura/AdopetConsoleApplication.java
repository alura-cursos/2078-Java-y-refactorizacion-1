package com.alura;

import java.util.Scanner;

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor();
        System.out.println("##### BIENVENIDOS AL SISTEMA ADOPET-CONSOLE #####");
        try {
            int opcionElegida = 0;
            while (opcionElegida != 5) {
                mostrarMenu();

                String textoEscrito = new Scanner(System.in).nextLine();
                opcionElegida = Integer.parseInt(textoEscrito);

                switch (opcionElegida) {
                    case 1 -> executor.executeCommand(new ListarRefugiosCommand());
                    case 2 -> executor.executeCommand(new RegistrarRefugioCommand());
                    case 3 -> executor.executeCommand(new ListarMascotasCommand());
                    case 4 -> executor.executeCommand(new ImportarMascotasCommand());
                    case 5 -> finalizarPrograma();
                    default -> opcionElegida = opcionInvalida();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mostrarMenu() {
        System.out.println("\nESCRIBA EL NÚMERO DE LA OPERACIÓN DESEADA:");
        System.out.println("1 -> Listar refugios registrados");
        System.out.println("2 -> Registrar nuevo refugio");
        System.out.println("3 -> Listar mascotas del refugio");
        System.out.println("4 -> Importar mascotas del refugio");
        System.out.println("5 -> Salir");
    }

    private static int opcionInvalida() {
        System.out.println("NÚMERO INVÁLIDO!");
        return 0;
    }

    private static void finalizarPrograma() {
        System.out.println("Finalizando el programa...");
        System.exit(0);
    }
}
