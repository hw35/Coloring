package cs1501_p5;
import java.util.*;
// This class should have a constructor that accepts a class that implements the 
// DistanceMetric_Inter interface to be used when computing distances between two pixels.
public class ClusteringMapGenerator implements ColorMapGenerator_Inter{
    //private double dm;
    private DistanceMetric_Inter dm;
    private Pixel[][] paletteArr;          
    private Pixel[][] pixelArray;
    private Map<Pixel,Pixel> mm;

    private Map<Pixel,Pixel> map;
    private Pixel[] updateColorPalette;     //for reassigning centroids
    private ArrayList<Pixel> centroidHist = new ArrayList<Pixel>(); //has previous centroids, helps calculate farthest distance from previous centroids
    private int[][] clusterColors;  //row indices correspond to indices of centroids, has 3 columns on each row, contains total value of red, green, blue 
    private int[] pointCount; //indices correspond to indices of centroids in updateColorPalette, int inside is #points in that cluster
    
    public ClusteringMapGenerator(DistanceMetric_Inter m){
        dm=m;
    }


    public Pixel[] generateColorPalette(Pixel[][] pixelArr, int numColors){
        if(pixelArr.length==0 || numColors<1){return null;}
        //centroids = new Pixel[pixelArr.length][pixelArr[0].length];
        // paletteArr = new Pixel[pixelArr.length][pixelArr[0].length];
        // for(int row = 0; row < pixelArr.length; row++){
        //     for(int col = 0; col < pixelArr[row].length; col++){
        //         paletteArr[row][col]=pixelArr[row][col];
        //     }
        // }
        paletteArr=pixelArr;
        Pixel[] palette = new Pixel[numColors];
        Pixel first =pixelArr[0][0];
        palette[0] = first;
        Pixel startCen=first;
        //paletteArr[0][0]=null;
        //centroids[0][0]=first;
        centroidHist.add(first);
        //System.out.println(first);
        mm= new HashMap<Pixel,Pixel>();
        mm.put(first,first);

        for(int i = 1; i<numColors;i++){
            Pixel farthest = nextCentroid();
            //startCen=farthest;
            if(farthest==null){
                System.out.println("HERE");
                break;
            }
            palette[i]= farthest;
            //System.out.println(farthest);
        // }System.out.println("start");
        // for(int i=0; i<palette.length;i++){
        //     System.out.println(palette[i]);
        // }
        // System.out.println("end");
        }
        
        //centroidHist.clear();
        // for(int i = 0; i<palette.length; i++){
        //     if(palette[i]==null){
        //         Pixel black = new Pixel(0,0,0);
        //         palette[i]=black;
        //     }
        // }
        return palette;
    }

    private Pixel nextCentroid(){
        // int frow=0;
        // int fcol=0;
        
        double minDist=0;
        Pixel nextCen = centroidHist.get(centroidHist.size()-1);
        //System.out.println("nextCen");
        

        for(int row = 0; row < paletteArr.length; row++){
            for(int col = 0; col < paletteArr[row].length; col++){
                Pixel cur = paletteArr[row][col];
                if(cur==null){
                    continue;
                    // Pixel black = new Pixel(0,0,0);
                    // paletteArr[row][col]=black;
                }
                Pixel curCen = centroidHist.get(centroidHist.size()-1);
                double findCentroid =Double.POSITIVE_INFINITY;
                for(Pixel p : centroidHist){
                    minDist=dm.colorDistance(cur,p);
                    if (minDist < findCentroid){
                        curCen = p;
                        findCentroid = minDist;
                    }
                }
                mm.put(cur,curCen);
            }
        }
        Double[] distArr = new Double[centroidHist.size()];
        for(int i =0; i<distArr.length;i++){distArr[i]=-1.0;}
        Pixel[] newCen = new Pixel[centroidHist.size()]; 
        //ArrayList<Double> distArr = new ArrayList<Double>();
        for(Map.Entry<Pixel,Pixel> entry : mm.entrySet()){
            for(int i =0;i<centroidHist.size();i++){
                Pixel curC = centroidHist.get(i);
                if(entry.getValue().equals(curC)){
                    if(centroidHist.contains(entry.getKey())){
                        continue;
                    }
                    double curDist = dm.colorDistance(entry.getKey(),curC);
                    
                    // if(entry.getKey().getRed()==200){
                    //     System.out.println(entry.getKey() + " has distance " + curDist);
                    //     System.out.println("curC = "+curC);
                    // }
                    // if(entry.getKey().getRed()==100){
                    //     System.out.println(entry.getKey() + " has distance " + curDist);
                    // }
                    if(entry.getKey().getRed()==150){
                        System.out.println(entry.getKey() + " has distance " + curDist);
                        System.out.println("curC = "+curC);
                    }
                    // if(entry.getKey().getRed()==50){
                    //     System.out.println(entry.getKey() + " has distance " + curDist);
                    // }
                    // if(entry.getKey().getRed()==250){
                    //     System.out.println(entry.getKey() + " has distance " + curDist);
                    // }
                    if(curDist>distArr[i]){
                        distArr[i] = curDist;
                        newCen[i]=entry.getKey();
                    }
                    else if(curDist==distArr[i]){
                        System.out.println("EQUAL");
                        
                        Pixel cur = entry.getKey();
                        Pixel ori = newCen[i]; 
                        System.out.println("cur = " + cur);
                        System.out.println("ori = " + ori);
                        int oriVal = (((ori.getRed()<<16) & 0xFF0000) | ((ori.getGreen()<<8) & 0xFF00) | (ori.getBlue() & 0xFF));
                        int curVal = (((cur.getRed()<<16) & 0xFF0000) | ((cur.getGreen()<<8) & 0xFF00) | (cur.getBlue() & 0xFF));
                        if(curVal > oriVal){
                            //distArr[i] = curDist;
                            System.out.println("current is bigger ");
                            newCen[i]=cur;
                            // frow = row;
                            // fcol = col;
                        }                        
                    }
                    // if(newCen[i]==null){
                    //     Pixel black = new Pixel(0,0,0);
                    //     newCen[i]=black;
                    // }
                    
                }
                //System.out.println("next pixel");
            }
        }
        System.out.print("new centroid array: ");
        
        for(int i =0; i<newCen.length;i++){
            // if(newCen[i]==null){
            //     continue;
            //     Pixel black = new Pixel(0,0,0);
            //     newCen[i]=black;
            // }
            System.out.print(newCen[i]);
        }
        double maxDist=-1.0;
        int maxIndex = 0;
        for(int i =0; i<distArr.length;i++){
            if(newCen[i]==null){
                //continue;
                Pixel black = new Pixel(0,0,0);
                newCen[i]=black;
            }
            if(distArr[i]>maxDist){
                maxDist=distArr[i];
                nextCen = newCen[i];
                maxIndex = i;
                
            }
            //System.out.println("has to print here right "+maxIndex);
            else if(distArr[i]==maxDist){
                Pixel ori = newCen[maxIndex];
                Pixel cur = newCen[i];
                System.out.println("Got here EQUAL with " + ori +" and "+cur);
                int oriVal = (((ori.getRed()<<16) & 0xFF0000) | ((ori.getGreen()<<8) & 0xFF00) | (ori.getBlue() & 0xFF));
                int curVal = (((cur.getRed()<<16) & 0xFF0000) | ((cur.getGreen()<<8) & 0xFF00) | (cur.getBlue() & 0xFF));
                if(curVal>oriVal){
                    maxDist=distArr[i];
                    nextCen = newCen[i];
                    maxIndex = i;
                }
            }
            
        }
        System.out.println();
        //System.out.println("finalized centroid is " + nextCen);
        //centroids[frow][fcol]=nextCen;
        //if(nextCen==centroidHist.get(centroidHist.size()-1)){return null;}
        centroidHist.add(nextCen);
        //paletteArr[frow][fcol]=null;
        
        return nextCen;
    }

    public Map<Pixel,Pixel> generateColorMap(Pixel[][] pixelArr, Pixel[] initialColorPalette){
        map = new HashMap<Pixel,Pixel>();
        updateColorPalette = new Pixel[initialColorPalette.length];
        clusterColors = new int [initialColorPalette.length][3];
        pointCount = new int[initialColorPalette.length];
        pixelArray = pixelArr;
        for(int i =0; i<initialColorPalette.length;i++){
            updateColorPalette[i]=initialColorPalette[i];
        }
        assignCentroid();
        //Arrays.fill(pixelArray, null);
        
        return map;
    }

    private void assignCentroid(){
        for(int row = 0; row < pixelArray.length; row++){
            for(int col = 0; col < pixelArray[row].length; col++){
                Pixel curPix = pixelArray[row][col];
                double minDist = Double.POSITIVE_INFINITY;
                for(int i=0; i<updateColorPalette.length;i++){ 
                    Pixel centroid = updateColorPalette[i];
                    // if(curPix==null){
                    //     Pixel black = new Pixel(0,0,0);
                    //     pixelArray[row][col]=black;
                    //     curPix=black;
                    // }
                    // if(centroid==null){
                    //     Pixel black = new Pixel(0,0,0);
                    //     centroid=black;
                    //     updateColorPalette[i]=black;
                    // }
                    double dist = dm.colorDistance(curPix, centroid);
                    if(dist<minDist){
                        minDist = dist;
                        map.put(curPix,centroid);
                        //break;
                    }
                }
            }
        }
        //System.out.println(map);
        //System.out.println("done");
        updateCentroid();
    }

    private void updateCentroid(){
        Double[] totalDist = new Double[updateColorPalette.length];
        for(Map.Entry<Pixel,Pixel> entry : map.entrySet()){
            for(int i =0;i<updateColorPalette.length;i++){
                Pixel curC = updateColorPalette[i];
                if(entry.getValue().equals(curC)){
                    Pixel key = entry.getKey();
                    assignVals(i,key);
                }
            }
        }
        boolean assignAgain = false;
        for(int i =0; i<updateColorPalette.length;i++){
            if(pointCount[i]==0){continue;}
            
            int red = clusterColors[i][0]/pointCount[i];
            int green = clusterColors[i][1]/pointCount[i];
            int blue = clusterColors[i][2]/pointCount[i];
            Pixel newCen = new Pixel(red,green,blue);
            //if(newCen == null || )
            if(!newCen.equals(updateColorPalette[i])){
                updateColorPalette[i]=newCen;
                assignAgain = true;
            }
        }
        if(assignAgain){
            assignCentroid();
        }        
    }

    private void assignVals(int CentroidInd, Pixel pix){//how to get the pixel of average distance between all points in cluster using dm.colordistance?
        //is this correct? using average features according to slides
        clusterColors[CentroidInd][0]+=pix.getRed();
        clusterColors[CentroidInd][1]+=pix.getGreen();
        clusterColors[CentroidInd][2]+=pix.getBlue();
        pointCount[CentroidInd]+=1;
    }

}