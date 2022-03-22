package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.stmt;

public class Route extends BaseFrame {
	JComboBox<String> combo = new JComboBox<String>();
	RoutePanel rp;
	ArrayList<String> list;
	
	public Route() {
		super("노선도", 850, 650);
		dataInit();
		
		try {
			ResultSet rs = stmt.rs("select name from metro");
			while(rs.next()) {
				combo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		add(combo, "North");
		add(c = new JPanel(new BorderLayout()));
		setData();
		c.add(new JScrollPane(rp = new RoutePanel()));
		setEmpty(combo, 10, 0, 0, 0);
		
		combo.addItemListener(a->{
			setData();
			rp.removeAll();
			rp.setXY();
			rp.drawL();
		});
		
		setEmpty((JPanel)getContentPane(), 20, 20, 20, 20);
		
		setVisible(true);
	}
	
	void setData() {
		list = new ArrayList<String>();
		try {
			ResultSet rs = stmt.rs("select r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro = "+(combo.getSelectedIndex() + 1));
			while(rs.next()) {
				list.add(rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	class RoutePanel extends JPanel {
		int x[], y[];
		int px[], py[];
		HashSet<Integer> hash = new HashSet<Integer>();
		
		public RoutePanel() {
			setLayout(null);
			
			setXY();
			drawL();
		}

		private void drawL() {
			for (int i = 0; i < x.length; i++) {
				JLabel jl = lbl(list.get(i)+ "", 0, 13, Color.BLACK);
				jl.setName(i + "");
				jl.setBounds(x[i] - 45, y[i] + 5, 100, 25);
				add(jl);
				
				jl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JLabel j = ((JLabel)e.getSource());
						int serial = rei(stmt.getOne("Select serial from station where name = '"+j.getText()+"'"));
						try {
							hash.clear();
							System.out.println(serial);
							ResultSet rs = stmt.rs("select * from path where departure = "+serial+" or arrive = "+serial+" group by cost");
							while(rs.next()) {
								hash.add(rs.getInt(2));
								hash.add(rs.getInt(3));
							}
							hash.remove(serial);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						try {
							ResultSet rs = stmt.rs("select r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.station = '"+serial+"'");
							String msg = j.getText()+"에서 갈 수 있는 역\n";
							for(var h : hash) {
								msg += stations.get(h)+",";
							}
							msg = msg.substring(0, msg.length() - 1);
							msg += "\n" + j.getText() +"를 지나가는 노선\n";
							while(rs.next()) {
								msg += rs.getString(4)+"\n";
							}
							iMsg(msg);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		}

		private void setXY() {
			x = new int[list.size()];
			y = new int[list.size()];
			px = new int[list.size() +(list.size() / 6) + 1];
			py = new int[list.size() +(list.size() / 6) + 1];
			
			int sx =130, offx = 100, sy =70, offy = 100, pidx = 0;
			x[0] = 0 + sx;
			y[0] = 0 + sy;
			px[pidx] = x[0] - offx / 2;
			py[pidx] = y[0];
			pidx++;
			px[pidx] = x[0] + offx / 2;
			py[pidx] = y[0];
			
			for (int i = 1; i < list.size(); i++) {
				pidx++;
				
				if(i%6 == 0) {
					px[pidx] = px[pidx - 1];
					py[pidx] = py[pidx - 1] + offy;
					pidx++;
				}
				
				y[i] = (i / 6) * offy + sy;
				py[pidx] = y[i];
				
				if((i/6) % 2 == 0) {
					x[i] = (i % 6) * offx + sx;
					px[pidx] = x[i] + offx / 2;
				}else {
					x[i] = 5 * offx - (i % 6) * 100 + sx;
					px[pidx] = x[i] - offx / 2;
				}
			}
			BaseFrame.sz(this, getWidth(), y[y.length - 1] + 100);
			repaint();
			revalidate();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			g2.drawString(combo.getSelectedItem()+"", 80, 40);
			
			g2.setColor(Color.blue);
			if(list.size() % 6 == 0) {
				g2.drawPolyline(px, py, px.length - 1);
			}else {
				g2.drawPolyline(px, py, px.length);
			}
			g2.setColor(new Color(255, 50, 100));
			for (int i = 0; i < x.length; i++) {
				g2.fillOval(x[i], y[i] - 5, 10, 10);
			}
			
			
		}
	}

	public static void main(String[] args) {
		new Route();
	}

}
