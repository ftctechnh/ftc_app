package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.containers.Location;
import com.qualcomm.ftcrobotcontroller.controllers.DriveControl;
import com.qualcomm.ftcrobotcontroller.controllers.SensorModel;
import com.qualcomm.ftcrobotcontroller.controllers.StateControl;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ARMAuto extends LinearOpMode {
    private DriveControl driveController;
    private StateControl stateController;
    private SensorModel sensorModel;

    @Override
    public void runOpMode() throws InterruptedException {
        driveController = new DriveControl(hardwareMap);
        stateController = new StateControl();
        sensorModel = new SensorModel();
        waitForStart();

        while (!stateController.isEndCondition()) {

            sensorModel.update();
            Location currentLocation = sensorModel.getCurrLocation();

            switch (stateController.getCurrent_state()) {
                case FWD:
                    driveController.driveStraight(currentLocation);
                    //@TODO update current state
                    break;
                case TURN:
                    driveController.turn(currentLocation);
                    //@TODO update current state
                    break;
                case STOP:
                    driveController.stop();
                    //@TODO update current state
                    break;
                default:
                    stateController.setCurrent_state(StateControl.State.STOP);
                    break;
            }
        }
    }
}

