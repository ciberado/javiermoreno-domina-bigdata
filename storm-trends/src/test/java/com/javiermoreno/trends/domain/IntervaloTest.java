package com.javiermoreno.trends.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervaloTest {

    public static final String TOKENS = "ABAABCACBBCCCABDABBD";
    
    @Test
    public void consultarExistentes() {
        Intervalo<Character> intervalo = new Intervalo<>(0);
        for (int idx=0; idx < TOKENS.length(); idx++) {
            intervalo.registrar(TOKENS.charAt(idx));
        }
        assertEquals("A", intervalo.getContadorParaValor('A'), contarLetras(TOKENS, 'A'));
        assertEquals("B", intervalo.getContadorParaValor('B'), contarLetras(TOKENS, 'B'));
        assertEquals("C", intervalo.getContadorParaValor('C'), contarLetras(TOKENS, 'C'));
        assertEquals("D", intervalo.getContadorParaValor('D'), contarLetras(TOKENS, 'D'));
    }

    @Test
    public void consultarInexistentes() {
        Intervalo<Character> intervalo = new Intervalo<>(0);
        for (int idx=0; idx < TOKENS.length(); idx++) {
            intervalo.registrar(TOKENS.charAt(idx));
        }
        assertEquals("X", intervalo.getContadorParaValor('X'), 0);
    }

    
    
    
    private int contarLetras(String frase, char letra) {
        int cuenta = 0;
        for (int idx=0; idx < frase.length(); idx++) {
            if (frase.charAt(idx) == letra) {
                cuenta = cuenta + 1;
            }
        }
        return cuenta;
    }
}
