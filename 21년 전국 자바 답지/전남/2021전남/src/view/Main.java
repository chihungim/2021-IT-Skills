package view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import db.DBManager;
import util.Util;

public class Main extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(2)), c = new JPanel();
	JButton btn[] = { Util.btn("로그아웃", a -> unload()), Util.btn("마이페이지", a -> ToMypage()) };
	JLabel imglbl[] = new JLabel[5];
	JLabel[] idxlbl = { new JLabel("검색", 0), new JLabel("게시판", 0), new JLabel("지역별 지점 찾기", 0),
			new JLabel("지역별 예약현황 차트", 0), new JLabel("퀴즈", 0) };
	Timer slideMover;
	int speed = 10;
	ArrayList<JPanel> list = new ArrayList<JPanel>();

	public Main() {
		super("메인", 600, 400);

		ui();
		data();
		event();

		setVisible(true);
	}

	void event() {
		idxlbl[0].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				new Search().addWindowListener(new Before(Main.this));
				idxlbl[0].setForeground(Color.red);
			}
		});
		idxlbl[1].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				idxlbl[1].setForeground(Color.red);
				new Notice().addWindowListener(new Before(Main.this));
			}
		});
		idxlbl[2].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				idxlbl[2].setForeground(Color.red);
				new FindPlace().addWindowListener(new Before(Main.this));
			}
		});
		idxlbl[3].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				idxlbl[3].setForeground(Color.red);
				new Chart().addWindowListener(new Before(Main.this));
			}
		});
		idxlbl[4].addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				idxlbl[4].setForeground(Color.red);
				try {
					ResultSet rs = DBManager.rs("SELECT u_no, r_date, r_time FROM reservation where u_no = " + uno + " and r_attend = 0");
					if (rs.next()) {
						new Quiz().addWindowListener(new Before(Main.this));
					} else {
						Util.eMsg("이미 참여 했습니다.");
						return;
					}

					ResultSet rs2 = DBManager.rs("SELECT * from reservation where u_no = " + uno + "and r_date != curdate()");
					if (rs2.next()) {
						Util.eMsg("예약한 정보가 없습니다.");
						return;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	void data() {
		for (int i = 0; i < idxlbl.length; i++) {
			idxlbl[i].setForeground(Color.black.darker());
			idxlbl[i].setFont(new Font("HY헤드라인M", Font.BOLD, 25));
		}
	}

	void ToMypage() {
		new MyPage().addWindowListener(new Before(Main.this));
	}

	void unload() {
		Util.iMsg("로그아웃이 완료되었습니다.");
		dispose();
	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		n.add(btn[0]);
		n.add(btn[1]);

		c.setLayout(null);
		c.setBorder(new EmptyBorder(5, 5, 5, 5));

		for (int i = 0; i < 5; i++) {
			imglbl[i] = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D)g;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					super.paintComponent(g);
				}
				
				@Override
				protected void paintChildren(Graphics g) {
					Graphics2D g2 = (Graphics2D)g;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					super.paintChildren(g);
				}
			};
			
			imglbl[i].setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("Datafiles/표지/" + (i + 1) + ".jpg")
					.getScaledInstance(580, 320, Image.SCALE_SMOOTH)));
			imglbl[i].setBounds(i * 580, 0, 580, 320);
			c.add(imglbl[i]);
			imglbl[i].setLayout(new BorderLayout());
		}

		new Thread() {
			public void run() {
				int shiftX = 1;
				try {
					while (true) {
						for (int i = 0; i < 5; i++) {
							imglbl[i].add(idxlbl[i]);
							if (imglbl[i].getX() - shiftX < -580) {
								imglbl[i].setLocation(580 * 4, 0);
								sleep(2000);
							} else
								imglbl[i].setLocation(imglbl[i].getX() - shiftX, 0);
						}
						sleep(3);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	public static void main(String[] args) {
		uno = 1;
		new Main();
	}

}
