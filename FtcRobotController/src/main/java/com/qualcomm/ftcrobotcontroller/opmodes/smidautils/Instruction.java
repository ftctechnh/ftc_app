package com.qualcomm.ftcrobotcontroller.opmodes.smidautils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nathan.Smith.19 on 9/13/2016.
 */
public class Instruction {

    public final String NAME;
    private List<Object> data;

    public Instruction(String name, Object...data){
        this.NAME = name;
        this.data = Arrays.asList(data);
    }

    public void execute(OpMode robot){
        /*
        override for instruction placement
         */
    }

    protected List<Object> getData(){
        return this.data;
    }

}
