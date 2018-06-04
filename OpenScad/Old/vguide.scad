

holeShift=5+16;
holeDepth = 6;
holeOffset = 8;
holeRadius = 2;

mountHeight=8;
mountWidth=32;
mountOffset=0.5;

gridWidth=59;
braceLength=gridWidth*sqrt(2)/2+2;

holeFacets = 144;

difference() {
    union() {
        translate([-(mountWidth+gridWidth)/2,-mountOffset,0])
            cube([mountWidth,2,mountHeight],center=true);
        
        translate([(mountWidth+gridWidth)/2,-mountOffset,0])
            cube([mountWidth,2,mountHeight],center=true);
        
        translate([-gridWidth/4,gridWidth/4,0])
        rotate([0,0,45])
        cube([braceLength, 2, mountHeight], center=true);

        translate([gridWidth/4,gridWidth/4,0])
        rotate([0,0,-45])
        cube([braceLength, 2, mountHeight], center=true);
    }
    translate([-(mountWidth+gridWidth)/2,0,0])
        rotate([90,0,0])
            Holes();
    translate([(mountWidth+gridWidth)/2,0,0])
        rotate([90,0,0])
            Holes();
}


module Holes() {
    translate([holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([-holeOffset,0,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
   /* translate([0,holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);
    translate([0,-holeOffset,0])
        cylinder(r=holeRadius,h=holeDepth,
                 center=true, $fn=holeFacets);*/
}
