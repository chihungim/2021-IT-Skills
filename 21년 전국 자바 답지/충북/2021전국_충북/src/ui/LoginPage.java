package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import base.BasePage;

public class LoginPage extends BasePage{
	
	String[] cap = "ID,PW".split(",");
	JTextField txt[] = {new JTextField(10), new JPasswordField(10)};
	
	JPanel master = new JPanel(new BorderLayout()) {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(3));
			g.setColor(Color.LIGHT_GRAY);
			RoundRectangle2D rec= new RoundRectangle2D.Float(1.5f, 1.5f, getWidth()-3, getHeight()-3, 25, 25);
			g2.fill(rec);
		}
	};
	
	public LoginPage() {
		JPanel c = new JPanel(new GridLayout(0, 1, 5, 5));
		JPanel s = new JPanel(new GridLayout());
		
		setBackground(Color.darkGray);
		master.setBackground(Color.DARK_GRAY);
		c.setBackground(Color.LIGHT_GRAY);
		
		add(master);
		master.add(lbl("Music Player", JLabel.CENTER, Font.BOLD, 30), "North");
		master.add(c);
		master.add(s, "South");
		
		for (int i = 0; i < cap.length; i++) {
			c.add(lbl(cap[i], JLabel.LEFT, 0, 15));
			txt[i].setBackground(Color.gray);
			c.add(txt[i]);
		}
		
		JButton btn = btn("로그인", a->{
			if(txt[0].getText().equals("") || txt[1].getText().equals("")) {
				eMsg("아이디와 비밀번호를 모두 입력해야 합니다.");
				return;
			}
			
			if(txt[0].getText().equals("admin") && txt[1].getText().equals("1234")) {
				
			}else {
				try {
					var rs = stmt.executeQuery("select serial, name, region from user where id = '"+txt[0].getText()+"' and pw ='"+txt[1].getText()+"'");
					if(rs.next() ) {
						u_serial = rs.getInt(1);
						u_region = rs.getInt(3);
						iMsg(rs.getString(2)+ "님 환영합니다.");
						mf.remove(LoginPage.this);
						homePage = new HomePage();
						mf.add(homePage);
						mf.repaint();
						mf.revalidate();
					}else {
						eMsg("아이디 또는 비밀번호가 일치하지 않습니다.");
						return;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btn.setBorder(BorderFactory.createEmptyBorder());
		
		c.add(btn);

		master.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		setBorder(new EmptyBorder(200, 500, 200, 500));
	}
	
	public static void main(String[] args) {
		mf.add(new LoginPage());
		mf.repaint();
		mf.revalidate();
	}
}
