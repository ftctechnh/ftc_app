package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.containers.Location;
import com.qualcomm.ftcrobotcontroller.controllers.DriveControl;
import com.qualcomm.ftcrobotcontroller.controllers.SensorModel;
import com.qualcomm.ftcrobotcontroller.controllers.StateControl;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Autonomous mode
 * @author micray01
 * @since 6/16/2016 7:03pm
 * @version 1
 */
public class ARMAuto extends LinearOpMode {
    private DriveControl driveController;
    private StateControl stateController;
    private SensorModel sensorModel;
    public static Telemetry debug;

    public static DcMotor leftMotor;
    public static DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        //Establish debug methods
        debug = telemetry;

        //Create the motors
        leftMotor = hardwareMap.dcMotor.get("motor_2");
        rightMotor = hardwareMap.dcMotor.get("motor_1");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        waitOneFullHardwareCycle();

        //Initialize class objects
        driveController = new DriveControl(hardwareMap);
        stateController = new StateControl();
        sensorModel = new SensorModel();
        waitForStart();

        while (!stateController.isEndCondition()) {

            //Update the sensor model
            //  Take the current encoder ticks and convert them to a location
            sensorModel.update();

            //Retrieve the current location from the sensor model
            Location currentLocation = sensorModel.getCurrLocation();

            //Behave according to the current state that the robot is in
            switch (stateController.getCurrent_state()) {
                case FWD:
                    driveController.driveStraight(currentLocation);
                    break;
                case TURN:
                    driveController.turn(currentLocation);
                    break;
                case STOP:
                    driveController.stop();
                    break;
            }

            //Update the state machine according to the current location
            //  Sets the next state if the current states condition's have been met
            stateController.update_state(currentLocation);
        }
    }
}

