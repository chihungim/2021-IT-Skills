package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Route extends BaseFrame {

	JComboBox<String> combo = new JComboBox<String>();
	RoutePanel routePanel;
	ArrayList<String> list;

	JScrollPane pane;

	public Route() {
		super("노선도", 850, 650);

		dataInit();
		try {
			var rs = stmt.executeQuery("select name from metro");
			while (rs.next()) {
				combo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.add(combo, "North");
		this.add(c = new JPanel(new BorderLayout()));
		setData();
		c.add(pane = new JScrollPane(routePanel = new RoutePanel()));
		setEmpty(c, 10, 0, 0, 0);
		pane.setHorizontalScrollBar(null);
		combo.addItemListener(a -> {
			setData();
			routePanel.removeAll();
			routePanel.setXY();
			routePanel.drawLabel();
		});
		setEmpty((JPanel) getContentPane(), 20, 20, 20, 20);

		this.setVisible(true);
	}

	void setData() {

		list = new ArrayList<String>();
		System.out.println(combo.getSelectedIndex());
		try {
			var rs = stmt.executeQuery(
					"SELECT r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro= "
							+ (combo.getSelectedIndex() + 1));
			while (rs.next()) {
				list.add(rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.size());
	}

	class RoutePanel extends JPanel {

		int x[], y[];
		int px[], py[];
		HashSet<Integer> hash = new HashSet<Integer>();

		public RoutePanel() {
			this.setLayout(null);

			setXY();
			drawLabel();
		}

		void setXY() {
			x = new int[list.size()];
			y = new int[list.size()];
			px = new int[list.size() + list.size() / 6 + 1];
			py = new int[list.size() + list.size() / 6 + 1];

			int sX = 130, offX = 100, sY = 70, offY = 100, pidx = 0;
			x[0] = 0 + sX;
			y[0] = 0 + sY;
			px[pidx] = x[0] - offX / 2;
			py[pidx] = y[0];
			pidx++;
			px[pidx] = x[0] + offX / 2;
			py[pidx] = y[0];

			for (int i = 1; i < list.size(); i++) {
				++pidx;

				// 세로 선 폴리 좌표 추가
				if (i % 6 == 0) {
					px[pidx] = px[pidx - 1];
					py[pidx] = py[pidx - 1] + offY;

					pidx++;
				}

				y[i] = (i / 6) * offY + sY;
				py[pidx] = y[i];

				if ((i / 6) % 2 == 0) {
					x[i] = (i % 6) * offX + sX;
					px[pidx] = x[i] + offX / 2;
				} else {
					x[i] = 5 * offX - (i % 6) * 100 + sX;
					px[pidx] = x[i] - offX / 2;
				}
			}
			setPreferredSize(new Dimension(getWidth(), y[y.length - 1] + 100));
			repaint();
			revalidate();
		}

		void drawLabel() {
			System.out.println(x.length);
			for (int i = 0; i < x.length; i++) {
				JLabel lbl = lbl(list.get(i) + "", 0, 13, Color.black, Font.PLAIN);
				lbl.setName(i + "");
				lbl.setBounds(x[i] - 45, y[i] + 5, 100, 25);
				this.add(lbl);

				lbl.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						JLabel l = ((JLabel) e.getSource());
						int serial = rei(getOne("select serial from station where name = '" + l.getText() + "'"));
						try {
							hash.clear();
							var rs = stmt.executeQuery("SELECT * FROM metro.path where departure=" + serial
									+ " or arrive=" + serial + " group by cost");
							while (rs.next()) {
								hash.add(rs.getInt(2));
								hash.add(rs.getInt(3));
							}
							hash.remove(serial);
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							var rs = stmt.executeQuery(
									"SELECT r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.station ='"
											+ serial + "'");
							String msg = l.getText() + "에서 갈 수 있는 역\n";
							for (var h : hash) {
								msg += station[h] + ",";
							}
							msg = msg.substring(0, msg.length() - 1);
							msg += "\n" + l.getText() + "를 지나가는 노선\n";
							while (rs.next()) {
								msg += rs.getString(4) + "\n";
							}
							iMsg(msg);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				});
			}
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(3));

			g2.setColor(Color.black);
			g2.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			g2.drawString(combo.getSelectedItem() + "", 80, 40);

			g2.setColor(new Color(50, 100, 255));
			if (list.size() % 6 == 0) {
				g2.drawPolyline(px, py, px.length - 1);
			} else {
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
