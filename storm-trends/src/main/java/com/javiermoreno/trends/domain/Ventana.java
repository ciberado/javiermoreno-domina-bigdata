package com.javiermoreno.trends.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

public class Ventana<T extends Comparable<T>> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final int NUMERO_INTERVALOS = 5;    // Intervalos de un minuto  

    private LinkedList<Intervalo<T>> intervalos = new LinkedList<>();
    
    private int tickActual;
        
    public Ventana() {
    }
    
    private void descartarIntervalosAntiguos() {
        int tickMinimo = tickActual - NUMERO_INTERVALOS + 1;
        // Borramos los intervalos que haya quedado fuera de la ventana
        while ((intervalos.size() > 0) && (intervalos.getFirst().getTick() < tickMinimo)) {
            intervalos.poll();
        }        
    }
    
    private void asegurarIntervalo() {
        // Añade intervalos hasta que tengamos el que pueda incluir el elemento actual
        if ((intervalos.size() == 0) || (intervalos.getLast().getTick() < tickActual)) {
            Intervalo<T> nuevoIntervalo = new Intervalo<>(tickActual);
            intervalos.addLast(nuevoIntervalo);
        }
    }
    
    public void registrar(T valor) {
        // Agrega el intervalo actual si es necesario
        asegurarIntervalo();
        Intervalo<T> intervaloActual = intervalos.getLast();
        intervaloActual.registrar(valor);
    }
    
    public List<Frecuencia<T>> obtenerTopFrecuenciasAndAvanzarTick() {       
        SumaFrecuenciasVentana<T> suma = 
                new SumaFrecuenciasVentana<>(tickActual - NUMERO_INTERVALOS, tickActual, intervalos);
        List<Frecuencia<T>> top = suma.getTop();
        tickActual = tickActual + 1;
        // Elimina los intervalos que hayan quedado fuera de la ventana
        descartarIntervalosAntiguos();
        return top;
    }
    
    public void mezclar(Ventana<T> otra) {
        // No queremos modificar la ventana que nos han pasado como parámetro
        LinkedList<Intervalo<T>> otraIntervalos = new LinkedList<>(otra.intervalos);
        // Acumulamos aquí los intervalos antes de sustituir los actuales
        LinkedList<Intervalo<T>> intervalosMezclados = new LinkedList<>();
        
        while (this.intervalos.isEmpty() == false || otra.intervalos.isEmpty() == false) {
            Intervalo<T> thisFirst = this.intervalos.isEmpty() ? null : this.intervalos.peek();
            Intervalo<T> otraFirst = otraIntervalos.isEmpty() ? null : otraIntervalos.peek();
            if ((thisFirst != null && otraFirst == null) || (thisFirst.isAnteriorOrIgual(otraFirst) == true)) {
                intervalosMezclados.add(thisFirst);
                this.intervalos.removeFirst();
            } else if (otraFirst != null) {
                intervalosMezclados.add(otraFirst); 
                otraIntervalos.removeFirst();
            }
        }
        
        this.intervalos = intervalosMezclados;
        this.tickActual = max(this.tickActual, otra.tickActual);
        this.descartarIntervalosAntiguos();
    }

    public LinkedList<Intervalo<T>> getIntervalos() {
        return intervalos;
    }

    public void setIntervalos(LinkedList<Intervalo<T>> intervalos) {
        this.intervalos = intervalos;
    }

    public int getTickActual() {
        return tickActual;
    }

    public void setTickActual(int tickActual) {
        this.tickActual = tickActual;
    }
    
    
    
}
