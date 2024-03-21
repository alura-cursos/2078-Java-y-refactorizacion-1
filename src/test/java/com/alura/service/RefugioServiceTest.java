package com.alura.service;

import com.alura.client.ClientHttpConfiguration;
import com.alura.domain.Refugio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RefugioServiceTest {

    private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);
    private RefugioService refugioService = new RefugioService(client);
    private HttpResponse<String> response = mock(HttpResponse.class);
    private Refugio refugio = new Refugio("Test","61981880392", "refugio_alura@gmail.com");

    @Test
    public void debeVerificarCuandoHayRefugios() throws IOException, InterruptedException {
        refugio.setId(0L);
        String expectedRefugioRegistrado = "Refugios registrados:";
        String expectedIdYNombre = "0 - Test";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.body()).thenReturn("[{"+refugio.toString()+"}]");
        when(client.dispararRequestGet(anyString())).thenReturn(response);

        refugioService.listarRefugios();

        String[] lines = baos.toString().split(System.lineSeparator());
        String actualRefugiosRegistrados = lines[0];
        String actualIdYNombre = lines[1];

        Assertions.assertEquals(expectedRefugioRegistrado, actualRefugiosRegistrados);
        Assertions.assertEquals(expectedIdYNombre, actualIdYNombre);
    }

    @Test
    public void debeVerificarCuandoNoHayRefugios() throws IOException, InterruptedException {
        String expected = "No hay refugios registrados";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.body()).thenReturn("[]");
        when(client.dispararRequestGet(anyString())).thenReturn(response);

        refugioService.listarRefugios();

        String[] lines = baos.toString().split(System.lineSeparator());
        String actual = lines[0];

        Assertions.assertEquals(expected, actual);
    }
}
