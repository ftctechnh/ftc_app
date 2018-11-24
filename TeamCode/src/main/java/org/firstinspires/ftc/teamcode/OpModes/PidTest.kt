package org.firstinspires.ftc.teamcode.OpModes

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.Components.DriveTrain
import org.firstinspires.ftc.teamcode.Models.Direction
import org.firstinspires.ftc.teamcode.Models.PIDConstants
import org.firstinspires.ftc.teamcode.Utils.Logger
import org.firstinspires.ftc.teamcode.Utils.WSS
import org.firstinspires.ftc.teamcode.Utils.getPIDConstantsFromFile

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "PID Test")

class  PidTest: LinearOpMode(){
    val l: Logger = Logger("PID Test")
    val pid: PIDConstants = getPIDConstantsFromFile("pid_rotation.json")
    init{
        l.log("Initialized")
        telemetry.addData("PID",pid.toString())
        l.log(pid.toString())
    }

    override fun runOpMode() {
        val dt = DriveTrain(this)
        l.log("waiting for start")
        telemetry.update()
//        val wss = WSS(port=3000,path="/pid")
//        wss.start()
        waitForStart()
        l.log("Opmode Started")
        if(opModeIsActive() && !isStopRequested){
            l.log("Starting turn")
            dt.rotate(Direction.SPIN_CCW,90,10,broadcast=false,pidConstants = pid)
            l.log("Finished turn")
        }

    }

}
