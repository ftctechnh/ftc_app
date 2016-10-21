package org.firstinspires.ftc.teamcode;
        import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
        import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
        import com.qualcomm.robotcore.eventloop.opmode.Disabled;
        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.hardware.VoltageSensor;
        import com.qualcomm.robotcore.util.ElapsedTime;
        import org.firstinspires.ftc.robotcore.external.Func;
        import org.firstinspires.ftc.robotcore.external.Telemetry;
/**
 * Created by FTC Team 4799-4800 on 10/13/2016.
 */
@Autonomous(name = "TinyAuto", group = "")
public class TinyAuto extends LinearOpMode {
        ElapsedTime poemElapsed = new ElapsedTime();
        ModernRoboticsI2cRangeSensor rangeSensor;
        ModernRoboticsI2cColorSensor colorSensor;
        DcMotor M1;
        Servo S1;
        DcMotor motorFrontLeft;
        DcMotor motorBackRight;
        DcMotor motorFrontRight;
        DcMotor motorBackLeft;


        @Override
        public void runOpMode() throws InterruptedException {
            rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "RF");
            colorSensor = hardwareMap.get(ModernRoboticsI2cColorSensor.class, "CS");

            motorBackRight = hardwareMap.dcMotor.get("RightBack");
            motorFrontRight = hardwareMap.dcMotor.get("RightFront");
            motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
            motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
            //M1 = hardwareMap.dcMotor.get("M1");
            S1 = hardwareMap.servo.get("S1");
            telemetry.log().setCapacity(6);
            ElapsedTime opmodeRunTime = new ElapsedTime();
            while (!isStarted()) {
                telemetry.addData("time", "%.1f seconds", opmodeRunTime.seconds());
                telemetry.update();
                idle();
            }
            while (opModeIsActive()) {
                telemetry.update();
                telemetry.addData("Optical Range ", rangeSensor.cmOptical());
                telemetry.addData("Ultrasonic Range ", rangeSensor.cmUltrasonic());
                telemetry.addData("Red Value ", colorSensor.red());
                telemetry.addData("Green Value ", colorSensor.green());
                telemetry.addData("Blue Value ", colorSensor.blue());
                telemetry.addData("Alpha Value ", colorSensor.alpha());

                if (getRuntime() < 5 || colorSensor.blue() > 2) {
                    motorBackLeft.setPower(1);
                    motorFrontLeft.setPower(1);
                    motorBackRight.setPower(-1);
                    motorFrontRight.setPower(-1);
                }

                else {
                    motorBackLeft.setPower(0);
                    motorFrontLeft.setPower(0);
                    motorBackRight.setPower(0);
                    motorFrontRight.setPower(0);
                }
                idle();
            }
        }
    }