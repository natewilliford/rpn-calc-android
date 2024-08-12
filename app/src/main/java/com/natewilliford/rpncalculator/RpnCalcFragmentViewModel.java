package com.natewilliford.rpncalculator;

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

    private final SavedStateHandle savedStateHandle;
    private final RpnCalculator rpnCalculator;

    private final MutableLiveData<Double> currentResult;
    private final MutableLiveData<Double> prevResult;

    @Inject
    public RpnCalcFragmentViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        rpnCalculator = new RpnCalculator();
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
            throw new IllegalArgumentException("Not a valid number.");
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

    private void updateResults() {
        currentResult.setValue(rpnCalculator.getLast());
        prevResult.setValue(rpnCalculator.getSecondLast());
    }
}
