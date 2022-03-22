package 광광주;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class Map extends BaseDialog {

	Structure maps[][] = new Structure[15][15];
	ArrayList<Structure> path1;
	ArrayList<Structure> path2;
	Sign s;
	Delivery d;
	int dirX[] = { -1, 0, 1, 0 };
	int dirY[] = { 0, -1, 0, 1 };
	Point uPos, sPos, rPos;
	int rno;
	boolean mode;

	public Map(BaseFrame b) {
		super(b, "지도", 1000, 1000);
		setLayout(new GridLayout(15, 15, 0, 0));
		if (b instanceof Sign) {
			mode = true;
			s = (Sign) b;
		}
		try {
			var rs = stmt.executeQuery("select * from map");
			while (rs.next()) {
				maps[rs.getInt(3) - 1][rs.getInt(2) - 1] = new Structure(rs.getInt(4), rs.getInt(1), rs.getInt(2),
						rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[0].length; j++) {
				add(maps[i][j]);

				if (maps[i][j].type == 2) {
					this.setBackground(Color.PINK);
					try {
						var rs = stmt.executeQuery("Select * from seller where map=" + maps[i][j].no);
						if (rs.next()) {
							maps[i][j].setToolTipText(rs.getString(5));
							maps[i][j].setText(rs.getString(5));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (mode) {
					maps[i][j].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (((Structure) e.getSource()).type == 1) {
								BaseFrame.eMsg("길은 선택할 수 없습니다.");
								return;
							}

							if (((Structure) e.getSource()).type != 0) {
								BaseFrame.eMsg("이미 사용중인 위치입니다.");
								return;
							}

							s.txt[4].setText(((Structure) e.getSource()).no + "," + ((Structure) e.getSource()).x + ","
									+ ((Structure) e.getSource()).y);

							dispose();
							super.mouseClicked(e);
						}
					});
				}

			}
		}
	}

	public Map(BaseFrame b, Point uPos, Point rPos, Point sPos, int rno) {
		this(b);
		this.uPos = uPos;
		this.rPos = rPos;
		this.sPos = sPos;
		this.rno = rno;
		RiderToSeller();
	}

	void RiderToSeller() {
		maps[sPos.y - 1][sPos.x - 1].setBackground(Color.RED);
		maps[rPos.y - 1][rPos.x - 1].setBackground(Color.MAGENTA.darker());
		var v = bfs(maps[sPos.y - 1][sPos.x - 1], new Point(rPos.x - 1, rPos.y - 1));

		new Thread(() -> {
			v.forEach(a -> {
				a.setBackground(Color.GRAY);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});

			BaseFrame.iMsg("음식점에 도착했습니다.");
			reset();
			RiderToUser();
		}).start();
	}

	void RiderToUser() {
		maps[sPos.y - 1][sPos.x - 1].setBackground(Color.RED);
		maps[uPos.y - 1][uPos.x - 1].setBackground(Color.MAGENTA.darker());
		var v = bfs(maps[uPos.y - 1][uPos.x - 1], new Point(sPos.x - 1, sPos.y - 1));
		new Thread(() -> {
			v.forEach(a -> {
				a.setBackground(Color.GRAY);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			BaseFrame.iMsg("배달을 완료했습니다!");
		}).start();
	}

	void reset() {
		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[0].length; j++) {
				maps[i][j].setBorder(new LineBorder(Color.BLACK));
				if (maps[i][j].type == 0)
					maps[i][j].setBackground(Color.WHITE);

				if (maps[i][j].type == 1)
					maps[i][j].setBackground(Color.ORANGE);

				if (maps[i][j].type == 2) {
					maps[i][j].setBackground(Color.PINK);
					try {
						var rs = stmt.executeQuery("Select * from seller where map=" + maps[i][j].no);
						if (rs.next()) {
							maps[i][j].setToolTipText(rs.getString(5));
							maps[i][j].setText(rs.getString(5));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (maps[i][j].type == 3)
					maps[i][j].setBackground(Color.BLUE.brighter());

				if (maps[i][j].type == 4)
					maps[i][j].setBackground(Color.green.darker());
			}
		}
	}

	ArrayList<Structure> bfs(Structure cur, Point arrive) {
		ArrayList<Structure> route = new ArrayList<>();
		ArrayList<Integer> routeCnt = new ArrayList<>();
		ArrayList<Integer> startP = new ArrayList<>();
		ArrayList<Structure> shortestPath = new ArrayList<>();

		for (int i = 0; i < dirX.length; i++) {
			int dx = cur.x + dirX[i];
			int dy = cur.y + dirY[i];
			if ((dx > -1 && dx < maps.length) && (dy > -1 && dy < maps.length)) {
				if (maps[dy][dx].type == 1) {
					route.add(maps[dy][dx]);
					routeCnt.add(1);
					startP.add(-1);
				}
			}
		}

		int deqidx = -1;

		while (true) {
			deqidx = deqidx + 1;
			var stopFlag = false;
			var current = route.get(deqidx);
			int pCnt = routeCnt.get(deqidx);

			for (int i = 0; i < dirX.length; i++) {
				int dx = current.x + dirX[i];
				int dy = current.y + dirY[i];
				if (dx > -1 && dx < maps.length && dy > -1 && dy < maps.length) {
					if (dy == arrive.y && dx == arrive.x) {
						stopFlag = true;
						break;
					}
				}
			}
			if (stopFlag) {
				break;
			}

			for (int i = 0; i < dirX.length; i++) {
				int dx = current.x + dirX[i];
				int dy = current.y + dirY[i];
				if (dx > -1 && dx < maps.length && dy > -1 && dy < maps.length) {
					if (!(route.contains(maps[dy][dx])) && maps[dy][dx].type == 1 && !maps[dy][dx].isVisited) {
						route.add(maps[dy][dx]);
						pCnt = pCnt + 1;
						routeCnt.add(pCnt);
						startP.add(deqidx);
					}
				}
			}
		}

		int idx = deqidx;

		while (true) {
			shortestPath.add(route.get(idx));
			idx = startP.get(idx);
			if (idx == -1)
				break;
		}

		return shortestPath;
	}

//	ArrayList<Structure> bfs(Point dep, Point arrv) {
//		Queue<Structure> q = new LinkedList<Structure>();
//		ArrayList<Structure> v = new ArrayList<Structure>();
//		ArrayList<Integer> from = new ArrayList<>();
//		for (int i = 0; i < dirX.length; i++) {
//			int dx = dep.x + dirX[i];
//			int dy = dep.y + dirY[i];
//			if ((dx > -1 && dx < maps.length) && (dy > -1 && dy < maps.length)) {
//				if (maps[dy][dx].type == 1) {
//					q.add(maps[dy][dx]);
//					v.add(maps[dy][dx]);
//					from.add(-1);
//				}
//			}
//		}
//
//		int deqidx = -1;
//
//		while (!q.isEmpty()) {
//			deqidx = deqidx + 1;
//			var cur = q.poll();
//			if (check(new Point(cur.x, cur.y), arrv))
//				break;
//
//			for (int i = 0; i < dirX.length; i++) {
//				int dx = cur.x + dirX[i];
//				int dy = cur.y + dirY[i];
//				if (dx > -1 && dx < maps.length && dy > -1 && dy < maps.length) {
//					if (!(v.contains(maps[dy][dx])) && maps[dy][dx].type == 1) {
//						q.add(maps[dy][dx]);
//						v.add(maps[dy][dx]);
//						from.add(v.indexOf(maps[dy][dx]));
//					}
//				}
//			}
//		}
//
//		ArrayList<Structure> path = new ArrayList<>();
//
//		while (true) {
//
//		}
//		return from;
//	}

	boolean check(Point cur, Point arrv) {
		for (int i = 0; i < 4; i++) {
			int dx = cur.x + dirX[i];
			int dy = cur.y + dirY[i];
			if (dx > -1 && dx < maps.length && dy > -1 && dy < maps.length) {
				if (dy == arrv.y && dx == arrv.x) {
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	class Structure extends JLabel {
		int type, no, x, y;
		boolean isVisited;

		// 지들 좌표에서 1뺸 값이 자기 좌표
		public Structure(int type, int no, int x, int y) {
			this.setOpaque(true);
			this.type = type;
			this.no = no;
			this.x = x - 1;
			this.y = y - 1;

			this.setBorder(new LineBorder(Color.BLACK));
			if (this.type == 0)
				this.setBackground(Color.WHITE);

			if (this.type == 1)
				this.setBackground(Color.ORANGE);

			if (this.type == 3)
				this.setBackground(Color.BLUE.brighter());

			if (this.type == 4)
				this.setBackground(Color.green.darker());
		}

		@Override
		public String toString() {
			return x + "," + y + "," + no;
		}
	}

}
