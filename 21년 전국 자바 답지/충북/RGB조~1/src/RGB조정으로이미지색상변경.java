import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RGB조정으로이미지색상변경 extends JFrame {
	BufferedImage img, img2;
	JLabel imgLbl;

	public RGB조정으로이미지색상변경() {
		super("이미지 색상 변경");
		this.setSize(1200, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		// 이미지
		try {
			img = ImageIO.read(new File("강원도.png"));
			img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

			colorChange();
			imgLbl = new JLabel(new ImageIcon(img2.getScaledInstance(img2.getWidth(), img2.getHeight(), Image.SCALE_DEFAULT)));
		} catch (IOException e1) {
		}

		add(imgLbl);
		this.setVisible(true);
	}

	private void colorChange() {
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color color = new Color(img.getRGB(x, y));
				int r = color.getRed();
				int g = color.getGreen();
				int b = color.getBlue();
				if (color.getRGB() != -16777216) {
					img2.setRGB(x, y, new Color(r, 0, 0).getRGB());
				}
			}
		}
	}

	public static void main(String[] args) {
		new RGB조정으로이미지색상변경();
	}
}