#Pure Pursuit Design Methodology

*How it works*: Pure pursuit is a broad class of path following algorithms that work by using a
_look-ahead_ distance. Many tutorials exist, including one by world champion team Gluten Free, but
this (and most other) tutorials are for mecanum drives only. As such, team 8802's software wizard
decided to design his own algorithm.

PPAF (Pure Pursuit Arc Following) works by extending a look-ahead distance and tracking to the
farthest ahead point on the look ahead, just like any other pure pursuit algorithm. PPAF's second
insight, however, is that if constant wheel powers are used, a tank drive can only move in circluar
arcs of varying radii. For example, a tank drive can perform a point turn (a circle with radius 0),
can drive straight (a circle with radius infinity), or can turn around another point, including one
the robot is on top of.

Thus to drive to the point pure pursuit has determined, PPAF draws a circlar arc between the robot's
current position (as determined through encoder wheels) and the target position, being sure the
arc is tangent to the robot's current point. Note that the endpoint of the arc may not (and probably
will not be) tangent to the line the robot wishes to follow, but that's OK. As the robot approaches
the line it wants to follow, its target position will slide forward, ensuring the robot regains the
line it wants to follow.

The details of calculating the arc may be found in PurePursuitController, but here we will discuss
how we make the robot follow an arc exactly. First, we assume the robot has a constant track width.
This can be either measured or determined emperically (RoadRunner has a TrackWidthCalculator I'll
probably steal). Next, recall the following equation:

turn radius = (movement x) / (movement theta)

This should be intuitively clear, if one imagines how this equation behaves at its endpoints. Now,
let us consider a more in-depth example. Consider a robot wiht a track width of two inches (thus
a track radius of one inch). Now imagine that we can control the exact speed its wheels move along
the ground. Each wheel can move at most 1 in / sec.

If this robot were to perform a point turn, it would move both its wheels in opposite directions.
If it did so, it would expend its entire energy budget. Since robot turning is only caused by an
imbalance of the two wheel powers, the

The robot's lateral movement is the average of its two wheel powers. This means if one wheel is set
to move at 0 in / sec, and the other at 1 in / sec, the robot as a whole will move at 0.5 in / sec.

Note also that the robot's heading movement is the *difference* of its wheel powers. Thus in the
above example, the robot would move heading-wise as though one of its wheels had power -0.5 in / sec
and the other 0.5 in / sec. Thus, the robot would turn at:

0.5 in / sec / (track radius of 1 in) = 0.5 radians / sec.

OR

(1 in / sec difference of speeds) / (track width of 2 in) =  0.5 radians / sec

Now we can also calculate the robot's true turn radius.

(0.5 in / sec) / (0.5 radians / sec) = 1 inch

This is clearly true, as in the above case one of the robot's wheels is stationary.

But now, we must go the other way. To assist ourselves, let us define some variables.

```
MAX_WHEEL_VELOCITY = 60 in / sce

TRACK_WIDTH = 16.5 in

TORQUE_BUDGET_USE = 0.5 // Use half our torque, but we'll worry about this afterwards

TURN_RADIUS = x
```

```
TURN_RADIUS = (TRACK_WIDTH * AVERAGE_OF_SPEEDS) / (DIFFERENCE OF SPEEDS);

TURN_RADIUS / TRACK_WIDTH = (GREATER + LESSER) / 2(GREATER - LESSER)

TURN_RADIUS / TRACK_WIDTH = (TORQUE_BUDGET_USE + LESSER) / 2(TORQUE_BUDGET_USE - LESSER)

k = (c + x) / 2(c - x)
2k(c - x) = c + x
2kc - c = x + 2kx
c(2k - 1) = x (2k + 1)

LESSER = TORQUE_BUDGET_USE * (2k - 1) / (2k + 1)
```


