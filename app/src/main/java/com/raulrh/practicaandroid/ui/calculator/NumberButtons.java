package com.raulrh.practicaandroid.ui.calculator;

import android.view.View;

import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;

import java.util.HashMap;
import java.util.Map;

public class NumberButtons implements View.OnClickListener {
    private final CalculatorFragment calculatorFragment;
    private final CalculatorFragmentBinding binding;
    private final Map<View, Integer> numbers = new HashMap<>();

    public NumberButtons(CalculatorFragment calculatorFragment, CalculatorFragmentBinding binding) {
        this.calculatorFragment = calculatorFragment;
        this.binding = binding;
        setup();
    }

    private void setup() {
        numbers.put(binding.bCalculator0, 0);
        numbers.put(binding.bCalculator2, 2);
        numbers.put(binding.bCalculator1, 1);
        numbers.put(binding.bCalculator3, 3);
        numbers.put(binding.bCalculator4, 4);
        numbers.put(binding.bCalculator5, 5);
        numbers.put(binding.bCalculator6, 6);
        numbers.put(binding.bCalculator7, 7);
        numbers.put(binding.bCalculator8, 8);
        numbers.put(binding.bCalculator9, 9);

        for (View view : numbers.keySet()) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        Integer number = numbers.get(view);
        if (number != null) {
            if (calculatorFragment.operator.isEmpty()) {
                if (number == 0 && calculatorFragment.leftNumber.isEmpty()) {
                    return;
                }

                calculatorFragment.leftNumber += number;
                // Comprobar q el numero no es mayor de lo q permite
            } else {
                if (number == 0 && calculatorFragment.rightNumber.isEmpty()) {
                    return;
                }

                calculatorFragment.rightNumber += number;
            }

            calculatorFragment.update();
        }
    }
}