package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import additional.Pattern;
import additional.Util;
import db.DBManager;
import view.MainFrame.BtnPanel;

public class LoginPage extends BasePage {
	
	String bcap[] = "������,�Ǹ���".split(",");
	String tcap[] = "user,seller".split(",");
	JPanel page;
	JPanel lbl;
	JTextField txt[] = { new JTextField(), new JPasswordField() };
	String cap[] = "���̵�,��й�ȣ".split(",");
	int login;
	JLabel sign;
	int cnt;
	boolean captcha;
	

	public LoginPage() {
		this.add(page = new JPanel(new GridBagLayout()));
		page.setBackground(Color.white);
		
		var grid = new JPanel(new GridLayout(1, 0, 10, 5));
		page.add(grid);
		
		for (int i = 0; i < bcap.length; i++) {
			int idx=i;
			grid.add(Util.sz(Util.btn(bcap[i] + "�� �α���", a->{
				login = idx;
				this.loginField();
			}), 150, 150));
		}
	}
	
	void loginField() {
		page.removeAll();
		page.add(c = new JPanel(new BorderLayout(5,5)));
		c.add(s = new JPanel(new GridLayout(1, 0, 10, 10)), "South");
		var c_c = new JPanel(new GridLayout(0, 1, 10, 10));
		c.add(c_c);
		c.setOpaque(false);
		c_c.setOpaque(false);
		s.setOpaque(false);
		
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(Util.sz(new JLabel(cap[i]), 60, 25));
			tmp.add(Util.sz(txt[i], 230, 35));
			c_c.add(tmp);
			tmp.setOpaque(false);
		}
		txt[1].addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				e.consume();
			}
		});
		txt[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Pattern(txt[1]).setVisible(true);
			}
		});
		var s_grid = new JPanel(new GridLayout(0, 1, 5, 10));
		s_grid.setOpaque(false);
		s.add(s_grid);
		
		s_grid.add(Util.sz(Util.btn(bcap[login]+"�� �α���", a->{
			String id = txt[0].getText(), pw = txt[1].getText();
			if (id.isEmpty() || pw.isEmpty()) {
				Util.eMsg("��ĭ�� �ֽ��ϴ�.");
				return;
			}
			if (pw.length() < 4) {
				Util.eMsg("������ 3�ڸ� �̻����� �Է����ּ���.");
				return;
			}
			String sql = (login==0)?"select * from user where u_Id = '"+id+"' and u_Pattern = '"+pw+"'":"select * from seller where s_Id = '"+id+"' and s_Pattern = '"+pw+"'";
			try {
				System.out.println(sql);
				var rs = DBManager.rs(sql);
				if (rs.next()) {
					if (login==0) {
						uno = rs.getInt(1);
						uname = rs.getString(4);
						//u_addr �߰�
						u_addr = rs.getInt(5);;
						Util.iMsg(uname+"ȸ����, ȯ���մϴ�.");
						mf.btnInit(true);
						mf.addPage(new MainPage());
					} else {
						sno = rs.getInt(1);
						sname = rs.getString(4);
						//s_addr �߰�
						s_addr = rs.getInt(5);
						Util.iMsg(sname+"�Ǹ��ڴ�, ȯ���մϴ�.");
						mf.btnInit(false);
						mf.addPage(new ManagePage());
					}
					mf.btn.setText("Logout");
					return;
				} else {
					Util.eMsg("���̵� ������ �ٽ� Ȯ�����ּ���.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}), 1, 35));
		
		
		repaint();
		revalidate();
	}
	
}
