package cs1501_p5;
import java.util.*;
import java.lang.Math;
public class CosineDistanceMetric implements DistanceMetric_Inter{
    public double colorDistance(Pixel p1, Pixel p2){
        int r1 = p1.getRed();       //5
        int g1 = p1.getGreen();     //5    
        int b1 = p1.getBlue();      //5
        int r2 = p2.getRed();
        int g2 = p2.getGreen();
        int b2 = p2.getBlue();
        double top = (double)(r1*r2+g1*g2+b1*b2);
        double in = r1*r1+g1*g1+b1*b1;
        double in1 = r2*r2+g2*g2+b2*b2;
        double bot = (double)(Math.sqrt(in*in1));
        // if(bot==0){
        //     System.out.println("denom is 0");
        // }
        
        double result = Math.abs(1.0- top/bot);
        // if (result < Math.pow(1,-9)){
        //     result=0.0;
        // }
        //if(result<0){System.out.println}
        return result;
    }
}