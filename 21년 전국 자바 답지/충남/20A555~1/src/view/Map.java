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
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

public class Map extends BaseFrame {

	Image img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/metro.png")).getImage();

	ArrayList<Integer> cpath;
	ArrayList<String> list = new ArrayList<String>();
	JLabel lblPath, lblCost;
	JScrollPane scr;
	int n[][] = { { 0, 0 }, { 1110, 83 }, { 1192, 82 }, { 1270, 82 }, { 1359, 81 }, { 1444, 80 }, { 1507, 82 },
			{ 1569, 80 }, { 1636, 83 }, { 1698, 80 }, { 1763, 83 }, { 1843, 82 }, { 1891, 130 }, { 1919, 159 },
			{ 1956, 192 }, { 1954, 229 }, { 1955, 254 }, { 1955, 288 }, { 1956, 321 }, { 1954, 357 }, { 1954, 391 },
			{ 1953, 417 }, { 1955, 478 }, { 1956, 514 }, { 1955, 543 }, { 1880, 543 }, { 1818, 546 }, { 1767, 546 },
			{ 1683, 547 }, { 1559, 545 }, { 1478, 546 }, { 1429, 546 }, { 1313, 549 }, { 1312, 640 }, { 1311, 826 },
			{ 1309, 898 }, { 1309, 1081 }, { 1218, 1080 }, { 1187, 1083 }, { 1140, 1080 }, { 1060, 1082 },
			{ 988, 1083 }, { 825, 1083 }, { 776, 1082 }, { 710, 1082 }, { 647, 1079 }, { 586, 1080 }, { 542, 1082 },
			{ 462, 1081 }, { 429, 1053 }, { 398, 1021 }, { 366, 989 }, { 341, 965 }, { 321, 942 }, { 273, 940 },
			{ 222, 944 }, { 172, 942 }, { 117, 946 }, { 115, 995 }, { 113, 1036 }, { 115, 1073 }, { 117, 1108 },
			{ 116, 1152 }, { 823, 1160 }, { 828, 1250 }, { 826, 1295 }, { 770, 1295 }, { 828, 1337 }, { 826, 1398 },
			{ 840, 1423 }, { 871, 1449 }, { 908, 1488 }, { 910, 1536 }, { 912, 1598 }, { 911, 1663 }, { 911, 1722 },
			{ 910, 1787 }, { 907, 1845 }, { 987, 1842 }, { 1066, 1846 }, { 1130, 1779 }, { 1144, 1843 }, { 1227, 1846 },
			{ 1311, 1844 }, { 1388, 1843 }, { 1462, 1842 }, { 1545, 1842 }, { 1617, 1845 }, { 1686, 1844 },
			{ 1763, 1844 }, { 1832, 1843 }, { 1904, 1841 }, { 1978, 1842 }, { 2051, 1846 }, { 2152, 1845 },
			{ 2260, 1845 }, { 2404, 1844 }, { 2504, 1844 }, { 2509, 1725 }, { 683, 960 }, { 685, 1008 }, { 810, 1011 },
			{ 918, 1011 }, { 988, 1164 }, { 991, 1234 }, { 994, 1312 }, { 1070, 1318 }, { 1136, 1320 }, { 1223, 1317 },
			{ 1311, 1320 }, { 1405, 1320 }, { 1450, 1320 }, { 1498, 1318 }, { 1540, 1317 }, { 1651, 1317 },
			{ 1696, 1319 }, { 1735, 1319 }, { 1804, 1258 }, { 1875, 1192 }, { 1916, 1151 }, { 1915, 1077 },
			{ 1916, 1000 }, { 1917, 940 }, { 1918, 903 }, { 1917, 864 }, { 1913, 818 }, { 1881, 788 }, { 1861, 763 },
			{ 1824, 727 }, { 1733, 641 }, { 1685, 639 }, { 1558, 636 }, { 1513, 637 }, { 1429, 637 }, { 1368, 640 },
			{ 1226, 640 }, { 1172, 685 }, { 1130, 729 }, { 1084, 774 }, { 1036, 810 }, { 990, 856 }, { 991, 903 },
			{ 987, 961 }, { 990, 1013 }, { 1917, 780 }, { 1914, 699 }, { 1818, 600 }, { 298, 367 }, { 359, 368 },
			{ 422, 368 }, { 485, 369 }, { 543, 370 }, { 628, 367 }, { 676, 368 }, { 723, 368 }, { 771, 366 },
			{ 820, 367 }, { 870, 367 }, { 923, 367 }, { 1031, 366 }, { 1108, 365 }, { 1203, 369 }, { 1255, 366 },
			{ 1320, 366 }, { 1425, 373 }, { 1430, 421 }, { 1433, 476 }, { 1483, 728 }, { 1542, 785 }, { 1582, 827 },
			{ 1623, 865 }, { 1620, 930 }, { 1620, 1056 }, { 1591, 1083 }, { 1540, 1138 }, { 1539, 1237 },
			{ 1543, 1392 }, { 1652, 1392 }, { 1700, 1397 }, { 1736, 1394 }, { 1782, 1357 }, { 1812, 1325 },
			{ 1883, 1320 }, { 1930, 1322 }, { 1971, 1322 }, { 1970, 1213 }, { 2103, 1213 }, { 2218, 1217 },
			{ 263, 1667 }, { 331, 1667 }, { 394, 1668 }, { 457, 1665 }, { 512, 1663 }, { 585, 1665 }, { 624, 1629 },
			{ 665, 1593 }, { 685, 1570 }, { 718, 1537 }, { 742, 1514 }, { 766, 1490 }, { 837, 1488 }, { 978, 1486 },
			{ 1029, 1490 }, { 1080, 1489 }, { 1165, 1488 }, { 1243, 1492 }, { 1293, 1490 }, { 1402, 1491 },
			{ 1402, 1430 }, { 1404, 1384 }, { 1402, 1238 }, { 1402, 1165 }, { 1401, 1080 }, { 1405, 1006 },
			{ 1400, 920 }, { 1371, 882 }, { 1358, 779 }, { 1410, 729 }, { 1558, 482 }, { 1556, 425 }, { 1557, 367 },
			{ 1557, 292 }, { 1674, 287 }, { 1756, 286 }, { 1814, 284 }, { 1875, 289 }, { 2068, 290 }, { 2173, 291 },
			{ 2251, 289 }, { 527, 556 }, { 572, 599 }, { 623, 652 }, { 678, 712 }, { 683, 767 }, { 681, 815 },
			{ 681, 858 }, { 682, 900 }, { 778, 965 }, { 834, 966 }, { 893, 966 }, { 947, 965 }, { 1060, 1018 },
			{ 1143, 982 }, { 1139, 924 }, { 1138, 869 }, { 1220, 781 }, { 1227, 705 }, { 1227, 485 }, { 1367, 481 },
			{ 1610, 725 }, { 1662, 726 }, { 1749, 729 }, { 1970, 727 }, { 2007, 760 }, { 2043, 797 }, { 2072, 829 },
			{ 2072, 879 }, { 2072, 943 }, { 2076, 1014 }, { 2218, 1013 }, { 2220, 970 }, { 2217, 930 }, { 2219, 894 },
			{ 2216, 851 }, { 2217, 813 }, { 2217, 773 }, { 2220, 736 }, { 2329, 736 }, { 2328, 775 }, { 2328, 815 },
			{ 2217, 1049 }, { 2220, 1086 }, { 2217, 1151 }, { 2217, 1290 }, { 2235, 1313 }, { 2269, 1349 } };
	JLabel lblImg, lbls[] = new JLabel[n.length];
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item[] = { new JMenuItem("출발으로 설정"), new JMenuItem("도착으로 설정"), new JMenuItem("해당 역 정보 보기") };
	int dep, arrv;
	final int INF = 10000000;
	
	int pathDim[][];
//	int adjDim[][], pathDim[][];
	

	public Map() {
		super("지도", 1600, 900);
		this.dataInit();
		
		try {
			this.dataInit2();
		} catch (SQLException e1) {
		}

		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new GridLayout()), "South");

		c.add(scr = new JScrollPane(
				lblImg = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./지급자료/images/metro.png"))) {
					@Override
					public void paint(Graphics g) {
						super.paint(g);

						for (int i = 0; i < list.size(); i++) {
							String[] p = list.get(i).substring(list.get(i).indexOf("{") + 1, list.get(i).indexOf("}"))
									.split(",");
							g.drawRect(rei(p[0]) - 10, rei(p[1]) - 10, 20, 20);
						}
					}
				}));

		s.add(btn("결정", a -> {
			new Reserve(cpath);
		}));

		for (int i = 0; i < item.length; i++) {
			pop.add(item[i]);
			item[i].addActionListener(a -> {
				if (a.getSource().equals(item[0])) {
					dep = sno;
					iMsg("출발역을 " + stations.get(dep)+"으로 설정하였습니다.");
					
					if (dep != 0 && arrv != 0) {
						dijkstra();
						showPath(arrv);
					}
					
				} else if (a.getSource().equals(item[1])) {
					arrv = sno;
					iMsg("도착역을 " + stations.get(arrv)+"으로 설정하였습니다.");
					
					if (dep != 0 && arrv != 0) {
						dijkstra();
						showPath(arrv);
					}
				} else {
					new Station(rei(pop.getName()));
				}
			});
		}
		
		for (int j = 0; j < lbls.length; j++) {
			lblImg.add(lbls[j] = new JLabel());
			lbls[j].setBounds(n[j][0]-10, n[j][1]-10, 20, 20);
			int k = j;
			lbls[j].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton()==3) {
						pop.setName(k+"");
						var p = getMousePosition();
						sno = k;
						pop.show(Map.this, p.x, p.y);
					}
				};
			});
		}
		
		lblCost = new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for (int i = 1; i < cost.length; i++) {
					int x1 = n[cost[i][0]][0], x2 = n[cost[i][1]][0];
					int y1 = n[cost[i][0]][1], y2 = n[cost[i][1]][1];
					g2.setFont(new Font("맑은 고딕", Font.BOLD, 15));
					g2.setColor(Color.red);
					g2.drawString(cost[i][2]+"", (x1+x2)/2-5, ((y1+y2)/2)+5);
				}
				
				setOpaque(false);
			}
		};
		lblCost.setBounds(0, 0, img.getWidth(null), img.getHeight(null));
		lblImg.add(lblCost);

//		lblImg.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mousePressed(MouseEvent e) {
//				if (e.getButton() == 3) {
//					list.remove(list.size() - 1);
//					lblImg.repaint();
//					lblImg.revalidate();
//					System.out.println("좌표목록: " + String.join(",", list));
//				} else {
//					list.add("{" + e.getX() + "," + e.getY() + "}");
//					lblImg.repaint();
//					lblImg.revalidate();
//					System.out.println("좌표목록: " + String.join(",", list));
//				}
//			}
//
//		});

		this.setVisible(true);

	}
	
	void dijkstra() {
//		adjDim = new int[n.length][n.length];
//		for (int i = 1; i < n.length; i++) {
//			for (int j = i+1; j < n.length; j++) {
//				adjDim[i][j] = adjDim[j][i] = INF;
//			}
//		}
//		
//		for (int i = 0; i < cost.length; i++) {
//			adjDim[cost[i][0]][cost[i][1]] = cost[i][2];
//		}
		
		pathDim = new int[4][n.length];
		for (int i = 0; i < n.length; i++) {
			pathDim[1][i] = adjDim[dep][i];
			pathDim[2][i] = 0;
			if (adjDim[i][dep] < INF) pathDim[3][i] = dep;
		}
		
		pathDim[2][dep] = 1;
		pathDim[3][dep] = -1;
		
		for (int i = 1; i < n.length-1; i++) {
			int min = 21000000, idx = 0;
			for (int j = 1; j < n.length; j++) {
				if (pathDim[2][j]==0 && pathDim[1][j] < min) {
					min = pathDim[1][j];
					idx = j;
				}
			}
			pathDim[2][idx]= 1;
			
			int from = idx;
			for (int to = 1; to < n.length; to++) {
				if (pathDim[2][to] == 0 && pathDim[1][to] > pathDim[1][from] + adjDim[from][to]) {
					pathDim[1][to] = pathDim[1][from] + adjDim[from][to];
					pathDim[3][to] = from;
				}
			}
		}
		
	}
	
	void showPath(int idx) {
		cpath = new ArrayList<Integer>();
		cpath.add(idx);
		
		while (pathDim[3][idx] != -1) {
			cpath.add(pathDim[3][idx]);
			idx = pathDim[3][idx];
		}
		
		Collections.reverse(cpath);
		
		dep =0;
		arrv=0;
		
		try {
			lblImg.remove(lblPath);
		} catch (Exception e) {
		}
		
		lblPath = new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				int[] xx = new int[cpath.size()], yy = new int[cpath.size()];
				for (int i = 0; i < xx.length; i++) {
					xx[i] = n[cpath.get(i)][0];
					yy[i] = n[cpath.get(i)][1];
				}
				
				g2.setStroke(new BasicStroke(5));
				g2.setColor(Color.white);
				g2.drawPolyline(xx, yy, cpath.size());
				setOpaque(false);
			}
		};
		lblPath.setBounds(0, 0, img.getWidth(null), img.getHeight(null));
		lblImg.add(lblPath);
		
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		Map m = new Map();
		
		//테스트
		try {
			m.dataInit2();
		} catch (SQLException e) {
		}
		
		//메트로 노선을 29로 선택한 경우
		int metroNum = 19;
		ArrayList al = (ArrayList)m.metroStInfos[metroNum][0];
		System.out.println(m.metroNames.get(metroNum) + "(" + m.metroStInfos[2][1] + "," + m.metroStInfos[2][2] + "," + m.metroStInfos[2][3] + ")");
		for(int i=1; i<al.size(); i++) {
			System.out.print(al.get(i) + "->");
		}
		System.out.println();
		
		//메트로 29번 노선의 모든 정류장 운행 시간표
		for(int metroStNum=1; metroStNum<al.size(); metroStNum++) {
			System.out.println(al.get(metroStNum));
			for(int k=0; k<100; k++) {
				int sec = m.metroTimeDim[metroNum][metroStNum][k];
				System.out.print(String.format("%02d:%02d:%02d",sec/3600, (sec%3600)/60, sec%60) + "->");
//				System.out.print(LocalTime.ofSecondOfDay(m.metroTimeDim[metroNum][metroStNum][k]).toString() + "->");
			}
			System.out.println();
		}
	}
}
