package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasePage extends JPanel {
	
	protected JPanel c, w, e, n, s;
	public static MainFrame mf = new MainFrame();
	static int uno, sno, s_addr, u_addr;
	//u_addr Ãß°¡
	static String uname, sname, pname;
	static LocalDateTime usermodeStartTime;
	
	public BasePage() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		
	}
	
}
