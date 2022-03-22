package View;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RouteMap extends BaseFrame {

	ArrayList<Integer> list = new ArrayList<>();
	JComboBox<String> box;
	JScrollPane pane;
	Route r;

	public RouteMap() {
		super("노선도", 1000, 700);
		add(box = new JComboBox<>(), "North");

		try {
			var rs = stmt.executeQuery("SELECT * FROM metro.metro");
			while (rs.next()) {
				box.addItem(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setData();

		add(pane = new JScrollPane(r = new Route()));

		box.addItemListener(i -> {
			setData();
			r.setXY();
		});

		setVisible(true);

	}

	void setData() {

		list = new ArrayList<>();
		try {
			var rs = stmt.executeQuery(
					"SELECT r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro= "
							+ (box.getSelectedIndex() + 1));
			while (rs.next()) {
				list.add(rs.getInt(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Route extends JPanel {
		int x[], y[];
		int px[], py[];

		public Route() {
			setXY();
		}

		void setXY() {
			x = new int[list.size()];
			y = new int[list.size()];
			px = new int[list.size() + list.size() / 6 + 1];
			py = new int[list.size() + list.size() / 6 + 1];

			int sX = 150, offX = 100, sY = 50, offY = 100, pidx = 0;
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
			revalidate();
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (list.size() % 6 == 0) {
				g.drawPolyline(px, py, px.length - 1);
			} else {
				g.drawPolyline(px, py, px.length);
			}
			for (int i = 0; i < x.length; i++) {
				g.drawOval(x[i], y[i], 5, 5);
			}

		}
	}

	public static void main(String[] args) {
		new RouteMap();
	}
}
