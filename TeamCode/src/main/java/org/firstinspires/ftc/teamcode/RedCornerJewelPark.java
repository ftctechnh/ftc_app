package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Jewel Park: Red Corner", group="Pushbot")
//@Disabled
public class RedCornerJewelPark extends OpMode{

    private int stateMachineFlow;
    RelicDrive robot       = new RelicDrive();
    private ElapsedTime     runtime = new ElapsedTime();

    GlyphArm gilgearmesh = new GlyphArm();

    JewelSystem sensArm = new JewelSystem();
    JewelColor jewelColor = JewelColor.UNKNOWN;
    double time;

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
        gilgearmesh.clawPos(1);
        //wait needed? Also... guessed parameters
        //gilgearmesh.armPos(1000,.6);
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
        telemetry.update();
    }*/


    @Override
    public void loop() {
        switch(stateMachineFlow){
            case 0:
                runtime.reset();
                gilgearmesh.armPos(50,.6);
                telemetry.addData("Color",jewelColor);
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                stateMachineFlow++;
                break;

            case 1:
                sensArm.armPos(ArmServoPosition.BOTTOM);
                sensArm.colorLED(true);
                time = getRuntime();
                while (jewelColor == JewelColor.UNKNOWN && getRuntime() < time + 3){
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
                }
                sensArm.colorLED(false);
                stateMachineFlow++;
                break;
            case 2:
                //knock off correct jewel
                if (jewelColor == JewelColor.BLUE){sensArm.wristPos(WristServoPosition.LEFT);
                    sensArm.wristPos(WristServoPosition.MID);
                    sensArm.wristPos(WristServoPosition.CENTER);
                    sensArm.armPos(ArmServoPosition.TOP);}
                else if (jewelColor == JewelColor.RED){sensArm.wristPos(WristServoPosition.RIGHT);
                    sensArm.armPos(ArmServoPosition.TOP);
                    sensArm.wristPos(WristServoPosition.CENTER);}
                else if (jewelColor == JewelColor.UNKNOWN){sensArm.armPos(ArmServoPosition.TOP);}

                telemetry.addData("Jewel Arm",sensArm.getArmPosition());
                telemetry.addData("Case",stateMachineFlow);
                telemetry.update();
                stateMachineFlow++;
                break;
            case 3:
                gilgearmesh.armPos(25,.6);
                robot.linearDrive(.25,20);
                stateMachineFlow++;
                break;
            case 4:
                robot.statTurn(.5,90);
                stateMachineFlow++;
                break;
            case 5:
                //robot.linearDrive(.25,-2);
                stateMachineFlow++;
                break;
            case 6:
                //end?
                break;
        }
    }
}//end of class

