package implementacoes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import swing.janelas.PDI_Lote;

/**
 * Classe para os métodos(funcoes) para o calculo do Greenness, onde serão mantidos, que serao usadas pelo resto do programa.
 * 
 * @author
 */
public class Greenness {

	/**
	 * 
	 * @param img A imagem onde o filtro será aplicado
	 * @param K O valor K da equação
	 * @return retorna a imagem depois de aplicado o filtro
	 */
	public BufferedImage GreennKG(BufferedImage img) {
		BufferedImage res = new BufferedImage(img.getWidth(), img.getHeight(), img.getType()); //Carregamento da imagem para res

		// Declaração de variáveis
		float R, G, B; //Utilizado para armazenar os valores de Red, Blue e Green de cada pixel da imagem.
		float maxS =0, minS=1;
		float maxT=0, minT=1;

		//Hue -> Matiz
		//Saturation -> Saturação
		//Brightness -> Brilho

		// Essa estrutura percorre toda a imagem, levando em consideração sua altura e largura.
		// O intuito é encontrar o mínimo e máximo de S(Saturation) e T(Matiz), dentro da imagem.
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color cor = new Color(img.getRGB(i, j)); //Identifica a cor na posição ij e desestrutura em cores RGB.
				
				//Acessa os valores das cores RGB existentes no pixel
				R = cor.getRed();
				G = cor.getGreen();
				B = cor.getBlue();
				
				float[] hsbValues = Color.RGBtoHSB((int)R, (int)G, (int)B, null);  // Conversão de RGB para HSB (Hue, Saturation, Brightness)
				float hue = hsbValues[0]; // Atribui o valor da matiz que o pixel possui ao vetor na posição 0.
				float saturation = hsbValues[1]; // Atribui o valor da saturação que o pixel possui ao vetor na posição 1.
				// float brightness = hsbValues[2];
				
				// Condicional utilizado para encontrar o valor máximo da Saturação na imagem
				if (maxS >= saturation){
					maxS = saturation;
				}

				// Condicional utilizado para encontrar o valor mínimo da Saturação na imagem
				if (minS <= saturation){
					minS = saturation;
				}

				// Condicional utilizado para encontrar o valor máximo da Matiz na imagem
				if (maxT >= hue){
					maxT = hue;
				}

				// Condicional utilizado para encontrar o valor máximo da Matiz na imagem
				if (minT <= hue){
					minT = hue;
				}
			}
		}


		for (int i = 0; i < img.getWidth(); i++) { 
			for (int j = 0; j < img.getHeight(); j++) {
				Color cor = new Color(img.getRGB(i, j)); //Identifica a cor na posição ij
				R = cor.getRed(); // Identifica o valor Red do pixel podendo ser 0-255, e atribuí esse valor a uma variável R.
				G = cor.getGreen(); // Identifica o valor Green do pixel podendo ser 0-255, e atribuí esse valor a uma variável G.
				B = cor.getBlue(); // Identifica o valor Blue do pixel podendo ser 0-255, e atribuí esse valor a uma variável B.

				float[] hsbValues = Color.RGBtoHSB((int)R, (int)G, (int)B, null); // Conversão de RGB para HSB (Hue, Saturation, Brightness)
				float hue = hsbValues[0]; // Atribuição do Hue, baseado na conversão da linha anterior.
				float saturation = hsbValues[1]; // Atribuição da Saturation, baseado na conversão da linha anterior.
				float brightness = hsbValues[2]; // Atribuição da Brightness, baseado na conversão da linha anterior.
				
				// Normalização simples para o valor de saturação em [0; 1.0]
				float S = (saturation - minS)/(maxS - minS); 
				float T = (hue - minS)/(maxT - minS);

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

		/* Em qualquer imagem com fundo complexo, a área da pele costuma ser menor que
		os pixels não skin. Uma vez que usamos um procedimento de avaliação de pixels, para reduzir o
		esforço de computação, aplicamos um filtro inicial que pode remover todos os pixels facilmente
		rotulado como sem pele. Este filtro visa apenas remover pixels de processamento posterior e, 
		com sorte, os pixels da capa não são removidos. Verde, azul, amarelo e outros bem
		cores definidas são eliminadas usando regras empíricas definido no condicional nas linhas 118 à 126.
		*/
		for (int i = 0; i < res.getWidth(); i++) {
			for (int j = 0; j < res.getHeight(); j++) {
				Color cor = new Color(res.getRGB(i, j));
				R = cor.getRed();
				G = cor.getGreen();
				B = cor.getBlue();
				
				Color pixelBranco = new Color(255, 255, 255); // Criação de uma variável referente ao pixel totalmente branco.

				if ( (B > 160 && R < 180 && G < 180) || // Too much blue
					(G > 160 && R < 180 && B < 180) || // Too much green
					(B < 100 && R < 100 && G < 100) || // Too dark
					(G > 200) || // Green
					(R+G > 400) || // Too much red and green (yellow like color)
					(G > 150 && B < 90 ) || // Yellow like also
					(B/(R+G+B) > .40) || // Too much blue in contrast to others
					(G/(R+G+B) > .40) || // Too much green in contrast to others
					(R < 102 && G > 100 && B > 110 && G < 140 && B < 160)){ // Ocean
						res.setRGB(i, j, pixelBranco.getRGB()); // Caso o pixel não seja considerado pele, ele se tornará um pixel branco
					}else{
						res.setRGB(i, j, img.getRGB(i, j)); // Caso o pixel seja considerado pele, o pixel da imagem é mantido.
					}
			}
		}

		return res;
	}
}


