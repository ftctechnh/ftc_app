package org.firstinspires.ftc.teamcode.Models

enum class Direction(val intRepr: Int) {
    FORWARD(1),
    BACKWARD(-1),
    SPIN_CW(1),
    SPIN_CCW(-1),
    FORWARD_CCW(1),
    FORWARD_CW(-1),
    BACKWARD_CCW(1),
    BACKWARD_CW(-1)
}