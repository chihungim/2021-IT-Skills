package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import view.Reserve.Item;

public class Purchase extends BaseFrame {

	JTextField txt[] = { new JTextField(8), new JTextField(3), new JTextField(3), new JTextField(3), new JTextField(3),
			new JTextField(3), new JTextField(5), };
	ArrayList<Integer> lists;
	int randomNumber = 0;
	int totalPrice = 1200;

	public Purchase(Item item, Reserve reserve) {
		super("����", 750, 400);

		this.add(w = new JPanel(new GridLayout(0, 1, 0, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));

		w.setBackground(new Color(50, 100, 255));
		sz(w, 250, 1);
		c.setBackground(Color.white);

		w.add(lbl("���� ����", JLabel.LEFT, 25, Color.white, Font.PLAIN));
		w.add(lbl("���� ����:", JLabel.LEFT, 10, Color.white, Font.PLAIN));

		w.add(lbl(stations.get(reserve.cpath.get(0)), JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl(stations.get(reserve.cpath.get(reserve.cpath.size() - 1)), JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl("����", JLabel.LEFT, 15, Color.white, Font.BOLD));

		w.add(lbl(reserve.date + ":", JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl(item.sTime + " ����", JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl("�� ���� �ݾ�:", JLabel.LEFT, 10, Color.white, Font.PLAIN));

		if (totalPrice < reserve.totalDistance)
			totalPrice = reserve.totalDistance * 5;

		if (age <= 13)
			totalPrice = (int) (totalPrice * 0.9);

		if (age >= 65)
			totalPrice = (int) (totalPrice * 0.5);

		w.add(lbl(new DecimalFormat("#,##0").format(totalPrice) + "��", JLabel.LEFT, 15, Color.white, Font.BOLD));
		setEmpty(w, 10, 10, 10, 10);

		var c_n = new JPanel(new GridLayout(0, 1));
		c_n.add(new JLabel(img("./�����ڷ�/images/logo.png", 180, 30), 2));
		c_n.add(lbl("Seoul Metro Ticket", 2, 20));
		c.add(c_n, "North");
		c_n.setOpaque(false);

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);

		var c_c1 = new JPanel(new FlowLayout(0));
		c_c1.add(lbl("�ȳ��ϼ���, " + uname + "��.", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c1);

		var c_c2 = new JPanel(new FlowLayout(0));
		c_c2.add(lbl("ž�±��� �̸��� ", 2, 15, Color.black, Font.PLAIN));
		c_c2.add(txt[0]);
		c_c2.add(lbl("�̰�,", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c2);

		var c_c3 = new JPanel(new FlowLayout(0));
		c_c3.add(lbl("ī���ȣ�� ", 2, 15, Color.black, Font.PLAIN));
		for (int i = 1; i < 5; i++) {
			c_c3.add(txt[i]);
		}
		c_c3.add(lbl("�̰�,", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c3);

		var c_c4 = new JPanel(new FlowLayout(0));
		c_c4.add(lbl("CVC��", 2, 15, Color.black, Font.PLAIN));
		c_c4.add(txt[5]);
		c_c4.add(lbl("ī�� ��й�ȣ�� ", 2, 15, Color.black, Font.PLAIN));
		c_c4.add(txt[6]);
		c_c4.add(lbl("�Դϴ�.", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c4);

		setNumber();

		c.add(btn("�����ϱ�", a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().equals("")) {
					eMsg("��� �׸��� �Է��ؾ� �մϴ�.");
					return;
				}
			}

			for (int i = 1; i < 5; i++) {
				if (txt[i].getText().matches(".*[^0-9].*") || txt[i].getText().length() > 4) {
					eMsg("ī�� ��ȣ�� �� 4�ڸ� ���ڷ� �����ؾ��մϴ�.");
					return;
				}
			}

			String cvc = "";

			for (int i = 1; i < 4; i++) {
				cvc += txt[i].getText().substring(0, 1);
			}

			if (!txt[5].getText().equals(cvc)) {
				eMsg("CVC �ڵ尡 ��ġ���� �ʽ��ϴ�.");
				return;
			}

			if (!txt[6].getText().equals(birthyear + "")) {
				eMsg("ī�� ��й�ȣ�� ��ġ ���� �ʽ��ϴ�.");
				return;
			}

			int yesno = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "�޽���", JOptionPane.YES_NO_OPTION);
			if (yesno == JOptionPane.YES_OPTION) {

				execute("insert purchase values('" + new DecimalFormat("000000").format(randomNumber) + "'," + uno + ","
						+ reserve.cpath.get(0) + "," + reserve.cpath.get(reserve.cpath.size() - 1) + "," + totalPrice
						+ ",'" + item.sTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "','" + reserve.date
						+ "'," + reserve.totalDistance + ")");
				iMsg("������ �Ϸ�Ǿ����ϴ�!\n���Ź�ȣ:" + new DecimalFormat("000000").format(randomNumber));
				dispose();
			}

		}), "South");

		c_c.setOpaque(false);
		c_c1.setOpaque(false);
		c_c2.setOpaque(false);
		c_c3.setOpaque(false);
		c_c4.setOpaque(false);
		setEmpty(c_c, 30, 0, 30, 0);
		setEmpty(c, 10, 20, 10, 10);

		for (int i = 0; i < txt.length; i++) {
			txt[i].setHorizontalAlignment(SwingConstants.CENTER);
			txt[i].setFont(new Font("���� ���", Font.BOLD, 15));
			txt[i].setBorder(new MatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		}

		this.setVisible(true);
	}

	void setNumber() {
		lists = new ArrayList<>();

		try {
			var rs = stmt.executeQuery("SELECT * FROM metro.purchase");
			while (rs.next()) {
				lists.add(rei(rs.getString(1)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			int rand = (int) (Math.random() * 1000000);
			if (lists.contains(randomNumber)) {
				continue;
			} else {
				randomNumber = rand;
				break;
			}
		}
	}
}
