

gridThickness = 1;
gridDepth = 24;

gridLength = 200;
gridSideLength = 32;
gridSpacing = gridSideLength*cos(30);

perimiterWidth = 2*gridSpacing;
perimiterHeight = 3 * gridSideLength;
perimiterThickness = 2;



holeDepth = 4;
holeOffset = 24;
holeRadius = 2;
holeFacets = 72;

rotate([0,180,0])
difference() {
    union() {
        translate([0,5.5,0])
        cube([54,2,8],center=true);
        translate([0,0,-4])
            cube([33,13,16], center=true);

    }
    color("green")
        cube([31,11,30], center=true);
    
    translate([holeOffset,5,0])
        rotate([90,0,0])
            cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-holeOffset,5,0])
        rotate([90,0,0])
            cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
}



module Holes() {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
}
