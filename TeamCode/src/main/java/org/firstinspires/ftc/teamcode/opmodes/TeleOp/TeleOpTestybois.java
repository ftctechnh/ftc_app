/*package org.firstinspires.ftc.teamcode.opmodes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Hardware.controller.Controller;
import org.firstinspires.ftc.teamcode.Hardware.controller.TriggerType;
import org.firstinspires.ftc.teamcode.systems.arm.ArmSystem;

@TeleOp(name= "cats")
public class TeleOpTestybois extends TeleOpMode {

    Telemetry.Item lastButtonTelemetryItem;
    public ElapsedTime elapsedTime = new ElapsedTime();
    private boolean superDrive;
    private boolean slowDrive = false;
    DcMotor motor1;
    Controller controller1 = new Controller(gamepad1);
    Controller controller2 = new Controller(gamepad1);



    public TeleOpTestybois()
    {
        super("TeleOpTestybois");
    }

    @Override
    public void loop() {
        for(int i = 0; i > 50; i++){
            telemetry.addData("Evan is this many stupids:", i);
            telemetry.update();
        }
    }

    @Override
    public void init() {


    }

}
*/
