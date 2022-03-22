package view;

import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Test {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/지도.png"))));
		f.setVisible(true);
	}
}
