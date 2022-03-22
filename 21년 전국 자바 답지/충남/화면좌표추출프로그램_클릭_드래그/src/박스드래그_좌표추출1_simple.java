import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class �ڽ��巡��_��ǥ����1_simple extends JFrame {
	static ArrayList<String> list = new ArrayList<String>();

	JPanel jp;
	int sx=0, sy=0, ex=0, ey=0;
	public Vector<Point> sv = new Vector<Point>();
	public Vector<Point> ev = new Vector<Point>();
	
	public �ڽ��巡��_��ǥ����1_simple() {
		this.setSize(600, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		jp = new MyPanel();
		jp.setLayout(null);
		this.add(jp);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new �ڽ��巡��_��ǥ����1_simple();
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
					
					//���� �ڽ� ��ǥ ���, ��ü �ڽ� ��ǥ ���
					list.add("{" + sx + "," + sy + "," + (ex-sx) + "," + (ey-sy) + "}");
					System.out.println("��ǥ���:" + String.join(",", list));
					
					sv.add(new Point(sx, sy));
					ev.add(new Point(ex, ey));
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
			//��ϵ� �ڽ��� ��� ǥ��
			for(int i=0; i<sv.size(); i++) {
				cs = sv.get(i);
				ce = ev.get(i);
				g.drawRect(cs.x, cs.y, ce.x-cs.x, ce.y-cs.y);
			}
		}
	}
}
