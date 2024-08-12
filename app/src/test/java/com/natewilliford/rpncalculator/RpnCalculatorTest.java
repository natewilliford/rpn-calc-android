package com.natewilliford.rpncalculator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RpnCalculatorTest {
    private static final double DECIMAL_DELTA = 0.00001;

    private RpnCalculator calc;

    @Before
    public void setUp() throws Exception {
        calc = new RpnCalculator();
    }

    @Test
    public void addOperand_addsToStack() {
        final List<Double> vals = Arrays.asList(3.2, 2.1, 4.2, 10.3);
        calc.addOperand(vals.get(0));
        calc.addOperand(vals.get(1));
        calc.addOperand(vals.get(2));
        calc.addOperand(vals.get(3));

        assertArrayEquals(vals.toArray(), calc.getStack().toArray());
    }

    @Test
    public void submitOperator_plus() {
        calc.addOperand(3);
        calc.addOperand(4);

        assertEquals(7, calc.submitOperator(RpnCalculator.Operator.PLUS), DECIMAL_DELTA);
        assertEquals(7, calc.getLast(), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_minus() {
        calc.addOperand(9);
        calc.addOperand(4);

        assertEquals(5, calc.submitOperator(RpnCalculator.Operator.MINUS), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_multiply() {
        calc.addOperand(3);
        calc.addOperand(4);

        assertEquals(12, calc.submitOperator(RpnCalculator.Operator.MULTIPLY), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_divide() {
        calc.addOperand(12);
        calc.addOperand(4);

        assertEquals(3, calc.submitOperator(RpnCalculator.Operator.DIVIDE), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_popsAndPushesToStack() {
        calc.addOperand(8);
        calc.addOperand(3);
        calc.addOperand(4);

        assertEquals(3, calc.getStack().size());

        calc.submitOperator(RpnCalculator.Operator.PLUS);

        List<Double> stack = calc.getStack();
        assertEquals(2, stack.size(), DECIMAL_DELTA);
        assertEquals(8, stack.get(0), DECIMAL_DELTA);
        assertEquals(7, stack.get(1), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_example2() {
        calc.addOperand(5);
        calc.addOperand(5);
        calc.addOperand(5);
        calc.addOperand(8);
        calc.submitOperator(RpnCalculator.Operator.PLUS);
        calc.submitOperator(RpnCalculator.Operator.PLUS);
        calc.submitOperator(RpnCalculator.Operator.MINUS);

        assertEquals(-13, calc.getLast(), DECIMAL_DELTA);

        calc.addOperand(13);
        calc.submitOperator(RpnCalculator.Operator.PLUS);
        assertEquals(0, calc.getLast(), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_example3() {
        calc.addOperand(-3);
        assertEquals(-3, calc.getLast(), DECIMAL_DELTA);
        calc.addOperand(-2);
        assertEquals(-2, calc.getLast(), DECIMAL_DELTA);
        calc.submitOperator(RpnCalculator.Operator.MULTIPLY);
        assertEquals(6, calc.getLast(), DECIMAL_DELTA);
        calc.addOperand(5);
        assertEquals(5, calc.getLast(), DECIMAL_DELTA);
        calc.submitOperator(RpnCalculator.Operator.PLUS);
        assertEquals(11, calc.getLast(), DECIMAL_DELTA);
    }

    @Test
    public void submitOperator_example4() {
        calc.addOperand(5);
        assertEquals(5, calc.getLast(), DECIMAL_DELTA);
        calc.addOperand(9);
        assertEquals(9, calc.getLast(), DECIMAL_DELTA);
        calc.addOperand(1);
        assertEquals(1, calc.getLast(), DECIMAL_DELTA);
        calc.submitOperator(RpnCalculator.Operator.MINUS);
        assertEquals(8, calc.getLast(), DECIMAL_DELTA);
        calc.submitOperator(RpnCalculator.Operator.DIVIDE);
        assertEquals(0.625, calc.getLast(), DECIMAL_DELTA);
    }

    @Test
    public void getLast_getsLast() {
        calc.addOperand(3);
        calc.addOperand(4);
        assertEquals(4, calc.getLast(), DECIMAL_DELTA);

        calc.submitOperator(RpnCalculator.Operator.PLUS);
        assertEquals(7, calc.getLast(), DECIMAL_DELTA);
    }

    @Test
    public void getLast_emptyStack() {
        // Don't add any operands.
        assertEquals(0, calc.getLast(), DECIMAL_DELTA);
    }

    @Test
    public void getSecondLast_getsValue() {
        calc.addOperand(3);
        calc.addOperand(4);
        assertEquals(3, calc.getSecondLast(), DECIMAL_DELTA);

        calc.addOperand(8);
        assertEquals(4, calc.getSecondLast(), DECIMAL_DELTA);

        calc.submitOperator(RpnCalculator.Operator.PLUS);
        assertEquals(3, calc.getSecondLast(), DECIMAL_DELTA);
    }

    @Test
    public void getSecondLast_noSecondLast() {
        // Don't add any operands.
        assertEquals(0, calc.getSecondLast(), DECIMAL_DELTA);

        // Add a value, but there still isn't a second to last.
        calc.addOperand(3);
        assertEquals(0, calc.getSecondLast(), DECIMAL_DELTA);
    }

    @Test
    public void clear_resets() {
        calc.addOperand(3);
        calc.addOperand(4);
        assertFalse(calc.getStack().isEmpty());

        calc.clear();
        assertTrue(calc.getStack().isEmpty());
    }
}
