
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.Reserve.Item;

public class Purchase extends BaseFrame {
	JTextField tt[] = { new JTextField(8), new JTextField(3), new JTextField(3), new JTextField(3), new JTextField(3),
			new JTextField(3), new JTextField(5) };
	ArrayList<Integer> list;
	int rand = 0, tot = 1200;
	LocalTime start, end;

	public Purchase(Item item, Reserve r) {
		super("결제", 750, 400);

		add(w = new JPanel(new GridLayout(0, 1, 0, 10)), "West");
		add(c = new JPanel(new BorderLayout()));

		w.setBackground(new Color(50, 100, 255));
		sz(w, 250, 1);
		c.setBackground(Color.WHITE);

		w.add(lbl("결제 정보", 2, 25, Color.WHITE));
		w.add(lbl("예매 구간", 2, 10, Color.WHITE));

		w.add(lbl(stNames.get(0), 2, 15, Color.WHITE));
		w.add(lbl(stNames.get(r.path.size() - 1), 2, 15, Color.WHITE));
		w.add(lbl("구간", 2, 15, Color.WHITE));

		start = LocalTime.of(item.st / 3600, (item.st % 3600) / 60, item.st % 60);

		w.add(lbl("탑승시간:", 2, 10, Color.WHITE));
		w.add(lbl(r.time.toString(), 2, 15, Color.WHITE));
		w.add(lbl(start + " 열차", 2, 15, Color.WHITE));

		w.add(lbl("총 결제 금액:", 2, 10, Color.WHITE));

		if (tot < r.total) {
			tot = r.total * 5;
		}

		if (age <= 13) {
			tot = (int) (tot * 0.9);
		}
		if (age >= 65) {
			tot = (int) (tot * 0.5);
		}

		w.add(lbl(new DecimalFormat("#,##0").format(tot), 2, 15, Color.WHITE));

		JPanel c_n = new JPanel(new GridLayout(0, 1));
		c_n.add(new JLabel(getIcon("logo.png", 180, 30), 2));
		c_n.add(lbl("Seoul Metro Ticket", 2, 20));
		c.add(c_n, "North");
		c_n.setOpaque(false);

		JPanel c_c = new JPanel(new GridLayout(0, 1));
		c.add(c_c);

		JPanel in = new JPanel(new FlowLayout(0));
		in.add(lbl("안녕하세요, " + name + "님.", 2, 15));
		c_c.add(in);

		JPanel in1 = new JPanel(new FlowLayout(0));
		in1.add(lbl("탑승권자 이름은 ", 2, 15));
		in1.add(tt[0]);
		in1.add(lbl("이고,", 2, 15));
		c_c.add(in1);

		JPanel in2 = new JPanel(new FlowLayout(0));
		in2.add(lbl("카드번호는 ", 2, 15));
		for (int i = 1; i < 5; i++) {
			in2.add(tt[i]);
		}
		in2.add(lbl("이고,", 2, 15));
		c_c.add(in2);

		JPanel in3 = new JPanel(new FlowLayout(0));
		in3.add(lbl("CVC는 ", 2, 15));
		in3.add(tt[5]);
		in3.add(lbl("카드 비밀번호는 ", 2, 15));
		in3.add(tt[6]);
		in3.add(lbl("입니다.", 2, 15));
		c_c.add(in3);

		setNum();

		c.add(btn("결제하기", a -> {
			int yn = JOptionPane.showConfirmDialog(null, "구매하시겠습니까?", "메시지", JOptionPane.YES_NO_OPTION);
			if (yn == JOptionPane.YES_OPTION) {
				for (int i = 0; i < tt.length; i++) {
					if (tt[i].getText().isEmpty()) {
						eMsg("모든 항목을 입력해야 합니다.");
						return;
					}
				}

				for (int i = 1; i < 5; i++) {
					if (tt[i].getText().matches(".*\\D.*") || tt[i].getText().length() > 4) {
						eMsg("카드 번호는 각 4자리 숫자로 구성해야합니다.");
						return;
					}
				}

				String cv = "";

				for (int i = 1; i < 4; i++) {
					cv += tt[i].getText().substring(0, 1);
				}

				if (!tt[5].getText().equals(cv)) {
					eMsg("CVC 코드가 일치하지 않습니다.");
					return;
				}
				if (!tt[6].getText().equals(birth + "")) {
					eMsg("카드 비밀번호가 일치하지 않습니다.");
					return;
				}

				String code = new DecimalFormat("000000").format(rand);
				execute("insert into purchase values('" + code + "','" + no + "','" + r.path.get(0) + "','"
						+ r.path.get(r.path.size() - 1) + "','" + tot + "','"
						+ start.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "','" + r.time + "','" + r.total
						+ "')");
				iMsg("결제가 완료되었습니다.\n예매번호:" + code);
				dispose();
			}
		}), "South");

		c_c.setOpaque(false);
		in.setOpaque(false);
		in1.setOpaque(false);
		in2.setOpaque(false);
		in3.setOpaque(false);

		for (int i = 0; i < tt.length; i++) {
			tt[i].setHorizontalAlignment(0);
		}

		setVisible(true);
	}

	private void setNum() {
		list = new ArrayList<Integer>();
		try {
			ResultSet rs = stmt.executeQuery("select * from purchase");
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		while (true) {
			int r = (int) (Math.random() * 1000000);
			if (list.contains(rand)) {
				continue;
			} else {
				rand = r;
				break;
			}
		}
	}

}
