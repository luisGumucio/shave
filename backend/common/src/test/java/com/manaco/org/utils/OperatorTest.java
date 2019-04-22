/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lucho
 */
public class OperatorTest {
    
    @Test
    public void ufvOperatorTest() {
        BigDecimal actualUfv = new BigDecimal(1.695541);
        BigDecimal beforeUfv = new BigDecimal(1.694562);
        BigDecimal expected = new BigDecimal(1.000577);
        Operator operator = new Operator();
        BigDecimal current = operator.calculateUfv(actualUfv, beforeUfv);
        assertTrue(expected.setScale(6, RoundingMode.DOWN).compareTo(current.setScale(6, RoundingMode.DOWN)) == 0);
    }
}
