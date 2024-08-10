package com.natewilliford.rpncalculator;

import static com.natewilliford.rpncalculator.RpnCalculator.Operator;

import android.os.Bundle;
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

    @Nullable private RpnCalcFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RpnCalcFragmentBinding.inflate(getLayoutInflater());
        RpnCalcFragmentViewModel vm =
                new ViewModelProvider(this).get(RpnCalcFragmentViewModel.class);
        vm.getCalcResult().observe(getViewLifecycleOwner(), result ->
                binding.resultTextView.setText(String.format(Locale.ENGLISH,"%.2f", result)));
        binding.enterButton.setOnClickListener(v -> {
            EditText input = binding.textInput;
            vm.enterNumber(input.getText().toString());
            input.setText("");
        });
        binding.plusButton.setOnClickListener(v -> vm.submitOperator(Operator.PLUS));
        binding.minusButton.setOnClickListener(v -> vm.submitOperator(Operator.MINUS));
        binding.multiplyButton.setOnClickListener(v -> vm.submitOperator(Operator.MULTIPLY));
        binding.divideButton.setOnClickListener(v -> vm.submitOperator(Operator.DIVIDE));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
