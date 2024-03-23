package com.alura;

import com.alura.client.ClientHttpConfiguration;
import com.alura.service.RefugioService;

import java.io.IOException;

public class ListarRefugiosCommand implements Command {
    @Override
    public void execute() {
        try{
            ClientHttpConfiguration client = new ClientHttpConfiguration();
            RefugioService refugioService = new RefugioService(client);

            refugioService.listarRefugios();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
