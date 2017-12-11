
holeOffset = 8;
holeRadius = 2;

holeFacets = 144;

height=40;
flagRadius = 5;

difference() {
    union() {
        cube([40,1,12]);
        translate([20,-(flagRadius),0])
            Holder();
    }

    translate([4,0,6])
    rotate([90,0,0])
    translate([0,0,-2.5])
    cylinder(r=holeRadius, h=4, $fn = holeFacets);

    translate([36,0,6])
    rotate([90,0,0])
    translate([0,0,-2.5])
    cylinder(r=holeRadius, h=4, $fn = holeFacets);
}

module Holder() {
    difference() {
        union() {
        translate([-(flagRadius+1),0,0])
            cube([2*(flagRadius+1),(flagRadius+1),12]);
            cylinder(r=flagRadius+1,h=height,$fn=holeFacets);
        }
        
        translate([0,0,4])
        cylinder(r=flagRadius, h=height-2, $fn=holeFacets);
    }
}

module Holes() {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=2*sliderDepth, center=true, $fn=holeFacets);
}