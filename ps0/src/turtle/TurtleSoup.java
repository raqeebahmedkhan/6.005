/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package turtle;

import java.util.List;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        if (turtle == null || sideLength <= 0) {
            throw new IllegalArgumentException();
        }
        double angle = 90;
        for (int i = 0; i < 3; ++i) {
            turtle.forward(sideLength);
            turtle.turn(angle);
        }
        turtle.forward(sideLength);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        if (sides <= 2) {
            throw new IllegalArgumentException();
        }
        double angle = (double)((sides - 2) * 180) / sides;
        return angle;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        if (angle <= 0 || angle >= 180) {
            throw new IllegalArgumentException();
        }
        int sides = (int) Math.round((2 * 180) / (180 - angle)); 
        return sides;
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        if (turtle == null || sides < 2 || sideLength <= 0) {
            throw new IllegalArgumentException();
        }
        double angle = 180 -calculateRegularPolygonAngle(sides);
        for (int i = 0; i < sides-1; ++i) {
            turtle.forward(sideLength);
            turtle.turn(angle);
        }
        turtle.forward(sideLength);
    }

    /**
     * Given the current direction, current location, and a target location, calculate the heading
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentHeading. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentHeading current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to heading (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateHeadingToPoint(double currentHeading, int currentX, int currentY,
                                                 int targetX, int targetY) {
        if (currentHeading < 0 || currentHeading >= 360) {
            throw new IllegalArgumentException();
        }
        
        double thetaInRad = Math.atan2(targetX-currentX, targetY-currentY);
        double thetaInDeg = Math.toDegrees(thetaInRad);
        if (currentHeading > thetaInDeg) {
            return 360 - currentHeading - thetaInDeg;
        } else {
            return thetaInDeg - currentHeading;
        }
        
    }

    /**
     * Given a sequence of points, calculate the heading adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateHeadingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of heading adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateHeadings(List<Integer> xCoords, List<Integer> yCoords) {
        if (xCoords == null || yCoords == null || xCoords.size() != yCoords.size()) {
            throw new IllegalArgumentException();
        }
        List<Double> headings = new ArrayList<>();
        if (xCoords.isEmpty()) {
            return headings;
        }
        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        double currentHeading = 0.0;
        
        for (int i = 1, n = xCoords.size(); i < n; ++i) {
            double angle = calculateHeadingToPoint(currentHeading, currentX, currentY, xCoords.get(i), yCoords.get(i));
            headings.add(angle);
            currentHeading = angle;
            currentX = xCoords.get(i);
            currentY = yCoords.get(i);
        }
        
        return headings;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        int sideLength = 256;
        int level = 6;
        drawSierpenskiTriangle(turtle, sideLength, level);
    }
    
    /**
     * Draws the sierpenski triangle
     * @param turtle the turtle context
     * @param sideLength  the sidelength of current level
     * @param level number of levels of recursion
     */
    private static void drawSierpenskiTriangle(Turtle turtle, int sideLength, int level) {
        if (level == 0) {
            drawTriangle(turtle, sideLength);
        } else {
            sideLength /= 2;
            drawSierpenskiTriangle(turtle, sideLength, level-1);
            turtle.turn(30);                    //turn 30deg to move to point halfway of first side
            turtle.forward(sideLength);
            turtle.turn(330);                   //turn 330deg (330+30=360)to keep it's face vertical
            drawSierpenskiTriangle(turtle, sideLength, level-1);
            turtle.turn(150);                   //turn 150deg to move to draw third triangle (30+60+60 = 150)
            turtle.forward(sideLength);
            turtle.turn(210);                   //turn 210deg to return to keep it's face vertical (150+210 = 360)
            drawSierpenskiTriangle(turtle, sideLength, level-1);
            turtle.turn(270);                   //turn 270deg to turn to move to horizontally to leftmost point (270+90 = 360)
            turtle.forward(sideLength);
            turtle.turn(90);                    //turn 90deg to keep it's face vertical
        }
    }
    
    /**
     * draws an equilateral triangle of given sidelength and leaves the turtle face in it's initial direction
     * @param turtle
     * @param sideLength
     */
    private static void drawTriangle(Turtle turtle, int sideLength) {
        turtle.turn(30);            //initially turtle is in vertical position so turn 30deg
        turtle.forward(sideLength);
        turtle.turn(120);           //turn 120deg to make 60deg angle with initial line
        turtle.forward(sideLength);
        turtle.turn(120);           //same as above
        turtle.forward(sideLength);
        turtle.turn(90);            //turn 90deg to leave the turtle in it's initial position.
    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

       // drawSquare(turtle, 40);
        //drawRegularPolygon(turtle, 6, 60);
        // draw the window
        drawPersonalArt(turtle);
        turtle.draw();
    }

}
