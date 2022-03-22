package ±§±§¡÷;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class BaseDialog extends JDialog {

	static Connection con = DB.con;
	static Statement stmt = DB.stmt;
	static Color btnColor = new Color(52, 146, 75);
	static {
		try {
			stmt.execute("use eats");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void execute(String sql) {
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	JButton btn(String cap, ActionListener a) {
		JButton b = new JButton(cap);
		b.setBackground(btnColor);
		b.setForeground(Color.WHITE);
		b.addActionListener(a);
		return b;
	}

	public BaseDialog(BaseFrame b, String title, int w, int h) {
		super(b, true);
		setTitle(title);
		setSize(w, h);
		setLocationRelativeTo(null);
	}
}
