package com.natewilliford.rpncalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * The view model for the RpnCalcFragment. This manages the business logic as well as the saved
 * state for the calculator.
 */
@HiltViewModel
public class RpnCalcFragmentViewModel extends ViewModel {

    private static final String KEY_CALCULATOR_STACK = "calculator-stack";
    private static final int STACK_LIMIT = 100;

    private final SavedStateHandle savedStateHandle;
    private final RpnCalculator rpnCalculator;
    private final MutableLiveData<Double> currentResult;
    private final MutableLiveData<Double> prevResult;

    @Inject
    public RpnCalcFragmentViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        if (savedStateHandle.contains(KEY_CALCULATOR_STACK)) {
            Double[] stateStack = savedStateHandle.get(KEY_CALCULATOR_STACK);
            rpnCalculator = RpnCalculator.Create(STACK_LIMIT, Arrays.asList(stateStack));
        } else {
            rpnCalculator = RpnCalculator.Create(STACK_LIMIT);
        }
        currentResult = new MutableLiveData<>(0.0);
        prevResult = new MutableLiveData<>(0.0);
    }

    /**
     * Enters a new value into the calculator.
     *
     * @throws IllegalArgumentException when number fails to parse as a double.
     **/
    public void enterNumber(String numberString) throws IllegalArgumentException {
        try {
            double value = Double.parseDouble(numberString);
            rpnCalculator.addOperand(value);
            updateResults();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a valid number.", e);
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException("Calculator stack is full.", e);
        }
    }

    /** Submits an operation to be done on previously entered numbers. **/
    public void submitOperator(RpnCalculator.Operator operator) {
        rpnCalculator.submitOperator(operator);
        updateResults();
    }

    /**
     * Returns live data that can be observed. This is updated whenever a new result is calculated
     * via {@link #enterNumber(String)} or {@link #submitOperator(RpnCalculator.Operator)}.
     **/
    public LiveData<Double> getCurrentResult() {
        return currentResult;
    }
    public LiveData<Double> getPrevResult() {
        return prevResult;
    }

    public void clearCalculator() {
        rpnCalculator.clear();
        updateResults();
    }

    private void updateResults() {
        savedStateHandle.set(KEY_CALCULATOR_STACK, rpnCalculator.getStack().toArray(new Double[0]));

        currentResult.setValue(rpnCalculator.getLast());
        prevResult.setValue(rpnCalculator.getSecondLast());
    }
}
