package com.natewilliford.rpncalculator;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * The view model for the RpnCalcFragment. This manages the business logic as well as the saved
 * state for the calculator.
 */
@HiltViewModel
public class RpnCalcFragmentViewModel extends ViewModel {

    private static final String TAG = "RpnCalcFragmentViewModel";
    private static final String KEY_CALCULATOR_STACK = "calculator-stack";
    private static final int STACK_LIMIT = 100;

    private final SavedStateHandle savedStateHandle;
    private final RpnCalculator rpnCalculator;
    private final MutableLiveData<Double> currentResult;
    private final MutableLiveData<Double> prevResult;

    @Inject
    public RpnCalcFragmentViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        Double[] savedStack = null;
        if (savedStateHandle.contains(KEY_CALCULATOR_STACK)) {
            Log.i(TAG, "Restoring from saved state.");
            savedStack = savedStateHandle.get(KEY_CALCULATOR_STACK);
        }

        rpnCalculator = RpnCalculator.Create(STACK_LIMIT, savedStack);
        currentResult = new MutableLiveData<>(rpnCalculator.getLast());
        prevResult = new MutableLiveData<>(rpnCalculator.getSecondLast());
    }

    /**
     * Enters a new value into the calculator.
     *
     * @throws NumberFormatException when number fails to parse as a double.
     * @throws IllegalStateException when the calculator stack is full.
     **/
    public void enterNumber(String numberString) throws NumberFormatException,
            IllegalStateException {
        // Bubble up exceptions so the UI can handle errors.
        double value = Double.parseDouble(numberString);
        rpnCalculator.addOperand(value);
        updateResults();
    }

    /**
     * Submits an operation to be done on previously entered numbers.
     *
     * @throws IllegalStateException when there aren't enough operands.
     **/
    public void submitOperator(RpnCalculator.Operator operator) throws IllegalStateException {
        rpnCalculator.submitOperator(operator);
        updateResults();
    }

    /**
     * Returns live data that can be observed. This is updated whenever a new result is calculated
     * via {@link #enterNumber(String)} or {@link #submitOperator(RpnCalculator.Operator)}.
     **/
    public LiveData<Double> getCurrentResult() { return currentResult; }

    public LiveData<Double> getPrevResult() { return prevResult; }

    public void deleteLast() {
        rpnCalculator.deleteLast();
        updateResults();
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
