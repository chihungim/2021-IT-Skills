import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageClassification {
	static int line1 = new Color(0, 56, 170).getRGB();
	static int line2 = new Color(26, 199, 47).getRGB();
	static int line3 = new Color(255, 122, 12).getRGB();
	static int line4 = new Color(0, 153, 229).getRGB();
	static int line5 = new Color(112, 12, 209).getRGB();

	public static void main(String[] args) {
		BufferedImage line1 = null;
		BufferedImage line2 = null;
		BufferedImage line3 = null;
		BufferedImage line4 = null;
		BufferedImage line5 = null;

		try {
			line1 = ImageIO.read(new File("./metro.png"));
			line2 = ImageIO.read(new File("./metro.png"));
			line3 = ImageIO.read(new File("./metro.png"));
			line4 = ImageIO.read(new File("./metro.png"));
			line5 = ImageIO.read(new File("./metro.png"));
			getLine1(line1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getLine1(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				if (new Color(img.getRGB(w, h)).getGreen() > 170) {
					img.setRGB(w, h, Color.BLACK.getRGB());
				} else if (new Color(img.getRGB(w, h)).getBlue() > 56) {
					img.setRGB(w, h, Color.BLACK.getRGB());
				}
			}
		}
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				if (new Color(img.getRGB(w, h)).equals(Color.BLACK))
					img.setRGB(w, h, Color.WHITE.getRGB());
			}
		}

		try {
			ImageIO.write(img, "png", new File("./line1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getLine2(BufferedImage img) {

	}

	public static void getLine3(BufferedImage img) {

	}

	public static void getLine4(BufferedImage img) {

	}

	public static void getLine5(BufferedImage img) {

	}
}
