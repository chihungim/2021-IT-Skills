package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Purchase extends BaseFrame {

	String listof;

	String name;
	String cardNum[] = new String[4];
	String cvc;

	public Purchase(Reserve r) {
		super("����", 800, 500);
		var w = new JPanel(new BorderLayout());
		var c = new JPanel(new BorderLayout());
		var c_n = new JPanel(new BorderLayout());
		var c_c = new JPanel(new GridLayout(0, 1));
		var c_s = new JPanel(new GridLayout(0, 1));

		ArrayList list = new ArrayList<>(); // �ӽÿ� map -> reserve -> purchase -> �� ���� �� ���� �׳�baseframe�� �ִ°� �������� ��
		list.add("���빮");
		list.add("����");
		list.add("����");

		listof = String.join("<br>", list);

		add(w, "West");
		add(c);
		c.add(c_n, "North");
		c.add(c_s, "South");

		w.setPreferredSize(new Dimension((int) (getWidth() * 0.3), getHeight()));

		w.add(lbl("<html>" + "<font size = \"7\"; face = \"Arial\"; color = \"WHITE\">" + "<left>���� ����" + "<br>"
				+ "<br>" + "<font size = \"5\"; face = \"Calibri\">" + "���� ����" + "<br>"
				+ "<font size = \"6\"; face = \"BOLD\">" + listof + "<br>" + "<br>"
				+ "<font size = \"5\"; font face = \"Calibri\">" + "ž�½ð�" + "<br>" + "2021/10/04" + "<br>"
				+ "21:27:40 ����" + "<br>" + "<br>" + "�� ���� �ݾ�" + "<br>" + "1,080��", JLabel.LEFT, 25), "North");

		w.setBorder(new EmptyBorder(20, 20, 20, 20));

		w.setBackground(Color.BLUE);

		JLabel logo = new JLabel(new ImageIcon("./�����ڷ�/images/logo.png"), JLabel.LEFT);

		c_n.add(logo, "North");
		c_n.add(lbl("Seoul Metro Ticket", JLabel.LEFT, 25));

		c_s.add(btn("�����ϱ�", a -> {

		}));

		c.setBorder(new EmptyBorder(20, 20, 20, 20));

		setVisible(true);
	}

	public static void main(String[] args) {
		new Purchase(null);
	}
}
