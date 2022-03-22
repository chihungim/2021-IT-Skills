package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import additional.Util;
import db.Tools;

public class MapFrame_org extends JDialog {
	// 추가
	HashMap<Integer, Object[]> allUserMap = new HashMap<Integer, Object[]>();
	HashMap<Integer, Object[]> sellerDeliveryMap = new HashMap<Integer, Object[]>();
	ArrayList<Integer> path[];
	ArrayList<LocalDateTime> time[];

	HashMap<Integer, Point> locMap = new HashMap<Integer, Point>();

	ArrayList<JRadioButton> jrb = new ArrayList<JRadioButton>();
	JPanel jp;
	int bwSize = 70, bhSize = 50, hOff = -35, vOff = -15;
	int adjDim[][], pathDim[][];
	final int INF = 1000000000;
	boolean hasMinPath = false;
	Color color;
	MyLabel ml;
	int cnt = 0;
	static String kind;

	public MapFrame_org(String kind) {
		super(BasePage.mf, "배송 현황", true);
		this.kind = kind;
		makeUI1();

		dataInit();

		makeUI2();

		this.color = Color.RED;

		dijkstra(BasePage.s_addr);

		// 셀러 주소와 배달 고객(i)의 주소 간의 최단 경로를 지도에 표시한다.
		int showidx = 0;

		if (kind.contentEquals("seller")) {
			path = new ArrayList[sellerDeliveryMap.size()];
			time = new ArrayList[sellerDeliveryMap.size()];

			for (var i : sellerDeliveryMap.keySet()) {
				// 최단 경로 탐색 결과 기록
				hasMinPath = true;
				int userAddr = (int) allUserMap.get(i)[1];
				// 수정
				shortestPath(userAddr, showidx++, LocalDateTime.parse((String) sellerDeliveryMap.get(i)[1],
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}
			hasMinPath = false;

			// 실시간 배달경로 표시
			realtimeDraw();

			// 배달 상황판을 만들지 않는다.
			this.setSize(1000, 750);
			this.setLocationRelativeTo(null);
		} else if (kind.contentEquals("user")) {

			path = new ArrayList[1];
			time = new ArrayList[1];

			// 최단 경로 탐색 결과 기록
			hasMinPath = true;
			int userAddr = (int) allUserMap.get(BasePage.uno)[1];
			System.out.println(userAddr);
			System.out.println(BasePage.usermodeStartTime);

			// 수정
			shortestPath(userAddr, showidx++, BasePage.usermodeStartTime);
			hasMinPath = false;

			// 실시간 배달경로 표시
			realtimeDraw();

			// 배달 상황판을 jl에 나타낸다.
			showDeliveryState(0);
		}
	}

	private void makeUI1() {
		this.setSize(1200, 750);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
		this.getContentPane().setBackground(Color.white);

		this.add(Util.lbl("[" + BasePage.sname + "] 물품 배달 현황", 0, 25), "North");
		jp = new JPanel();
		this.add(jp);

		jp.setLayout(null);
		jp.setBackground(Color.WHITE);
		jp.setSize(100, 700);
		jp.setLayout(null);
	}

	private void makeUI2() {
		ml = new MyLabel();
		ml.setBounds(0, 0, 1000, 700);
		jp.add(ml);
	}

	private void realtimeDraw() {
		hasMinPath = true;
		new Thread(() -> {
			while (true) {
				ml.repaint();
				ml.revalidate();
			}
		}).start();
	}

	private void showDeliveryState(int showidx) {
		ArrayList<Integer> cpath = path[showidx];
		ArrayList<LocalDateTime> ctime = time[showidx];
		int eidx = cpath.size() - 1;

		// 배달 상황 기록
		int totalSec = pathDim[1][cpath.get(eidx)] / 10;
		String stag = "<font color=\"blue\">", etag = "</font>";
		JLabel lbl = new JLabel();
		lbl.setFont(new Font("굴림체", Font.LAYOUT_RIGHT_TO_LEFT, 16));
		lbl.setText("<html><br> ○ 출발지점 " + stag + jrb.get(cpath.get(0) - 1).getText() + etag + "<br><br> ○ 도착지점 " + stag
				+ jrb.get(cpath.get(eidx) - 1).getText() + etag + "<br><br> ○ 최단거리 " + stag
				+ pathDim[1][cpath.get(eidx)] + "m" + etag + "<br><br> ○ 소요시간 " + stag
				+ String.format("%02d", totalSec / 3600) + ":" + String.format("%02d", (totalSec % 3600) / 60) + ":"
				+ String.format("%02d", totalSec % 60) + etag + "<br><br> ○ 배송시간 " + stag
				+ DateTimeFormatter.ofPattern("HH:mm:ss").format(ctime.get(0)).toString() + etag + "<br><br> ○ 도착시간 "
				+ stag + DateTimeFormatter.ofPattern("HH:mm:ss").format(ctime.get(eidx)).toString() + etag + "</html>");
		jp.add(lbl);

		lbl.setBounds(950, 50, 190, 250);
		lbl.setBackground(new Color(255, 255, 0));
		lbl.setOpaque(true);
		lbl.setVerticalAlignment(JLabel.TOP);
		lbl.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	private void dijkstra(int s) {
		pathDim = new int[4][locMap.size() + 1]; // ★: [1][]는 거리,[2][]는 방문체크, [3][]는 직전 출발지점, [4][] 소요시간

		// 초기화: 출발지점(s)에서 모든 연결 지점(i)까지 거리, 방문체크, 직전 출발지점을 기록한다.
		for (int i = 1; i <= locMap.size(); i++) {
			pathDim[1][i] = adjDim[s][i];
			pathDim[2][i] = 0;
			if (adjDim[i][s] < INF)
				pathDim[3][i] = s;
		}

		// 최초 출발지점 기록: 방문체크를 1로, 직전 출발지점이 없음(-1)로 기록한다.
		pathDim[2][s] = 1;
		pathDim[3][s] = -1;

		for (int i = 1; i < locMap.size(); i++) {
			// minSpot결정: 미확정(방문체크:false)인 곳 중 거리가 가장 가까운 지점을 찾고, 찾은 곳은 확정(방문체크:true)한다.
			int minDist = 2147483647, idx = 0;
			for (int j = 1; j <= locMap.size(); j++) {
				if (pathDim[2][j] == 0 && minDist > pathDim[1][j]) {
					minDist = pathDim[1][j];
					idx = j;
				}
			}
			pathDim[2][idx] = 1;

			// 미확정인 곳 중 찾은 곳(idx:from)과 연결된 지점(to)들을 모두 찾고, pathDim[0][to]에는 거리, [2][to]에는
			// 출발지점을 기록한다.
			int from = idx, to;
			for (to = 1; to <= locMap.size(); to++) {
				// ★: (현재 to까지 최단 거리 > 현재 from까지 최단 거리 + from에서 to까지 거리)이면 현재 to까지 최단 거리를 더 ?은
				// 거리로 갱신한다.
				if (pathDim[2][to] == 0 && pathDim[1][to] > pathDim[1][from] + adjDim[from][to]) {
					pathDim[1][to] = pathDim[1][from] + adjDim[from][to];
					pathDim[3][to] = from;
				}
			}
		}
	}

	// 수정
	private void shortestPath(int pathidx, int showidx, LocalDateTime startTime) {
		path[showidx] = new ArrayList<Integer>();
		ArrayList<Integer> cpath = path[showidx];

		time[showidx] = new ArrayList<LocalDateTime>();
		ArrayList<LocalDateTime> ctime = time[showidx];

		cpath.add(pathidx);
		while (pathDim[3][pathidx] != -1) {
			cpath.add(pathDim[3][pathidx]);
			pathidx = pathDim[3][pathidx];
		}

		// 올바른 순서로 출력한다.
		Collections.reverse(cpath);

		// UI 표시
		if (showidx == 0) {
			JLabel lb1 = new JLabel("출발", 0);
			jp.add(lb1);
			lb1.setBounds(jrb.get(cpath.get(0) - 1).getLocation().x, jrb.get(cpath.get(0) - 1).getLocation().y - 20, 60,
					25);
		}

		int eidx = cpath.size() - 1;
		JLabel lb2 = new JLabel("도착", 0);
		jp.add(lb2);
		lb2.setBounds(jrb.get(cpath.get(eidx) - 1).getLocation().x, jrb.get(cpath.get(eidx) - 1).getLocation().y - 18,
				60, 25);

		for (int i = 0; i < cpath.size(); i++) {
			// 수정
			ctime.add(startTime.plusSeconds(pathDim[1][cpath.get(i)] / 10));
			jrb.get(cpath.get(i) - 1).setEnabled(true);
			// 수정
			if (kind.contentEquals("seller") && i < cpath.size() - 1)
				jrb.get(cpath.get(i) - 1).setToolTipText("");
			else
				jrb.get(cpath.get(i) - 1).setToolTipText(
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(ctime.get(i)).toString());
		}
	}

	void dataInit() {
		// locMap 데이터 준비
		try {
			var rs = Tools.rs("select * from point");
			while (rs.next()) {
				locMap.put(rs.getInt(1), new Point(rs.getInt(2), rs.getInt(3)));
				jrb.add(new JRadioButton(rs.getInt(2) + "-" + rs.getInt(3)));
			}
		} catch (SQLException e) {
		}

		// allUserMap 데이터 준비: u_Name, u_Addr
		try {
			var rs = Tools.rs("SELECT * from user");
			while (rs.next()) {
				allUserMap.put(rs.getInt(1), new Object[] { rs.getString(4), rs.getInt(5) });
			}
		} catch (SQLException e) {
		}

		// sellerDeliveryMap 데이터 준비: pu_No, r_Time
		try {
			var rs = Tools.rs(
					"SELECT p.u_No, p.pu_No, r.r_Time FROM purchase as p, receive as r where p.pu_No = r.pu_No and p.s_No = "
							+ BasePage.sno + " and date(r.r_Time) = date(now())");
			while (rs.next()) {
//				sellerDeliveryMap.put(rs.getInt(1), new Object[] {rs.getInt(2), rs.getDate(3)});
				sellerDeliveryMap.put(rs.getInt(1), new Object[] { rs.getInt(2), rs.getString(3) });
			}
		} catch (SQLException e) {
		}

		// JRadioButton을 화면에
		for (int i = 0; i < jrb.size(); i++) {
			Point p = locMap.get(i + 1);
			jrb.get(i).setName(i + 1 + "");
			jrb.get(i).setBounds(p.x + hOff, p.y + vOff, bwSize, bhSize);
			jrb.get(i).setVerticalAlignment(JRadioButton.CENTER);
			jrb.get(i).setHorizontalAlignment(JRadioButton.CENTER);
			jrb.get(i).setHorizontalTextPosition(JRadioButton.CENTER);
			jrb.get(i).setVerticalTextPosition(JRadioButton.BOTTOM);
			jrb.get(i).setEnabled(false);
			jrb.get(i).setOpaque(false);
			jp.add(jrb.get(i));
		}

		//
		adjDim = new int[locMap.size() + 1][locMap.size() + 1];

		// 모두 도달할 수 없는 거리가 되게 한다.
		for (int i = 1; i <= locMap.size(); i++) {
			for (int j = i + 1; j <= locMap.size(); j++) {
				adjDim[i][j] = adjDim[j][i] = INF;
			}
		}

		try {
			var rs = Tools.rs("select * from connect");
			while (rs.next()) {
				int r = rs.getInt(1), c = rs.getInt(2);

				int x1 = locMap.get(r).x;
				int x2 = locMap.get(c).x;
				int y1 = locMap.get(r).y;
				int y2 = locMap.get(c).y;
				adjDim[r][c] = adjDim[c][r] = (int) (Math.sqrt(0.0 + Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
			}
		} catch (SQLException e) {
		}

	}

	class MyLabel extends JLabel {
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2.5f));
			g2.setColor(Color.lightGray);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Set<Integer> set = locMap.keySet();
			for (var r : set) {
				for (int c = 1; c <= locMap.size(); c++) {
					if (adjDim[r][c] > 0 && adjDim[r][c] < INF) {
						int x1 = locMap.get(r).x;
						int x2 = locMap.get(c).x;
						int y1 = locMap.get(r).y;
						int y2 = locMap.get(c).y;
						g2.drawLine(x1, y1, x2, y2);
					}
				}
			}

			g2.setColor(Color.red);
			g2.setStroke(new BasicStroke(4f));

			if (hasMinPath) {
				for (int i = 0; i < path.length; i++) {
					ArrayList<Integer> cpath = path[i];
					ArrayList<LocalDateTime> ctime = time[i];

					for (int j = 0; j < cpath.size(); j++) {
						jrb.get(cpath.get(j) - 1).setSelected(true);
					}

					for (int j = 0; j < cpath.size() - 1; j++) {
						int x1 = locMap.get(cpath.get(j)).x;
						int x2 = locMap.get(cpath.get(j + 1)).x;
						int y1 = locMap.get(cpath.get(j)).y;
						int y2 = locMap.get(cpath.get(j + 1)).y;

						// 수정
						if (ctime.get(j).isAfter(LocalDateTime.now()))
							g2.setColor(Color.red);
						else if (ctime.get(j + 1).isBefore(LocalDateTime.now()))
							g2.setColor(Color.blue);
						else
							g2.setColor((LocalDateTime.now().getSecond() % 2 == 0) ? Color.RED : Color.blue);

						g2.drawLine(x1, y1, x2, y2);
					}
				}
			}

		}
	}

	public static void main(String[] args) {
		// seller
		// 시스템 시간: 2021-08-02 08:17:00
		kind = "seller";
		BasePage.sno = 1;
		BasePage.s_addr = 59;
		BasePage.sname = "셀러1";
		new MapFrame_org("seller").setVisible(true);
	}

}
