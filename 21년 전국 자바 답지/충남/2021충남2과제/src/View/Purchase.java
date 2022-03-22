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
		super("결제", 800, 500);
		var w = new JPanel(new BorderLayout());
		var c = new JPanel(new BorderLayout());
		var c_n = new JPanel(new BorderLayout());
		var c_c = new JPanel(new GridLayout(0, 1));
		var c_s = new JPanel(new GridLayout(0, 1));

		ArrayList list = new ArrayList<>(); // 임시용 map -> reserve -> purchase -> 를 거쳐 올 예정 그냥baseframe에 넣는게 나을지도 모름
		list.add("동대문");
		list.add("동작");
		list.add("구간");

		listof = String.join("<br>", list);

		add(w, "West");
		add(c);
		c.add(c_n, "North");
		c.add(c_s, "South");

		w.setPreferredSize(new Dimension((int) (getWidth() * 0.3), getHeight()));

		w.add(lbl("<html>" + "<font size = \"7\"; face = \"Arial\"; color = \"WHITE\">" + "<left>결제 정보" + "<br>"
				+ "<br>" + "<font size = \"5\"; face = \"Calibri\">" + "예매 구간" + "<br>"
				+ "<font size = \"6\"; face = \"BOLD\">" + listof + "<br>" + "<br>"
				+ "<font size = \"5\"; font face = \"Calibri\">" + "탑승시간" + "<br>" + "2021/10/04" + "<br>"
				+ "21:27:40 열차" + "<br>" + "<br>" + "총 결제 금액" + "<br>" + "1,080원", JLabel.LEFT, 25), "North");

		w.setBorder(new EmptyBorder(20, 20, 20, 20));

		w.setBackground(Color.BLUE);

		JLabel logo = new JLabel(new ImageIcon("./지급자료/images/logo.png"), JLabel.LEFT);

		c_n.add(logo, "North");
		c_n.add(lbl("Seoul Metro Ticket", JLabel.LEFT, 25));

		c_s.add(btn("결제하기", a -> {

		}));

		c.setBorder(new EmptyBorder(20, 20, 20, 20));

		setVisible(true);
	}

	public static void main(String[] args) {
		new Purchase(null);
	}
}
