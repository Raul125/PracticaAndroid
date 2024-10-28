package com.raulrh.practicaandroid.ui.calculator;

import android.view.View;
import android.widget.Button;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilButtons implements View.OnClickListener {
    private final CalculatorFragment calculatorFragment;
    private final CalculatorFragmentBinding binding;
    private final Map<Integer, Runnable> buttonActions;

    public UtilButtons(CalculatorFragment calculatorFragment, CalculatorFragmentBinding binding) {
        this.calculatorFragment = calculatorFragment;
        this.binding = binding;
        this.buttonActions = initializeButtonActions();

        setupButtons();
    }

    private void setupButtons() {
        List<Button> buttons = Arrays.asList(
                binding.bCalculatorClean,
                binding.bCalculatorErase,
                binding.bCalculatorResolve,
                binding.bCalculatorDivision,
                binding.bCalculatorMultiplication,
                binding.bCalculatorAddition,
                binding.bCalculatorSubtraction
        );

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }
    }

    private Map<Integer, Runnable> initializeButtonActions() {
        Map<Integer, Runnable> actions = new HashMap<>();
        actions.put(R.id.bCalculatorErase, this::eraseToLeft);
        actions.put(R.id.bCalculatorClean, this::clearAll);
        actions.put(R.id.bCalculatorResolve, this::resolveOperation);
        actions.put(R.id.bCalculatorDivision, () -> setOperator("/"));
        actions.put(R.id.bCalculatorMultiplication, () -> setOperator("*"));
        actions.put(R.id.bCalculatorAddition, () -> setOperator("+"));
        actions.put(R.id.bCalculatorSubtraction, () -> setOperator("-"));
        return actions;
    }

    public void onClick(View view) {
        Runnable action = buttonActions.get(view.getId());
        if (action != null) {
            action.run();
        }

        calculatorFragment.update();
    }

    private void clearAll() {
        calculatorFragment.leftNumber = "";
        calculatorFragment.operator = "";
        calculatorFragment.rightNumber = "";
    }

    private void eraseToLeft() {
        if (calculatorFragment.operator.isEmpty()) {
            if (!calculatorFragment.leftNumber.isEmpty()) {
                calculatorFragment.leftNumber = calculatorFragment.leftNumber.substring(0, calculatorFragment.leftNumber.length() - 1);
            }
        } else {
            if (!calculatorFragment.rightNumber.isEmpty()) {
                calculatorFragment.rightNumber = calculatorFragment.rightNumber.substring(0, calculatorFragment.rightNumber.length() - 1);
            } else {
                calculatorFragment.operator = "";
            }
        }
    }

    private void resolveOperation() {
        if (!calculatorFragment.operator.isEmpty() && !calculatorFragment.rightNumber.isEmpty()) {
            resolver();
        }
    }

    private void setOperator(String operator) {
        if (!calculatorFragment.rightNumber.isEmpty()) {
            resolver();
        }

        if (calculatorFragment.operator.isEmpty() && !calculatorFragment.leftNumber.isEmpty()) {
            calculatorFragment.operator = operator;
        }
    }

    private void resolver() {
        try {
            double leftNumber = parseNumber(calculatorFragment.leftNumber);
            double rightNumber = parseNumber(calculatorFragment.rightNumber);

            double result = performOperation(leftNumber, rightNumber, calculatorFragment.operator);
            updateCalculatorFragmentResult(result);
        } catch (NumberFormatException e) {
            handleError();
        }
    }

    private double parseNumber(String number) {
        return number.isEmpty() ? 0 : Double.parseDouble(number);
    }

    private double performOperation(double leftNumber, double rightNumber, String operator) {
        double result = 0;

        switch (operator) {
            case "+":
                result = leftNumber + rightNumber;
                break;
            case "-":
                result = leftNumber - rightNumber;
                break;
            case "*":
                result = leftNumber * rightNumber;
                break;
            case "/":
                if (rightNumber == 0) {
                    calculatorFragment.leftNumber = "Error";
                    calculatorFragment.operator = "";
                    calculatorFragment.rightNumber = "";
                    return result;
                }

                result = leftNumber / rightNumber;
                break;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
        return result;
    }

    private void updateCalculatorFragmentResult(double result) {
        calculatorFragment.leftNumber = String.valueOf(result);
        calculatorFragment.operator = "";
        calculatorFragment.rightNumber = "";
    }

    private void handleError() {
        calculatorFragment.leftNumber = "Error";
        calculatorFragment.operator = "";
        calculatorFragment.rightNumber = "";
    }
}