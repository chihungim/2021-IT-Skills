package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public class Map extends BaseFrame {

//	ArrayList<String> pos = new ArrayList<>();
	ArrayList<Integer> cpath;
	int n[][] = { { 0, 0 }, { 1112, 83 }, { 1195, 80 }, { 1270, 80 }, { 1354, 82 }, { 1441, 79 }, { 1506, 80 },
			{ 1571, 80 }, { 1633, 81 }, { 1696, 83 }, { 1761, 80 }, { 1840, 80 }, { 1890, 129 }, { 1923, 162 },
			{ 1956, 191 }, { 1958, 232 }, { 1956, 260 }, { 1956, 292 }, { 1956, 321 }, { 1956, 356 }, { 1955, 394 },
			{ 1957, 421 }, { 1956, 478 }, { 1954, 513 }, { 1954, 541 }, { 1879, 547 }, { 1814, 545 }, { 1760, 544 },
			{ 1678, 545 }, { 1562, 545 }, { 1481, 546 }, { 1430, 542 }, { 1314, 548 }, { 1313, 641 }, { 1314, 825 },
			{ 1310, 902 }, { 1310, 1083 }, { 1216, 1080 }, { 1187, 1083 }, { 1140, 1081 }, { 1057, 1083 },
			{ 988, 1084 }, { 825, 1081 }, { 772, 1081 }, { 710, 1082 }, { 645, 1082 }, { 589, 1080 }, { 538, 1079 },
			{ 463, 1079 }, { 432, 1050 }, { 401, 1019 }, { 367, 988 }, { 337, 958 }, { 319, 942 }, { 273, 944 },
			{ 219, 944 }, { 171, 944 }, { 116, 946 }, { 114, 995 }, { 112, 1036 }, { 114, 1075 }, { 115, 1112 },
			{ 116, 1151 }, { 829, 1164 }, { 828, 1249 }, { 827, 1289 }, { 773, 1289 }, { 829, 1340 }, { 826, 1404 },
			{ 846, 1422 }, { 875, 1451 }, { 912, 1490 }, { 908, 1535 }, { 909, 1606 }, { 908, 1661 }, { 909, 1724 },
			{ 908, 1789 }, { 908, 1841 }, { 984, 1843 }, { 1071, 1844 }, { 1132, 1780 }, { 1145, 1844 }, { 1232, 1845 },
			{ 1310, 1847 }, { 1387, 1845 }, { 1464, 1844 }, { 1543, 1844 }, { 1617, 1844 }, { 1689, 1844 },
			{ 1757, 1844 }, { 1833, 1844 }, { 1908, 1843 }, { 1981, 1843 }, { 2052, 1842 }, { 2151, 1845 },
			{ 2261, 1843 }, { 2403, 1843 }, { 2508, 1842 }, { 2511, 1724 }, { 686, 962 }, { 686, 1008 }, { 811, 1010 },
			{ 915, 1010 }, { 989, 1161 }, { 991, 1233 }, { 992, 1320 }, { 1077, 1319 }, { 1139, 1317 }, { 1222, 1320 },
			{ 1312, 1320 }, { 1401, 1317 }, { 1458, 1315 }, { 1500, 1320 }, { 1544, 1320 }, { 1655, 1320 },
			{ 1695, 1322 }, { 1735, 1317 }, { 1809, 1254 }, { 1880, 1192 }, { 1913, 1155 }, { 1919, 1074 },
			{ 1914, 1004 }, { 1917, 944 }, { 1916, 907 }, { 1913, 863 }, { 1919, 820 }, { 1883, 789 }, { 1860, 764 },
			{ 1824, 727 }, { 1732, 641 }, { 1684, 638 }, { 1562, 635 }, { 1519, 633 }, { 1429, 635 }, { 1370, 637 },
			{ 1221, 636 }, { 1172, 683 }, { 1125, 728 }, { 1087, 767 }, { 1037, 807 }, { 990, 852 }, { 990, 902 },
			{ 989, 957 }, { 989, 1011 }, { 1914, 779 }, { 1916, 693 }, { 1818, 599 }, { 297, 368 }, { 356, 370 },
			{ 423, 371 }, { 488, 371 }, { 546, 371 }, { 626, 366 }, { 673, 369 }, { 728, 370 }, { 774, 370 },
			{ 821, 367 }, { 873, 370 }, { 928, 370 }, { 1033, 366 }, { 1107, 370 }, { 1201, 368 }, { 1255, 370 },
			{ 1324, 371 }, { 1427, 369 }, { 1431, 420 }, { 1429, 478 }, { 1482, 728 }, { 1540, 788 }, { 1585, 830 },
			{ 1621, 869 }, { 1624, 930 }, { 1619, 1053 }, { 1594, 1081 }, { 1540, 1138 }, { 1539, 1238 },
			{ 1542, 1395 }, { 1648, 1393 }, { 1696, 1396 }, { 1738, 1394 }, { 1781, 1358 }, { 1813, 1326 },
			{ 1887, 1321 }, { 1934, 1322 }, { 1973, 1320 }, { 1975, 1216 }, { 2103, 1214 }, { 2217, 1211 },
			{ 267, 1665 }, { 325, 1664 }, { 390, 1666 }, { 458, 1666 }, { 518, 1663 }, { 585, 1666 }, { 630, 1626 },
			{ 661, 1594 }, { 687, 1564 }, { 724, 1534 }, { 744, 1510 }, { 765, 1488 }, { 842, 1489 }, { 977, 1491 },
			{ 1030, 1491 }, { 1080, 1488 }, { 1162, 1490 }, { 1241, 1489 }, { 1295, 1490 }, { 1401, 1489 },
			{ 1401, 1427 }, { 1402, 1385 }, { 1403, 1241 }, { 1404, 1166 }, { 1404, 1082 }, { 1404, 1002 },
			{ 1403, 919 }, { 1370, 884 }, { 1355, 784 }, { 1410, 729 }, { 1559, 478 }, { 1559, 421 }, { 1562, 362 },
			{ 1560, 290 }, { 1671, 287 }, { 1754, 287 }, { 1812, 287 }, { 1872, 288 }, { 2071, 286 }, { 2168, 289 },
			{ 2250, 289 }, { 526, 553 }, { 571, 602 }, { 623, 657 }, { 685, 714 }, { 682, 768 }, { 685, 815 },
			{ 683, 858 }, { 680, 904 }, { 777, 962 }, { 830, 964 }, { 889, 962 }, { 951, 964 }, { 1062, 1017 },
			{ 1146, 982 }, { 1141, 923 }, { 1141, 873 }, { 1225, 780 }, { 1225, 706 }, { 1226, 484 }, { 1363, 479 },
			{ 1609, 725 }, { 1667, 725 }, { 1748, 729 }, { 1973, 728 }, { 2010, 760 }, { 2043, 796 }, { 2074, 826 },
			{ 2071, 878 }, { 2074, 939 }, { 2075, 1010 }, { 2221, 1014 }, { 2220, 972 }, { 2220, 936 }, { 2220, 894 },
			{ 2220, 858 }, { 2220, 811 }, { 2218, 774 }, { 2221, 736 }, { 2325, 735 }, { 2330, 776 }, { 2327, 814 },
			{ 2216, 1049 }, { 2217, 1087 }, { 2217, 1154 }, { 2218, 1291 }, { 2234, 1310 }, { 2268, 1345 } };
	JLabel[] slbl = new JLabel[n.length];
	int deq, arrv;
	int path[][];

	public Map() {
		super("지도", 1600, 900);
		try {
			datainit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel img = new JLabel(getIcon(IMAGE + "metro.png")) {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				var g2 = (Graphics2D) g;

				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				for (int i = 1; i <= 275; i++) {
					for (int j = 1; j <= 275; j++) {
						if (adjDim[i][j] != 0 && adjDim[i][j] != INF) {
							int x1 = n[i][0], x2 = n[j][0];
							int y1 = n[i][1], y2 = n[j][1];

							g2.setColor(Color.RED);
							g2.drawString(adjDim[i][j] / 5 + "", (x1 + x2) / 2 - 5, ((y1 + y2) / 2) + 5);
						}
					}
				}

				if (cpath != null) {
					int xx[] = new int[cpath.size()];
					int yy[] = new int[cpath.size()];
					var i = 0;
					for (var idx : cpath) {
						xx[i] = n[idx][0];
						yy[i] = n[idx][1];
						i++;
					}
					g2.setStroke(new BasicStroke(2f));
					g2.setColor(Color.WHITE);
					g2.drawPolyline(xx, yy, xx.length);
				}
			}
		};

		img.setLayout(null);

		for (int i = 1; i < 276; i++) {
			slbl[i] = new JLabel();
			slbl[i].setBounds(n[i][0] - 10, n[i][1] - 10, 20, 20);

			var popup = new JPopupMenu();
			for (var bcap : "출발으로 설정,도착으로 설정,해당 역 정보 보기".split(",")) {
				var item = new JMenuItem(bcap);
				item.setName(i + "");
				item.addActionListener(a -> {
					var no = toInt(((JMenuItem) a.getSource()).getName());
					if (a.getActionCommand().equals("출발으로 설정")) {
						deq = no;
						iMsg("출발역:"+ stNames.get(no));
						dijkstra();
						if (arrv != 0)
							cpath = getPath(arrv);
						img.repaint();
					} else if (a.getActionCommand().equals("도착으로 설정")) {
						arrv = no;
						iMsg("종착역:"+ stNames.get(no));
						cpath = getPath(arrv);
						img.repaint();
					} else {
						new Station(no).addWindowListener(new before(Map.this));
					}
				});
				popup.add(item);
			}
			slbl[i].setComponentPopupMenu(popup);

			img.add(slbl[i]);
		}

		add(new JScrollPane(img));
		add(btn("결정", a -> {
			if (cpath == null) {
				eMsg("출발역과 도착역을 모두 선택해야합니다.");
				return;
			}

			new Reserve(cpath).addWindowListener(new before(this));
		}), "South");
		setVisible(true);
	}

	ArrayList<Integer> getPath(int arrv) {
		ArrayList<Integer> path = new ArrayList<>();
		path.add(arrv);
		while (this.path[3][arrv] != -1) {
			path.add(this.path[3][arrv]);
			arrv = this.path[3][arrv];
		}

		Collections.reverse(path);

		return path;
	}

	void dijkstra() {
		path = new int[4][n.length];
		for (int i = 0; i < n.length; i++) {
			path[1][i] = adjDim[deq][i];
			path[2][i] = 0;
			if (adjDim[i][deq] < INF)
				path[3][i] = deq;
		}

		path[2][deq] = 1;
		path[3][deq] = -1;

		for (int i = 1; i < n.length - 1; i++) {
			int min = 2147483647, idx = 0;
			for (int j = 1; j < n.length; j++) {
				if (path[2][j] == 0 && path[1][j] < min) {
					min = path[1][j];
					idx = j;
				}
			}
			path[2][idx] = 1;
			int from = idx;
			for (int to = 0; to < adjDim.length; to++) {
				if (path[2][to] == 0 && path[1][to] > adjDim[from][to] + path[1][from]) {
					path[1][to] = adjDim[from][to] + path[1][from];
					path[3][to] = from;
				}
			}
		}
	}

	public static void main(String[] args) {
		Map m = new Map();
	}
}
