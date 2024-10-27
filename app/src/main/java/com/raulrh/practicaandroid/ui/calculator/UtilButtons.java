package com.raulrh.practicaandroid.ui.calculator;

import android.view.View;
import android.widget.Button;

import com.raulrh.practicaandroid.R;
import com.raulrh.practicaandroid.databinding.CalculatorFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class UtilButtons implements View.OnClickListener {
    private final CalculatorFragment calculatorFragment;
    private final CalculatorFragmentBinding binding;

    public UtilButtons(CalculatorFragment calculatorFragment, CalculatorFragmentBinding binding) {
        this.calculatorFragment = calculatorFragment;
        this.binding = binding;

        setup();
    }

    private void setup() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(binding.bCalculatorClean);
        buttons.add(binding.bCalculatorErase);
        buttons.add(binding.bCalculatorResolve);
        buttons.add(binding.bCalculatorDivision);
        buttons.add(binding.bCalculatorMultiplication);
        buttons.add(binding.bCalculatorAddition);
        buttons.add(binding.bCalculatorSubtraction);

        for (Button button : buttons) {
            button.setOnClickListener(this);
        }
    }

    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.bCalculatorErase:
                eraseToLeft();
                break;
            case R.id.bCalculatorClean:
                clearAll();
                break;
            case R.id.bCalculatorResolve:
                resolveOperation();
                break;
            case R.id.bCalculatorDivision:
                setOperator("/");
                break;
            case R.id.bCalculatorMultiplication:
                setOperator("*");
                break;
            case R.id.bCalculatorAddition:
                setOperator("+");
                break;
            case R.id.bCalculatorSubtraction:
                setOperator("-");
                break;
            default:
                break;
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
            double leftNumber = Double.parseDouble(calculatorFragment.leftNumber.isEmpty() ? "0" : calculatorFragment.leftNumber);
            double rightNumber = Double.parseDouble(calculatorFragment.rightNumber);

            double result = 0;
            switch (calculatorFragment.operator) {
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
                    // No debería suceder nunca, pero quién sabe
                    if (rightNumber == 0) {
                        calculatorFragment.leftNumber = "Error";
                        calculatorFragment.operator = "";
                        calculatorFragment.rightNumber = "";
                        return;
                    }

                    result = leftNumber / rightNumber;
                    break;
            }

            calculatorFragment.leftNumber = String.valueOf(result);
            calculatorFragment.operator = "";
            calculatorFragment.rightNumber = "";
        } catch (NumberFormatException e) {
            handleError();
        }
    }

    private void handleError() {
        calculatorFragment.leftNumber = "Error";
        calculatorFragment.operator = "";
        calculatorFragment.rightNumber = "";
    }
}