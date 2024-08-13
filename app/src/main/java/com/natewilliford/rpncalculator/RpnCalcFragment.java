package com.natewilliford.rpncalculator;

import static com.natewilliford.rpncalculator.RpnCalculator.Operator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
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
        binding.enterButton.setOnClickListener(v -> enterInputNumber());
        binding.plusButton.setOnClickListener(v -> submitOperator(Operator.PLUS));
        binding.minusButton.setOnClickListener(v -> submitOperator(Operator.MINUS));
        binding.multiplyButton.setOnClickListener(v -> submitOperator(Operator.MULTIPLY));
        binding.divideButton.setOnClickListener(v -> submitOperator(Operator.DIVIDE));
        binding.deleteButton.setOnClickListener(v -> deleteLast());
        binding.clearButton.setOnClickListener(v -> clear());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    /** @return Whether the action succeeded. */
    private boolean enterInputNumber() {
        if (binding == null || viewModel == null) {
            return false;
        }

        EditText input = binding.valueInput;
        if (input.getText().toString().isEmpty()) {
            showErrorMessage(R.string.error_message_empty_input);
            return false;
        }

        String inputString = input.getText().toString();
        try {
            viewModel.enterNumber(inputString);
            input.setText("");
            return true;
        } catch (NumberFormatException e) {
            Log.w(TAG, "Failed to parse number input: " + inputString);
            showErrorMessage(R.string.error_message_failed_parse);
        } catch (IllegalStateException e) {
            showErrorMessage(R.string.error_message_stack_full);
        }
        return false;
    }

    private void submitOperator(Operator operator) {
        if (viewModel == null || binding == null) {
            return;
        }

        // If there is a pending value in the input field, enter it before running the operation.
        if (binding.valueInput.getText().length() > 0) {
            if (!enterInputNumber()) {
                return;
            }
        }
        try {
            viewModel.submitOperator(operator);
        } catch (IllegalStateException e) {
            showErrorMessage(R.string.error_message_not_enough_operands);
        }
    }

    private void deleteLast() {
        if (binding == null || viewModel == null) {
            return;
        }

        // If there is a pending value in the input field, delete it rather than deleting from the
        // calculator stack.
        if (binding.valueInput.getText().length() > 0) {
            binding.valueInput.setText("");
        } else {
            viewModel.deleteLast();
        }
    }

    private void clear() {
        if (binding == null || viewModel == null) {
            return;
        }
        binding.valueInput.setText("");
        viewModel.clearCalculator();
    }

    private void showErrorMessage(@StringRes int message) {
        if (binding != null) {
            Snackbar.make(binding.valueInput, message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
