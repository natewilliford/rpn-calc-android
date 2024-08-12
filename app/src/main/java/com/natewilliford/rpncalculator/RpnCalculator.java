package com.natewilliford.rpncalculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * A Reverse Polish notation calculator. This class manages the operations and state of an RPN
 * calculator where an operator is entered after several operands have been entered. This means a
 * stack structure is used to save all previous operands and solutions.
 *
 * A new instance should be created per use as state is stored. This class is NOT thread safe.
 *
 * @link <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">Wikipedia</a>
 */
public class RpnCalculator {

    private static final String TAG = "RpnCalculator";

    /** Supported operators to submit to previously added operands. */
    public enum Operator {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE
    }

    private final Stack<Double> operandsStack = new Stack<>();

    public RpnCalculator() {}

    /**
     * Adds an operand (a decimal number) to the stack.
     *
     * @param operand The operand to add.
     */
    public void addOperand(double operand) {
        operandsStack.push(operand);
    }

    /**
     * Submits an operator to do a calculation. Takes the last two operands in the stack and applies
     * the operation to them, then adds the result back to the stack.
     *
     * @param operator The operator to apply.
     * @return The result of the operation.
     */
    public double submitOperator(Operator operator) {
        if (operandsStack.size() < 2) {
            // TODO: Add better error handling.
            Log.e(TAG, "Not enough operands");
            return 0;
        }

        double last = operandsStack.pop();
        double secondLast = operandsStack.pop();
        double result = 0;

        switch (operator) {
            case PLUS:
                result = secondLast + last;
                break;
            case MINUS:
                result = secondLast - last;
                break;
            case MULTIPLY:
                result = secondLast * last;
                break;
            case DIVIDE:
                result = secondLast / last;
                break;
            default:
                Log.e(TAG, "Invalid operator " + operator);
        }

        operandsStack.push(result);
        return result;
    }

    /** Returns the internal stack state as a list copy. */
    public List<Double> getStack() {
        return new ArrayList<>(operandsStack);
    }

    /**
     * Gets the last item in the stack. This could be the most recently added operand or the result
     * of the last operation.
     */
    public double getLast() {
        if (operandsStack.isEmpty()) {
            return 0;
        }
        return operandsStack.peek();
    }

    /**
     * Gets the second to last item in the stack. This is a convenience method as alternative to
     * getting the whole stack.
     */
    public double getSecondLast() {
        if (operandsStack.size() >= 2) {
            return operandsStack.get(operandsStack.size() - 2);
        }
        return 0;
    }
}
