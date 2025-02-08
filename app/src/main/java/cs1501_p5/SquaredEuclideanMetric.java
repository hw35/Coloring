package cs1501_p5;
import java.util.*;
import java.lang.Math;
public class SquaredEuclideanMetric implements DistanceMetric_Inter{
    public double colorDistance(Pixel p1, Pixel p2){
        //double result  =;
        // if (result < Math.pow(1,-9)){
        //     result=0.0;
        // }
        return (double)Math.abs((Math.pow(p1.getRed()-p2.getRed(),2)+Math.pow(p1.getGreen()-p2.getGreen(),2)+Math.pow(p1.getBlue()-p2.getBlue(),2)));
    }
}