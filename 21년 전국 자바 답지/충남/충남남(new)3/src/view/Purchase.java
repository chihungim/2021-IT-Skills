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
		super("결제", 750, 400);

		this.add(w = new JPanel(new GridLayout(0, 1, 0, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));

		w.setBackground(new Color(50, 100, 255));
		sz(w, 250, 1);
		c.setBackground(Color.white);

		w.add(lbl("결제 정보", JLabel.LEFT, 25, Color.white, Font.PLAIN));
		w.add(lbl("예매 구간:", JLabel.LEFT, 10, Color.white, Font.PLAIN));

		w.add(lbl(stations.get(reserve.cpath.get(0)), JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl(stations.get(reserve.cpath.get(reserve.cpath.size() - 1)), JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl("구간", JLabel.LEFT, 15, Color.white, Font.BOLD));

		w.add(lbl(reserve.date + ":", JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl(item.sTime + " 열차", JLabel.LEFT, 15, Color.white, Font.BOLD));
		w.add(lbl("총 결제 금액:", JLabel.LEFT, 10, Color.white, Font.PLAIN));

		if (totalPrice < reserve.totalDistance)
			totalPrice = reserve.totalDistance * 5;

		if (age <= 13)
			totalPrice = (int) (totalPrice * 0.9);

		if (age >= 65)
			totalPrice = (int) (totalPrice * 0.5);

		w.add(lbl(new DecimalFormat("#,##0").format(totalPrice) + "원", JLabel.LEFT, 15, Color.white, Font.BOLD));
		setEmpty(w, 10, 10, 10, 10);

		var c_n = new JPanel(new GridLayout(0, 1));
		c_n.add(new JLabel(img("./지급자료/images/logo.png", 180, 30), 2));
		c_n.add(lbl("Seoul Metro Ticket", 2, 20));
		c.add(c_n, "North");
		c_n.setOpaque(false);

		var c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);

		var c_c1 = new JPanel(new FlowLayout(0));
		c_c1.add(lbl("안녕하세요, " + uname + "님.", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c1);

		var c_c2 = new JPanel(new FlowLayout(0));
		c_c2.add(lbl("탑승권자 이름은 ", 2, 15, Color.black, Font.PLAIN));
		c_c2.add(txt[0]);
		c_c2.add(lbl("이고,", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c2);

		var c_c3 = new JPanel(new FlowLayout(0));
		c_c3.add(lbl("카드번호는 ", 2, 15, Color.black, Font.PLAIN));
		for (int i = 1; i < 5; i++) {
			c_c3.add(txt[i]);
		}
		c_c3.add(lbl("이고,", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c3);

		var c_c4 = new JPanel(new FlowLayout(0));
		c_c4.add(lbl("CVC는", 2, 15, Color.black, Font.PLAIN));
		c_c4.add(txt[5]);
		c_c4.add(lbl("카드 비밀번호는 ", 2, 15, Color.black, Font.PLAIN));
		c_c4.add(txt[6]);
		c_c4.add(lbl("입니다.", 2, 15, Color.black, Font.PLAIN));
		c_c.add(c_c4);

		setNumber();

		c.add(btn("결제하기", a -> {
			for (int i = 0; i < txt.length; i++) {
				if (txt[i].getText().equals("")) {
					eMsg("모든 항목을 입력해야 합니다.");
					return;
				}
			}

			for (int i = 1; i < 5; i++) {
				if (txt[i].getText().matches(".*[^0-9].*") || txt[i].getText().length() > 4) {
					eMsg("카드 번호는 각 4자리 숫자로 구성해야합니다.");
					return;
				}
			}

			String cvc = "";

			for (int i = 1; i < 4; i++) {
				cvc += txt[i].getText().substring(0, 1);
			}

			if (!txt[5].getText().equals(cvc)) {
				eMsg("CVC 코드가 일치하지 않습니다.");
				return;
			}

			if (!txt[6].getText().equals(birthyear + "")) {
				eMsg("카드 비밀번호가 일치 하지 않습니다.");
				return;
			}

			int yesno = JOptionPane.showConfirmDialog(null, "구매하시겠습니까?", "메시지", JOptionPane.YES_NO_OPTION);
			if (yesno == JOptionPane.YES_OPTION) {

				execute("insert purchase values('" + new DecimalFormat("000000").format(randomNumber) + "'," + uno + ","
						+ reserve.cpath.get(0) + "," + reserve.cpath.get(reserve.cpath.size() - 1) + "," + totalPrice
						+ ",'" + item.sTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "','" + reserve.date
						+ "'," + reserve.totalDistance + ")");
				iMsg("결제가 완료되었습니다!\n예매번호:" + new DecimalFormat("000000").format(randomNumber));
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
			txt[i].setFont(new Font("맑은 고딕", Font.BOLD, 15));
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
