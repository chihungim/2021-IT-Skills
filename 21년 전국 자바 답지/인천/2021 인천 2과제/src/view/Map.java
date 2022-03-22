
package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class Map extends JDialog {

	HashMap<Integer, Point> location_map = new HashMap<>();
	HashMap<Integer, Object[]> user_map = new HashMap<>();
	HashMap<Integer, Object[]> delivery_map = new HashMap<>();

	final int INF = Short.MAX_VALUE; // INF 값은 minDist 값과 편차가 있어야함 -> 420정도
	ArrayList<Integer> beeLine = new ArrayList<>();
	ArrayList<LocalDateTime> times[];
	ArrayList<LocalDateTime> time;
	ArrayList<JRadioButton> addr = new ArrayList<>();
	ArrayList<ArrayList<Integer>> beeLineList;
	int adjDim[][], pathDim[][];
	static String kind;
	int bwSize = 70, bhSize = 50, hOff = -35, vOff = -15;

	JPanel center;

	public Map(String kind) {
		super(BasePage.mf, "배송 현황", true);

		Map.kind = kind;

		data();
		ui();

		dijkstra(BasePage.s_addr);

		if (kind.contentEquals("seller")) {
			beeLineList = new ArrayList<>();
			for (var v : delivery_map.keySet()) {
				beeLineList.add(getBeepath(v));
			}
		} else {
			beeLine = getBeepath((int) user_map.get(BasePage.uno)[1]);
			time = new ArrayList<>();
			LocalDateTime sTime = BasePage.startTime;
			for (int i = 0; i < beeLine.size(); i++) {
				time.add(sTime.plusSeconds(pathDim[1][beeLine.get(i)] / 10));
			}

			showState();
		}
	}

	void ui() {
		setSize(1200, 750);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
		getContentPane().setBackground(Color.white);

		add(BasePage.lbl("[" + BasePage.sname + "] 물품 배달 현황", 0, 25), "North");
		center = new JPanel();
		add(center);

		var c_line = new JLabel() {
			public void paint(Graphics g) {
				super.paint(g);
				var g2 = (Graphics2D) g;

				g2.setStroke(new BasicStroke(2.5f));
				g2.setColor(Color.LIGHT_GRAY);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				for (var r : location_map.keySet()) {
					for (int c = 1; c <= location_map.size(); c++) {
						if (adjDim[r][c] > 0 && adjDim[r][c] < INF) {
							int x1 = location_map.get(r).x;
							int x2 = location_map.get(c).x;
							int y1 = location_map.get(r).y;
							int y2 = location_map.get(c).y;
							g2.drawLine(x1, y1, x2, y2);
						}
					}
				}

				if (beeLineList != null) {

					beeLineList.forEach(a -> {
						a.forEach(e -> {
							addr.get(e - 1).setSelected(true);
							addr.get(e - 1).setEnabled(true);
						});
					});

					beeLineList.forEach(a -> {
						final int xx[] = new int[a.size()], yy[] = new int[a.size()];
						for (int i = 0; i < a.size(); i++) {
							xx[i] = location_map.get(a.get(i)).x;
							yy[i] = location_map.get(a.get(i)).y;

							g2.setColor(Color.BLACK);
							if (i == 0)
								g2.drawString("출발", xx[i] - 10, yy[i] - 15);
							else if (i == a.size() - 1)
								g2.drawString("도착", xx[i] - 10, yy[i] - 15);

							g2.drawPolyline(xx, yy, xx.length);
						}
					});
				}

				if (beeLine != null) {

					for (int i = 0; i < beeLine.size(); i++) {
						addr.get(beeLine.get(i) - 1).setSelected(true);
						addr.get(beeLine.get(i) - 1).setEnabled(true);
					}

					for (int i = 0; i < beeLine.size() - 1; i++) {
						int x1 = location_map.get(beeLine.get(i)).x;
						int x2 = location_map.get(beeLine.get(i + 1)).x;
						int y1 = location_map.get(beeLine.get(i)).y;
						int y2 = location_map.get(beeLine.get(i + 1)).y;
						g2.setColor(Color.BLACK);
						if (i == 0)
							g2.drawString("출발", x1 - 10, y1 - 15);
						else if (i == beeLine.size() - 2)
							g2.drawString("도착", x2 - 10, y2 - 15);

						if (time.get(i).isAfter(LocalDateTime.now()))
							g2.setColor(Color.RED);
						else if (time.get(i + 1).isBefore(LocalDateTime.now()))
							g2.setColor(Color.BLUE);
						else
							g2.setColor(((LocalDateTime.now().getSecond() % 2 == 0) ? Color.red : Color.BLUE));
						g2.drawLine(x1, y1, x2, y2);
					}

				}

			}
		};

		for (int i = 0; i < addr.size(); i++) {
			Point p = location_map.get(i + 1);
			var me = addr.get(i);
			me.setName(i + 1 + "");
			me.setBounds(p.x + hOff, p.y + vOff, bwSize, bhSize);
			me.setVerticalAlignment(0);
			me.setHorizontalAlignment(0);
			me.setHorizontalTextPosition(0);
			me.setVerticalTextPosition(3);
			me.setEnabled(false);
			me.setOpaque(false);
			center.add(me);
			center.setComponentZOrder(me, 0);
		}

		var p = new Timer(1, a -> {
			c_line.repaint();
			c_line.revalidate();
		});
		p.start();

		center.add(c_line);
		c_line.setBounds(0, 0, 1000, 700);
		center.setLayout(null);
		center.setBackground(Color.WHITE);
		center.setSize(100, 700);
	}

	void dijkstra(int dep) {
		pathDim = new int[4][location_map.size() + 1];

		for (int i = 1; i <= location_map.size(); i++) {
			pathDim[1][i] = adjDim[dep][i];
			pathDim[2][i] = 0;
			if (adjDim[i][dep] < INF)
				pathDim[3][i] = dep;

		}

		pathDim[2][dep] = 1;
		pathDim[3][dep] = -1;

		for (int i = 1; i < location_map.size(); i++) {
			int minDist = 2147483647, idx = 0;
			for (int j = 1; j <= location_map.size(); j++) {
				if (pathDim[2][j] == 0 && minDist > pathDim[1][j]) {
					minDist = pathDim[1][j];
					idx = j;
				}
			}
			pathDim[2][idx] = 1;

			int from = idx, to;
			for (to = 1; to <= location_map.size(); to++) {
				if (pathDim[2][to] == 0 && pathDim[1][to] > pathDim[1][from] + adjDim[from][to]) {
					pathDim[1][to] = pathDim[1][from] + adjDim[from][to];
					pathDim[3][to] = from;
				}
			}
		}
	}

	ArrayList<Integer> getBeepath(int arrv) {

		ArrayList<Integer> path = new ArrayList<>();

		path.add(arrv);
		while (pathDim[3][arrv] != -1) {

			path.add(pathDim[3][arrv]);
			arrv = pathDim[3][arrv];

		}

		Collections.reverse(path);
		return path;
	}

	void showState() {
		int eidx = beeLine.size() - 1;

		// 배달 상황 기록
		int totalSec = pathDim[1][beeLine.get(eidx)] / 10;
		String stag = "<font color=\"blue\">", etag = "</font>";
		JLabel lbl = new JLabel();
		lbl.setFont(new Font("굴림체", Font.LAYOUT_RIGHT_TO_LEFT, 16));
		lbl.setText("<html><br> ○ 출발지점 " + stag + addr.get(beeLine.get(0) - 1).getText() + etag + "<br><br> ○ 도착지점 "
				+ stag + addr.get(beeLine.get(eidx) - 1).getText() + etag + "<br><br> ○ 최단거리 " + stag
				+ pathDim[1][beeLine.get(eidx)] + "m" + etag + "<br><br> ○ 소요시간 " + stag
				+ String.format("%02d", totalSec / 3600) + ":" + String.format("%02d", (totalSec % 3600) / 60) + ":"
				+ String.format("%02d", totalSec % 60) + etag + "<br><br> ○ 배송시간 " + stag
				+ DateTimeFormatter.ofPattern("HH:mm:ss").format(time.get(0)).toString() + etag + "<br><br> ○ 도착시간 "
				+ stag + DateTimeFormatter.ofPattern("HH:mm:ss").format(time.get(eidx)).toString() + etag + "</html>");
		center.add(lbl);
		lbl.setBounds(950, 50, 190, 250);
		lbl.setBackground(new Color(255, 255, 0));
		lbl.setOpaque(true);
		lbl.setVerticalAlignment(JLabel.TOP);
		lbl.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	void data() {
		try {
			var rs = BasePage.rs("select * from point");
			while (rs.next()) {
				location_map.put(rs.getInt(1), new Point(rs.getInt(2), rs.getInt(3)));
				addr.add(new JRadioButton(rs.getInt(2) + "-" + rs.getInt(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			var rs = BasePage.rs("SELECT * from user");
			while (rs.next()) {
				user_map.put(rs.getInt(1), new Object[] { rs.getString(4), rs.getInt(5) });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			var rs = BasePage.rs(
					"SELECT p.u_No, p.pu_No, r.r_Time FROM purchase as p, receive as r where p.pu_No = r.pu_No and p.s_No = "
							+ BasePage.sno + " and date(r.r_Time) = date(now())");
			while (rs.next()) {

				delivery_map.put(rs.getInt(1), new Object[] { rs.getInt(2), rs.getString(3) });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		adjDim = new int[location_map.size() + 1][location_map.size() + 1];

		for (int i = 1; i <= location_map.size(); i++) {
			for (int j = i + 1; j <= location_map.size(); j++) {
				adjDim[i][j] = adjDim[j][i] = INF;
			}
		}

		try {
			var rs = BasePage.rs("select * from connect");
			while (rs.next()) {
				int r = rs.getInt(1), c = rs.getInt(2);
				int x1 = location_map.get(r).x;
				int x2 = location_map.get(c).x;
				int y1 = location_map.get(r).y;
				int y2 = location_map.get(c).y;
				adjDim[r][c] = adjDim[c][r] = (int) Point.distance(x1, y1, x2, y2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BasePage.uno = 1;
		BasePage.sno = 1;
		BasePage.s_addr = 1;
		new Map("Seller").setVisible(true);
	}
}