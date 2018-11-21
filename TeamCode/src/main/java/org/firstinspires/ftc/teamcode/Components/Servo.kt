import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.Utils.Logger



public class Servo(val hardwareMap: HardwareMap, val name:String){
    val servo = hardwareMap.servo.get(name)
    val l = Logger("SERVO", name)
    val POSITION_TO_ANGLE = 1.8; // use angle to decide position
    var INIT_ANGLE = 90
    var MAX_POSITION = 180
    var MIN_ANGLE = 0


    fun setPosition(angle:Int){
        servo.position = angle/POSITION_TO_ANGLE
    }

    fun setMaxAngle(){
        setPosition(MAX_POSITION)
    }

    fun setMinAngle(){
        setPosition(MIN_ANGLE)
    }

    fun setInitAngle(){
        setPosition(INIT_ANGLE)
    }

    fun setDirection(direction:com.qualcomm.robotcore.hardware.Servo.Direction){
        servo.direction = direction
    }

    fun logInfo(){
        l.logData("Position",servo.position)
        l.logData("Angle",servo.position*POSITION_TO_ANGLE)
        l.logData("Direction",servo.direction.toString())
    }

}