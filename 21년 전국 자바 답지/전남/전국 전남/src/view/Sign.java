package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Sign extends BaseFrame {
	String cap[] = "ÀÌ¸§,¾ÆÀÌµð,ºñ¹Ð¹øÈ£,»ý³â¿ùÀÏ,°ÅÁÖÁö,Á÷Á¢ÀÔ·Â".split(",");

	JTextField txt[] = { new JTextField(15), new JTextField(15), new JTextField(15), new JTextField(15) };
	JComboBox<String> loc;
	JComboBox birth[] = { new JComboBox(), new JComboBox(), new JComboBox() };
	JRadioButton chk;

	public Sign() {
		super("È¸¿ø°¡ÀÔ", 350, 400);
		setUI();
		setData();
		addEvents();
		setVisible(true);
	}

	void setUI() {
		var c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(c);
		add(s, "South");
		for (int i = 0, idx = 0; i < cap.length; i++) {
			var t = new JPanel(new FlowLayout(FlowLayout.LEFT));
			t.add(sz(lbl(cap[i], JLabel.LEFT), 80, 20));
			if (i == 3) {
				String cap[] = "³â,¿ù,ÀÏ".split(",");
				for (int j = 0; j < birth.length; j++) {
					t.add(birth[j]);
					t.add(lbl(cap[j], 0));
				}
			} else if (i == 4) {
				t.add(loc = new JComboBox<String>());
			} else {
				t.add(txt[idx]);
				idx++;
			}
			c.add(t);
		}

		for (var bcap : "È¸¿ø°¡ÀÔ,Ãë¼Ò".split(",")) {
			s.add(btn(bcap, a -> {
				if (a.getActionCommand().equals("Ãë¼Ò")) {
					dispose();
				} else {
					sign();
				}
			}));
		}

		c.add(chk = new JRadioButton("°³ÀÎÁ¤º¸ ÀÌ¿ë µ¿ÀÇ"));

		c.setBorder(new MatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}

	void setData() {
		for (int i = 1990; i < 2022; i++) {
			birth[0].addItem(i);
		}

		for (int i = 1; i < 13; i++) {
			birth[1].addItem(i);
		}

		try {
			var rs = stmt.executeQuery(
					"SELECT left(c_address,2) as loc FROM roomescape.cafe where left(c_address,2) <> 'ÀüºÐ' group by loc");
			while (rs.next()) {
				loc.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void sign() {
		for (int j = 0; j < txt.length; j++) {
			if (txt[j].getText().isEmpty()) {
				eMsg("ºóÄ­ÀÌ ÀÖ½À´Ï´Ù.");
				return;
			}
		}
		if (isExists("user", "u_id", txt[1].getText())) {
			eMsg("¾ÆÀÌµð°¡ Áßº¹µÇ¾ú½À´Ï´Ù.");
			return;
		}

		String pw = txt[2].getText();

		if (!(pw.matches(".*[0-9].*") && pw.matches(".*[a-zA-Z].*") && pw.matches(".*[^0-9a-zA-Z¤¡-ÆR].*"))
				|| pw.length() < 5) {
			eMsg("ºñ¹Ð¹øÈ£ Çü½Ä ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.");
			return;
		}

		if (!chk.isSelected()) {
			eMsg("°³ÀÎÁ¤º¸ µ¿ÀÇ¸¦ ÇØÁÖ¼¼¿ä.");
			return;
		}

		String u_date = birth[0].getSelectedItem() + "-" + birth[1].getSelectedItem() + "-"
				+ birth[2].getSelectedItem();
		String u_address = loc.getSelectedItem() + " " + txt[3].getText();

		execute("insert into user values(0, '" + txt[0].getText() + "', '" + txt[2].getText() + "', '"
				+ txt[0].getText() + "', '" + u_date + "', '" + u_address + "')");

		iMsg("È¸¿ø°¡ÀÔÀÌ ¿Ï·áµÇ¾ú½À´Ï´Ù.");
		dispose();
	}

	void addEvents() {
		for (int i = 0; i < birth.length - 1; i++) {
			birth[i].setSelectedIndex(-1);
			birth[i].addItemListener(a -> {
				if (birth[0].getSelectedIndex() == -1 || birth[1].getSelectedIndex() == -1) {
					return;
				}

				birth[2].removeAllItems();

				var date = LocalDate.of(toInt(birth[0].getSelectedItem()), toInt(birth[1].getSelectedItem()), 1);
				for (int j = 1; j <= date.lengthOfMonth(); j++) {
					birth[2].addItem(j);
				}
			});
		}
	}

	public static void main(String[] args) {
		new Sign();
	}
}
