package com.crm.crmapp.order.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class TextValidator implements TextWatcher {
    private final EditText textView;

    public TextValidator(EditText textView) {
        this.textView = textView;
    }

    public abstract void validate(EditText textView, String text);

    @Override
    final public void afterTextChanged(Editable s) {

    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = textView.getText().toString();
        validate(textView, text); }
}