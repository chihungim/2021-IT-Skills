package view;

import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import db.DBManager;

public class Detail extends BaseFrame {
	
	JScrollPane scr;
	
	public Detail(int no) {
		super("상세설명", 400, 600);

		this.add(lbl("상세설명", 2, 25), "North");
		this.add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 1))));
		
		try {
			var rs = DBManager.rs("SELECT title, l.no, name, img, descrption FROM recommend_info r2, recommend r, location l where  r2.recommend_no =r.no and r.location_no = l.no and l.no = "+no);
			while (rs.next()) {
				c.add(new JLabel(img("./지급파일/images/recommend/"+hash.get(rs.getString(3))+"/"+rs.getString(1)+".jpg", 330, 250)));
				if (rs.getString(5) != null) {
					JTextArea ta = new JTextArea(rs.getString(5));
					ta.setLineWrap(true);
					c.add(new JScrollPane(ta));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setEmpty((JPanel)getContentPane(), 15, 15, 15, 15);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Detail(10);
	}
	
}
