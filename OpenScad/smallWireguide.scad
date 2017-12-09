
width=12;
holeOffset = 8;
holeRadius = 2;

facets = 144;

height=10;
wrenchRadius=1.5;
wireRadius = 6;
screwRadius=4;
screwHeight=2;
wallThickness = 1;
baseThickness = 3;

cutOffset=-0.5;
cutSize=20;
cutThickness=1.5;

yScale = 1.0;

difference() {
    union() {
        translate([0,(baseThickness+wireRadius*yScale)/2,0])
            cube([(wireRadius + wallThickness)*2,
                  baseThickness+wireRadius*yScale, height],center=true);
        
        scale([1,yScale,1])
            cylinder(r=wireRadius + wallThickness, h=height, center=true, $fn = facets);
        
 
    }  

    scale([1,yScale,1])
        cylinder(r=wireRadius, h=height+2, center=true, $fn = facets);


    translate([0,(wireRadius*yScale+screwHeight),0])
        rotate([90,0,0])
            cylinder(r=screwRadius, h=wireRadius*yScale+screwHeight,$fn=facets);
    
    translate([0,baseThickness+wireRadius+2,0])
        rotate([90,0,0])
            cylinder(r=holeRadius, h=baseThickness+wireRadius+2,$fn=facets); 

    rotate([90,0,0])
        cylinder(r=wrenchRadius, h=wireRadius*2,$fn=facets); 

    translate([wireRadius,cutOffset+baseThickness-height/2,0])
        rotate([0,45,90]) {
            translate([0,0,(cutSize-cutThickness)/2])
                cube([cutThickness,wallThickness+6,cutSize], center=true);

            translate([(cutSize-cutThickness)/2,0,0])
                cube([cutSize,wallThickness+6,cutThickness], center=true);
    }
}
