
holeDepth = 4;
holeOffset = 8;
holeRadius = 2.1;
holeFacets = 72;

thickness = 2;
bracketWidth = 16;
bracketLength = 72;

railLength=120;
railRadius = 140;
railThickness = 3;
railFacets = 144;



scaleFactor = 1;
rotate([0,90,0])
union() {
   translate([0,-thickness, 0])
        Bracket();
    Top(bracketLength - thickness);
    Top(21);
    Top(49);
    
    translate([0, 0, bracketLength-thickness])
        rotate([34.5,0,0])
            translate([0,-86,0])
                cube([bracketWidth, 86, thickness]);
    
    translate([0, 0, bracketLength-thickness])
        rotate([11,0,0])
            translate([0,-107,0])
                cube([bracketWidth, 107, thickness]);
    Rail();

}

module Bracket() {
    difference() {
        cube([bracketWidth, thickness, bracketLength]);   
    
        translate([10+(bracketWidth-12)/2,
                  thickness/2, bracketLength/2])
            rotate([90,0,0])
                Holes();
    }
}

module Top(z) {
    topLength = sqrt(railRadius * railRadius -
                 (railRadius - z) *
                 (railRadius - z));
    echo(topLength);
    translate([0, -(topLength-railThickness/2), z])
    cube([bracketWidth, topLength-railThickness/2, thickness]);
}

module Rail() {
    intersection() {
        translate([-1,-railRadius,0])
        cube([bracketWidth+2,railRadius,bracketLength]);
        translate([0,0,railRadius])
            rotate([0,90,0])
            difference() {
                cylinder(r = railRadius,
                         h = bracketWidth, $fn = railFacets);
                translate([0,0,-bracketWidth])
                    cylinder(r = railRadius - railThickness,
                          h =  3*bracketWidth, $fn = railFacets);
        }
    }
}

module Holes() {
  //  translate([holeOffset,0,0])
       // cylinder(r=holeRadius,h=holeDepth,
               //  center=true, $fn=holeFacets);
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
