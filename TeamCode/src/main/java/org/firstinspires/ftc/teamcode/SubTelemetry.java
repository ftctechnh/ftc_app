package org.firstinspires.ftc.teamcode;

import java.util.AbstractList;
import java.util.ArrayList;

public class SubTelemetry extends AbstractList {
    private ArrayList<String> subTelem;

    SubTelemetry(){
        subTelem = new ArrayList<>();
    }

    @Override
    public int size() {
        return subTelem.size();
    }

    @Override
    public Object get(int i) {
        return subTelem.get(i);
    }

    void addData(String str, Object o)
    {
        subTelem.add(str + " :" + o.toString());
    }

    void addLine(String str)
    {
        subTelem.add(str);
    }

    void clearAll()
    {
        subTelem.clear();
    }

}
