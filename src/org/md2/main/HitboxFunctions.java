package org.md2.main;
import org.jbox2d.common.Vec2;


/**
 * Beschreiben Sie hier die Klasse HitboxController.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class HitboxFunctions
{    
    //public static boolean interferes(WorldObject o1, WorldObject o2)
    //{
    //    if(o1.hitbox == WorldObject.HB_CIRCLE){
    //        if(o2.getHitbox() == WorldObject.HB_CIRCLE){
    //            return HitboxFunctions.circleInterferesCircle(o1.getCenterX(), o1.getCenterY(), o1.getXBounds(), o2.getCenterX(), o2.getCenterY(), o2.getXBounds());
    //        }
    //        else if(o2.getHitbox() == WorldObject.HB_RECT){
    //            if(o2.isRotated()){ // tests if the rect is rotated
    //                return HitboxFunctions.circleInterferesRect(o1.getCenterX(), o1.getCenterY(), o1.getXBounds(), o2.getCenterX(), o2.getCenterY(), o2.getXBounds(), o2.getYBounds(), o2.getRotation());
    //            }
    //            else{
    //                return HitboxFunctions.circleInterferesRect(o1.getCenterX(), o1.getCenterY(), o1.getXBounds(), o2.getCenterX(), o2.getCenterY(), o2.getXBounds(), o2.getYBounds());
    //            }
    //        }
    //        else{
    //            return false;
    //        }
    //    }
    //    else if(o1.hitbox == WorldObject.HB_RECT){
    //        if(o2.getHitbox() == WorldObject.HB_CIRCLE){
    //            if(o1.isRotated()){
    //                return HitboxFunctions.circleInterferesRect(o2.getCenterX(), o2.getCenterY(), o2.getXBounds(), o1.getCenterX(), o1.getCenterY(), o1.getXBounds(), o1.getYBounds(), o1.getRotation());
    //            }
    //            else{
    //                return HitboxFunctions.circleInterferesRect(o2.getCenterX(), o2.getCenterY(), o2.getXBounds(), o1.getCenterX(), o1.getCenterY(), o1.getXBounds(), o1.getYBounds());
    //            }
    //        }
    //        else if(o2.getHitbox() == WorldObject.HB_RECT) {
    //            return HitboxFunctions.rectInterferesRect(o2.getCenterX(), o2.getCenterY(), o2.getXBounds(), o2.getYBounds(), o1.getCenterX(), o1.getCenterY(), o1.getXBounds(), o1.getYBounds());
    //        }
    //        else{
    //            return false;
    //        }
    //    }
    //    else{
    //        return false;
    //    }
    //}
    

    
    public static boolean circleInterferesCircle(float centerX, float centerY, float rad, float centerX2, float centerY2, float rad2)
    {
        float sumRad = rad+rad2;
        float xDis = centerX - centerX2;
        float yDis = centerY - centerY2;
        float dis = (float)Math.sqrt(xDis*xDis+yDis*yDis);
        if(dis <= sumRad){
            return true;
        }
        return false;
    }
    
    public static boolean rectInterferesRect(float centerX, float centerY, float xLength, float yLength, float centerX2, float centerY2, float xLength2, float yLength2)
    {
        if(centerX+xLength >= centerX2-xLength2 &&
           centerX2+xLength2 >= centerX-xLength &&
           centerY+yLength >= centerY2-yLength2 &&
           centerY2+yLength2 >= centerY-yLength)
        {
            return true;
        }
        return false;
    }
    
    public static boolean rectInterferesRect(float centerX1, float centerY1, float xLength1, float yLength1, float rotation1, float centerX2, float centerY2, float xLength2, float yLength2, float rotation2)
    {
        Vec2[] vertices1Buffer = getRectVertices(centerX1, centerY1, xLength1, yLength1, rotation1);
        Vec2[] vertices2Buffer = getRectVertices(centerX2, centerY2, xLength1, yLength1, rotation1);
        Vec2[] vertices1 = new Vec2[vertices1Buffer.length + 1];
        Vec2[] vertices2 = new Vec2[vertices2Buffer.length + 1];
        vertices1[vertices1Buffer.length] = vertices1Buffer[0];
        vertices2[vertices2Buffer.length] = vertices2Buffer[0];
        System.arraycopy(vertices1Buffer, 0, vertices1, 0, vertices1Buffer.length);
        System.arraycopy(vertices2Buffer, 0, vertices2, 0, vertices2Buffer.length); // brings the arrays in a form like this: p1, p2, p3, p4, p1. Like this its easier to iterate through it
        for(int index1 = 0; index1 < vertices1.length-1; index1++){
            Vec2 p1 = vertices1[index1];
            Vec2 p2 = vertices1[index1+1];// first linesegment
            for(int index2 = 0; index2 < vertices2.length-1; index2++){
                Vec2 p3 = vertices2[index2];
                Vec2 p4 = vertices2[index2+1]; //second linesgement
                if(lineSegInterferesLineSeg(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean lineSegInterferesLineSeg(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
    {
        float t = ( (y3-y1)*(x2-x1)/(y2-y1) - (x3-x1))/( (x4-x3) - (y4-y3)*(x2-x1)/(y2-y1) );
        float s = (x3-x1+t*(x4-x3))/(x2-x1);
        if(t >= 0 && t <= 1 && s >= 0 && s <= 1){
            return true;
        }
        return false;
    }
    
    public static Vec2[] getRectVertices(float centerX, float centerY, float xLength, float yLength, float rotationAngle)
    {
        Vec2 p1 = new Vec2(centerX + xLength, centerY + yLength);
        Vec2 p2 = new Vec2(centerX + xLength, centerY - yLength);
        Vec2 p3 = new Vec2(centerX - xLength, centerY - yLength);
        Vec2 p4 = new Vec2(centerX - xLength, centerY + yLength);
        if(rotationAngle == 0){
            Vec2[] vertices = {p1, p2, p3, p4};
            return vertices;
        }
        Vec2 p5 = new Vec2(centerX, centerY);
        float cosRot = (float)Math.cos(rotationAngle);
        float sinRot = (float)Math.sin(rotationAngle);
        Vec2 p1Rot = new Vec2((p1.x - p5.x)*cosRot - (p1.y - p5.y)*sinRot + p5.x, (p1.x - p5.x)*sinRot + (p1.y - p5.y)*cosRot+ + p5.y);
        Vec2 p2Rot = new Vec2((p2.x - p5.x)*cosRot - (p2.y - p5.y)*sinRot + p5.x, (p2.x - p5.x)*sinRot + (p2.y - p5.y)*cosRot+ + p5.y);
        Vec2 p3Rot = new Vec2((p3.x - p5.x)*cosRot - (p3.y - p5.y)*sinRot + p5.x, (p3.x - p5.x)*sinRot + (p3.y - p5.y)*cosRot+ + p5.y);
        Vec2 p4Rot = new Vec2((p4.x - p4.x)*cosRot - (p4.y - p5.y)*sinRot + p5.x, (p4.x - p5.x)*sinRot + (p4.y - p5.y)*cosRot+ + p5.y);
        Vec2[] vertices = {p1Rot, p2Rot, p3Rot, p4Rot};
        return vertices;
    }
    
    //public static boolean circleInterferesLineSeg(float x1, float y1, float x2, float y2, float xCircle, float yCircle, float rad)
    //{
    //    //the height to(xCircle, yCircle) of a triangle going through all of the 3 Vec2s is the lowest distance between the linesegment and the circlecenter
    //    float xDis = x1-xCircle;
    //    float yDis = y1-yCircle;
    //    float a = Math.sqrt(xDis*xDis+yDis*yDis);
    //    xDis = x2-xCircle;
    //    yDis = y2-yCircle;
    //    float b = Math.sqrt(xDis*xDis+yDis*yDis);
    //    xDis = x2-x1;
    //    yDis = y2-y1;
    //    float c = Math.sqrt(xDis*xDis+yDis*yDis);
    //    float alpha = Math.acos((a*a-b*b-c*c)/(-2*b*c));
    //    float height = Math.sin(alpha)*b;
    //    if(Math.abs(height) <= rad){
    //        return true;
    //    }
    //    return false;
    //}
    
    //public static boolean circleInterferesRect(float centerX, float centerY, float rad, float centerX2, float centerY2, float xLength, float yLength, float rotationAngle)
    //{
    //    Vec2[] vertices = getRectVertices(centerX2, centerY2, xLength, yLength, rotationAngle);
    //    if(circleInterferesLineSeg(vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y, centerX, centerY, rad)){
    //        return true;
    //    }
    //    else if(circleInterferesLineSeg(vertices[1].x, vertices[1].y, vertices[2].x, vertices[2].y, centerX, centerY, rad)){
    //        return true;
    //    }
    //    else if(circleInterferesLineSeg(vertices[2].x, vertices[2].y, vertices[3].x, vertices[3].y, centerX, centerY, rad)){
    //        return true;
    //    }
    //    else if(circleInterferesLineSeg(vertices[3].x, vertices[3].y, vertices[0].x, vertices[0].y, centerX, centerY, rad)){
    //        return true;
    //    }
    //    else {
    //        return false;
    //    }
    //}
    
    public static boolean circleInterferesRect(float centerX, float centerY, float rad, float centerX2, float centerY2, float xLength, float yLength)
    {
        //tests if the circle is inside of the rect
        if(Vec2InsideRect(centerX2, centerY2, xLength, yLength, centerX, centerY)){
            return true;
        }
        float x1 = centerX + xLength;
        float y1 = centerY + yLength;
        float x2 = centerX - xLength;
        float y2 = centerY - yLength;
        float x3 = centerX + xLength;
        float y3 = centerY - yLength;
        float x4 = centerX - xLength;
        float y4 = centerY + yLength;
        //test if the circle is inside a circle arround the corners
        if(Vec2InsideCircle(centerX, centerY, rad, x1, y1)){
            return true;   
        }
        if(Vec2InsideCircle(centerX, centerY, rad, x2, y2)){
            return true;   
        }
        if(Vec2InsideCircle(centerX, centerY, rad, x3, y3)){
            return true;   
        }
        if(Vec2InsideCircle(centerX, centerY, rad, x4, y4)){
            return true;   
        }
        //test if the circle is indside of rects longside the sides of the rect
        if(Vec2InsideRect(centerX2+xLength, centerY2, rad, yLength, centerX, centerY)){
            return true;
        }
        if(Vec2InsideRect(centerX2-xLength, centerY2, rad, yLength, centerX, centerY)){
            return true;
        }
        if(Vec2InsideRect(centerX2, centerY2+yLength, xLength, rad, centerX, centerY)){
            return true;
        }
        if(Vec2InsideRect(centerX2, centerY2-yLength, xLength, rad, centerX, centerY)){
            return true;
        }
        return false;
    }
    
    
    public static boolean Vec2InsideCircle(float centerX, float centerY, float rad, float Vec2X2, float Vec2Y2 )
    {
        float xDis = Math.abs(centerX - Vec2X2);
        float yDis = Math.abs(centerY - Vec2Y2);
        double dis = Math.sqrt(xDis*xDis+yDis*yDis);
        if(dis <= rad){
            return true;
        }
        return false;
    }
    
    public static boolean Vec2InsideRect(float centerX, float centerY, float xLength, float yLength, float Vec2X2, float Vec2Y2)
    {
        if(Vec2X2 >= centerX-xLength && Vec2X2 <= centerX+xLength &&
           Vec2Y2 >= centerY-yLength && Vec2Y2 <= centerY+yLength)
        {
            return true;
        }
        return false;
    }
    
    public static float square(float x)
    {
        return x*x;
    }
    
    
}

//     public static float[] circleCutsRect(float centerX, float centerY, float rad, float centerX2, float centerY2, float xLength, float yLength)
//     {
//         float x1 = float.NaN;
//         float y1 = 0;
//         float x2 = 0;
//         float y2 = 0;
//         for(float t = -3.15; t <= 3.15; t = t + 0.001){
//             float x = xLength*(Math.abs(Math.cos(t))*Math.cos(t) + Math.abs(Math.sin(t))*Math.sin(t)) + centerX2;
//             float y = yLength*(Math.abs(Math.cos(t))*Math.cos(t) - Math.abs(Math.sin(t))*Math.sin(t)) + centerY2;
//             float d1 = (float)Math.round(square((x - centerX) + square(y - centerX)) * 100) / 100;
//             float d2 = (float)Math.round(square(rad) * 100) / 100;
//             if( d1 == d2 ){
//                 x = (float)Math.round(x * 100) / 100;
//                 y = (float)Math.round(y * 100) / 100;
//                 if(float.isNaN(x1)){
//                     x1 = x;
//                     y1 = y;
//                 }
//                 else{
//                     x2 = x;
//                     y2 = y;
//                 }
//             }
//         }
//         if(float.isNaN(x1)){
//             return null;
//         }
//         float[]ret = {x1, y1, x2, y2};
//         return ret;
//     }
//     
//     public static float[] circleCutsCircle(float centerX, float centerY, float rad, float centerX2, float centerY2, float rad2)
//     {
//         float a = -2*centerX-(-2*centerX2);
//         float b = -2*centerY-(-2*centerY2);
//         float c = -((square(centerY)+ square(centerX)-rad)-(square(centerY2)+ square(centerX2)-rad2));
//         float d = c - centerX * a - centerY * b;
//         float y1 = centerY + (d*b+a*(Math.sqrt(square(rad)*(square(a)+square(b))-d*d)))/(square(a)+square(b));
//         float y2 = centerY + (d*b-a*(Math.sqrt(square(rad)*(square(a)+square(b))-d*d)))/(square(a)+square(b));
//         float x1 = (c-b*y1)/a;
//         float x2 = (c-b*y2)/a;
//         if(float.isNaN(x1)){
//             return null;
//         }
//         float[]ret = {x1, y1, x2, y2};
//         return ret;
//     }