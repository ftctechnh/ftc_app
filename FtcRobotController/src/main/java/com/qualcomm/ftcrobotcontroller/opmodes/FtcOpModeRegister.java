package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.technicbots.Testing1010;
public class FtcOpModeRegister implements OpModeRegister
{public void register(OpModeManager manager)
{manager.register("Calibration",Calibration.class);manager.register("Robot4Teleop",Robot4Teleop.class);manager.register("ResQAuto_D_R",ResQAutoDelayRed.class);manager.register("ResQAuto_D_B",ResQAutoDelayBlue.class);manager.register("ResQAuto_R",ResQAutoRed.class);manager.register("ResQAuto_B",ResQAutoBlue.class);manager.register("ResQPitCrew",ResQTeleopPitCrew.class);}}