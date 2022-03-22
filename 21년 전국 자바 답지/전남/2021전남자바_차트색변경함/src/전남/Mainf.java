package ����;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Mainf extends Baseframe {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(2)), c = new JPanel();
	JButton btn[] = { btn("�α׾ƿ�", a -> {
		errmsg("�α׾ƿ��� �Ϸ�Ǿ����ϴ�.");
		dispose();
	}), btn("����������", a -> {
		new My().addWindowListener(new be(this));
	}) };
	JLabel imglbl[] = new JLabel[5];
	JLabel[] idxlbl = { new JLabel("�˻�", 0), new JLabel("�Խ���", 0), new JLabel("������ ���� ã��", 0),
			new JLabel("������ ������Ȳ ��Ʈ", 0), new JLabel("����", 0) };
	Timer slideMover;
	int speed = 10;
	ArrayList<JPanel> list = new ArrayList<JPanel>();

	public Mainf() {
		super("����", 600, 400);

		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		n.add(btn[0]);
		n.add(btn[1]);

		c.setLayout(null);
		emp((JPanel)getContentPane(), 5, 5, 5, 5);
		n.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

		for (int i = 0; i < 5; i++) {
			imglbl[i] = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
					super.paintComponent(g);
				}

				@Override
				protected void paintChildren(Graphics g) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					super.paintChildren(g);
				}
			};

			imglbl[i].setIcon(img("ǥ��/" + (i + 1) + ".jpg", 580, 320));
			imglbl[i].setBounds(i * 580, 0, 580, 320);
			c.add(imglbl[i]);
			imglbl[i].setLayout(new BorderLayout());
		}

		new Thread() {
			@Override
			public void run() {
				int x = 1;
				while (true) {
					try {
						for (int i = 0; i < 5; i++) {
							imglbl[i].add(idxlbl[i]);
							if (imglbl[i].getX() - x < -580) {
								imglbl[i].setLocation(580 * 4, 0);
								sleep(2000);
							} else
								imglbl[i].setLocation(imglbl[i].getX() - x, 0);
						}
						sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();

		for (int i = 0; i < 5; i++) {
			idxlbl[i].setForeground(Color.BLACK);
			idxlbl[i].setFont(new Font("HY������M", Font.BOLD, 25));
			idxlbl[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JLabel j = (JLabel) e.getSource();
					j.setForeground(Color.RED);
					if (e.getSource().equals(idxlbl[0])) {
						new Search().addWindowListener(new be(Mainf.this));
					}
					if (e.getSource().equals(idxlbl[1])) {
						new Notice().addWindowListener(new be(Mainf.this));
					}
					if (e.getSource().equals(idxlbl[2])) {
						new Find().addWindowListener(new be(Mainf.this));
					}
					if (e.getSource().equals(idxlbl[3])) {
						new chart().addWindowListener(new be(Mainf.this));
					}
					if (e.getSource().equals(idxlbl[4])) {
						try {
							ResultSet rs1 = stmt.executeQuery("select * from reservation where u_no = "+NO+" and r_date != curdate()");
							if(rs1.next()) {
								errmsg("������ ������ �����ϴ�.");
								return;
							}
							
							ResultSet rs = stmt.executeQuery("Select u_no, r_Date, r_time from reservation where u_no = "+NO+" and r_attend = 0");
							if(rs.next()) {
								new Quiz().addWindowListener(new be(Mainf.this));
							}else {
								errmsg("�̹� ���� �߽��ϴ�.");
								return;
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}

		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Mainf();
	}

}
