package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="relic claw", group="TeleOp")
public class okay extends OpMode {
    private DcMotor clawMotor  = null;
    private Servo thiccClaw1   = null;
    private Servo thiccClaw2   = null;
    private Servo blockEjector = null;
    @Override
    public void init() {
        telemetry.addData("Status", "Uninitialized...");
        //thiccClaw1 = hardwareMap.get(Servo.class, "thiccClaw1");
        //thiccClaw2 = hardwareMap.get(Servo.class, "thiccClaw2");
        blockEjector = hardwareMap.get(Servo.class, "blockEjector");
        //clawMotor = hardwareMap.get(DcMotor.class, "clawMotor");
        //clawMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //clawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        telemetry.addData("Status", "Initialized");
    }

    double CLOSE_POS = 0;
    double OPEN_POS = 0.75;
    @Override
    public void loop() {

        /*
        if(gamepad1.x){
            clawMotor.setPower(0.1);
        } else if (gamepad1.y) {
            clawMotor.setPower(-0.1);
        } else {
            clawMotor.setPower(0);
        }
        */
        if (gamepad1.a) {
            blockEjector.setPosition(1);
        } else if (gamepad1.b) {
            blockEjector.setPosition(0);
        }
            //telemetry.addData("enco", clawMotor.getCurrentPosition());

        /*
        if (gamepad1.a) {
            thiccClaw1.setPosition(CLOSE_POS);
            thiccClaw2.setPosition(CLOSE_POS);
        }else if (gamepad1.b){
            thiccClaw1.setPosition(OPEN_POS);
            thiccClaw2.setPosition(OPEN_POS);
        }
        */
}
}
