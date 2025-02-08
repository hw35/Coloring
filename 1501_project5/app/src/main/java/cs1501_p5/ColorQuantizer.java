package cs1501_p5;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
/*
This class should have two constructors. 
The first should accept a two-dimensional Pixel array  which represents the pixels from the 
.bmp file and an instance of a class that implements the ColorMapGenerator_Inter interface 
which should be used to generate a color map. 

The second should accept the name of .bmp file to read and an instance of a class that 
implements the ColorMapGenerator_Inter interface which should be used to generate a color map.
*/

public class ColorQuantizer implements ColorQuantizer_Inter{
    private ColorMapGenerator_Inter gen;
    private Pixel[][] image;
    private Pixel[] palette;
    private Map<Pixel,Pixel> map;
    
    public ColorQuantizer(Pixel[][] p, ColorMapGenerator_Inter m){
        //System.out.println(p[0][0]);
        image = new Pixel[p.length][p[0].length];
        for(int row = 0; row<p.length;row++){
            for(int col=0; col< p[0].length;col++){
                image[row][col]=p[row][col];
            }
        }
        //Arrays.fill(palette,null);
        //System.out.println(image[0][0]);
        gen=m;
    }

    public ColorQuantizer(String file, ColorMapGenerator_Inter m){
        gen=m;
        try {
            // Load bitmap image
            BufferedImage test = ImageIO.read(new File(file));

            // Create pixel matrix
            Pixel[][] p = convertBitmapToPixelMatrix(test);
            image = new Pixel[p.length][p[0].length];
            for(int row = 0; row<p.length;row++){
                for(int col=0; col< p[0].length;col++){
                    image[row][col]=p[row][col];
                }
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public Pixel[][] quantizeTo2DArray(int numColors){
        //System.out.println(image[0][0]);
        palette = gen.generateColorPalette(image, numColors);
        
        map = gen.generateColorMap(image, palette);

        
        
        Pixel[][] newImage = new Pixel[image.length][image[0].length];
        
        for(int row = 0; row < newImage.length; row++){
            for(int col = 0; col < newImage[row].length; col++){
                Pixel cur = image[row][col];
                
                Pixel val = map.get(cur);
                if(val==null){
                    Pixel black = new Pixel(0,0,0);
                    val =black;
                }
                newImage[row][col] = val;
                //System.out.println(image[row][col]);
            }
        }
        return newImage;
    }
    public void quantizeToBMP(String fileName, int numColors){
        // Save pixel matrix to file
        try {
            // Load bitmap image
            //BufferedImage test = ImageIO.read(new File(fileName));

            // Create pixel matrix
            //image = convertBitmapToPixelMatrix(test);
            Pixel[][] x = quantizeTo2DArray(numColors);
            savePixelMatrixToFile(fileName, x);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }


    private static void savePixelMatrixToFile(String filePath, Pixel[][] matrix) {

        try {
            // Open file for writing
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // Write matrix to file
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(matrix[i][j] + String.valueOf('\t'));
                }
                writer.newLine();
            }

            // Close file
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Pixel[][] convertBitmapToPixelMatrix(BufferedImage pic) {
        Pixel[][] pixelMatrix = new Pixel[pic.getWidth()][pic.getHeight()];

        for (int x = 0; x < pic.getWidth(); x++) {
            for (int y = 0; y < pic.getHeight(); y++) {
                int rgb = pic.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                pixelMatrix[x][y] = new Pixel(red, green, blue);
            }
        }

        return pixelMatrix;
    }

    public static void savePixelMatrixToBitmap(String filePath, Pixel[][] pixelMatrix) {
        int width = pixelMatrix.length;
        int height = pixelMatrix[0].length;
        BufferedImage pic = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Pixel pixel;
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                pixel = pixelMatrix[row][col];
                // if(pixel == null){
                //     System.out.println(""+x+","+y);
                // }
                Color color = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue());
                pic.setRGB(row, col, color.getRGB());
            }
        }
        try {
            File file = new File(filePath);
            ImageIO.write(pic, "bmp", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}