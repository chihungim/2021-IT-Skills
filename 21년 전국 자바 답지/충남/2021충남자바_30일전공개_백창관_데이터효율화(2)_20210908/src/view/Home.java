package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import db.stmt;

public class Home extends BaseFrame {
	
	JLabel back;
	JLabel logo;
	JPanel jp[] = new JPanel[3];
	int marg = 40;
	double dis = 0;
	JLabel time;
	
	public Home() {
		super("Ȩ", 1000, 430);
		
		back = new JLabel(img("./background.png", getWidth(), getHeight()));
		this.add(back);
		back.setLayout(new BorderLayout());
		
		back.add(n = new JPanel(new BorderLayout()), "North");
		back.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		n.setOpaque(false);
		c.setOpaque(false);
		
		n.add(new JLabel("<html><font size = \"6\"; color = \"blue\"; face = \"���� ���\">������ ����ö��, ���� ���� ����<br><font size = \"4\"; color = \"white\"; face = \"���� ���\">���ﱳ����簡 ���ñ����� �̷��� ����� ���ϴ�.", 0), "West");
		n.add(logo = new JLabel(img("./logo.png", 180, 30)), "East");
		logo.setOpaque(false);
		
		for (int i = 0; i < jp.length; i++) {
			c.add(jp[i] = new JPanel(new GridLayout(0, 1, 10, 10)));
			jp[i].setBackground(new Color(0, 0, 0, 128));
			setEmpty(jp[i], 10, 10, 10, 10);
		}
		
		this.setBtn();
		
		Timer re = new Timer(1, a->{
			tnow = LocalTime.now();
			time.setText(tformat(tnow, "HH:mm:ss"));
			repaint();
			revalidate();
		});
		re.start();
		
		setEmpty(back, marg, marg, marg, marg);
		setEmpty(n, 0, 0, 10, 0);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				re.stop();
			}
		});
		
		
	}
	
	void setBtn() {
		jp[0].add(btn("��� �˻�", a->{
			new Map().addWindowListener(new Before(h));
		}));
		
		JPanel grid = new JPanel(new GridLayout(1, 0, 10, 10));
		grid.add(btn("�뼱��", a->{
			new Route().addWindowListener(new Before(h));
		}));
		grid.add(btn("���� �ð�ǥ", a->{
			new TimeTable().addWindowListener(new Before(h));
		}));
		grid.setOpaque(false);
		jp[0].add(grid);
		jp[1].add(time = new JLabel(tformat(tnow, "HH:mm:ss"), 0));
		time.setFont(new Font("", Font.PLAIN, 50));
		time.setForeground(Color.white);
		time.setOpaque(false);
		
		jp[1].add(btn("����������", a->{
			if (uno.isEmpty()) {
				eMsg("�α��� �� �̿� �����մϴ�.");
				return;
			}
			new MyPage().addWindowListener(new Before(h));
		}));
		
		jp[2].add(btn("�α���", a->{
			h.setVisible(false);
			new Login();
			
		}));
		jp[2].add(btn("ȸ������", a->{
			new SignUp().addWindowListener(new Before(h));
		}));
	}
	
	void setLog(boolean chk) {
		jp[2].removeAll();
		if (chk) {
			dis = 0;
			try {
				var rs= stmt.rs("select * from purchase where user ="+uno);
				while (rs.next()) {
					dis += rs.getInt(8);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dis /= 10.0;
			jp[2].add(lbl("<html><font face =\"���� ���\"; color = \"white\"; size=\"5\"><b>�ȳ��ϼ���? " + uname+"��</b><br><font face = \"���ʷ� ����\"; color = \"white\"; size = \"4\">�����Ʈ�ο� �� "+dis+"km�� �Բ��߽��ϴ�.</html>", 0));
			jp[2].add(btn("�α׾ƿ�", a->{
				uno = "";
				uname = "";
				setLog(false);
			}));
		} else {
			jp[2].add(btn("�α���", a->{
				h.setVisible(false);
				new Login();
				
			}));
			jp[2].add(btn("ȸ������", a->{
				new SignUp().addWindowListener(new Before(h));
			}));
		}
		
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) {
		h.setVisible(true);
	}
	
}
