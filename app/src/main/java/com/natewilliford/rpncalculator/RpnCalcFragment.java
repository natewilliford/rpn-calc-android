package com.natewilliford.rpncalculator;

import static com.natewilliford.rpncalculator.RpnCalculator.Operator;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.natewilliford.rpncalculator.databinding.RpnCalcFragmentBinding;

import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

/** The fragment that holds all the calculator interaction. */
@AndroidEntryPoint
public class RpnCalcFragment extends Fragment {

    private static final String TAG = "RpnCalcFragment";
    @Nullable private RpnCalcFragmentBinding binding;
    @Nullable private RpnCalcFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RpnCalcFragmentBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(RpnCalcFragmentViewModel.class);
        viewModel.getCurrentResult().observe(getViewLifecycleOwner(), result -> binding
                .currentValueText.setText(String.format(Locale.ENGLISH,"%.2f", result)));
        viewModel.getPrevResult().observe(getViewLifecycleOwner(), prevResult -> binding
                .prevValueText.setText(String.format(Locale.ENGLISH, "%.2f", prevResult)));
        binding.enterButton.setOnClickListener(v -> handleEnterButton());
        binding.plusButton.setOnClickListener(v -> viewModel.submitOperator(Operator.PLUS));
        binding.minusButton.setOnClickListener(v -> viewModel.submitOperator(Operator.MINUS));
        binding.multiplyButton.setOnClickListener(v -> viewModel.submitOperator(Operator.MULTIPLY));
        binding.divideButton.setOnClickListener(v -> viewModel.submitOperator(Operator.DIVIDE));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void handleEnterButton() {
        if (binding == null || viewModel == null) {
            return;
        }

        EditText input = binding.valueInput;
        if (input.getText().toString().isEmpty()) {
            Log.d(TAG, "Skipping input of empty string.");
            return;
        }

        try {
            viewModel.enterNumber(input.getText().toString());
            input.setText("");
        } catch (IllegalArgumentException e) {
            // TODO: Show human readable errors in UI.
            Log.w(TAG, "Failed to parse number input.");
        }
    }
}
