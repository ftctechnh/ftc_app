
width=12;
holeOffset = 8;
holeRadius = 2;

holeFacets = 144;

height=40;
flagRadius = 5;

difference() {
    union() {
        cube([40,1,width]);
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
            cube([2*(flagRadius+1),(flagRadius+1),width]);
            cylinder(r=flagRadius+1,h=width,$fn=holeFacets);
        }
        
        translate([0,0,-2])
        cylinder(r=flagRadius, h=width+4, $fn=holeFacets);
        
        translate([0,-flagRadius,width/2])
        rotate([0,20,0])
        cube([4,flagRadius,width+4],center=true);
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