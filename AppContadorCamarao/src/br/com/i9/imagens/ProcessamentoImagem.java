/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.i9.imagens;

import java.awt.image.BufferedImage;

/**
 *
 * @author geoleite
 */
public class ProcessamentoImagem {

    /**
     * Conta os objetos de uma imagem binária
     * @param bi - Imagem binária
     * @param matriz - Matriz de detecção da descontinuidade dos pontos
     * @return Quantidade de objetos
     */
    public int contar(int[][] matrizImg, int matriz[][]) {
        int largura = matrizImg.length;
        int altura = matrizImg[0].length;
        int contObjetos = 0;
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                //Localizando píxel da imagem
                int pixelImg = matrizImg[i][j];
                if (pixelImg == 1) {
                    //Analisar com a matriz de detecção
                    int central = matriz.length/2;
//                    int x0 = central -1;
//                    int y0 = central -1;
//                    
//                    int y1 = central;
//                    
//                    int y2 = central +1;
//                    
//                    int x1 = central;
//                    
//                    int x2 = central +1;
                    boolean connect = false;
                    for(int x=0; x < matriz.length; x++) {
                        for (int y = 0; y < matriz[0].length; y++) {
                            //Acessando os pontos da matriz
                            int xi = 0;
                            int yj = 0;
                            if (x < central) {
                                xi = i - x;
                            } else {
                                xi = i + x;
                            }
                            
                            if (y < central) {
                                yj = j - y;
                            } else {
                                yj = j + y;
                            }
                            
                            if (xi >= 0 && yj >= 0 && xi < largura && yj < altura) {
                                if (matriz[x][y] == 1 ) {
                                    //conexao[contConexao] = matrizImg[xi][yj] == 1;
                                    if (matrizImg[xi][yj] == 1) {
                                        connect = true;
                                        //Próximo pixel da imagem
                                        x = y = 10;
                                        i = i+central;
                                        j = j+central;
                                    }
                                }
                            }
                            //contConexao++;
                        }
                    }
                    if (!connect)
                        contObjetos++;
                }
            }
        }        
        return contObjetos;
    }
    
    public int[][] erosao(int[][] matrizImg, int matriz[][]) {
        int largura = matrizImg.length;
        int altura = matrizImg[0].length;
        int[][] matrizImgNew = new int[matrizImg.length][matrizImg[0].length];
        for (int i = 0; i < largura; i++) {
            for (int j = 0; j < altura; j++) {
                //Localizando píxel da imagem
                boolean erodir = false;
                int pixelImg = matrizImg[i][j];
                if (pixelImg == 1) {
                    //Analisar com a matriz de detecção
                    int central = matriz.length/2;
                    for(int x=0; x < matriz.length; x++) {
                        for (int y = 0; y < matriz[0].length; y++) {
                            //Acessando os pontos da matriz
                            int xi = 0;
                            int yj = 0;
                            if (x < central) {
                                xi = i - x;
                            } else {
                                xi = i + x;
                            }
                            
                            if (y < central) {
                                yj = j - y;
                            } else {
                                yj = j + y;
                            }
                            
                            if (xi >= 0 && yj >= 0 && xi < largura && yj < altura) {
                                if (matriz[x][y] == 1 ) {
                                    //conexao[contConexao] = matrizImg[xi][yj] == 1;
                                    if (matrizImg[xi][yj] == 0) {
                                        erodir = true;
                                        x = y = 10;
                                    }
                                }
                            }
                        }
                    }
                }   
                matrizImgNew[i][j] = erodir?0:matrizImg[i][j];
            }
        }        
        return matrizImgNew;
    }    
    
    /**
     * Converte a matriz em imagem
     * @param matrizImg
     * @return 
     */
    public BufferedImage matrizToBufferedImage(int[][] matrizImg) {
        BufferedImage bi = new BufferedImage(matrizImg.length, matrizImg[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int cor = matrizImg[i][j] == 0?0xFFFFFF:0;
                bi.setRGB(i, j, cor);
            }
        }
        return bi;
    }
}
