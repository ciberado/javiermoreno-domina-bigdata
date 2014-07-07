package com.javiermoreno.trends.domain;

import java.io.Serializable;

import backtype.storm.utils.MutableLong;

public class Frecuencia<T extends Comparable<T>> 
implements Comparable<Frecuencia<T>>, Serializable {
    private static final long serialVersionUID = 1L;

    private T valor;
    private MutableLong contador;
    
    public Frecuencia(T valor, long contador) {
        this.valor = valor;
        this.contador = new MutableLong(contador);
    }
    
    public Frecuencia(Frecuencia<T> prototipo) {
        this.valor = prototipo.valor;
        this.contador = new MutableLong(prototipo.contador.get());
    }

    public void incrementar() {
        contador.increment();
    }
    
    public void incrementar(long cantidad) {
        contador.increment(cantidad);
    }
    
    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    public MutableLong getContador() {
        return contador;
    }

    public void setContador(MutableLong contador) {
        this.contador = contador;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
        Frecuencia<T> other = (Frecuencia<T>) obj;
        if (valor == null) {
            if (other.valor != null)
                return false;
        } else if (!valor.equals(other.valor))
            return false;
        return true;
    }

    @Override
    public int compareTo(Frecuencia<T> other) {
        int res = (int) (this.contador.get() - other.contador.get());
        if (res == 0) {
            res = this.valor.compareTo(other.valor);
        } 

        return res;
    }

    @Override
    public String toString() {
        return valor + ":" + contador.get();
    }
    
    
    
    
    
}
