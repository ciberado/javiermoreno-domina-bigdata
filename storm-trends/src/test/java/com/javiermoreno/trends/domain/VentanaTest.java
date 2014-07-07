package com.javiermoreno.trends.domain;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class VentanaTest {

    public static final String TOKENS = "ABAA" + 
                                		"BCAC" + 
                                		"BBCC" + 
                                		"CABD" + 
                                		"ABBD" + 
                                		"BBBD" + 
                                		"BDCD" + 
                                		"DDDA" +
                                		"CDAA";
//  CALC:
// 
//  INTERVALO     LETRAS             CUENTA             VENTANA 5 
//                                A   B   C   D       A   B   C   D
//  ========= =============       =============       =============
//    1       A   B   A   A       3   1   0   0       3   1   0   0
//    2       B   C   A   C       1   1   2   0       4   2   2   0
//    3       B   B   C   C       0   2   2   0       4   4   4   0
//    4       C   A   B   D       1   1   1   1       5   5   5   1
//    5       A   B   B   D       1   2   0   1       6   7   5   2
//    6       B   B   B   D       0   3   0   1       3   9   5   3
//    7       B   D   C   D       0   1   1   2       2   9   4   5
//    8       D   D   D   A       1   0   0   3       3   7   2   8
//    9       C   D   A   A       2   0   1   1       4   6   2   8

    private static final String[] RESULTADOS_VENTANA = { 
        "3100", "4220", "4440", "5551", "6752", "3953", "2945", "3728", "4628"
    };
     
    @Test
    public void test() {
        Ventana<Character> ventana = new Ventana<>();
        for (int idx=0; idx < TOKENS.length(); idx++) {
            ventana.registrar(TOKENS.charAt(idx));
            if ((idx+1) % 4 == 0) {
                List<Frecuencia<Character>> top = ventana.obtenerTopFrecuenciasAndAvanzarTick();
                assertEquals(RESULTADOS_VENTANA[idx/4], listaTopToString(top));
            }
        }
    }
    
    
    private String listaTopToString(List<Frecuencia<Character>> top) {
        Map<Character, Long> tmp = new HashMap<>();
        for (char letra='A'; letra <= 'D'; letra++) {
            tmp.put(letra, 0L);
        }
        for (Frecuencia<Character> freq : top) {
            tmp.put(freq.getValor(), freq.getContador().get());
        }
        
        return "" + tmp.get('A') + tmp.get('B') + tmp.get('C') + tmp.get('D');
    }
     
}

