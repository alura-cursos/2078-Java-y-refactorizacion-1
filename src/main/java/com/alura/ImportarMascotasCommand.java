package com.alura;

import com.alura.client.ClientHttpConfiguration;
import com.alura.service.MascotaService;

import java.io.IOException;

public class ImportarMascotasCommand implements Command {
    @Override
    public void execute() {
        try {
            ClientHttpConfiguration client = new ClientHttpConfiguration();
            MascotaService mascotaService = new MascotaService(client);

            mascotaService.importarMascotasEnElRefugio();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
