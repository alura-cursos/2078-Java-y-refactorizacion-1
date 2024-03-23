package com.alura;

import com.alura.client.ClientHttpConfiguration;
import com.alura.service.MascotaService;
import com.alura.service.RefugioService;
import java.util.Scanner;

public class AdopetConsoleApplication {

    public static void main(String[] args) {
        CommandExecutor executor = new CommandExecutor();
        ClientHttpConfiguration client = new ClientHttpConfiguration();
        MascotaService mascotaService = new MascotaService(client);
        System.out.println("##### BIENVENIDOS AL SISTEMA ADOPET-CONSOLE #####");
        try {
            int opcionElegida = 0;
            while (opcionElegida != 5) {
                System.out.println("\nESCRIBA EL NÚMERO DE LA OPERACIÓN DESEADA:");
                System.out.println("1 -> Listar refugios registrados");
                System.out.println("2 -> Registrar nuevo refugio");
                System.out.println("3 -> Listar mascotas del refugio");
                System.out.println("4 -> Importar mascotas del refugio");
                System.out.println("5 -> Salir");

                String textoEscrito = new Scanner(System.in).nextLine();
                opcionElegida = Integer.parseInt(textoEscrito);

                if (opcionElegida == 1) {
                    executor.executeCommand(new ListarRefugiosCommand());
                } else if (opcionElegida == 2) {
                    executor.executeCommand(new RegistrarRefugioCommand());
                } else if (opcionElegida == 3) {
                    mascotaService.listarMascotasDelRefugio();
                } else if (opcionElegida == 4) {
                    mascotaService.importarMascotasEnElRefugio();
                } else if (opcionElegida == 5) {
                    break;
                } else {
                    System.out.println("NÚMERO INVÁLIDO!");
                    opcionElegida = 0;
                }
            }
            System.out.println("Finalizando el programa...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
