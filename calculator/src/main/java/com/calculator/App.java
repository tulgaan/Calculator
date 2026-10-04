package com.calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private TextField display;

    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void start(Stage stage) {
        display = new TextField("0");


        display.setEditable(false);
        display.setAlignment(Pos.CENTER_RIGHT);
        display.setPrefHeight(110);

        display.setStyle(
            "-fx-background-color: #000000;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 52px;" +
            "-fx-border-color: transparent;" +
            "-fx-padding: 10px;"
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        String[][] buttons = {
            {"C", "DEL", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "-"},
            {"1", "2", "3", "+"},
            {"0", ".", "+/-", "="},
            {"√", "x²", "", ""}
        };

        for (int row = 0; row < buttons.length; row++) {

            for (int col = 0; col < buttons[row].length; col++) {

                String text = buttons[row][col];

                if (text.isEmpty()) {
                    continue;
                }

                Button button = createButton(text);

                button.setOnAction(e -> handleButton(text));

                grid.add(button, col, row);
            }
        }

        VBox root = new VBox(10);

        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #000000;");

        root.getChildren().addAll(display, grid);

        Scene scene = new Scene(root, 390, 610);

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefSize(78, 78);

        String color;

        if (text.equals("÷") ||
            text.equals("×") ||
            text.equals("-") ||
            text.equals("+") ||
            text.equals("=")) {

            // Operator buttons
            color = "#ff9f0a";

        } else if (text.equals("C") ||
                   text.equals("DEL") ||
                   text.equals("%") ||
                   text.equals("+/-")) {

            // Function buttons
            color = "#a5a5a5";

        } else {

            // Number buttons
            color = "#333333";
        }

        String textColor =
            color.equals("#a5a5a5") ? "black" : "white";

        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-font-size: 22px;" +
            "-fx-font-weight: normal;" +
            "-fx-background-radius: 50%;"
        );

        return button;
    }

    private void handleButton(String value) {

        // Number 0-9
        if (value.matches("[0-9]")) {

            if (startNewNumber || display.getText().equals("0")) {

                display.setText(value);
                startNewNumber = false;

            } else {

                display.appendText(value);
            }

            return;
        }

        // Decimal point
        if (value.equals(".")) {

            if (startNewNumber) {

                display.setText("0.");
                startNewNumber = false;

            } else if (!display.getText().contains(".")) {

                display.appendText(".");
            }

            return;
        }

        switch (value) {

            case "C":
                clear();
                break;

            case "DEL":
                deleteLastDigit();
                break;

            case "+/-":
                changeSign();
                break;

            case "+":
            case "-":
            case "×":
            case "÷":
                selectOperator(value);
                break;

            case "=":
                calculate();
                break;

            case "%":
                percentage();
                break;

            case "√":
                squareRoot();
                break;

            case "x²":
                square();
                break;
        }
    }
    private void selectOperator(String selectedOperator) {

        try {

            firstNumber =
                Double.parseDouble(display.getText());

            operator = selectedOperator;

            startNewNumber = true;

        } catch (NumberFormatException e) {

            showError();
        }
    }

    private void calculate() {

        if (operator.isEmpty()) {
            return;
        }

        try {

            double secondNumber =
                Double.parseDouble(display.getText());

            double result;

            switch (operator) {

                case "+":
                    result = firstNumber + secondNumber;
                    break;

                case "-":
                    result = firstNumber - secondNumber;
                    break;

                case "×":
                    result = firstNumber * secondNumber;
                    break;

                case "÷":

                    if (secondNumber == 0) {

                        display.setText(
                            "Cannot divide by zero"
                        );

                        operator = "";
                        startNewNumber = true;

                        return;
                    }

                    result = firstNumber / secondNumber;
                    break;

                default:
                    return;
            }

            display.setText(formatNumber(result));

            operator = "";
            startNewNumber = true;

        } catch (NumberFormatException e) {

            showError();
        }
    }

    private void clear() {

        display.setText("0");

        firstNumber = 0;
        operator = "";

        startNewNumber = true;
    }

    private void deleteLastDigit() {

        String text = display.getText();

        if (text.equals("Error") ||
            text.equals("Cannot divide by zero")) {

            clear();
            return;
        }

        if (text.length() > 1) {

            String newText =
                text.substring(0, text.length() - 1);

            if (newText.equals("-")) {

                display.setText("0");
                startNewNumber = true;

            } else {

                display.setText(newText);
            }

        } else {

            display.setText("0");
            startNewNumber = true;
        }
    }

    private void changeSign() {

        try {

            double value =
                Double.parseDouble(display.getText());

            display.setText(
                formatNumber(-value)
            );

        } catch (NumberFormatException e) {

            showError();
        }
    }

    private void percentage() {

        try {

            double value =
                Double.parseDouble(display.getText());

            display.setText(
                formatNumber(value / 100)
            );

            startNewNumber = true;

        } catch (NumberFormatException e) {

            showError();
        }
    }

    private void squareRoot() {

        try {

            double value =
                Double.parseDouble(display.getText());

            if (value < 0) {

                showError();
                return;
            }

            display.setText(
                formatNumber(Math.sqrt(value))
            );

            startNewNumber = true;

        } catch (NumberFormatException e) {

            showError();
        }
    }

    private void square() {

        try {

            double value =
                Double.parseDouble(display.getText());

            display.setText(
                formatNumber(value * value)
            );

            startNewNumber = true;

        } catch (NumberFormatException e) {

            showError();
        }
    }

    private void showError() {

        display.setText("Error");

        firstNumber = 0;
        operator = "";

        startNewNumber = true;
    }
    private String formatNumber(double number) {

        if (number == Math.rint(number)) {

            return String.valueOf((long) number);
        }

        return String.valueOf(number);
    }

    public static void main(String[] args) {
        launch(args);
    }
}