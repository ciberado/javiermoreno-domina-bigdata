package com.javiermoreno.stormdrpc;

import java.util.Random;


/**
 * Calcula PI mediante simulación usando un Monte Carlo.
 * 
 * Si tenemos un círculo de radio RADIO inscrito en un cuadrado de lado 2*RADIO (el diámetro)
 * el área del círculo será PI*RADIO*RADIO y el área del cuadrado será RADIO*2*RADIO*2.
 * 
 * (PI*RADIO*RADIO) / (4*RADIO*RADIO) -> PI / 4 es el tamaño relativo del círculo respecto
 * al cuadrado y por lo tanto la probabilidad de que un punto caiga dentro del círculo.
 * Si se repite la tirada N veces, aproximádamente N*PI/4 puntos caerán dentro. Llamemos
 * a esta cantidad de puntos M.
 * 
 * Es posible saber si el punto está dentro del círuclo porque x*x + y*y < RADIO*RADIO 
 * tomando como 0,0 el centro del círculo.
 * 
 * Estadísticamente PI será 4*M / N.
 * 
 */
public class PIMonteCarlo {

    private static final double RADIO = 10000.0;             
    
    private static final Random rnd = new Random();
    
    public static double calcularPI(int iteraciones) {
        
        int m = 0;
        for (int n=0; n < iteraciones; n++) {
            double x = rnd.nextDouble() * RADIO * 2 - RADIO;
            double y = rnd.nextDouble() * RADIO * 2 - RADIO;
            if (x*x + y*y <= RADIO*RADIO) {
                m = m + 1;
            }
        }
        return 4.0 * m / iteraciones;
    }
    
    
    
    
}








