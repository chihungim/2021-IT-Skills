package view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Util;

public class Chart extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), c = new JPanel(new BorderLayout());
	JPanel n_n = new JPanel(), n_c = new JPanel(), c_w = new JPanel(), c_c = new JPanel();
	JLabel lbl[] = { Util.lbl("������ ���� ��Ȳ", 0, 25), Util.lbl("C H A R T", 0, 15) };
	JLabel imglbl = new JLabel();

	public Chart() {
		super("��Ʈ", 750, 650);

		ui();
		setVisible(true);
	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		n.add(n_n, "North");
		n.add(n_c, "Center");
		c.add(c_w, "West");
		c.add(c_c, "Center");

		n_n.add(lbl[0]);
		n_c.add(lbl[1]);
		c_w.add(imglbl);

		imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("Datafiles/����/�ѹݵ�.png").getScaledInstance(280,
				500, Image.SCALE_SMOOTH)));
	}

	public static void main(String[] args) {
		new Chart();
	}
}
