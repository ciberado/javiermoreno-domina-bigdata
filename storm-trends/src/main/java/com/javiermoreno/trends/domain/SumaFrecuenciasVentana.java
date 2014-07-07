package com.javiermoreno.trends.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.Interval;


public class SumaFrecuenciasVentana<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int TOP_SLOTS = 5;
    
    private int tickInicial;
    private int tickFinal;
    
    private Map<T, Frecuencia<T>> frecuencias = new HashMap<>();
    private List<Frecuencia<T>> top = new ArrayList<>();
    
    public SumaFrecuenciasVentana(int tickInicial, int tickFinal, List<Intervalo<T>> intervalos) {
        super();
        this.tickInicial = tickInicial;
        this.tickFinal = tickFinal;
        for (Intervalo<T> intervaloActual : intervalos) {
            acumularIntervalo(intervaloActual);
            calcularTop();
        }
    }

    private void acumularIntervalo(Intervalo<T> intervalo) 
    {
        if (intervalo.getTick() >= tickInicial && intervalo.getTick() <= tickFinal) {
            for (T key : intervalo.getFrecuencias().keySet()) {
                Frecuencia<T> frecuenciaVentana = frecuencias.get(key);
                if (frecuenciaVentana == null) {
                    frecuenciaVentana = new Frecuencia<>(key, 0);
                    frecuencias.put(key, frecuenciaVentana);
                }
                long incremento = intervalo.getFrecuencias().get(key).getContador().get();
                frecuenciaVentana.incrementar(incremento);
            }
        }
    }
    
    private void calcularTop() {
        List<Frecuencia<T>> lista = new ArrayList<>(frecuencias.values());
        Collections.sort(lista);
        Collections.reverse(lista);
        top = lista.subList(0, lista.size() < TOP_SLOTS ? lista.size() : TOP_SLOTS);
    }
    
    public int getTickInicial() {
        return tickInicial;
    }

    public void setTickInicial(int tickInicial) {
        this.tickInicial = tickInicial;
    }

    public int getTickFinal() {
        return tickFinal;
    }

    public void setTickFinal(int tickFinal) {
        this.tickFinal = tickFinal;
    }

    public Map<T, Frecuencia<T>> getFrecuencias() {
        return frecuencias;
    }

    public void setFrecuencias(Map<T, Frecuencia<T>> frecuencias) {
        this.frecuencias = frecuencias;
    }

    public List<Frecuencia<T>> getTop() {
        return top;
    }

    public void setTop(List<Frecuencia<T>> top) {
        this.top = top;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tickFinal;
        result = prime * result + tickInicial;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SumaFrecuenciasVentana other = (SumaFrecuenciasVentana) obj;
        if (tickFinal != other.tickFinal)
            return false;
        if (tickInicial != other.tickInicial)
            return false;
        return true;
    }

    
    
}
