package com.javiermoreno.stormdrpc;

import org.junit.Assert;
import org.junit.Test;

public class PIMonteCarloTest {

    @Test
    public void test() {
        double pi = PIMonteCarlo.calcularPI(100000000);
        
        Assert.assertEquals(Math.PI, pi, 0.001);
    }

}
