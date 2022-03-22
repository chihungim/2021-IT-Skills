package View;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class 화면설계 extends JFrame {
	static ArrayList<String> list = new ArrayList<String>();

	JPanel jp;
	int sx=0, sy=0, ex=0, ey=0, tmpex=0, tmpey=0;
	public Vector<Point> sv = new Vector<Point>();
	public Vector<Point> ev = new Vector<Point>();
	
	public 화면설계() {
		this.setSize(1016,143+39+400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		jp = new MyPanel();
		jp.setLayout(null);
		this.add(jp);
		this.setVisible(true);
		
		this.addKeyListener(new  KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar()=='Z' || e.getKeyChar()=='z') {
					sv.remove(sv.size()-1);
					ev.remove(ev.size()-1);
					
					//마지막 박스 좌표 삭제, 전체 박스 좌표 출력
					list.remove(list.size()-1);
					System.out.println("좌표목록:" + String.join(",", list));
					
					sx=sy=tmpex=tmpey=0;
					jp.repaint();
					jp.revalidate();
				}
			}
		});
	}
	
	public static void main(String[] args) {
		new 화면설계();
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
					
					//현재 박스 좌표 기록, 전체 박스 좌표 출력
					list.add("{" + sx + "," + sy + "," + (ex-sx) + "," + (ey-sy) + "}");
					System.out.println("좌표목록:" + String.join(",", list));
					
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
			//기록된 박스들 모두 표시
			for(int i=0; i<sv.size(); i++) {
				cs = sv.get(i);
				ce = ev.get(i);
				g.drawRect(cs.x, cs.y, ce.x-cs.x, ce.y-cs.y);
			}
			
			//드래그 자취 표시
			if(sx>0 || sy>0 || tmpex>0 || tmpey>0) 
				g.drawRect(sx, sy, tmpex-sx, tmpey-sy);
		}
	}
}
