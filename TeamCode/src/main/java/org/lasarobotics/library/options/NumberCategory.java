package com.lasarobotics.library.options;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple number entry category
 */
public class NumberCategory implements Category {
    String name;
    View view;
    int id;

    public NumberCategory(String name) {
        this.name = name;
    }

    /**
     * @inheritDoc Constructs an EditText object with input type set to number
     */
    @Override
    public View getView(Context context) {
        //Create row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        //Create TextView
        TextView t = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
        t.setText(name);
        row.addView(t);
        //Create EditText
        EditText text = new EditText(context);
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        text.setId(id);
        row.addView(text);
        view = row;
        return view;
    }

    /**
     * @inheritDoc Return value of EditText
     */
    @Override
    public String getResult() {
        EditText edit = (EditText) view.findViewById(id);
        return edit.getText().toString();
    }

    @Override
    public String getName() {
        return name;
    }
}
