package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NDHSB-Emma on 9/26/17.
 *
 * Our opmode that we built on ftc's opmode whoooooo it does crap yayyyyyy
 */

public abstract class Team7593OpMode extends OpMode{
    private ArrayList<AutonStep> steps = null;
    private int currentStep = -1;
    protected Team7593Hardware robot = new Team7593Hardware();
    private HashMap<String, Object> sharedInfo = new HashMap<>();


    //final values
    public final double v0 = 13.0597; //avg voltage
    public final double voltageSlope = 3.22031; //slope of a graph plotted dist vs voltage
    public final double avgDist = 46.9611; //avg distance for the data
    public final double calibrationTime = 1.5; //control time used

    public abstract ArrayList<AutonStep> createAutonSteps();

    public boolean isInitialized = false;

    public enum Alliance{
        BLUE, RED
    }

    public void shareInfo(String name, Object value) {
        this.sharedInfo.put(name, value);
    }

    public Object getSharedInfo(String name) {
        return this.sharedInfo.get(name);
    }

    //probably will delete this later
    public void stopAuton(){
        steps = null;
        currentStep = -1;
    }

    @Override
    public void init() {
        this.robot.init(hardwareMap);
        steps = this.createAutonSteps();

        if(steps != null) {
            telemetry.log().add("INIT: Steps are there");
            //telemetry.log().add("current angle" + robot.getCurrentAngle());
            //telemetry.log().add("imu angle" + robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            //telemetry.log().add("init angle" + robot.initAngle);
        }
        else
            telemetry.log().add("INIT PROBLEM: Steps are NOT there");

    }

    public void newDriverAssist(ArrayList<AutonStep> driverAssistSteps)
    {
        this.newDriverAssist(driverAssistSteps, false);
    }

    public void newDriverAssist(ArrayList<AutonStep> driverAssistSteps, boolean append){
        if (append) {
            if(steps != null) steps.addAll(driverAssistSteps);
            else {
                steps = driverAssistSteps;
                currentStep = -1;
            }
        } else {
            steps = driverAssistSteps;
            currentStep = -1;
        }
    }

    @Override
    public void loop() {

        if (!isInitialized) {
            isInitialized = true;
            robot.resetInitAngle();
        }

        //telemetry
        for (String name : sharedInfo.keySet()) {
            telemetry.addLine().addData(name, sharedInfo.get(name));
        }

        if (steps == null) {
            telemetry.addLine().addData("Step:", "No more Steps to run");
            return;
        }

        //start the steps
        if (currentStep == -1) {
            currentStep = 0;
            steps.get(currentStep).start(this);
        }


        steps.get(currentStep).loop(this);

        //telemetry
        telemetry.addLine().addData("Step:", steps.get(currentStep).name());
        steps.get(currentStep).updateTelemetry(this);

        //check if the steps are done
        if (steps.get(currentStep).isDone(this)) {
            telemetry.log().add("Step %s is done.", steps.get(currentStep).name());
            currentStep += 1;
            if (currentStep >= steps.size()) {
                steps = null;
                currentStep = -1;
            } else {
                steps.get(currentStep).start(this);
            }

        }


    }

}