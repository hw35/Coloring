package cs1501_p5;
import java.util.*;
public class BucketingMapGenerator implements ColorMapGenerator_Inter{

    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors){
        if(numColors==0){return null;}
        double Bsize = (double)Math.pow(2,24)/numColors;
        Pixel[] palette = new Pixel[numColors];

        for(int i=0;i<numColors;i++){
            int center = (int)(Bsize*i + ((Bsize)*(i+1)))/2;
            
            int red = (center & 0xFF0000) >> 16;
            
            int green = (center & 0xFF00) >> 8;
            int blue = (center & 0xFF);
            palette[i] = new Pixel(red,green,blue);
        }
        return palette;
    }
    
    public Map<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette){
        Map<Pixel,Pixel> map = new HashMap<Pixel,Pixel>();
        int numColors = initialColorPalette.length;
        double BsizeHalf = (double)Math.pow(2,24)/(numColors*2);
        double Bsize = (double)Math.pow(2,24)/(numColors);
        
        for(int row = 0; row < pixelArr.length; row++){
            for(int col = 0; col < pixelArr[row].length; col++){
                double val = Double.POSITIVE_INFINITY;
                // for(int i = 0; i<numColors; i++){
                //     Pixel center = initialColorPalette[i];
                //     int cenVal = ((center.getRed()<<16 & 0xFF0000) | (center.getGreen()<<8 & 0xFF00) | (center.getBlue() & 0xFF));
                //     Pixel cur = pixelArr[row][col];
                //     int curVal = ((cur.getRed()<<16 & 0xFF0000) | (cur.getGreen()<<8 & 0xFF00) | (cur.getBlue() & 0xFF));
                //     if(Math.abs(cenVal-curVal)<val){
                //         map.put(cur,center);
                //         val = Math.abs(cenVal-curVal);
                //     }

                //     // if(pix<= (cenVal+BsizeHalf) && pix >= (cenVal-BsizeHalf)){
                //     //     map.put(cur,center);
                //     //     break;
                //     // }
                // }
                Pixel cur = pixelArr[row][col];
                int curVal = ((cur.getRed()<<16 & 0xFF0000) | (cur.getGreen()<<8 & 0xFF00) | (cur.getBlue() & 0xFF));
                double num = (double)curVal/Bsize;
                int index = (int)num;
                map.put(cur,initialColorPalette[index]);
            }   
        }
        return map;
    }
}