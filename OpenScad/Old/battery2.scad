

gridThickness = 1;
gridDepth = 24;

gridLength = 200;
gridSideLength = 32;
gridSpacing = gridSideLength*cos(30);

perimiterWidth = 2*gridSpacing;
perimiterHeight = 3 * gridSideLength;
perimiterThickness = 2;



holeDepth = 4;
holeOffset = 64;
holeRadius = 2.1;
holeFacets = 72;

rotate([0,180,0])
difference() {
    union() {

        translate([12.5,0,24])
            cube([24,136,2],center=true);
        translate([0,0,10])
            cube([49,114,30],center=true);
        
        translate([21,0,-6.5])
            cube([7,80,40],center=true);
    }
        translate([0,0,-1.5])
        color("green")
            cube([47,112,48], center=true);


    cube([50,64,100], center=true);
    translate([10,40,-28])
        cube([50,2,100], center=true);
    translate([10,-40,-28])
        cube([50,2,100], center=true);
    
    translate([12.5,holeOffset,24])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([12.5,-holeOffset,24])
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
