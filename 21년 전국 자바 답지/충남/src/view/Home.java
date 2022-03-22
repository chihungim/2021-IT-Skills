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

import db.DBManager;

public class Home extends BaseFrame {
	
	JLabel back, logo;
	JPanel jp[] = new JPanel[3];
	int mar = 40;
	double dis = 0;
	JLabel time;
	
	public Home() {
		super("Ȩ", 1000, 430);
		
		try {
			dataInit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		back = new JLabel(img("background.png", this.getWidth(), this.getHeight()));
		this.add(back);
		back.setLayout(new BorderLayout());
		
		back.add(n = new JPanel(new BorderLayout()), "North");
		back.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		
		n.add(new JLabel("<html><font size = '6'; color ='blue'; face = '���� ���'>������ ����ö��, ���� ���� ����<br><font size='4'; color = 'white'>���ﱳ����簡 ���ñ����� �̷��� ����� ���ϴ�.", 0), "West");
		n.add(logo = new JLabel(img("logo.png", 180, 30)), "East");
		logo.setOpaque(false);
		
		for (int i = 0; i < jp.length; i++) {
			c.add(jp[i] = new JPanel(new GridLayout(0, 1, 10, 10)));
			jp[i].setBackground(new Color(0,0,0,128));
			setEmpty(jp[i], 10, 10, 10, 10);
		}
		
		setUI();
		
		Timer re = new Timer(1, a->{
			now = LocalTime.now();
			time.setText(tFormat(now, "HH:mm:ss"));
			repaint();
			revalidate();
		});
		re.start();
		
		setEmpty(back, mar, mar, mar, mar);
		setEmpty(n, 0, 0, 10, 0);
		
		n.setOpaque(false);
		c.setOpaque(false);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				re.restart();
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				re.stop();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	void setUI() {
		jp[0].add(btn("��� Ž��", a->{
			new Map().addWindowListener(new Before(home));
		}));
		
		var grid = new JPanel(new GridLayout(1, 0, 10, 10));
		grid.add(btn("�뼱��", a->{
			new Route().addWindowListener(new Before(this));
		}));
		grid.add(btn("���� �ð�ǥ", a->{
			new TimeTable().addWindowListener(new Before(home));
		}));
		grid.setOpaque(false);
		jp[0].add(grid);
		
		jp[1].add(time = new JLabel(tFormat(now, "HH:mm:ss"), 0));
		time.setFont(new Font("", Font.PLAIN, 50));
		time.setForeground(Color.white);
		time.setOpaque(false);
		
		jp[1].add(btn("����������", a->{
			if (uno.isEmpty()) {
				eMsg("�α��� �� �̿� �����մϴ�.");
				return;
			}
			new MyPage().addWindowListener(new Before(home));
		}));
		
		jp[2].add(btn("�α���", a->{
			new Login().addWindowListener(new Before(home));
		}));
		jp[2].add(btn("ȸ������", a->{
			new SignUp().addWindowListener(new Before(home));
		}));
		
		
	}
	
	void setLog(boolean u) {
		jp[2].removeAll();
		if (u) {
			dis = 0;
			try {
				var rs = DBManager.rs("select * from purchase where user ="+uno);
				while (rs.next()) {
					dis += rs.getInt(8);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dis /= 10.0;
			jp[2].add(new JLabel("<html><font face = '���� ���'; color = 'white'; size ='5'><b>�ȳ��ϼ���? "+uname+"��</b><br><font face = '���ʷ� ����'; size = '4'>�����Ʈ�ο� �� "+dis+"km�� �Բ��߽��ϴ�."));
			jp[2].add(btn("�α׾ƿ�", a->{
				uno = "";
				uname ="";
				setLog(false); 
			}));
		} else {
			jp[2].add(btn("�α���", a->{
				new Login().addWindowListener(new Before(home));
			}));
			jp[2].add(btn("ȸ������", a->{
				new SignUp().addWindowListener(new Before(home));
			}));
		}
		
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) {
		home.setVisible(true);
	}
	
}
