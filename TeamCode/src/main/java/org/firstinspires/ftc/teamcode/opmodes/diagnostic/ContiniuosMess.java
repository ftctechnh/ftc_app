package org.firstinspires.ftc.teamcode.opmodes.diagnostic;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 3/6/2018.
 */

@TeleOp(name="Servo Fix")
//@Disabled
public class ContiniuosMess extends OpMode {
    private enum Incs {
        SMALL(10),
        MEDIUM(25),
        EHH(100),
        HOLYMOTHER(500);

        public double inc;
        Incs(double inc) {
            this.inc = inc;
        }
    }


    private CRServo stupid;
    private ServoControllerEx stupidControl;
    private DigitalChannel out;
    private int port;
    private boolean lastDPad = false;
    private boolean lastA;
    private int incIndex = 0;

    public void init() {
        stupid = hardwareMap.get(CRServo.class, "crl");
        stupidControl = (ServoControllerEx) stupid.getController();
        out = hardwareMap.get(DigitalChannel.class, "stupid");
        out.setMode(DigitalChannel.Mode.OUTPUT);
        stupid.getPortNumber();
    }

    public void start() {
        stupid.setPower(1);
    }

    public void loop() {
        PwmControl.PwmRange range = stupidControl.getServoPwmRange(port);
        if(!lastDPad) {
            if (gamepad1.dpad_up) range = new PwmControl.PwmRange(100, range.usPulseUpper + Incs.values()[incIndex].inc);
            else if (gamepad1.dpad_down) range = new PwmControl.PwmRange(100, range.usPulseUpper - Incs.values()[incIndex].inc);
            else if (gamepad1.dpad_left) incIndex = Range.clip(incIndex - 1, 0, Incs.values().length - 1);
            else if (gamepad1.dpad_right) incIndex = Range.clip(incIndex + 1, 0, Incs.values().length - 1);
            stupidControl.setServoPwmRange(port, range);
            stupid.setPower(1);
        }

        lastDPad = gamepad1.dpad_up || gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_down;

        if(!lastA && gamepad1.a) out.setState(!out.getState());
        lastA = gamepad1.a;

        telemetry.addData("Servo Duty Cycle", range.usPulseUpper);
        telemetry.addData("Increment", Incs.values()[incIndex].inc);
        telemetry.addData("Digital Out", out.getState());
    }

}
