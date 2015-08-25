/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.ifad.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author danilo
 */
public class ImagemUtil {
    
    /**
     * Diminui a imagem para o menor tamanho aceitavel!
     * @param caminhoImg
     * @param imgLargura
     * @param imgAltura
     * @throws IOException 
     */
    public static void diminuirImagemProporcialmente(File image, Integer imgLargura, Integer imgAltura) {
        try {
            BufferedImage imagem = ImageIO.read(image);
            
            Double novaImgLargura = (double) imagem.getWidth();
            Double novaImgAltura = (double) imagem.getHeight();

            Double imgProporcao = null;
            if (novaImgLargura >= imgLargura) {
                imgProporcao = (novaImgAltura / novaImgLargura);
                novaImgLargura = (double) imgLargura;
                novaImgAltura = (novaImgLargura * imgProporcao);
                while (novaImgAltura > imgAltura) {
                    novaImgLargura = (double) (--imgLargura);
                    novaImgAltura = (novaImgLargura * imgProporcao);
                }
            } else if (novaImgAltura >= imgAltura) {
                imgProporcao = (novaImgLargura / novaImgAltura);
                novaImgAltura = (double) imgAltura;
                while (novaImgLargura > imgLargura) {
                    novaImgAltura = (double) (--imgAltura);
                    novaImgLargura = (novaImgAltura * imgProporcao);
                }
            }

            BufferedImage novaImagem = new BufferedImage(novaImgLargura.intValue(), novaImgAltura.intValue(), BufferedImage.TYPE_INT_RGB);
            Graphics g = novaImagem.getGraphics();
            g.drawImage(imagem.getScaledInstance(novaImgLargura.intValue(), novaImgAltura.intValue(), Image.SCALE_SMOOTH), 0, 0, Color.white, null);
            g.dispose();
            ImageIO.write(novaImagem, "JPG", image);
        } catch (IOException ex) {
            Logger.getLogger(ImagemUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
