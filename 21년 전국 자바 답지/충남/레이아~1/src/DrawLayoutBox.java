import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DrawLayoutBox extends JFrame {
	JPanel jp;

	int sx=0, sy=0, ex=0, ey=0, tmpex=0, tmpey=0;
	public Vector<Point> sv = new Vector<Point>();
	public Vector<Point> ev = new Vector<Point>();
	
	public DrawLayoutBox() {
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		jp = new MyPanel();
		this.add(jp);

		jp.setLayout(null);
		jp.setBackground(Color.WHITE);

		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new DrawLayoutBox();
	}
	
	public class MyPanel extends JPanel {
		public MyPanel() {
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					sx = e.getX();
					sy = e.getY();
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
					ex = e.getX();
					ey = e.getY();
					
					System.out.println(sx + "," + sy + "," + (ex-sx) + "," + (ey-sy));
					
					sv.add(new Point(sx, sy));
					ev.add(new Point(ex, ey));
					repaint();
				}
			});
			
			this.addMouseMotionListener(new MouseMotionAdapter() {
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

			Graphics2D g2 = (Graphics2D)g;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLACK);
			
			Point cs, ce;
			for(int i=0; i<sv.size(); i++) {
				cs = sv.get(i);
				ce = ev.get(i);
				g.drawRect(cs.x, cs.y, ce.x-cs.x, ce.y-cs.y);
			}
			
			g.drawRect(sx, sy, tmpex-sx, tmpey-sy);
		}
	}
}
