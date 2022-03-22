package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import base.BasePage;

public class Chart extends BasePage {

	int t[] = { 20, 30, 40, 50, 60 };
	int top[][] = { { 190, 0 }, { 300, 0 }, { 210, 0 }, { 240, 0 }, { 250, 0 } };
	JPanel c = new JPanel(new BorderLayout()), chart;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.white);
		super.paint(g);
	}

	public Chart() {
//		setOpaque(false);

//		setBorder(new LineBorder(Color.blue));

		c.setBackground(Color.black);
		c.setOpaque(false);

		add(c);

		drawChart();
	}


	public static void main(String[] args) {
		BasePage.mf.add(new Chart());
		mf.setVisible(true);
	}
	
	private void drawChart() {
		c.removeAll();
		chart = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.fillRect(0, 0, getWidth(), getHeight());
				int max = 0, w = 25, width = 0, h = 0, base = 200;
				for (int i = 0; i < t.length; i++) {
					try {
//						Äõ¸® ¹®Á¦ ÀÖÀ½
						var rs = stmt.executeQuery(
								"select count(*) from artist ar, album al, user u, song s, history h where h.song = s.serial and h.user = u.serial and s.album = al.serial and al.artist = ar.serial and ar.serial = 26 and "
										+ (t[i] - 10) + " <= 2021-year(u.birth)  and 2021-year(u.birth) < " + (t[i]));
						if (rs.next()) {
							if (max < rs.getInt(1)) {
								max = rs.getInt(1);
							}
//							top[i][0] = rs.getInt(1);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				max = 300;

				for (int i = 0; i < t.length; i++) {
					if (top[i][0] == max) {
						System.out.println("max: " + max);
						System.out.println("max index: " + i);
						top[i][1] = 1;
					}
				}

				for (int j = 0; j < t.length; j++) {
					h = (int) (top[j][0] / (double) max * 200);
					if (top[j][1] == 1) {
						System.out.println("sdf: " + j);
						g2.setColor(Color.red);
					} else {
						g2.setColor(Color.white);
					}
					g2.fillRect(50 + (30 * j), base - h, w, h);
					g2.setColor(Color.white);
					g2.setFont(new Font("¸¼Àº °íµñ", 0, 10));
					g2.drawString((t[j] - 10) + "´ë", 50 + (30 * j), base + 10);
				}
			}
		};
		c.add(chart);
	}

}
