package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

import com.qualcomm.robotcore.hardware.Servo;

import org.chathamrobotics.common.hardware.utils.HardwareListener;

/**
 * A gripper system
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Gripper implements System {
    public static final double[] DEFAULT_OPEN_POSITIONS = {0.6, 0.4};
    public static final double[] DEFAULT_CLOSED_POSITIONS = {0, 1};
    public static final double[] DEFAULT_GRIPPING_POSITIONS = {0.45, 0.55};
    public static final double DEFAULT_TOLERANCE = 0.01;

    public enum State {
        OPEN,
        CLOSED,
        GRIPPING
    }

    private final double[] openPositions, closedPositions, grippingPositions;
    private final Servo leftClaw, rightClaw;
    private final HardwareListener listener;

    private double tolerance = DEFAULT_TOLERANCE;
    private State state;

    /**
     * Creates a new instance of {@link Gripper}
     * @param leftClaw          the left claw
     * @param rightClaw         the right claw
     * @param listener          the hardware listener
     */
    public Gripper(Servo leftClaw, Servo rightClaw, HardwareListener listener) {
        this(leftClaw, rightClaw, listener, DEFAULT_OPEN_POSITIONS, DEFAULT_CLOSED_POSITIONS, DEFAULT_GRIPPING_POSITIONS);
    }

    /**
     * Creates a new instance of {@link Gripper}
     * @param leftClaw          the left claw
     * @param rightClaw         the right claw
     * @param listener          the hardware listener
     * @param openPositions     the open positions for the servos. The left claw's position should be at index 0
     * @param closedPositions   the closed positions for the servos. The left claw's position should be at index 0
     * @param grippingPositions the gripping positions for the servos. The left claw's position should be at index 0
     */
    public Gripper(Servo leftClaw, Servo rightClaw, HardwareListener listener, double[] openPositions, double[] closedPositions, double grippingPositions[]) {
        this.leftClaw = leftClaw;
        this.rightClaw = rightClaw;

        this.listener = listener;

        this.openPositions = openPositions;
        this.closedPositions = closedPositions;
        this.grippingPositions = grippingPositions;
    }

    /**
     * Sets the tolerance for the servo positions
     * @param tolerance the tolerance for the servo positions
     */
    public void setTolerance(double tolerance) {this.tolerance = tolerance;}

    /**
     * Returns the grippers current state
     * @return  the grippers current state
     */
    public State getState() {
        return this.state;
    }

    /**
     * Opens
     */
    public void open() {
        leftClaw.setPosition(openPositions[0]);
        rightClaw.setPosition(openPositions[1]);

        listener.once(leftClaw,
                (Servo claw) -> clawsAreAtTarget(openPositions),
                () -> state = State.OPEN,
                2000
        );
    }

    /**
     * Opens synchronously (blocks)
     * @throws InterruptedException thrown if the thread is interrupted
     */
    public void openSync() throws InterruptedException {
        leftClaw.setPosition(openPositions[0]);
        rightClaw.setPosition(openPositions[1]);

        while (! clawsAreAtTarget(openPositions))
            Thread.sleep(10);

        this.state = State.OPEN;
    }

    /**
     * Closes
     */
    public void close() {
        leftClaw.setPosition(closedPositions[0]);
        rightClaw.setPosition(closedPositions[1]);

        listener.once(leftClaw,
                (Servo claw) -> clawsAreAtTarget(closedPositions),
                () -> state = State.CLOSED,
                2000
        );
    }

    /**
     * Closes synchronously (blocks)
     * @throws InterruptedException thrown if the thread is interrupted
     */
    public void closeSync() throws InterruptedException {
        leftClaw.setPosition(closedPositions[0]);
        rightClaw.setPosition(closedPositions[1]);

        while (! clawsAreAtTarget(closedPositions))
            Thread.sleep(10);

        this.state = State.CLOSED;
    }

    /**
     * Grips
     */
    public void grip() {
        leftClaw.setPosition(grippingPositions[0]);
        rightClaw.setPosition(grippingPositions[1]);

        listener.once(leftClaw,
                (Servo claw) -> clawsAreAtTarget(grippingPositions),
                () -> state = State.GRIPPING,
                2000
        );
    }

    /**
     * Grips synchronously (blocks)
     * @throws InterruptedException thrown if the thread is interrupted
     */
    public void gripSync() throws InterruptedException {
        leftClaw.setPosition(grippingPositions[0]);
        rightClaw.setPosition(grippingPositions[1]);

        while (! clawsAreAtTarget(grippingPositions))
            Thread.sleep(10);

        this.state = State.GRIPPING;
    }

    @Override
    public void stop() {
        listener.removeAllListeners(leftClaw);
    }

    private boolean clawsAreAtTarget(double[] target) {
        return inTolerance(leftClaw.getPosition(), target[0]) && inTolerance(rightClaw.getPosition(), target[1]);
    }

    private boolean inTolerance(double current, double target) {
        return Math.abs(current - target) <= tolerance;
    }
}
