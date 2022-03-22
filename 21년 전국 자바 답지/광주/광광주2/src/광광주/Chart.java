package ������;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Chart extends BaseFrame {

	JLabel chart;

	public Chart() {
		super("���", 450, 400);
		add(lbl("���", JLabel.LEFT, 25), "North");
		drawChart();
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	void drawChart() {
		chart = new JLabel() {

			int size, vgap = 20, height = 30, y = 20;
			double max = 0;
			String name;

			@Override
			public void paint(Graphics g) {
//				vgap = 30;
				y = 0;
				Graphics2D g2 = (Graphics2D) g;

				try {
					var rs = stmt.executeQuery(
							"select m.name, sum(rd.COUNT) from receipt r, receipt_detail rd, menu m where r.NO = rd.RECEIPT and m.NO = rd.MENU and r.SELLER = "
									+ sno + " group by m.name order by sum(rd.count) desc limit 1, 5");
					if (rs.next()) {
						max = rs.getInt(2);
					}

					rs.previous();

					while (rs.next()) {
						size = (int) ((rs.getInt(2) / max) * 100) * 4;
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						name = rs.getString(1) + "(" + rs.getInt(2) + "�� �Ǹŵ�)";
						g2.setFont(new Font("��������", Font.BOLD, 15));
						FontMetrics fm = g2.getFontMetrics();
						int fh = fm.getHeight();
						System.out.println(fh);
						g2.setColor(Color.BLACK);
						y += height + vgap;
						g2.drawString(name, 10, y - 5);
						g2.setColor(Color.BLUE);
						GradientPaint gp = new GradientPaint(10, y + fh, Color.WHITE, size, y, Color.BLUE.brighter());
						g2.setPaint(gp);
						g2.fillRect(10, y, size, height);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.paint(g2);
			}
		};

		add(chart);

	}

	public static void main(String[] args) {
		sno = 1;
		new Chart();
	}
}
