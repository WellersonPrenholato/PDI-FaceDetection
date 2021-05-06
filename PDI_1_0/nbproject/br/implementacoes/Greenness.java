package implementacoes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import swing.janelas.PDI_Lote;

/**
 * Classe para os métodos(funcoes) para o calculo do Greenness, onde serão mantidos
 * , que serao usadas pelo resto do programa.
 * 
 * @author
 */
public class Greenness {

	/**
	 * Essa função é a implementação da método de GreennesskG = kG − (R + B)
	 * onde K é o valor passado pelo usuário e o R,G e B são os valores obtido da imagem
	 * 
	 * @param img A imagem onde o filtro será aplicado
	 * @param K O valor K da equação
	 * @return retorna a imagem depois de aplicado o filtro
	 */
	public BufferedImage GreennKG(BufferedImage img) {
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		float R, G, B;
		float maxS =0, minS=1;	
		float maxT=0, minT=1;
		
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color cor = new Color(img.getRGB(i, j));
		
				R = cor.getRed();
				G = cor.getGreen();
				B = cor.getBlue();
				
				// System.out.println("\n1 - Red:" + R + " Verde:" + G + " Azul:" + B);
	
				float[] hsbValues = Color.RGBtoHSB((int)R, (int)G, (int)B, null);
				float hue = hsbValues[0]; 
				float saturation = hsbValues[1];
				// float brightness = hsbValues[2];

				if (maxS >= saturation){
					maxS = saturation;
				}

				if (minS <= saturation){
					minS = saturation;
				}

				if (maxT >= hue){
					maxT = hue;
				}

				if (minT <= hue){
					minT = hue;
				}
			}
		}

		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color cor = new Color(img.getRGB(i, j));
				// Color spaceColor = new Color(img.getColorSpace(i,j));
				R = cor.getRed();
				G = cor.getGreen();
				B = cor.getBlue();
				
				float[] hsbValues = Color.RGBtoHSB((int)R, (int)G, (int)B, null);
				float hue = hsbValues[0]; 
				float saturation = hsbValues[1];
				float brightness = hsbValues[2];
				
				// System.out.println("\n Hue: " + hue + " Saturacao: " + saturation + " Brilho: "+ brightness);

				float S = (saturation - minS)/(maxS - minS);
				float T = (hue - minT)/(maxT - minT);

				int hsbToRgb = Color.HSBtoRGB(T, S, brightness);

				// int red = (hsbToRgb >> 16) & 0xFF;
				// int green = (hsbToRgb >> 8) & 0xFF;
				// int blue = hsbToRgb & 0xFF;

				// System.out.println("\n2 - Red:" + red + " Verde:" + green + " Azul:" + blue);
				
				// Color novoPixel = new Color(red, green, blue);
				Color novoPixel = new Color(hsbToRgb);
				
				res.setRGB(i, j, novoPixel.getRGB());
			}
		}

		for (int i = 0; i < res.getWidth(); i++) {
			for (int j = 0; j < res.getHeight(); j++) {
				Color cor = new Color(res.getRGB(i, j));
				R = cor.getRed();
				G = cor.getGreen();
				B = cor.getBlue();
				
				Color pixelBranco = new Color(255, 255, 255);

				if ( (B > 160 && R < 180 && G < 180) || // Too much blue
					(G > 160 && R < 180 && B < 180) || // Too much green
					(B < 100 && R < 100 && G < 100) || // Too dark
					(G > 200) || // Green
					(R+G > 400) || // Too much red and green (yellow like color)
					(G > 150 && B < 90 ) || // Yellow like also
					(B/(R+G+B) > .40) || // Too much blue in contrast to others
					(G/(R+G+B) > .40) || // Too much green in contrast to others
					(R < 102 && G > 100 && B > 110 && G < 140 && B < 160)){ // Ocean
						res.setRGB(i, j, pixelBranco.getRGB());
					}else{
						res.setRGB(i, j, img.getRGB(i, j));
					}
			}
		}

		return res;
	}
}


