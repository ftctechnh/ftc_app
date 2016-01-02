package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by bennettliu on 1/1/16.
 */
public class AutoEncoderDriveTest extends PacmanBotHWB2 {
    AutoDriveController autoDriveController;
    int thing=1000;
    int s=-1;
    @Override
    public void init() {
        setupHardware();
        autoDriveController = new AutoDriveController(rearLeft, rearRight);
        frontLeft.setPowerFloat();
        frontRight.setPowerFloat();
    }
    @Override
    public void loop() {
        autoDriveController.check();
        if(autoDriveController.getStep()!=-1) {
            s=autoDriveController.getStep();
        }
        switch (autoDriveController.getStep()) {//steps :)
            case 0:
                autoDriveController.encoderDrive(thing, thing, 0.5);
                break;
            case 1:
                autoDriveController.encoderDrive(0, thing, 0.5);
                break;
            case 2:
                autoDriveController.encoderDrive(thing, thing, 0.5);
                break;
            case 3:
                autoDriveController.delay(2.0);
                break;
            case 4:
                autoDriveController.encoderDrive(thing, 0, 0.5);
                break;
            case 5:
                autoDriveController.encoderDrive(-2*thing, -2*thing, 0.5);
                break;
            default:
        }
        telemetry.addData("Step",s);
    }
}
