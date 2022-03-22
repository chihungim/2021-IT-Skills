package view;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends BaseFrame {

	static String cap[] = "로그인,회원가입,위치정보,예매,매직패스(0),Mypage,놀이기구 인기순위 Top5,놀이기구 등록/수정,월별 분석,종료".split(",");
	static JLabel lbl[] = new JLabel[cap.length];

	int lotty=1, lorry=1;
	
	public Main() {
		super("메인", 1000, 450);

		this.add(w = new JPanel(new GridLayout(0, 1)), "West");
		this.add(c = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				setOpaque(false);
				
				Image imgLotty = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./datafiles/캐릭터/로티"+lotty+".jpg")).getImage();
				Image imgLorry  = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./datafiles/캐릭터/로리"+lorry+".jpg")).getImage();
				
				g.drawImage(imgLotty, 100, 30, 250, 280, null);
				g.drawImage(imgLorry, 500, 30, 250, 280, null);
				
			}
		});

		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(lbl[i] = new JLabel(cap[i], 0));
			lbl[i].setEnabled(false);
			w.add(tmp);
			tmp.setOpaque(false);

			lbl[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getSource().equals(lbl[0])) {
						if (lbl[0].getText().contentEquals("로그인")) {
							new Login().addWindowListener(new Before(Main.this));
						} else {
							uno = "";
							uname= "";
							uheight = 0;
							uold = 0;
							udisable=0;
							lbl[0].setText("로그아웃");
							loginEnabled(0);
						}
					} else if (e.getSource().equals(lbl[1])) {
						new SignUp().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[2])) {
						new Locate().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[3])) {
						new Reserve().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[4])) {
						new MagicPass().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[5])) {
						new MyPage().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[6])) {
						new Chart().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[7])) {
						new Ride().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[8])) {
						new Cal().addWindowListener(new Before(Main.this));
					} else if (e.getSource().equals(lbl[9])) {
						Main.this.dispose();
					}
				}
			});
		}

		loginEnabled(0);

		w.setOpaque(false);
		c.setOpaque(false);

		new Thread(()->{
			while (true) {
				if (lorry==3) {
					lorry=1;
					lotty=1;
				} else {
					lorry++;
					lotty++;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				repaint();
				revalidate();
			}
		}).start();
		
		setEmpty((JPanel) getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}

	static void loginEnabled(int no) {
		// 0 = �α׾ƿ�, 1= ����, 2= ����;
		for (int i = 0; i < lbl.length; i++) {
			lbl[i].setEnabled(false);
		}
		int nums[];
		
		
		if (no==0) {
			nums = new int[] { 0,1,6 };
		} else if (no==1) {
			nums = new int[] { 0,2,3,5,6 };
		} else {
			nums = new int[] { 0,6,7,8 };
		}
		
		for (int i = 0; i < nums.length; i++) {
			lbl[nums[i]].setEnabled(true);
		}
		lbl[9].setEnabled(true);
	}

	public static void main(String[] args) {
		new Main();
	}

}
