package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class MoreInform extends BaseFrame {
	JCheckBox chk = new JCheckBox("장애인");
	JTextArea area = new JTextArea();
	String cap[] = "탑승인원,탑승제한,위치".split(",");
	String info[] = new String[3];
	String title;
	JLabel imglbl;

	public MoreInform(String title) {
		super("상세정보", 700, 400);
		this.title = title;
		data();
		ui();

		setVisible(true);
	}

	void ui() {

		setLayout(new BorderLayout(5, 5));

		var w = new JPanel(new BorderLayout());
		var c = new JPanel(new BorderLayout());
		var s = new JPanel(new FlowLayout(2));
		var c_c = new JPanel(new BorderLayout());
		var c_s = new JPanel(new GridLayout(0, 2));

		add(w, "West");
		add(c);
		add(s, "South");
		c.add(c_c);
		c.add(c_s, "South");

		c.add(lbl(title, 0, 25), "North");
		c_c.add(new JLabel("설명"), "North");
		c_c.add(new JScrollPane(area));
		w.add(setLine(imglbl, Color.BLACK));
		s.add(btn("예매", a -> btn_event()));

		for (int i = 0; i < info.length + 1; i++) {
			var tmp = new JPanel(new FlowLayout(0));
			tmp.setOpaque(false);
			c_s.add(tmp);

			if (i == 3) {
				tmp.setLayout(new FlowLayout(1));
				tmp.add(chk);
			} else {
				tmp.add(new JLabel(cap[i] + " : " + info[i], JLabel.LEFT));
				tmp.setOpaque(false);
			}
		}
		area.setLineWrap(true);
		area.setEnabled(false);
		setLine(area, Color.black);
		w.setOpaque(false);
		c.setOpaque(false);
		s.setOpaque(false);
		c_c.setOpaque(false);
		c_s.setOpaque(false);
		chk.setOpaque(false);
		chk.setEnabled(false);
		chk.setHorizontalAlignment(SwingConstants.RIGHT);
		setEmpty(c, 0, 20, 0, 20);
		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
	}

	void data() {
		area.setText(getOne("select r_explation from ride where r_name ='" + title + "'").split("#")[0]);

		try {
			var rs = stmt.executeQuery("select r_max, r_height, r_floor from ride where r_name = '" + title + "'");
			if (rs.next()) {
				for (int i = 0; i < info.length; i++) {
					info[i] = rs.getString(i + 1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			var rs = stmt.executeQuery("select * from ride where r_name like '%" + title + "%'");
			if (rs.next()) {
				imglbl = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(rs.getBytes("r_img"))
						.getScaledInstance(230, 300, Image.SCALE_SMOOTH)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		chk.setSelected(getOne("select r_disable from ride where r_name = '" + title + "'").contentEquals("1"));
	}

	void btn_event() {
		var no = getOne("select r_no from ride where r_name = '" + title + "'");
		if (rei(getOne("SELECT count(*) FROM ticket where t_date = curdate() and r_no=" + no)) > rei(info[0])) {
			eMsg(title + "은(는) 만석입니다.");
		}

		iMsg("예매가 완료되었습니다.");
		execute("insert into ticket values(0, '" + uno + "',curdate(),'" + no + "',0)");
		for (var w : getFrames()) {
			if (w instanceof Main) {
				w.setVisible(true);
				w.addWindowListener(new Before(this));
			}
		}
	}

	public static void main(String[] args) {
		new MoreInform("신밧드의 모험");
	}

}
