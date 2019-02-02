import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.ChassisConfig;
import org.firstinspires.ftc.teamcode.DelayBullDepot;
import org.firstinspires.ftc.teamcode.TeamMarkerCrater;

@Autonomous(name="Phat; MarkerCrater", group="BPhatSwipe")
public class PhatSwipeDleyaBullDepot extends DelayBullDepot {
    public PhatSwipeDleyaBullDepot() {
        super(ChassisConfig.forPhatSwipe());
    }
}