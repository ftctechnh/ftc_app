package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.CENTER;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.LEFT;
import static org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark.RIGHT;
//blue alliance,drive forward, turn

@Autonomous
//@Disabled
public class WestAutonomous2 extends OpMode
{

    private DcMotor m1, m2;
    private Servo drop1,drop2,arm;
    private ColorSensor cs;


    private double drop1Min =0.5 ;
    private double drop2Max = 0.5;
    private double interval = 0.01;

    private double extendedArm = 0.5;



    private int leftPos = 200;
    private int centerPos = 400;
    private int rightPos = 600;

    private int movePos1 = 200;
    private int movePos2 = 50;
    private int target;

    private int rotatePos = 300;

    public static final String TAG = "Vuforia VuMark Sample";



    OpenGLMatrix lastLocation = null;


    VuforiaLocalizer vuforia;

    VuforiaTrackable relicTemplate;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        m1 = hardwareMap.get(DcMotor.class, "Motor1");
        m2 = hardwareMap.get(DcMotor.class, "Motor2");

        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        drop1 = hardwareMap.get(Servo.class, "Drop1");
        drop2 = hardwareMap.get(Servo.class, "Drop2");
        arm = hardwareMap.get(Servo.class,"Arm");

        cs  =hardwareMap.get(ColorSensor.class,"Color_Sensor");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "ASHHQIj/////AAAAGXYJVW7cAUFsgkCKi6Nhif9NSD8eT9qEc1ACRGnc4hTvEXGEXuyMiud30yKeNLWeVSRaauEwn7EsvmW3hLBIPAoP1O5tVZ3AKXoP7gtDsaqTX9zP457gxk4vDSBmO4vNsHgl4sa+ijZTb50UJ3nvA92U/4lPicHcyLYenOpWGXEe/4MJF1P6uQ/Hp4M0n2mrLRw8Q5xzjyV5CSY+Vv1H4/mCYH0zlwOl/ScN/ibmouIT6mOJfWQjlvx8xaIpbg5slN5j7L7CGrRs2284z1WF6aSn7Fo20IUe/FevV+ZLKmgAMZGmizpx91L7SskSWNSyjn6S/a/wDVPCScUm/iEEouKOEAa4yMkCYll62tn6VR8q";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");

        relicTrackables.activate();

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop(){

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        arm.setPosition(extendedArm);
        boolean done = false;

        while (!done) {
            double l = cs.alpha();
            double[] colors = {cs.red(), cs.green(), cs.blue()};

            if (l >= 2) {
                if ((colors[0] >= 2) && (colors[1] <= 5) && (colors[2] <= 5)){
                    rotate(true);
                    done = true;
                    rotate(false);
                }
                else if ((colors[0] <= 5) && (colors[1] <= 5) && (colors[2] >= 2)){
                    rotate(false);
                    done = true;
                    rotate(false);
                }

            }
        }

        arm.setPosition(0);
        done = false;

        while (!done){
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN){

                if (vuMark == RIGHT){
                    target = rightPos;
                }
                else if(vuMark == CENTER){
                    target = centerPos;
                }
                else if (vuMark == LEFT){
                    target = leftPos;
                }
                done = true;
            }
        }

        move(movePos1);

        rotate(true);

        move(target);

        rotate(false);

        move(movePos2);

        dropBlock();

        move(-movePos2);

        rotate(false);

        move(target);

        rotate(false);

        move(movePos1);







    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {



    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    private void rotate(boolean right){
        double power = 0.0;
        if (right) {
            m1.setTargetPosition(rotatePos);
            m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            power = 0.2;

        }
        else{
            m1.setTargetPosition(-rotatePos);
            m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        while (m1.isBusy()) {
            m1.setPower(power);
            m2.setPower(-power);
        }
        m1.setPower(0);
        m2.setPower(0);
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    private void move(int encoder){
        double power = 0;
        m1.setTargetPosition(encoder);
        m1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        int current = m1.getCurrentPosition();

        if (encoder > current){
            power = 0.5;
        }
        else{
            power  = -0.5;
        }

        while (m1.isBusy()){
            m1.setPower(power);
            m2.setPower(power);
        }

        m1.setPower(0);
        m2.setPower(0);
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void dropBlock(){
        double current1 = 1;
        double current2 = 0;

        while ((current1 >= drop1Min) || (current2 <= drop2Max)){
            current1 = (current1 > drop1Min)? current1 - interval:current1;
            current2 = (current2 < drop2Max)? current2 + interval:current2;
            drop1.setPosition(current1);
            drop2.setPosition(current2);
        }
    }


}
