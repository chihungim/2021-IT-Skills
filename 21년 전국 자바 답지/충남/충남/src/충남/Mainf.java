package �泲;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Mainf extends BaseFrame {
	JLabel back, logo;
	JPanel jp[] = new JPanel[3];
	int mar = 40;
	double dis = 0;
	JLabel time;
	
	public Mainf() {
		super("Ȩ", 1000, 430);
		
		back = new JLabel(img("background.png",getWidth(),getHeight()));
		add(back);
		
		back.setLayout(new BorderLayout());
		
		back.add(n = new JPanel(new BorderLayout()),"North");
		back.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		
		n.add(new JLabel("<html><font size = \"6\"; color = \"blue\"; face = \"���� ���\";>������ ����ö��, ���� ���� ����<br><font size = \"4\"; color = \"white\"; face = \"���� ���\">���ﱳ����簡 ���ñ����� �̷��� ����� ���ϴ�.",0),"West");
		n.add(logo = new JLabel(img("logo.png", 180, 30)),"East");
		logo.setOpaque(false);
		
		for (int i = 0; i < jp.length; i++) {
			c.add(jp[i] = new JPanel(new GridLayout(0, 1, 10, 10)));
			jp[i].setBackground(new Color(0, 0, 0, 128));
			emp(jp[i], 10, 10, 10, 10);
		}
		
		set();
		
		Timer t = new Timer(1, a->{
			now = LocalTime.now();
			time.setText(tformat(now, "HH:mm:ss"));
			repaint();
			revalidate();
		});
		t.start();
		
		emp(back, mar, mar, mar, mar);
		emp(n, 0, 0, 10, 0);
		
		n.setOpaque(false);
		c.setOpaque(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				t.stop();
			}
		});
		
		setVisible(true);
	}

	private void set() {
		jp[0].add(btn("��� Ž��", a->{
			new Map().addWindowListener(new be(this));
		}));
		JPanel grid = new JPanel(new GridLayout(1, 0, 10, 10));
		grid.add(btn("�뼱��", a->{
			new Route().addWindowListener(new be(this));
		}));
		grid.add(btn("���� �ð�ǥ", a->{
			new TimeTable().addWindowListener(new be(this));
		}));
		grid.setOpaque(false);
		jp[0].add(grid);
		jp[1].add(time = new JLabel(tformat(now, "HH:mm:ss"),0));
		time.setFont(new Font("", Font.PLAIN, 50));
		time.setForeground(Color.WHITE);
		time.setOpaque(false);
		
		jp[1].add(btn("����������", a->{
			if(NO == -1) {
				errmsg("�α��� �� �̿� �����մϴ�.");
				return;
			}
			new Mypage().addWindowListener(new be(this));
		}));
		jp[2].add(btn("�α���", a->{
			new Login(Mainf.this).addWindowListener(new be(this));
		}));
		jp[2].add(btn("ȸ������", a->{
			new Sign().addWindowListener(new be(this));
		}));
	}
	
	void setLog(boolean chk) {
		jp[2].removeAll();
		if(chk) {
			dis = 0;
			try {
				ResultSet rs = stmt.executeQuery("select * from purchase where user = "+NO);
				while(rs.next()) {
					dis += rs.getInt(8);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dis /= 10.0;
			jp[2].add(label("<html><font face =\"���� ���\"; color = \"white\"; size=\"5\"><b>�ȳ��ϼ���? " + NAME+"��</b><br><font face = \"���ʷ� ����\"; color = \"white\"; size = \"4\">�����Ʈ�ο� �� "+dis+"km�� �Բ��߽��ϴ�.</html>"	, 0));
			jp[2].add(btn("�α׾ƿ�", a->{
				NO = -1;
				NAME = "";
				setLog(false);
			}));
		}else {
			jp[2].add(btn("�α���", a->{
				new Login(Mainf.this).addWindowListener(new be(this));
			}));
			jp[2].add(btn("ȸ������", a->{
				new Sign().addWindowListener(new be(this));
			}));
		}
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new Mainf();
	}

}
