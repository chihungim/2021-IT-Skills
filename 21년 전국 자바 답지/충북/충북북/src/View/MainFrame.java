package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	JPanel mainP;

	public MainFrame() {
		super("Music");
		setSize(1300, 800);
		add(mainP = new JPanel(new BorderLayout()));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./지급자료/images/logo.png"));
		setBackground(Color.BLACK);
	}

	void swapPage(BasePage p) {
		mainP.removeAll();
		mainP.add(p);
		mainP.repaint();
		mainP.revalidate();
	}

}
