package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by christinecarroll on 12/14/17.
 */

public class RelicTelyMode  extends MeccyMode{
    //<editor-fold desc="Startify">
    double degreeOfRobotPower = 1;
    DrivingAction drivingAction = DrivingAction.Driving;
    //<editor-fold desc="Controls"
    double leftX;
    double leftY;
    double rightX;
    boolean halfPower;
    boolean quarterPower;
    //</editor-fold>
    //
    //<
    //</editor-fold>
    //
    public void runOpMode() {
        //<editor-fold desc="Initialize">
        leftX = Math.abs(gamepad1.left_stick_x);
        leftY = Math.abs(gamepad1.left_stick_y);
        rightX = Math.abs(gamepad1.right_stick_x);
        halfPower = gamepad1.left_bumper;
        quarterPower = gamepad1.right_bumper;
        //</editor-fold>
        //
        setDegreePower();
        //
        switchMove();
        //
        moveTheRobot();
        //
        telemetryJazz();
        //
    }
    //
    //<editor-fold desc="Functions">
    private void setDegreePower() {
        if(halfPower) {
            degreeOfRobotPower = 0.5;
        }
        else if(quarterPower){
            degreeOfRobotPower = 0.25;
        }else{
            degreeOfRobotPower = 1.0;
        }
    }
    //
    private void switchMove(){
        if (!(rightX == 0)){
            DrivingAction drivingAction = DrivingAction.Turning;
        }else if(leftX > leftY){
            DrivingAction drivingAction = DrivingAction.Strafing;
        }else{
            DrivingAction drivingAction = DrivingAction.Driving;
        }
    }
    //
    private void moveTheRobot() {
        switch (drivingAction){
            case Driving:
                if (leftY < 0){
                    driveForward(leftY);
                }else{
                    driveBackward(leftY);
                }
            case Turning:
                if (rightX > 0){
                    turnRight(rightX);
                }else{
                    turnLeft(rightX);
                }
                break;
            case Strafing:
                if (leftX > 0){
                    strafeRight(leftX);
                }else{
                    strafeLeft(leftX);
                }
                break;
        }
    }
    //
    private void telemetryJazz() {
        telemetry.addData("Unicorn Crossing", "Always a danger WATCH OUT");
        telemetry.update();
    }
    //</editor-fold>
}
