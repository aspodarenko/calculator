package com.example.simpleCalculator;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

/**
 * Created with IntelliJ IDEA.
 * User: corwin
 * Date: 2/9/13
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpressionEvaluator implements View.OnClickListener {

    private EditText editText;
    private TextView notificationText;

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public TextView getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(TextView notificationText) {
        this.notificationText = notificationText;
    }

    @Override
    public void onClick(View view) {
        Calculable calc = null;
        try {
           calc = new ExpressionBuilder(getEditText().getText().toString()).build();
           getEditText().setText(String.valueOf(calc.calculate()));
        }  catch (Exception e) {
            Log.d(this.getClass().getName(),"not correct expression try to be parsed",e);
            getNotificationText().setText(R.string.syntax_error);
        }
    }
}
