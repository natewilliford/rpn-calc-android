package com.natewilliford.rpncalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.natewilliford.rpncalculator.databinding.RpnCalcFragmentBinding;

import dagger.hilt.android.AndroidEntryPoint;

/** The fragment that holds all the calculator interaction. */
@AndroidEntryPoint
public class RpnCalcFragment extends Fragment {

    @Nullable private RpnCalcFragmentBinding binding;
    @Nullable private RpnCalcFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RpnCalcFragmentBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(RpnCalcFragmentViewModel.class);
        binding.resultTextView.setText(viewModel.getAThing());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
