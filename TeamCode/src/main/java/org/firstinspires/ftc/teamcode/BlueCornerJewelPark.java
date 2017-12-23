package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Autonomous(name="Jewel Park: Blue Corner", group="Pushbot")
//@Disabled
public class BlueCornerJewelPark extends OpMode{

    private int stateMachineFlow;
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();

    RelicRecoveryVuMark glyph;
    GlyphArm gilgearmesh = new GlyphArm();

    JewelSystem sensArm = new JewelSystem();
    JewelColor jewelColor = JewelColor.UNKNOWN;
    double time;


    //VuforiaLocalizer vuforia;
    /*int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);
*/

    @Override
    public void init() {
        telemetry.addData("before init","here");
        telemetry.update();
        robot.init(hardwareMap);
        telemetry.addData("after robot","here");
        telemetry.update();
        gilgearmesh.init(hardwareMap);
        telemetry.addData("after gilgearmesh","here");
        telemetry.update();
        sensArm.init(hardwareMap);
        telemetry.addData("after init","here");
        telemetry.update();

        //code for gripping glyph and moving arm slightly up
        //gilgearmesh.clawPos(1);
        //wait needed? Also... guessed parameters
        //gilgearmesh.armPos(800,.6);
        stateMachineFlow = 0;

        sensArm.colorLED(false);

        //telemetry.addData("Key",glyph);
        telemetry.addData("Arm Pos",gilgearmesh.getArmPosition());
        telemetry.addData("Color",jewelColor);
        telemetry.addData("Case",stateMachineFlow);
        telemetry.update();
    }

    /*@Override
    public void init_loop(){


        telemetry.addData("Arm Pos",gilgearmesh.getArmPosition());
        telemetry.addData("Color",jewelColor);
        telemetry.addData("Case",stateMachineFlow);
        telemetry.update();}*/

    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                gilgearmesh.armPos(50,.6);
                telemetry.addData("Color",jewelColor);
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                time = getRuntime();
                stateMachineFlow++;
                break;

            case 1:
                sensArm.wristPos(WristServoPosition.CENTER);
                sensArm.armPos(ArmServoPosition.BOTTOM);
                sensArm.colorLED(true);
                if (1 < getRuntime() - time ) {
                    stateMachineFlow++;
                }
                break;

            case 2:
                    if (sensArm.colorSens() == JewelColor.BLUE){
                        jewelColor = JewelColor.BLUE;
                        telemetry.addData("Color",jewelColor);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}
                    else if (sensArm.colorSens() == JewelColor.RED){
                        jewelColor = JewelColor.RED;
                        telemetry.addData("Color",jewelColor);
                        telemetry.addData("Case",stateMachineFlow);
                        telemetry.update();}

                    stateMachineFlow++;
                    break;
            case 3:
                //knock off correct jewel
                if (jewelColor == JewelColor.BLUE){sensArm.wristPos(WristServoPosition.RIGHT);}
                else if (jewelColor == JewelColor.RED){sensArm.wristPos(WristServoPosition.LEFT);}
                else if (jewelColor == JewelColor.UNKNOWN){sensArm.armPos(ArmServoPosition.TOP);}

                time = getRuntime();

                telemetry.addData("Jewel Arm",sensArm.getArmPosition());
                telemetry.addData("Jewel Wrist",sensArm.getWristPosition());
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                stateMachineFlow++;
                break;
            case 4:
                if (2 < getRuntime() - time){
                    sensArm.wristPos(WristServoPosition.CENTER);
                    sensArm.armPos(ArmServoPosition.TOP);
                    stateMachineFlow++;
                }
                break;
            case 5:
                robot.linearDrive(.25,-20);
                stateMachineFlow++;
                break;
            case 6:
                robot.statTurn(.5,50);
                stateMachineFlow++;
                break;
            case 7:
                robot.linearDrive(.25,-2);
                stateMachineFlow++;
                break;
            case 8:
                //end?
                break;
        }
    }
}//end of class

