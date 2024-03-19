package com.example.task21p;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerSourceUnit, spinnerDestinationUnit, spinnerConversionType;
    private EditText editTextValue;
    private TextView textViewResult;
    private ArrayAdapter<CharSequence> adapterLength, adapterWeight, adapterTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerSourceUnit = findViewById(R.id.spinnerSourceUnit);
        spinnerDestinationUnit = findViewById(R.id.spinnerDestinationUnit);
        spinnerConversionType = findViewById(R.id.spinnerConversionType);
        editTextValue = findViewById(R.id.editTextValue);
        textViewResult = findViewById(R.id.textViewResult);

        initializeAdapters();
        setupConversionTypeSpinner();
    }

    private void initializeAdapters() {
        adapterLength = ArrayAdapter.createFromResource(this, R.array.length_units_array, android.R.layout.simple_spinner_item);
        adapterWeight = ArrayAdapter.createFromResource(this, R.array.weight_units_array, android.R.layout.simple_spinner_item);
        adapterTemperature = ArrayAdapter.createFromResource(this, R.array.temperature_units_array, android.R.layout.simple_spinner_item);

        adapterLength.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTemperature.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupConversionTypeSpinner() {
        ArrayAdapter<CharSequence> conversionTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.conversion_types_array, android.R.layout.simple_spinner_item);
        conversionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerConversionType.setAdapter(conversionTypeAdapter);

        spinnerConversionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // Length
                        spinnerSourceUnit.setAdapter(adapterLength);
                        spinnerDestinationUnit.setAdapter(adapterLength);
                        break;
                    case 1: // Weight
                        spinnerSourceUnit.setAdapter(adapterWeight);
                        spinnerDestinationUnit.setAdapter(adapterWeight);
                        break;
                    case 2: // Temperature
                        spinnerSourceUnit.setAdapter(adapterTemperature);
                        spinnerDestinationUnit.setAdapter(adapterTemperature);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action required here
            }
        });
    }

    public void onConvertClick(View view) {
        try {
            String sourceUnit = spinnerSourceUnit.getSelectedItem().toString();
            String destinationUnit = spinnerDestinationUnit.getSelectedItem().toString();
            double inputValue = Double.parseDouble(editTextValue.getText().toString());

            // First, convert the input value to a base unit (cm/g/C).
            double baseValue = convertToBaseUnit(sourceUnit, inputValue);

            // Next, convert from the base unit to the target unit.
            double convertedValue = convertFromBaseUnit(destinationUnit, baseValue);

            // Display the result in the textViewResult.
            textViewResult.setText(String.format("Result: %.6f", convertedValue));
        } catch (NumberFormatException e) {
            // Handle the case where the input value is not a valid number.
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }


    private double convertToBaseUnit(String unit, double value) {
        switch (unit) {
            case "Inch":
                return value * 2.54;
            case "Foot":
                return value * 30.48;
            case "Yard":
                return value * 91.44;
            case "Mile":
                return value * 160934;
            case "Meter":
                return value * 100;
            case "Kilometer":
                return value * 100000;

            case "Pound":
                return value * 453.592;
            case "Ounce":
                return value * 28.3495;
            case "Ton":
                return value * 907185;
            case "Kilogram":
                return value * 1000;

            case "Fahrenheit":
                return (value - 32) / 1.8;
            case "Kelvin":
                return value - 273.15;
            default:
                return value; // In case of unknown unit or same unit selected
        }
    }

    private double convertFromBaseUnit(String unit, double value) {
        switch (unit) {
            case "Inch":
                return value / 2.54;
            case "Foot":
                return value / 30.48;
            case "Yard":
                return value / 91.44;
            case "Mile":
                return value / 160934;
            case "Meter":
                return value / 100;
            case "Kilometer":
                return value / 100000;

            case "Pound":
                return value / 453.592;
            case "Ounce":
                return value / 28.3495;
            case "Ton":
                return value / 907185;
            case "Kilogram":
                return value / 1000;

            case "Celsius":
                return value;
            case "Fahrenheit":
                return (value * 1.8) + 32;
            case "Kelvin":
                return value + 273.15;
            default:
                return value; // In case of unknown unit or same unit selected
        }
    }
}