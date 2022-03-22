package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Detail extends BaseDialog {
	String name;
	JPanel p = new JPanel(new GridLayout(0, 1, 0, 5));
	JScrollPane pane;

	public Detail(String name, BaseFrame f) {
		super("상세설명", 400, 500, f);
		this.name = name;
		System.out.println(name);
		setUI();
		pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMinimum());
		setVisible(true);
	}

	void setUI() {
		add(lbl("상세설명", 2, 25), "North");
		add(pane = new JScrollPane(p));
		try {
			var rs = stmt.executeQuery(
					"select * from recommend_info ri, recommend r, location l where ri.recommend_no = r.no and r.location_no = l.no and name = '"
							+ name + "'");
			while (rs.next()) {
				var tmp = new JPanel(new BorderLayout());
				tmp.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit()
						.getImage("지급파일/images/recommend/" + hashMap.get(name) + "/" + rs.getString("title") + ".jpg")
						.getScaledInstance(320, 250, Image.SCALE_SMOOTH))), "North");
				if (!rs.getString("description").isEmpty()) {
					JTextArea area = new JTextArea(rs.getString("description"));
					tmp.add(size(area, 280, 150));
					area.setBorder(new LineBorder(Color.BLACK));
					area.setLineWrap(true);
				} else {
					size(tmp, 280, 250);
				}

				p.add(tmp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));
		p.setBackground(Color.WHITE);
	}
}
