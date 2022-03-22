package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Purchase extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(), c = new JPanel(new BorderLayout()), s = new JPanel(new BorderLayout());
	JPanel c_n = new JPanel(new BorderLayout()), c_c = new JPanel(new BorderLayout()),
			c_s = new JPanel(new BorderLayout());
	JPanel s_n = new JPanel(new FlowLayout(0)), s_c = new JPanel(new GridLayout(0, 2));
	JPanel c_n_w = new JPanel(new GridLayout(0, 1)), c_n_e = new JPanel(new GridLayout(0, 1)),
			c_c_w = new JPanel(new GridLayout()), c_c_e = new JPanel(new GridLayout(0, 2)),
			c_s_w = new JPanel(new GridLayout(0, 1)), c_s_e = new JPanel(new GridLayout(0, 1));
	JLabel titlelbl = lbl("결제", JLabel.CENTER, 25);
	JLabel lbl[] = { new JLabel("공연명"), new JLabel("장소"), new JLabel("날짜"), new JLabel("좌석"), new JLabel("총금액"),
			new JLabel("V") };
	JLabel idxlbl[] = { new JLabel(), new JLabel(), new JLabel(), new JLabel() };
	ArrayList<JLabel> seatlbl = new ArrayList<JLabel>();
	ArrayList<JLabel> pricelbl = new ArrayList<JLabel>();
	JButton btn[] = { new JButton("본인 인증"), new JButton("결제하기"), new JButton("취소") };
	boolean bool;

	public Purchase() {
		super("결제", 350, 330);

		data();
		ui();
		properties();
		event();
		setVisible(true);
	}

	private void event() {
		for (int i = 0; i < btn.length; i++) {
			btn[i].addActionListener(a -> {
				if (a.getActionCommand().equals("본인 인증")) {
					String yesno = JOptionPane.showInputDialog("비밀번호를 입력해주세요.");
					try {
						ResultSet rs = stmt.executeQuery(
								"SELECT * FROM 2021전국.user where u_no = " + u_no + " and u_pw = '" + yesno + "'");
						if (rs.next()) {
							if (yesno.equals(rs.getString(4))) {
								btn[0].setEnabled(false);
								lbl[5].setVisible(true);
								bool = true;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (a.getActionCommand().equals("결제하기")) {
					if (bool == false) {
						eMsg("본인인증을 해주세요.");
						return;
					} else {
						iMsg("결제가 완료되었습니다.");
						new Main().addWindowListener(new before(Purchase.this));
//						execute("insert into perform values");
					}
				} else {
					dispose();
				}
			});
		}
	}

	private void data() {
		for (int i = 0; i < count; i++) {
			seatlbl.add(new JLabel("A01"));
			pricelbl.add(new JLabel(p_price));
			c_c_e.add(seatlbl.get(i));
			c_c_e.add(pricelbl.get(i));
		}
	}

	private void properties() {
		idxlbl[0].setText(p_name);
		idxlbl[1].setText(p_place);
		idxlbl[2].setText(date);
		idxlbl[3].setText(total_price);

		lbl[5].setVisible(false);
		lbl[5].setForeground(Color.GREEN);
	}

	private void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		mainp.add(s, "South");
		c.add(c_n, "North");
		c.add(c_c, "Center");
		c.add(c_s, "South");
		s.add(s_n, "North");
		s.add(s_c, "Center");
		c_n.add(c_n_w, "West");
		c_n.add(c_n_e, "East");
		c_c.add(c_c_e, "East");
		c_c.add(c_c_w, "West");
		c_s.add(c_s_e, "East");
		c_s.add(c_s_w, "West");

		n.add(titlelbl);
		for (int i = 0; i < 3; i++) {
			c_n_w.add(lbl[i]);
			c_n_e.add(idxlbl[i]);
		}
		c_c_w.add(lbl[3]);
		c_s_w.add(lbl[4]);
		c_s_e.add(idxlbl[3]);
		s_n.add(btn[0]);
		s_n.add(lbl[5]);
		s_c.add(btn[1]);
		s_c.add(btn[2]);
	}

	public static void main(String[] args) {
		u_no = 1;
		new Purchase();
	}
}
