package com.javiermoreno.trends.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class FrecuenciaTest {


    @Test
    public void incrementoUnitario() {
        Frecuencia<String> f = new Frecuencia<>("x", 0);
        f.incrementar();
        assertEquals(1, f.getContador().get());
    }
    
    @Test
    public void incrementoRango() {
        Frecuencia<String> f = new Frecuencia<>("x", 5);
        f.incrementar(5);
        assertEquals(10, f.getContador().get());
    }

}
