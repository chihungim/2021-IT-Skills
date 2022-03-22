package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import db.DBManager;

public class MoreInform extends BaseFrame {

	JCheckBox chk = new JCheckBox("장애인");
	JTextArea area = new JTextArea();
	String cap[] = "탑승인원,탑승제한,위치".split(",");
	String info[] = new String[3];

	public MoreInform(String tit) {
		super("상세정보", 700, 400);

		this.add(w = new JPanel(new BorderLayout()), "West");
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");

		w.setOpaque(false);
		c.setOpaque(false);
		s.setOpaque(false);

		JLabel lbl;
		w.add(lbl = new JLabel(img("./datafiles/이미지/" + tit + ".jpg", 230, 300)));
		setLine(lbl, Color.black);

		s.add(btn("예매", a -> {
			var no = DBManager.getOne("select r_no from ride where r_name = '" + tit + "'");
			if (rei(DBManager.getOne("SELECT count(*) FROM ticket where t_date = curdate() and r_no=" + no)) > rei(
					info[0])) {
				eMsg(tit + "은(는) 만석입니다.");
			}

			iMsg("예매가 완료되었습니다.");
			DBManager.execute("insert into ticket values(0, '" + uno + "',curdate(),'" + no + "',0)");
			new Main().addWindowListener(new Before(this));

		}));

		c.add(lbl(tit, 0, 25), "North");

		var c_c = new JPanel(new BorderLayout());
		c.add(c_c);
		c_c.setOpaque(false);

		c_c.add(new JLabel("설명"), "North");
		c_c.add(new JScrollPane(area));
		area.setLineWrap(true);
		setLine(area, Color.black);
		area.setText(DBManager.getOne("select r_explation from ride where r_name ='" + tit + "'"));

		var c_s = new JPanel(new GridLayout(0, 2));
		c.add(c_s, "South");

		try {
			var rs = DBManager.rs("select r_max, r_height, r_floor from ride where r_name = '" + tit + "'");
			if (rs.next()) {
				for (int i = 0; i < info.length + 1; i++) {
					var tmp = new JPanel(new FlowLayout(0));

					c_s.add(tmp);
					if (i == 3) {
						tmp.setLayout(new FlowLayout(1));
						tmp.add(chk);

					} else {
						info[i] = rs.getString(i + 1);
						tmp.add(new JLabel(cap[i] + " : " + info[i], JLabel.LEFT));
					}
					tmp.setOpaque(false);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c_s.setOpaque(false);
		chk.setOpaque(false);
		chk.setEnabled(false);	
		chk.setSelected(DBManager.getOne("select r_disable from ride where r_name = '" + tit + "'").contentEquals("1"));
		chk.setHorizontalAlignment(SwingConstants.RIGHT);

		setEmpty(c, 0, 20, 0, 20);
		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new MoreInform("신밧드의 모험");
	}

}
