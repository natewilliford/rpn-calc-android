package com.natewilliford.rpncalculator;

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

    private SavedStateHandle savedStateHandle;

    @Inject
    public RpnCalcFragmentViewModel(SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    public String getAThing() {
        return "a thing";
    }
}
