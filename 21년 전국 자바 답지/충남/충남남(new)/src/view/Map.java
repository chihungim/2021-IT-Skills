package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class Map extends BaseFrame {

	Image img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/metro.png")).getImage();
	JScrollPane scr;
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item[] = { new JMenuItem("출발으로 설정"), new JMenuItem("도착으로 설정"), new JMenuItem("해당 역 정보 보기") };
	JLabel lblImg;
	ArrayList<Integer> cpath;
	ArrayList<String> list = new ArrayList<String>();
	int n[][] = { { 0, 0 }, { 1110, 81 }, { 1195, 81 }, { 1267, 82 }, { 1355, 82 }, { 1443, 82 }, { 1506, 81 },
			{ 1570, 79 }, { 1633, 81 }, { 1696, 81 }, { 1760, 83 }, { 1841, 80 }, { 1894, 127 }, { 1921, 162 },
			{ 1956, 194 }, { 1955, 230 }, { 1956, 256 }, { 1955, 286 }, { 1955, 321 }, { 1955, 357 }, { 1956, 389 },
			{ 1956, 421 }, { 1955, 476 }, { 1955, 514 }, { 1956, 546 }, { 1881, 548 }, { 1816, 545 }, { 1765, 545 },
			{ 1684, 547 }, { 1557, 545 }, { 1480, 545 }, { 1429, 545 }, { 1314, 550 }, { 1309, 641 }, { 1313, 824 },
			{ 1308, 902 }, { 1309, 1082 }, { 1213, 1084 }, { 1186, 1082 }, { 1139, 1084 }, { 1054, 1082 },
			{ 992, 1081 }, { 824, 1080 }, { 772, 1082 }, { 711, 1080 }, { 645, 1082 }, { 587, 1080 }, { 538, 1084 },
			{ 463, 1082 }, { 431, 1052 }, { 401, 1022 }, { 368, 991 }, { 339, 962 }, { 321, 943 }, { 273, 941 },
			{ 219, 942 }, { 169, 943 }, { 117, 945 }, { 116, 996 }, { 115, 1037 }, { 114, 1073 }, { 116, 1112 },
			{ 113, 1149 }, { 825, 1161 }, { 823, 1248 }, { 824, 1293 }, { 774, 1295 }, { 825, 1336 }, { 827, 1404 },
			{ 841, 1423 }, { 872, 1452 }, { 910, 1491 }, { 910, 1537 }, { 910, 1601 }, { 910, 1661 }, { 908, 1724 },
			{ 909, 1789 }, { 908, 1846 }, { 986, 1845 }, { 1065, 1844 }, { 1130, 1780 }, { 1146, 1844 }, { 1227, 1843 },
			{ 1311, 1842 }, { 1387, 1843 }, { 1465, 1842 }, { 1544, 1843 }, { 1614, 1844 }, { 1690, 1843 },
			{ 1759, 1842 }, { 1831, 1844 }, { 1906, 1844 }, { 1978, 1844 }, { 2052, 1841 }, { 2149, 1843 },
			{ 2261, 1844 }, { 2404, 1843 }, { 2506, 1841 }, { 2506, 1725 }, { 680, 963 }, { 685, 1008 }, { 812, 1008 },
			{ 918, 1008 }, { 991, 1163 }, { 990, 1232 }, { 991, 1320 }, { 1073, 1318 }, { 1138, 1319 }, { 1221, 1318 },
			{ 1308, 1322 }, { 1401, 1320 }, { 1453, 1319 }, { 1496, 1318 }, { 1540, 1318 }, { 1649, 1319 },
			{ 1696, 1319 }, { 1738, 1317 }, { 1806, 1257 }, { 1874, 1191 }, { 1914, 1152 }, { 1916, 1080 },
			{ 1917, 1003 }, { 1916, 941 }, { 1917, 911 }, { 1913, 867 }, { 1912, 822 }, { 1882, 793 }, { 1859, 765 },
			{ 1823, 731 }, { 1729, 645 }, { 1685, 640 }, { 1557, 636 }, { 1517, 637 }, { 1427, 640 }, { 1370, 641 },
			{ 1226, 642 }, { 1173, 684 }, { 1127, 732 }, { 1081, 772 }, { 1036, 811 }, { 991, 855 }, { 988, 905 },
			{ 992, 964 }, { 990, 1012 }, { 1916, 781 }, { 1910, 696 }, { 1815, 599 }, { 300, 370 }, { 357, 367 },
			{ 422, 368 }, { 488, 368 }, { 545, 368 }, { 627, 368 }, { 678, 370 }, { 722, 369 }, { 775, 371 },
			{ 822, 368 }, { 870, 369 }, { 924, 369 }, { 1030, 366 }, { 1104, 371 }, { 1200, 370 }, { 1258, 368 },
			{ 1325, 367 }, { 1430, 369 }, { 1429, 418 }, { 1429, 474 }, { 1484, 722 }, { 1537, 788 }, { 1587, 829 },
			{ 1619, 864 }, { 1617, 928 }, { 1622, 1053 }, { 1594, 1082 }, { 1539, 1141 }, { 1537, 1235 },
			{ 1543, 1394 }, { 1646, 1393 }, { 1695, 1392 }, { 1737, 1394 }, { 1779, 1356 }, { 1813, 1325 },
			{ 1887, 1321 }, { 1934, 1322 }, { 1977, 1322 }, { 1973, 1211 }, { 2100, 1214 }, { 2222, 1212 },
			{ 266, 1668 }, { 325, 1668 }, { 391, 1665 }, { 460, 1664 }, { 512, 1665 }, { 590, 1667 }, { 628, 1626 },
			{ 663, 1594 }, { 688, 1569 }, { 720, 1537 }, { 744, 1512 }, { 765, 1490 }, { 838, 1490 }, { 978, 1487 },
			{ 1025, 1490 }, { 1083, 1487 }, { 1165, 1487 }, { 1242, 1490 }, { 1293, 1488 }, { 1400, 1488 },
			{ 1404, 1425 }, { 1401, 1387 }, { 1404, 1238 }, { 1406, 1164 }, { 1401, 1077 }, { 1404, 1006 },
			{ 1401, 919 }, { 1367, 883 }, { 1359, 782 }, { 1412, 730 }, { 1561, 483 }, { 1562, 421 }, { 1562, 365 },
			{ 1562, 291 }, { 1674, 287 }, { 1752, 286 }, { 1809, 287 }, { 1870, 290 }, { 2074, 288 }, { 2171, 288 },
			{ 2249, 287 }, { 526, 558 }, { 571, 601 }, { 624, 652 }, { 682, 712 }, { 682, 770 }, { 685, 814 },
			{ 682, 860 }, { 682, 903 }, { 777, 962 }, { 830, 961 }, { 889, 963 }, { 949, 962 }, { 1059, 1019 },
			{ 1138, 985 }, { 1142, 922 }, { 1142, 870 }, { 1226, 779 }, { 1225, 705 }, { 1228, 484 }, { 1366, 482 },
			{ 1607, 728 }, { 1667, 725 }, { 1746, 726 }, { 1972, 729 }, { 2009, 761 }, { 2040, 795 }, { 2069, 828 },
			{ 2071, 878 }, { 2074, 941 }, { 2077, 1015 }, { 2220, 1014 }, { 2217, 972 }, { 2220, 931 }, { 2216, 891 },
			{ 2218, 855 }, { 2218, 813 }, { 2219, 773 }, { 2220, 735 }, { 2329, 737 }, { 2330, 774 }, { 2327, 816 },
			{ 2216, 1053 }, { 2216, 1080 }, { 2218, 1152 }, { 2219, 1293 }, { 2234, 1311 }, { 2269, 1344 } };

	int dep, arrv;
	JLabel lbls[] = new JLabel[station.length];
	int adjDim[][], pathDim[][];
	final int INF = 1000000000;
	JLabel path;

	public Map() {
		super("지도", 1600, 900);
		dataInit();

		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new GridLayout()), "South");

		c.add(scr = new JScrollPane(
				lblImg = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/metro.png")))));

		s.add(btn("결정", a -> {
			if (cpath == null) {
				eMsg("출발역과 도착역을 모두 선택해야합니다.");
				return;
			}
			new Reserve(cpath).addWindowListener(new Before(this));
		}));

		for (int i = 0; i < item.length; i++) {
			pop.add(item[i]);
			item[i].addActionListener(a -> {
				if (a.getSource().equals(item[0])) {
					dep = sno;
					System.out.println(dep);
					iMsg("출발역을 " + station[dep] + "으로 설정하였습니다.");

					if (dep != 0 && arrv != 0) {
						dijkstra();
						shortestPath(arrv);
					}

				} else if (a.getSource().equals(item[1])) {
					arrv = sno;
					System.out.println(arrv);
					iMsg("도착역을 " + station[arrv] + "으로 설정하였습니다.");

					if (dep != 0 && arrv != 0) {
						dijkstra();
						shortestPath(arrv);
					}
				} else {
					new Station(rei(pop.getName()));
				}
			});
		}

		for (int i = 1; i < n.length; i++) {
			lbls[i] = new JLabel();
			lbls[i].setName(i + "");
			lbls[i].setBounds(n[i][0] - 10, n[i][1] - 10, 25, 25);
			lblImg.add(lbls[i]);
			lbls[i].setBorder(new LineBorder(Color.BLACK));
			lbls[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JLabel l = ((JLabel) e.getSource());
					int idx = rei(l.getName());
					if (e.getButton() == 3) {
						pop.setName(((JLabel) e.getSource()).getName());
						sno = idx;
						var p = getMousePosition();
						pop.show(Map.this, p.x, p.y);
					}
				}
			});
		}

		JLabel jl;
		jl = new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				for (int i = 1; i < cost.length; i++) {
					int x1 = n[cost[i][0]][0], x2 = n[cost[i][1]][0];
					int y1 = n[cost[i][0]][1], y2 = n[cost[i][1]][1];
					g2.setColor(Color.RED);
					g2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
					g2.drawString(cost[i][2] + "", (x1 + x2) / 2, (y1 + y2) / 2);
				}
				setOpaque(false);
			}
		};
		jl.setBounds(0, 0, 2700, img.getHeight(null));
		lblImg.add(jl);

		this.setVisible(true);
	}

	void dijkstra() {
		adjDim = new int[n.length][n.length];
		for (int i = 1; i < n.length; i++) {
			for (int j = i + 1; j < n.length; j++) {
				adjDim[i][j] = adjDim[j][i] = INF;
			}
		}

		for (int i = 0; i < cost.length; i++) {
			adjDim[cost[i][0]][cost[i][1]] = cost[i][2];
		}

		pathDim = new int[5][n.length]; // 거리, 확정, 이전지점, Cost
		for (int i = 1; i < n.length; i++) {
			pathDim[1][i] = adjDim[dep][i];
			pathDim[2][i] = 0;
			if (adjDim[i][dep] < INF)
				pathDim[3][i] = dep;
		}

		pathDim[2][dep] = 1;
		pathDim[3][dep] = -1;

		for (int i = 1; i <= n.length - 2; i++) {
			int minDist = 2147483647, idx = 0;
			for (int j = 1; j < n.length; j++) {
				if (pathDim[2][j] == 0 && minDist > pathDim[1][j]) {
					minDist = pathDim[1][j];
					idx = j;
				}
			}
			pathDim[2][idx] = 1;

			int from = idx;
			for (int to = 1; to < n.length; to++) {
				if (pathDim[2][to] == 0 && pathDim[1][to] > pathDim[1][from] + adjDim[from][to]) {
					pathDim[1][to] = pathDim[1][from] + adjDim[from][to];
					pathDim[3][to] = from;
				}
			}
		}
	}

	void shortestPath(int pathidx) {
		cpath = new ArrayList<Integer>();
		cpath.add(pathidx);
		while (pathDim[3][pathidx] != -1) {
			cpath.add(pathDim[3][pathidx]);
			pathidx = pathDim[3][pathidx];
		}
		Collections.reverse(cpath);
		dep = 0;
		arrv = 0;

		try {
			lblImg.remove(path);
		} catch (Exception e) {
			System.out.println("path가 널이 것이와요..");
		}

		path = new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("맑은 고딕", Font.BOLD, 100));

				g2.setColor(Color.white);
				g2.setStroke(new BasicStroke(4));
				int xx[], yy[];
				xx = new int[cpath.size()];
				yy = new int[cpath.size()];

				for (int i = 0; i < cpath.size(); i++) {
					xx[i] = n[cpath.get(i)][0];
					yy[i] = n[cpath.get(i)][1];
				}

				g2.drawPolyline(xx, yy, cpath.size());

				setOpaque(false);

			}
		};
		path.setBounds(0, 0, 2700, img.getHeight(null));
		lblImg.add(path);
		repaint();
		revalidate();

	}

	public static void main(String[] args) {
		new Map();
	}
}
