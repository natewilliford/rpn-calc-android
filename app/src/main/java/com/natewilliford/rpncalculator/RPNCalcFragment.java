package com.natewilliford.rpncalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.natewilliford.rpncalculator.databinding.RpnCalcFragmentBinding;

public class RPNCalcFragment extends Fragment {

    @Nullable
    private RpnCalcFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RpnCalcFragmentBinding.inflate(getLayoutInflater());
        binding.resultTextView.setText("Set text from binding");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
