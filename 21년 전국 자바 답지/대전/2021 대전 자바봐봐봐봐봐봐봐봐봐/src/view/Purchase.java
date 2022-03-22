package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Purchase extends BaseFrame {
	String cap_w[] = "공연명,장소,날짜,좌석,총금액".split(",");
	String cap_e[] = { pinfo[2], pinfo[3], pinfo[6], "", new DecimalFormat("#,##0").format(Stage.sum) };
	String bcap[] = "결제하기,취소".split(",");
	static int height = 320;
	JLabel check;

	public Purchase(String seats, String discounts) {
		super("결제", 300, height);
		var title = lbl("결제", JLabel.CENTER, 30);
		var c = new JPanel();

		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
		var s = new JPanel(new GridLayout(1, 0, 5, 5));
		var c_s = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(title, "North");
		add(c);
		for (int i = 0; i < cap_w.length; i++) {
			if (i != 3) {
				var tmp = new JPanel(new BorderLayout());
				c.add(sz(tmp, 270, 30));
				tmp.add(lbl(cap_w[i], JLabel.LEFT));
				tmp.add(lbl(cap_e[i], JLabel.RIGHT), "East");
			} else {
				var tmp = new JPanel(new BorderLayout());
				var tmp_c = new JPanel(new GridLayout(0, 1));
				tmp.add(lbl(cap_w[i], JLabel.LEFT), "West");
				tmp.add(tmp_c);
				for (int j = 0; j < seats.split("#").length; j++) {
					tmp_c.add(lbl(seats.split("#")[j], JLabel.RIGHT));
				}
				c.add(sz(tmp, 270, seats.split("#").length * 20));
				tmp.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.lightGray));
			}

		}
		c.add(c_s, "South");

		c_s.add(check = lbl("", JLabel.CENTER));
		c_s.add(btn("본인 인증", a -> {
			String result = JOptionPane.showInputDialog("비밀번호를 입력해주세요");

			if (result == null)
				return;

			try {
				var rs = stmt.executeQuery("select * from user where u_no=" + uno + " and u_pw = '" + result + "'");
				if (!rs.next()) {
					eMsg("비밀번호가 일치하지 않습니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			iMsg("본인 인증이 완료되었습니다.");

			((JButton) (a.getSource())).setEnabled(false);
			check.setText("V");
		}));
		for (String bc : bcap) {
			s.add(btn(bc, a -> {
				if (a.getActionCommand().equals("결제하기")) {
					if (!check.getText().equals("V")) {
						eMsg("본인인증을 해주세요.");
						return;
					}

					iMsg("결제가 완료되었습니다.");

					execute("insert ticket values(0," + uno + "," + pinfo[0] + ",'" + seats + "','" + discounts + "')");

					for (var f : getFrames()) {
						if (f instanceof Main) {
							f.setVisible(true);
							f.addWindowListener(new Before(this));
						}
					}
				} else {
					dispose();
				}
			}));
		}

		add(s, "South");

		check.setForeground(Color.GREEN);
		c_s.setBorder(new EmptyBorder(5, 5, 5, 5));
		title.setBorder(new MatteBorder(0, 0, 3, 0, Color.BLACK));
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		pack();
		setVisible(true);
	}
}