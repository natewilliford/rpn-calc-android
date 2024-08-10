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

    private final MutableLiveData<Double> calcResult;

    @Inject
    public RpnCalcFragmentViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
        rpnCalculator = new RpnCalculator();
        calcResult = new MutableLiveData<>(0.0);
    }

    /** Enters a new value into the calculator. **/
    public void enterNumber(String numberString) {
        // TODO: Handle errors.
        double value = Double.parseDouble(numberString);
        rpnCalculator.addOperand(value);
        updateLastValue();
    }

    /** Submits an operation to be done on previously entered numbers. **/
    public void submitOperator(RpnCalculator.Operator operator) {
        rpnCalculator.submitOperator(operator);
        updateLastValue();
    }

    /**
     * Returns live data that can be observed. This is updated whenever a new result is calculated
     * via {@link #enterNumber(String)} or {@link #submitOperator(RpnCalculator.Operator)}.
     **/
    public LiveData<Double> getCalcResult() {
        return calcResult;
    }

    private void updateLastValue() {
        calcResult.setValue(rpnCalculator.getLast());
    }
}
