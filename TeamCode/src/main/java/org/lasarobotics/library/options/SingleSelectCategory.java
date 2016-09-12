package com.lasarobotics.library.options;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Category that provides a simple dropdown menu selector.
 */
public class SingleSelectCategory implements Category {
    public ArrayList<String> options;
    public String name;
    public int id;
    public View view;

    public SingleSelectCategory(String name) {
        this.name = name;
        options = new ArrayList<String>();
    }

    public void addOption(String item) {
        options.add(item);
    }

    /**
     * @inheritDoc Constructs an Spinner object, using an ArrayAdapter to set the spinner options
     * provided
     */
    @Override
    public View getView(Context context) {
        //Generate ID
        id = ID_OFFSET + new Random().nextInt(100);
        //Create row
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        //Create TextView
        TextView t = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, null);
        t.setText(name);
        row.addView(t);
        //Create Spinner
        Spinner spinner = new Spinner(context);
        spinner.setId(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        row.addView(spinner);
        view = row;
        return row;
    }

    /**
     * @inheritDoc Returns String value of the spinner option selected
     */
    @Override
    public String getResult() {
        Spinner s = (Spinner) (view.findViewById(id));
        return options.get(s.getSelectedItemPosition());
    }

    @Override
    public String getName() {
        return name;
    }
}
