package org.firstinspires.ftc.robotcontroller.internal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.text.SimpleDateFormat;
import java.util.Date;

@Autonomous(name="NullOp", group="Tests")
public class NullOp extends OpMode {

  private String startDate;
  private ElapsedTime runtime = new ElapsedTime();

  @Override
  public void init() {
  }

  @Override
  public void init_loop() {
    startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    runtime.reset();
    telemetry.addData("Null Op Init Loop", runtime.toString());
  }

  @Override
  public void loop() {
    telemetry.addData("1 Start", "NullOp started at " + startDate);
    telemetry.addData("2 Status", "running for " + runtime.toString());
  }
}
