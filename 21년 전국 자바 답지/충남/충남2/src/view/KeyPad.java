package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class KeyPad extends BaseFrame {

	JButton btn[] = new JButton[10];
	JTextField txt = new JTextField();
	ArrayList<Integer> nums = new ArrayList<>();

	public KeyPad() {
		super("키패드", 400, 500);
		add(n = new JPanel(new GridLayout(0, 1, 5, 5)), "North");
		add(c = new JPanel(new GridLayout(0, 3, 5, 5)));
		n.add(txt);
		txt.setHorizontalAlignment(SwingConstants.CENTER);
		n.add(btn("확인", a -> {

			if (txt.getText().equals("")) {
				eMsg("예매번호를 입력해야 합니다.");
				return;
			}

			try {
				var rs = stmt.executeQuery(
						"select * from purchase where user = '" + no + "' and serial = '" + txt.getText() + "'");
				if (rs.next()) {
					new Ticket(rs.getString(1)).addWindowListener(new before(KeyPad.this));
				} else {
					eMsg("예매번호가 일치하지 않습니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}));
		for (int i = 0; i < btn.length; i++) {
			nums.add(i);
			if (i == 9) {
				c.add(btn("←", a -> {
					txt.setText(txt.getText().substring(0, txt.getText().length() - 1));
				}));
			}

			btn[i] = new JButton(i + "");
			btn[i].addActionListener(a -> {
				txt.setText(txt.getText() + a.getActionCommand());
			});
			c.add(btn[i]);
			btn[i].setBackground(Color.WHITE);
			btn[i].setForeground(Color.BLACK);
			if (i == 9)
				c.add(btn("재배치", a -> {
					shuffle();
				}));
		}

		shuffle();

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	void shuffle() {
		Collections.shuffle(nums);
		for (int i = 0; i < nums.size(); i++) {
			btn[i].setText(nums.get(i) + "");
		}
	}

	public static void main(String[] args) {
		new KeyPad();
	}
}
