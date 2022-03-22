package 광광주;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import 광광주.Map.Structure;

public class Map extends BaseDialog {

	Structure maps[][] = new Structure[15][15];
	Sign s;
	Delivery d;

	int dirX[] = { -1, 0, 1, 0 };
	int dirY[] = { 0, -1, 0, 1 };
	Point uPos, sPos, rPos;
	int rno;
	boolean mode = false;

	public Map(BaseFrame b) {
		super(b, "지도", 1000, 1000);
		setLayout(new GridLayout(15, 15));
		if (b instanceof Sign) {
			mode = true;
			s = (Sign) b;
		}
		data();
		events();
		reset();

	}

	public Map(BaseFrame b, Point uPos, Point rPos, Point sPos, int rno) {
		this(b);
		this.uPos = uPos;
		this.rPos = rPos;
		this.sPos = sPos;
		this.rno = rno;
		showPath();
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

	void data() {
		try {
			var rs = stmt.executeQuery("select * from map");
			while (rs.next()) {
				maps[rs.getInt(3) - 1][rs.getInt(2) - 1] = new Structure(rs.getInt(4), rs.getInt(1), rs.getInt(2),
						rs.getInt(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	void showPath() {

		new Thread(() -> {
			var start = maps[rPos.y - 1][rPos.x - 1];
			var end = maps[sPos.y - 1][sPos.x - 1];

			start.setBackground(Color.MAGENTA);
			end.setBackground(Color.red);

			var path = beeLine(start, end);

			path.forEach(a -> {
				a.setBackground(Color.LIGHT_GRAY);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			BaseFrame.iMsg("음식점에 도착했습니다.");
			reset();

			start = maps[sPos.y - 1][sPos.x - 1];
			end = maps[uPos.y - 1][uPos.x - 1];

			start.setBackground(Color.MAGENTA);
			end.setBackground(Color.red);

			path = beeLine(start, end);
			path.forEach(a -> {
				a.setBackground(Color.LIGHT_GRAY);
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

	ArrayList<Structure> beeLine(Structure start, Structure end) {
		ArrayList<Structure> route = new ArrayList<>();
		ArrayList<Integer> rcnt = new ArrayList<>();
		ArrayList<Integer> before = new ArrayList<>();

		for (int i = 0; i < dirX.length; i++) {
			int dx = start.x + dirX[i];
			int dy = start.y + dirY[i];

			if ((dx > -1 && dx < 15) && (dy > -1 && dy < 15)) {
				if (maps[dy][dx].type == 1) {
					route.add(maps[dy][dx]);
					rcnt.add(1);
					before.add(-1);
				}
			}
		}

		int deqidx = -1;

		while (true) {
			deqidx = deqidx + 1;
			var cur = route.get(deqidx);
			int pcnt = rcnt.get(deqidx);
			if (isReached(cur, end)) {
				break;
			}

			for (int i = 0; i < dirX.length; i++) {
				int dx = cur.x + dirX[i];
				int dy = cur.y + dirY[i];
				if (dx > -1 && dx < maps.length && dy > -1 && dy < maps.length) {
					if (!(route.contains(maps[dy][dx])) && maps[dy][dx].type == 1) {
						route.add(maps[dy][dx]);
						pcnt = pcnt + 1;
						rcnt.add(pcnt);
						before.add(deqidx);
					}
				}
			}
		}

		int idx = deqidx;

		ArrayList<Structure> path = new ArrayList<>();

		while (true) {
			path.add(route.get(idx));
			idx = before.get(idx);
			if (idx == -1)
				break;
		}

		Collections.reverse(path);
		return path;
	}

	boolean isReached(Structure cur, Structure end) {
		for (int i = 0; i < 4; i++) {
			int dx = cur.x + dirX[i];
			int dy = cur.y + dirY[i];
			if (dx > -1 && dx < 15 && dy > -1 && dy < 15) {
				if (maps[dy][dx].equals(end)) {
					return true;
				}
			}
		}
		return false;
	}

	void events() {
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

	class Structure extends JLabel {
		int type, no, x, y;

		public Structure(int type, int no, int x, int y) {

			setOpaque(true);

			this.type = type;
			this.no = no;
			this.x = x - 1;
			this.y = y - 1;

			setBorder(new LineBorder(Color.BLACK));

			if (this.type == 0)
				setBackground(Color.BLACK);

			if (this.type == 1)
				this.setBackground(Color.orange);

			if (this.type == 3)
				this.setBackground(Color.BLUE.brighter());

			if (this.type == 4)
				setBackground(Color.GREEN.darker());
		}
	}
}
