package com.javiermoreno.trends.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Intervalo<T extends Comparable<T>> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private int tick;
    private Map<T, Frecuencia<T>> frecuencias = new HashMap<>();
    
    public Intervalo(int tick) {
        this.tick = tick;
    }

    public void registrar(T valor) {
        Frecuencia<T> frecuencia = frecuencias.get(valor);
        if (frecuencia == null) {
            frecuencia = new Frecuencia<T>(valor, 0);
            frecuencias.put(valor, frecuencia);
        }
        frecuencia.incrementar();
    }

    public long getContadorParaValor(T valor) {
        Frecuencia<T> frecuencia = frecuencias.get(valor);
        return (frecuencia == null) ? 0 : frecuencia.getContador().get();
    }
    
    public Map<T, Frecuencia<T>> getFrecuencias() {
        return frecuencias;
    }
    
    public void setFrecuencias(Map<T, Frecuencia<T>> frecuencias) {
        this.frecuencias = frecuencias;
    }
    
    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
    
    public boolean isAnteriorOrIgual(Intervalo<T> otro) {
        return tick <= otro.tick;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (tick ^ (tick >>> 32));
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
        @SuppressWarnings("unchecked")
        Intervalo<T> other = (Intervalo<T>) obj;
        if (tick != other.tick)
            return false;
        return true;
    }
    
    
    
}
