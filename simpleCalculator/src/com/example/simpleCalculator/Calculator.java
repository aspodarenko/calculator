package com.example.simpleCalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Calculator extends Activity implements View.OnClickListener {

    private EditText editText;
    private TextView notificationText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        inputButtonsInitialization();
        editText = (EditText)findViewById(R.id.editText);
        notificationText =(TextView)findViewById(R.id.notificationText);
        Button equalButton = (Button)findViewById(R.id.equal);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
        expressionEvaluator.setEditText(editText);
        expressionEvaluator.setNotificationText(notificationText);

        equalButton.setOnClickListener(expressionEvaluator);
    }

    private void inputButtonsInitialization() {
        Button button = (Button)findViewById(R.id.figure0);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure1);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure2);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure3);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure4);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure5);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure6);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure7);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure8);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.figure9);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.minus);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.plus);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.multiplication);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.dividing);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.dot);
        button.setOnClickListener(this);
        button = (Button)findViewById(R.id.clear);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.clear){
            editText.setText("");
        } else {
            editText.setText(editText.getText().toString() + ((Button)view).getText());
        }
        notificationText.setText("");
    }
}
