package 충남;

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
		super("홈", 1000, 430);
		
		back = new JLabel(img("background.png",getWidth(),getHeight()));
		add(back);
		
		back.setLayout(new BorderLayout());
		
		back.add(n = new JPanel(new BorderLayout()),"North");
		back.add(c = new JPanel(new GridLayout(1, 0, 10, 10)));
		
		n.add(new JLabel("<html><font size = \"6\"; color = \"blue\"; face = \"맑은 고딕\";>안전한 도시철도, 편리한 교통 서비스<br><font size = \"4\"; color = \"white\"; face = \"맑은 고딕\">서울교통공사가 도시교통의 미래를 만들어 갑니다.",0),"West");
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
		jp[0].add(btn("경로 탐색", a->{
			new Map().addWindowListener(new be(this));
		}));
		JPanel grid = new JPanel(new GridLayout(1, 0, 10, 10));
		grid.add(btn("노선도", a->{
			new Route().addWindowListener(new be(this));
		}));
		grid.add(btn("열차 시간표", a->{
			new TimeTable().addWindowListener(new be(this));
		}));
		grid.setOpaque(false);
		jp[0].add(grid);
		jp[1].add(time = new JLabel(tformat(now, "HH:mm:ss"),0));
		time.setFont(new Font("", Font.PLAIN, 50));
		time.setForeground(Color.WHITE);
		time.setOpaque(false);
		
		jp[1].add(btn("마이페이지", a->{
			if(NO == -1) {
				errmsg("로그인 후 이용 가능합니다.");
				return;
			}
			new Mypage().addWindowListener(new be(this));
		}));
		jp[2].add(btn("로그인", a->{
			new Login(Mainf.this).addWindowListener(new be(this));
		}));
		jp[2].add(btn("회원가입", a->{
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
			jp[2].add(label("<html><font face =\"맑은 고딕\"; color = \"white\"; size=\"5\"><b>안녕하세요? " + NAME+"님</b><br><font face = \"함초롬 돋움\"; color = \"white\"; size = \"4\">서울메트로와 총 "+dis+"km를 함께했습니다.</html>"	, 0));
			jp[2].add(btn("로그아웃", a->{
				NO = -1;
				NAME = "";
				setLog(false);
			}));
		}else {
			jp[2].add(btn("로그인", a->{
				new Login(Mainf.this).addWindowListener(new be(this));
			}));
			jp[2].add(btn("회원가입", a->{
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
