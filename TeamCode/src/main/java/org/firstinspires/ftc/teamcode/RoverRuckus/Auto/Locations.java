package org.firstinspires.ftc.teamcode.RoverRuckus.Auto;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Locations {
    //positions of game elements -- these won't change
    static final Vector2D craterCorner = new Vector2D(0,0);
    static final Vector2D depotCorner = new Vector2D(0,0);

    static final Vector2D[] particlesRelative = {
            new Vector2D(0,0),
            new Vector2D(0,0),
            new Vector2D(0,0)
    };
    static Vector2D depotParticles(int i) {
                                    //partclesRelative needs to be rotated accordingly
        return craterSideStart().add(particlesRelative[i]);
    }
    static Vector2D craterParticles(int i) {
                                    //partclesRelative needs to be rotated accordingly
        return craterSideStart().add(particlesRelative[i]);
    }

    //no-go zones
    //crater legs
    //craters
    //minerals
    //




    //objectives -- places we want to go. Change these!
    static final Vector2D startRelative = new Vector2D(0,0);
    static final Vector2D craterSideStart() {
        return new Vector2D(0,0);
    }
    static final Vector2D depotSideStart() {
        return new Vector2D(0,0);
    }

    //relative positions
    static final Vector2D markerDepositRelative = new Vector2D(0,0);
    static final Vector2D depotParkRelative = new Vector2D(0,0);
    static final Vector2D craterParkRelative = new Vector2D(0,0);

    static Vector2D markerDeposit() { return depotCorner.add(markerDepositRelative); }
    static Vector2D depotPark() { return depotCorner.add(depotParkRelative); }
    static Vector2D craterPark() { return craterCorner.add(craterParkRelative); }
}
