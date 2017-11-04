package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

// Created by Swagster_Wagster on 9/29/17
//
//
// Last edit: 10/22/17 BY MRINAAL RAMACHANDRAN


@Autonomous(name = "Red_Autonomous_Back",group="We Love Pi")
public class Red_Autonomous_Back extends LinearOpMode {

    Autonomous_Functions af = new Autonomous_Functions();

    VuforiaLocalizer vuforia;

    String jewelColor = null;

    ElapsedTime timer = new ElapsedTime();

    public String senseJewelColor() {

        while (opModeIsActive()) {

            int red = af.colorSensor.red();
            int blue = af.colorSensor.blue();
            int green = af.colorSensor.green();

            // sense color blue

            if (blue >= 4 && red <= 1 && green <= 1) {

                return "BLUE";
            }

            // sense color red
            if (red >= 4 && blue <= 1 && green <= 1) {

                return "RED";
            }

            telemetry.addData("values ", blue);
            telemetry.update();

        }

        return "NOTHING";
    }

    public void turnMotorUsingGyro(double power, float degrees, String direction) {

        af.modernRoboticsI2cGyro.resetZAxisIntegrator();

        if (direction == Constants.spinLeft) {

            af.F_L.setDirection(DcMotor.Direction.FORWARD);
            af.F_R.setDirection(DcMotor.Direction.FORWARD);
            af.R_L.setDirection(DcMotor.Direction.FORWARD);
            af.R_R.setDirection(DcMotor.Direction.FORWARD);

            af.F_L.setPower(power);
            af.F_R.setPower(power);
            af.R_L.setPower(power);
            af.R_R.setPower(power);
        } else {

            af.F_L.setDirection(DcMotor.Direction.REVERSE);
            af.F_R.setDirection(DcMotor.Direction.REVERSE);
            af.R_L.setDirection(DcMotor.Direction.REVERSE);
            af.R_R.setDirection(DcMotor.Direction.REVERSE);

            af.F_L.setPower(power);
            af.F_R.setPower(power);
            af.R_L.setPower(power);
            af.R_R.setPower(power);
        }

        float zAngle = af.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;

        while (opModeIsActive() && Math.abs(zAngle - degrees) > 5) {
            zAngle = af.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            telemetry.addData("angle", "%s deg", formatFloat(zAngle));
            telemetry.update();
        }

        af.F_L.setPower(0);
        af.F_R.setPower(0);
        af.R_L.setPower(0);
        af.R_R.setPower(0);
    }

    String formatFloat(float rate) {
        return String.format("%.3f", rate);
    }


    @Override
    public void runOpMode() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AQBbCRX/////AAAAGevvCPApu0phjwrtg1hDMb5FkMizE8AaJCDJtXwc8y45ZcarRtpG2Edqj3dWfUCXGTucT4Ovr/Exh6ekwjyC6D+N59nTw2BdGx9VxVquX6vBsKl2acD9cfQOBkxSs6puRSAH/Pm3FMiP4AN/LXC+VedYtfIAE2UZmIF041Lr8sLs+wgephTto8kZ8ELxQZx98Q5T/RaLo3wkz5+jYksV5Pi8VRG0vFhk47fwa0gUrZDTFhq11og/bD9zZmLiFqH3tyHeMMSkYFVakMctRPNjKYPgi9iG4jlDjGbtq2a56QxTxzjBo/J/8K2PN2A6vnBw3tMc2E1KitBtPuKrD9h/5ACsIGWh2Zo5TiQC1YGTQr6F";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.NONE;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);

        // Moved the init hardware below wait for start. Dropper motor doesnt go 0-1 above wait for start
        af.init(hardwareMap);

        af.modernRoboticsI2cGyro.calibrate();

        timer.reset();

        while (!isStopRequested() && af.modernRoboticsI2cGyro.isCalibrating()) {
            telemetry.addData("calibrating", "%s", Math.round(timer.seconds()) % 2 == 0 ? "|.." : "..|");
            telemetry.update();
            sleep(50);
        }

        waitForStart();

        relicTrackables.activate();

        boolean found = false;
        RelicRecoveryVuMark vuMark = null;

        while (opModeIsActive() && !found) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark == RelicRecoveryVuMark.LEFT || vuMark == RelicRecoveryVuMark.RIGHT || vuMark == RelicRecoveryVuMark.CENTER) {
                found = true;
            }
        }

        if (opModeIsActive() && found) {

            if (vuMark == RelicRecoveryVuMark.LEFT) {

                telemetry.addData("vuMark", vuMark);
                telemetry.update();

                af.dropper.setPosition(0);

                telemetry.addData(" COLOR IS ", jewelColor = senseJewelColor());
                telemetry.update();

                if (jewelColor == "BLUE") {

                    turnMotorUsingGyro(.1, -20, Constants.spinRight);
                    af.dropper.setPosition(1);
                    turnMotorUsingGyro(.1, 20, Constants.spinLeft);
                    af.stopMotor(500);
                    af.moveMotorWithTime(.2, 2200, Constants.forward);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 1200, Constants.left);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 500, Constants.forward);


                }

                if (jewelColor == "RED") {

                    turnMotorUsingGyro(.1, 20, Constants.spinLeft);
                    af.dropper.setPosition(1);
                    turnMotorUsingGyro(.1, -20, Constants.spinRight);
                    af.stopMotor(500);
                    af.moveMotorWithTime(.2, 2200, Constants.forward);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 1200, Constants.left);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 500, Constants.forward);
                }
            }
            else if (vuMark == RelicRecoveryVuMark.CENTER) {

                telemetry.addData("vuMark", vuMark);
                telemetry.update();

                af.dropper.setPosition(0);

                telemetry.addData(" COLOR IS ", jewelColor = senseJewelColor());
                telemetry.update();

                if (jewelColor == "BLUE") {

                    turnMotorUsingGyro(.1, -20, Constants.spinRight);
                    af.dropper.setPosition(1);
                    turnMotorUsingGyro(.1, 20, Constants.spinLeft);
                    af.stopMotor(500);
                    af.moveMotorWithTime(.2, 2200, Constants.forward);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 1200, Constants.left);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 500, Constants.forward);


                }

                if (jewelColor == "RED") {

                    turnMotorUsingGyro(.1, 20, Constants.spinLeft);
                    af.dropper.setPosition(1);
                    turnMotorUsingGyro(.1, -20, Constants.spinRight);
                    af.stopMotor(500);
                    af.moveMotorWithTime(.2, 2200, Constants.forward);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 1200, Constants.left);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 500, Constants.forward);
                }

            } else if (vuMark == RelicRecoveryVuMark.RIGHT) {

                telemetry.addData("vuMark", vuMark);
                telemetry.update();

                af.dropper.setPosition(0);

                telemetry.addData(" COLOR IS ", jewelColor = senseJewelColor());
                telemetry.update();

                if (jewelColor == "BLUE") {

                    turnMotorUsingGyro(.1, -20, Constants.spinRight);
                    af.dropper.setPosition(1);
                    turnMotorUsingGyro(.1, 20, Constants.spinLeft);
                    af.stopMotor(500);
                    af.moveMotorWithTime(.2, 2200, Constants.forward);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 1200, Constants.left);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 500, Constants.forward);


                }

                if (jewelColor == "RED") {

                    turnMotorUsingGyro(.1, 20, Constants.spinLeft);
                    af.dropper.setPosition(1);
                    turnMotorUsingGyro(.1, -20, Constants.spinRight);
                    af.stopMotor(500);
                    af.moveMotorWithTime(.2, 2200, Constants.forward);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 1200, Constants.left);
                    af.stopMotor(1000);
                    af.moveMotorWithTime(.2, 500, Constants.forward);
                }
            }

            while (opModeIsActive()) {


            }
        }
    }
}
