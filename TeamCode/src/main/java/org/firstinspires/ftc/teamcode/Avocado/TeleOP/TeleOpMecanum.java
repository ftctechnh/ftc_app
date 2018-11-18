package org.firstinspires.ftc.teamcode.Avocado.TeleOP;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import junit.framework.Test;
import org.firstinspires.ftc.teamcode.Avocado.Hardware.hardwaremap_TeleOpMecanum;

@TeleOp(name = "Avacado TeleOp", group = "Test")


public class TeleOpMecanum extends OpMode {


    hardwaremap_TeleOpMecanum robot = new hardwaremap_TeleOpMecanum();

    byte posleft = -1;


    @Override
    public void loop() {
        dpad();
        TankDrive();
        //angles();
        //collect();
        //lift();
    }

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    public void TankDrive() {

        float leftY_gp1 = (-gamepad1.left_stick_y);
        float rightY_gp1 = (gamepad1.right_stick_y);

        robot.topLeftMotor.setPower(leftY_gp1);
        robot.bottomLeftMotor.setPower(leftY_gp1);
        robot.topRightMotor.setPower(rightY_gp1);
        robot.bottomRightMotor.setPower(rightY_gp1);

    }

    public void dpad(){
        if (gamepad1.dpad_left){
            robot.topLeftMotor.setPower(1 * posleft);
            robot.bottomLeftMotor.setPower(-1 * posleft);
            robot.topRightMotor.setPower(-1);
            robot.bottomRightMotor.setPower(1);
        }
        else if (gamepad1.dpad_right){
            robot.topLeftMotor.setPower(-1 * posleft);
            robot.bottomLeftMotor.setPower(1 * posleft);
            robot.topRightMotor.setPower(1);
            robot.bottomRightMotor.setPower(-1);
        }
        else if (gamepad1.dpad_up) {
            robot.topLeftMotor.setPower(0.5 * posleft);
            robot.bottomLeftMotor.setPower(0.5 * posleft);
            robot.topRightMotor.setPower(0.5);
            robot.bottomRightMotor.setPower(0.5);


        }
        else if (gamepad1.dpad_down) {

            robot.topLeftMotor.setPower(-0.5 * posleft);
            robot.bottomLeftMotor.setPower(-0.5 * posleft);
            robot.topRightMotor.setPower(-0.5);
            robot.bottomRightMotor.setPower(-0.5);

        }
    }

/*    public void collect() {




    } */

/*     Note that x, y, and z serve as placeholder values for the position of the motor: uncommenting
    this will cause syntax errors as the placeholder variables are undefined.

        public void angles() {

            if(gamepad2.dpad_up) {

                while(robot.tiltMotor.getCurrentPosition() < x) {

                    robot.tiltMotor.setPower(0.75);

                }

                while(robot.tiltMotor.getCurrentPosition() > x) {

                    robot.tiltMotor.setPower(-0.75);

                }

            }

            if(gamepad2.dpad_left) {

                while(robot.tiltMotor.getCurrentPosition() > y) {

                    robot.tiltMotor.setPower(-0.75);

                }

                while(robot.tiltMotor.getCurrentPosition() < y) {

                    robot.tiltMotor.setPower(0.75);

                }

            }

            if(gamepad2.dpad_down) {

                while(robot.tiltMotor.getCurrentPosition() > z) {

                    robot.tiltMotor.setPower(-0.75);

                }

                while(robot.tiltMotor.getCurrentPosition() < z) {

                    robot.tiltMotor.setPower(0.75);

                }

            }

            }
    */
/*
    public void lift() {

        float leftY_gp2 = (-gamepad2.left_stick_y);
        float rightY_gp2 = (-gamepad2.right_stick_y);

    }
*/
    public void hangar(){



    }

    public void stop(){

    }

}