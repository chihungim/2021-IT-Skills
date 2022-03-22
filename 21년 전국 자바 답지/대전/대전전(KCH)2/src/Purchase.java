import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Purchase extends BaseFrame {

	String cap_w[] = "������,���,��¥,�¼�,�ѱݾ�".split(",");
	String cap_e[] = { p_name, p_place, p_date, "", new DecimalFormat("#,##0").format(sumPrice) };
	String bcap[] = "�����ϱ�,���".split(",");
	static int height = 320;

	JLabel check;

	public Purchase(String seatsAndPrice, String seats, String discounts) {
		super("����", 300, height);
		setLayout(new BorderLayout(5, 5));

		var title = lbl("����", JLabel.CENTER, 30);
		var c = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var s = new JPanel(new GridLayout(1, 0, 5, 5));
		var c_s = new JPanel(new FlowLayout(FlowLayout.LEFT));

		add(title, "North");
		add(c);

		for (int i = 0; i < cap_w.length; i++) {
			if (i != 3) {
				var tmp = new JPanel(new BorderLayout());
				c.add(size(tmp, 270, 30));
				tmp.add(lbl(cap_w[i], JLabel.LEFT));
				tmp.add(lbl(cap_e[i], JLabel.RIGHT), "East");
			} else {
				var tmp = new JPanel(new BorderLayout());
				var tmp_c = new JPanel(new GridLayout(0, 1));
				tmp.add(lbl(cap_w[i], JLabel.LEFT), "West");
				tmp.add(tmp_c);
				for (String seat : seatsAndPrice.split("'")) {
					tmp_c.add(lbl(seat, JLabel.RIGHT));
				}

				height += seatsAndPrice.split("'").length * 20;
				c.add(size(tmp, 270, seatsAndPrice.split("'").length * 20));
				tmp.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.lightGray));
			}

		}

		c.add(c_s, "South");
		add(s, "South");

		c_s.add(btn("���� ����", a -> {
			String result = JOptionPane.showInputDialog("��й�ȣ�� �Է����ּ���");

			if (result == null)
				return;

			try {
				var rs = stmt.executeQuery("select * from user where u_no=" + u_no + " and u_pw = '" + result + "'");
				if (!rs.next()) {
					eMsg("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			iMsg("���� ������ �Ϸ�Ǿ����ϴ�.");

			((JButton) (a.getSource())).setEnabled(false);
			check.setText("V");
		}));

		c_s.add(check = lbl("", JLabel.CENTER));

		for (String bc : bcap) {
			s.add(btn(bc, a -> {
				if (a.getActionCommand().equals("�����ϱ�")) {
					if (!check.getText().equals("V")) {
						eMsg("���������� ���ּ���.");
						return;
					}

					iMsg("������ �Ϸ�Ǿ����ϴ�.");

					execute("insert ticket values(0," + u_no + "," + p_no + ",'" + seats + "','" + discounts + "')");

					this.removeWindowListener(bf);
					before.baseFrames.get(0).setVisible(true);
					dispose();
				} else {
					dispose();
				}
			}));
		}

		check.setForeground(Color.GREEN);
		c_s.setBorder(new EmptyBorder(5, 5, 5, 5));
		title.setBorder(new MatteBorder(0, 0, 3, 0, Color.BLACK));
		setSize(300, height);
		setVisible(true);

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
	}
}
