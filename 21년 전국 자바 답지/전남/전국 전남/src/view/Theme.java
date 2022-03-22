package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class Theme extends BaseFrame {

	JScrollPane pane;
	JPanel c;

	public Theme() {
		super("테마목록", 450, 600);
		setUI();
		setVisible(true);
	}

	void setUI() {
		var n = new JPanel(new BorderLayout());

		add(n, "North");
		add(pane = new JScrollPane(c = new JPanel(new GridLayout(0, 1))));
		n.add(lbl(cname.split(" ")[0] + " 테마목록", 0, 35));
		n.add(lbl("T H E M E  L I S T", 0, 15), "South");

		try {
			var rs = stmt.executeQuery("select * from theme where t_no in (" + cdivision + ")");
			while (rs.next()) {
				JPanel tmp = new JPanel(new BorderLayout()), s = new JPanel(new FlowLayout());
				tmp.add(imglbl("./Datafiles/테마/" + rs.getInt(1) + ".jpg", 320, 400));
				var lbl1 = lbl(rs.getString(2), 0, 20);
				s.add(lbl1);
				lbl1.setForeground(Color.WHITE);
				var btn1 = btn("테마소개", a -> {
					tno = toInt(((JButton) a.getSource()).getName().split("-")[0]);
					tname = ((JButton) a.getSource()).getName().split("-")[1];
					tdfficulty = ((JButton) a.getSource()).getName().split("-")[2];
					texplain = ((JButton) a.getSource()).getName().split("-")[3];
					ttime = ((JButton) a.getSource()).getName().split("-")[4];
					tpersonnel = ((JButton) a.getSource()).getName().split("-")[5];
					new ThemeInfo().addWindowListener(new Before(this));
				});

				btn1.setName(rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(7) + "-"
						+ rs.getString(4).replace(". ", ".<br><br>") + "-" + rs.getString(6) + "-" + rs.getString(5));
				s.add(btn1);
				tmp.setOpaque(false);
				s.setOpaque(false);
				tmp.add(s, "South");
				c.add(tmp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c.setBackground(Color.BLACK);
	}

	public static void main(String[] args) {
		cdivision = "37,38,41";
		cno = "A-001";
		cname = "에이스 강남점";
		new Theme();
	}
}
