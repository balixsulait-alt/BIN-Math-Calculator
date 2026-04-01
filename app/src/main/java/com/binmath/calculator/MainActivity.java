package com.binmath.calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView displayResult;
    private TextView displayExpression;

    private String currentInput = "";
    private String operator = "";
    private double firstOperand = 0;
    private boolean isOperatorPressed = false;
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayResult = findViewById(R.id.displayResult);
        displayExpression = findViewById(R.id.displayExpression);

        // Number buttons
        int[] numberIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        for (int id : numberIds) {
            findViewById(id).setOnClickListener(v -> onNumberClick((Button) v));
        }

        // Operator buttons
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperatorClick("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> onOperatorClick("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> onOperatorClick("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> onOperatorClick("÷"));

        // Special buttons
        findViewById(R.id.btnEquals).setOnClickListener(v -> onEqualsClick());
        findViewById(R.id.btnClear).setOnClickListener(v -> onClearClick());
        findViewById(R.id.btnDecimal).setOnClickListener(v -> onDecimalClick());
        findViewById(R.id.btnPlusMinus).setOnClickListener(v -> onPlusMinusClick());
        findViewById(R.id.btnPercent).setOnClickListener(v -> onPercentClick());
        findViewById(R.id.btnBackspace).setOnClickListener(v -> onBackspaceClick());
    }

    private void onNumberClick(Button btn) {
        String digit = btn.getText().toString();

        if (isResultDisplayed) {
            currentInput = "";
            isResultDisplayed = false;
            displayExpression.setText("");
        }

        if (currentInput.equals("0")) {
            currentInput = digit;
        } else {
            if (currentInput.length() < 12) {
                currentInput += digit;
            }
        }
        displayResult.setText(currentInput.isEmpty() ? "0" : currentInput);
    }

    private void onOperatorClick(String op) {
        if (!currentInput.isEmpty()) {
            if (!operator.isEmpty() && !isOperatorPressed) {
                calculate();
            } else {
                firstOperand = Double.parseDouble(currentInput.isEmpty() ? "0" : currentInput);
            }
        }

        operator = op;
        isOperatorPressed = true;
        isResultDisplayed = false;

        String displayFirst = formatNumber(firstOperand);
        displayExpression.setText(displayFirst + " " + op);
        currentInput = "";
    }

    private void onEqualsClick() {
        if (operator.isEmpty() || currentInput.isEmpty()) return;
        String exprText = displayExpression.getText().toString() + " " + currentInput + " =";
        displayExpression.setText(exprText);
        calculate();
        operator = "";
        isResultDisplayed = true;
    }

    private void calculate() {
        if (currentInput.isEmpty()) return;
        double secondOperand = Double.parseDouble(currentInput);
        double result;

        switch (operator) {
            case "+":
                result = firstOperand + secondOperand;
                break;
            case "-":
                result = firstOperand - secondOperand;
                break;
            case "×":
                result = firstOperand * secondOperand;
                break;
            case "÷":
                if (secondOperand == 0) {
                    displayResult.setText("Error");
                    currentInput = "";
                    firstOperand = 0;
                    operator = "";
                    return;
                }
                result = firstOperand / secondOperand;
                break;
            default:
                return;
        }

        String formatted = formatNumber(result);
        displayResult.setText(formatted);
        currentInput = formatted;
        firstOperand = result;
        isOperatorPressed = false;
    }

    private void onClearClick() {
        currentInput = "";
        operator = "";
        firstOperand = 0;
        isOperatorPressed = false;
        isResultDisplayed = false;
        displayResult.setText("0");
        displayExpression.setText("");
    }

    private void onDecimalClick() {
        if (isResultDisplayed) {
            currentInput = "0";
            isResultDisplayed = false;
        }
        if (currentInput.isEmpty()) currentInput = "0";
        if (!currentInput.contains(".")) {
            currentInput += ".";
            displayResult.setText(currentInput);
        }
    }

    private void onPlusMinusClick() {
        if (!currentInput.isEmpty() && !currentInput.equals("0")) {
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1);
            } else {
                currentInput = "-" + currentInput;
            }
            displayResult.setText(currentInput);
        }
    }

    private void onPercentClick() {
        if (!currentInput.isEmpty()) {
            double val = Double.parseDouble(currentInput) / 100;
            currentInput = formatNumber(val);
            displayResult.setText(currentInput);
        }
    }

    private void onBackspaceClick() {
        if (isResultDisplayed) return;
        if (currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            displayResult.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        } else {
            // Limit decimal places to 8
            String formatted = String.format("%.8f", value).replaceAll("0+$", "").replaceAll("\\.$", "");
            return formatted;
        }
    }
}
