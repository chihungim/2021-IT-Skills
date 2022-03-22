package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import db.DBManager;
import util.Util;

public class Introduce extends BaseFrame {

	JPanel n, c, c_n, c_s;

	public Introduce() {
		super("지점소개", 700, 400);
		
		add(n = new JPanel(new BorderLayout()), "North");
		add(c = new JPanel(new BorderLayout()));
		
		c.add(c_n = new JPanel(new BorderLayout()), "North");
		c.add(c_s = new JPanel(new GridLayout(1, 0)), "South");
		
		n.add(Util.lbl("지점 소개", 0, 35));
		n.add(Util.lbl("B R A N C H  O F F I C E", 0, 15), "South");
		
		c_n.add(Util.lbl("| "+cname, JLabel.LEFT), "West");
		c_n.add(Util.btn("테마 보러가기", a->{
			new ThemeList().addWindowListener(new Before(Introduce.this));
		}), "East");
		
		c.add(new JLabel(Util.img("./Datafiles/지점/"+cafename+".jpg", 600, 200)));
		
		try {
			var rs = DBManager.rs("select c_address, c_tel from cafe where c_name='"+cname+"'");
			if(rs.next()) {
				c_s.add(Util.lbl(rs.getString(1), JLabel.CENTER));
				c_s.add(Util.lbl(rs.getString(2), JLabel.CENTER));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Search();
	}
}
