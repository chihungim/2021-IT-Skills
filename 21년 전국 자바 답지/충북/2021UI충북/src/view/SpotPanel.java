package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

public class SpotPanel extends JPanel {

	static ArrayList<String> posList = new ArrayList<>();
	int sx = 0, sy = 0, ex = 0, ey = 0, tmpex = 0, tmpey = 0;

	public Vector<Point> sv = new Vector<Point>();
	public Vector<Point> ev = new Vector<Point>();

	public SpotPanel() {
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'Z' || e.getKeyChar() == 'z') {
					sv.remove(sv.size() - 1);
					ev.remove(ev.size() - 1);

					// ¸¶Áö¸· ¹Ú½º ÁÂÇ¥ »èÁ¦, ÀüÃ¼ ¹Ú½º ÁÂÇ¥ Ãâ·Â
					posList.remove(posList.size() - 1);
					System.out.println("ÁÂÇ¥¸ñ·Ï:" + String.join(",", posList));

					sx = sy = tmpex = tmpey = 0;
					repaint();
					revalidate();
				}
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				sx = e.getX();
				sy = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				ex = e.getX();
				ey = e.getY();
				posList.add("{" + sx + "," + sy + "," + (ex-sx) + "," + (ey-sy) + "}");
				System.out.println("ÁÂÇ¥¸ñ·Ï:" + String.join(",", posList));
				sv.add(new Point(sx, sy));
				ev.add(new Point(ex, ey));
				repaint();
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				tmpex = e.getX();
				tmpey = e.getY();
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.BLACK);

		Point cs, ce;

		for (int i = 0; i < sv.size(); i++) {
			cs = sv.get(i);
			ce = ev.get(i);
			g.drawRect(cs.x, cs.y, ce.x - cs.x, ce.y - cs.y);
		}

		if (sx > 0 || sy > 0 || tmpex > 0 || tmpey > 0)
			g.drawRect(sx, sy, tmpex - sx, tmpey - sy);
	}

}