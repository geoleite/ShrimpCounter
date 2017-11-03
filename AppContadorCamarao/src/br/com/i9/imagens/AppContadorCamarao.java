/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.i9.imagens;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;

/**
 *
 * @author geoleite
 */
public class AppContadorCamarao {

    public static final int R=0, G=1, B=2;
    private static int limiar = 130;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
//        int cor = 0xF0A1B2;
//        int r, g, b;
//        b = cor & 0xFF;
//        cor = cor >> 8;
//        g = cor & 0xFF;
//        cor = cor >> 8;
//        r = cor & 0xFF;
//        System.out.println("cor " + cor + " " + r + " " + g + " " + b);

        FileInputStream fis = null;
        try {
//2,3%=39 camarões
            
            // TODO code application logic here
//            fis = new FileInputStream("/Users/geoleite/Downloads/camarao.jpg");
            fis = new FileInputStream("images/camarao.jpg");
            BufferedImage bi = ImageIO.read(fis);
            BufferedImage biCinza = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            BufferedImage biBP = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            BufferedImage biHSV = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            System.out.println("Inicio");
            int matrizImg[][] = new int[bi.getWidth()][bi.getHeight()];
            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    int cor = bi.getRGB(x, y);
                    /*int r, g, b;
                    b = cor & 0xFF;
                    cor = cor >> 8;
                    g = cor & 0xFF;
                    cor = cor >> 8;
                    r = cor & 0xFF;
                    */
                    int[] componentes = splitCor(cor);
                    //System.out.println("cor " + r + " " + g + " " + b);
                    int cinza = (componentes[R] + componentes[G] + componentes[B]) / 3;
                    //Montando a Cor 
                    /*cor = cinza;
                    cor <<= 8;
                    cor = cor | cinza;
                    cor <<= 8;
                    cor = cor | cinza;
                     */
                    cor = uniaoCor(cinza, cinza, cinza);
                    biCinza.setRGB(x, y, cor);
                    boolean corPB = (cinza > limiar);
                    biBP.setRGB(x, y, corPB ? 0xFFFFFF : 0x0);
                    matrizImg[x][y] = corPB ? 0 :1;
                    double[] hsv = RGBtoHSV(componentes[R], componentes[G], componentes[B]);
                    cor = uniaoCor((int)hsv[R], (int)hsv[G], (int)hsv[B]);
                    int cinzaHSV = (componentes[R] + componentes[G])/3;
                    cor = uniaoCor(cinzaHSV, cinzaHSV, cinzaHSV);
                    biHSV.setRGB(x, y, cor);
                }
            }
            int matriz1[][] = {{1,1,1}, {0,1,0}, {0,0,0}};
            int matriz2[][] = {{0,0,0}, {1,1,1}, {0,0,0}};
            int matriz3[][] = {{0,0,0}, {0,1,0}, {1,1,1}};
            int matriz4[][] = {{0,1,0}, {0,1,0}, {0,1,0}};
            int matriz5[][] = {{0,0,0}, {1,1,1}, {0,0,0}};
//            int matriz[][] = {{1,0,1,0,1}, {0,1,1,1,0}, {1,1,1,1,1}, {0,1,1,1,0}, {1,0,1,0,1}};
            ProcessamentoImagem pi = new ProcessamentoImagem();
            int contObjetos1 = pi.contar(matrizImg, matriz1);
            int contObjetos2 = pi.contar(matrizImg, matriz2);
            int contObjetos3 = pi.contar(matrizImg, matriz3);
            int contObjetos4 = pi.contar(matrizImg, matriz4);
            int contObjetos5 = pi.contar(matrizImg, matriz5);
            System.out.println(contObjetos1 + "-" + contObjetos2 + "-" + contObjetos3 + "-" + contObjetos4 + "-" + contObjetos5);
            System.out.println("Nr Objetos " + (contObjetos1 + contObjetos2 + contObjetos3 + contObjetos4 + contObjetos5)/5);
            int matrizErosao[][] = {{0,1,0}, {1,1,1}, {0,1,0}};
            matrizImg = pi.erosao(matrizImg, matrizErosao);
            BufferedImage biErosao = pi.matrizToBufferedImage(matrizImg);
            FileOutputStream fos = new FileOutputStream("out/camarao_cinza.jpg");
            ImageIO.write(biCinza, "JPG", fos);
            int area = calcularArea(biBP);
            fos = new FileOutputStream("out/camarao_bp.jpg");
            ImageIO.write(biBP, "JPG", fos);
            fos = new FileOutputStream("out/camarao_erosao.jpg");
            ImageIO.write(biErosao, "JPG", fos);
            fos = new FileOutputStream("out/camarao_hsv.jpg");
            ImageIO.write(biHSV, "JPG", fos);
            System.out.println("Fim");
            double percentual = (area / ((double)biBP.getWidth() * biBP.getHeight())) * 100;
            System.out.println("Area da imagem: " + area + " " + percentual);
        } catch (Exception ex) {
            Logger.getLogger(AppContadorCamarao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(AppContadorCamarao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public static int calcularArea(BufferedImage biPB) {
        int cont=0;
        for (int x = 0; x < biPB.getWidth(); x++) {
            for (int y = 0; y < biPB.getHeight(); y++) {
                int cor = biPB.getRGB(x, y);
                int componentes[] = splitCor(cor);
                if (componentes[R] == 0) {
                  cont++;  
                }
            }
        }
        return cont;
    }

    public static int[] splitCor(int cor) {
        int r, g, b;
        b = cor & 0xFF;
        cor = cor >> 8;
        g = cor & 0xFF;
        cor = cor >> 8;
        r = cor & 0xFF;
        return new int[]{r, g, b};
    }

    public static int uniaoCor(int r, int g, int b) {
        int cor = r;
        cor <<= 8;
        cor = cor | g;
        cor <<= 8;
        cor = cor | b;
        return cor;
    }

    public static double[] RGBtoHSV(double r, double g, double b) {

        double h, s, v;

        double min, max, delta;

        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);

        // V
        v = max;

        delta = max - min;

        // S
        if (max != 0) {
            s = delta / max;
        } else {
            s = 0;
            h = -1;
            return new double[]{h, s, v};
        }

        // H
        if (r == max) {
            h = (g - b) / delta; // between yellow & magenta
        } else if (g == max) {
            h = 2 + (b - r) / delta; // between cyan & yellow
        } else {
            h = 4 + (r - g) / delta; // between magenta & cyan
        }
        h *= 60;    // degrees

        if (h < 0) {
            h += 360;
        }

        return new double[]{h, s, v};
    }
}
