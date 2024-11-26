package com.raulrh.practicaandroid.ui.calculator;

import android.view.View;

import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;

import java.util.HashMap;
import java.util.Map;

public class NumberButtons implements View.OnClickListener {
    private static final int MAX_NUMBER_LENGTH = 15;
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
        numbers.put(binding.bCalculator1, 1);
        numbers.put(binding.bCalculator2, 2);
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
            handleNumberInput(number);
            calculatorFragment.update();
        }
    }

    private void handleNumberInput(int number) {
        if (canAddNumber(number, calculatorFragment.operator.isEmpty())) {
            updateNumberInput(number, calculatorFragment.operator.isEmpty());
        }
    }

    private boolean canAddNumber(int number, boolean isLeft) {
        String currentNumber = isLeft ? calculatorFragment.leftNumber : calculatorFragment.rightNumber;
        return !(number == 0 && currentNumber.isEmpty());
    }

    private void updateNumberInput(int number, boolean isLeft) {
        String currentNumber = isLeft ? calculatorFragment.leftNumber : calculatorFragment.rightNumber;
        if (!currentNumber.isEmpty()) {
            double currentNumberDouble = Double.parseDouble(currentNumber);
            if (currentNumberDouble % 1 != 0) {
                return;
            }
        }

        String newNumber = currentNumber + number;
        if (allowMore(newNumber)) {
            if (isLeft) {
                calculatorFragment.leftNumber = newNumber;
            } else {
                calculatorFragment.rightNumber = newNumber;
            }
        }
    }

    private boolean allowMore(String number) {
        return number.length() <= MAX_NUMBER_LENGTH && isValidNumber(number);
    }

    private boolean isValidNumber(String number) {
        try {
            double numberDouble = Double.parseDouble(number);
            return numberDouble < Double.MAX_VALUE && numberDouble > -Double.MAX_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}