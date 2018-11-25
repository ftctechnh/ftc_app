package teamcode.examples;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.Comparator;

public class MineralComparator implements Comparator<Mineral> {

    public MineralComparator() {
    }

    @Override
    public int compare(Mineral mineral, Mineral t1) {
        if (mineral.getLeft() < t1.getLeft()) {
            return -1;
        }

        if (mineral.getLeft() > t1.getLeft()) {
            return 1;
        }

        return 0;
    }
}
