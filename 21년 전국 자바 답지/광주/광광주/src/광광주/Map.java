package 광광주;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

public class Map extends BaseDialog {

	GridBagLayout gBag;

	MyJLabel maps[][] = new MyJLabel[15][15];
	ArrayList<MyJLabel> path1;
	ArrayList<MyJLabel> path2;
	boolean mode;
	Sign s;
	Delivery d;
	int dirX[] = { -1, 0, 1, 0 };
	int dirY[] = { 0, -1, 0, 1 };
	Point uPos, sPos, rPos;
	int rno;
	Queue<MyJLabel> queue = new LinkedList<Map.MyJLabel>();

	public Map(BaseFrame b) {
		super(b, "지도", 1000, 1000);
		setLayout(new GridLayout(15, 15, 0, 0));
		if (b instanceof Sign) {
			mode = true;
			s = (Sign) b;
		}
		init();
	}

	public Map(BaseFrame b, Point uPos, Point rPos, Point sPos, int rno) {
		super(b, "지도", 1000, 1000);
		setLayout(new GridLayout(15, 15, 0, 0));
		init();
		this.uPos = uPos;
		this.rPos = rPos;
		this.sPos = sPos;
		this.rno = rno;
		RiderToSeller();
	}

	void init() {
		try {
			var rs = stmt.executeQuery("select * from map");
			while (rs.next()) {
				maps[rs.getInt(3) - 1][rs.getInt(2) - 1] = new MyJLabel(rs.getInt(4), rs.getInt(1), rs.getInt(2),
						rs.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[0].length; j++) {
				add(maps[i][j]);
				if (mode) {
					maps[i][j].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (((MyJLabel) e.getSource()).type == 1) {
								BaseFrame.eMsg("길은 선택할 수 없습니다.");
								return;
							}

							if (((MyJLabel) e.getSource()).type != 0) {
								BaseFrame.eMsg("이미 사용중인 위치입니다.");
								return;
							}

							s.txt[4].setText(((MyJLabel) e.getSource()).no + "," + ((MyJLabel) e.getSource()).x + ","
									+ ((MyJLabel) e.getSource()).y);

							dispose();
							super.mouseClicked(e);
						}
					});
				}
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

	void RiderToSeller() {
		maps[sPos.y - 1][sPos.x - 1].setBackground(Color.RED);
		maps[rPos.y - 1][rPos.x - 1].setBackground(Color.MAGENTA.darker());
		bfs(maps[sPos.y - 1][sPos.x - 1], new Point(rPos.x - 1, rPos.y - 1));
		showAnimation();
	}

	void RiderToUser() {
		maps[sPos.y - 1][sPos.x - 1].setBackground(Color.RED);
		maps[uPos.y - 1][uPos.x - 1].setBackground(Color.MAGENTA.darker());
		bfs(maps[uPos.y - 1][uPos.x - 1], new Point(sPos.x - 1, sPos.y - 1));
	}

	void showAnimation() {
		new Thread(() -> {
			for (int i = 0; i < path1.size(); i++) {
				path1.get(i).setBackground(Color.LIGHT_GRAY);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			BaseFrame.iMsg("음식점에 도착했습니다!");
			reset();
			RiderToUser();
			for (int i = 0; i < path2.size(); i++) {
				path2.get(i).setBackground(Color.LIGHT_GRAY);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			BaseFrame.iMsg("배달을 완료했습니다!");
			execute("update receipt set status = 3 where no = " + rno);
		}).start();
	}

	void bfs(MyJLabel cur, Point arrive) {
		ArrayList<MyJLabel> route = new ArrayList<>();
		ArrayList<Integer> routeCnt = new ArrayList<>();
		ArrayList<Integer> startP = new ArrayList<>();
		ArrayList<MyJLabel> shortestPath = new ArrayList<>();

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

			boolean stopFlag = false;
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
			if (path1 == null)
				this.path1 = shortestPath;
			else {
				this.path2 = shortestPath;
			}
		}

		int idx = deqidx;
		while (true) {
			shortestPath.add(route.get(idx));
			idx = startP.get(idx);
			if (idx == -1)
				break;
		}
	}

	class MyJLabel extends JLabel {
		int type, no, x, y;
		boolean isVisited;

		// 지들 좌표에서 1뺸 값이 자기 좌표
		public MyJLabel(int type, int no, int x, int y) {
			this.setOpaque(true);
			this.type = type;
			this.no = no;
			this.x = x - 1;
			this.y = y - 1;
		}
	}
}
