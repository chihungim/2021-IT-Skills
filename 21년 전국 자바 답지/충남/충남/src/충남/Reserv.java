package 충남;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

public class Reserv extends BaseFrame {
	ArrayList<Integer> path;
	String ssta, esta;
	LocalDate date = LocalDate.now();
	LocalTime now = LocalTime.now().plusMinutes(30);
	JLabel ltime, lread;
	JPanel cc, ccc, cn = new JPanel(new FlowLayout(0));
	int tot, tcnt, inter;
	JLabel jl;

	int timetable[][], eidx;

	public Reserv(ArrayList<Integer> arr) {
		super("예매", 500, 600);

		path = arr;

		add(n = new JPanel(new BorderLayout()), "North");
		add(c = new JPanel(new BorderLayout()));

		ssta = stNames.get(arr.get(0));
		esta = stNames.get(arr.get(arr.size() - 1));

		n.add(jl = label(ssta + " → " + esta, 0, 25));
		jl.setForeground(Color.BLUE);

		c.add(cn, "North");

		cn.add(btn("시간 변경", a -> {
			new TimePick(Reserv.this);
		}));

		cn.add(ltime = label(date + " " + tformat(now, "HH:mm:ss") + " 탑승", 2));

		c.add(new JScrollPane(cc = new JPanel(new BorderLayout())));

		JPanel ccn = new JPanel(new BorderLayout());
		cc.add(ccc = new JPanel(new GridLayout(0, 1, 5, 5)));

		mkTime();

		cc.add(ccn, "North");
		JLabel j;
		ccn.add(j = label(arr.size() + "개역 정차" + tcnt + "회 환승", 2, 15), "West");
		j.setForeground(Color.BLUE);
		ccn.add(lread = label("자세히보기", 4, 15), "East");
		lread.setForeground(Color.RED);

		lread.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Way(Reserv.this);
			}
		});

		setTime();

		setVisible(true);
	}

	void setTime() {
		ltime.setText(date + " " + tformat(now, "HH:mm:ss") + " 탑승");

		System.out.println(tformat(now, "HH:mm:ss") + "," + date);

		ccc.removeAll();
		for (int i = 4; i < 300; i++) {
			if (timetable[eidx][i] == 0)
				break;
			ccc.add(new item(i));
		}
		repaint();
		revalidate();
	}

	class item extends JPanel {
		String mname;
		JLabel ssta, esta;
		JPanel ce, cw, c, cc;
		JLabel time;
		int st, et;

		public item(int si) {
			setLayout(new BorderLayout(2, 2));
			st = timetable[0][si];
			et = timetable[eidx][si];

			System.out.println(st);

			add(c = new JPanel(new BorderLayout()));
			c.add(cc = new JPanel(new BorderLayout()));
			c.add(ce = new JPanel(new BorderLayout()), "East");
			c.add(cw = new JPanel(new BorderLayout()), "West");

			cw.add(ssta = label(Reserv.this.ssta, 2, 15), "South");
			ce.add(esta = label(Reserv.this.esta, 4, 15), "South");

			ssta.setForeground(Color.LIGHT_GRAY);
			esta.setForeground(Color.LIGHT_GRAY);

			c.setBackground(Color.WHITE);

			cw.add(label(String.format("%02d:%02d", st / 3600, (st % 3600) / 60), 0, 35));
			add(btn("선택", a -> {
				if (NO == -1) {
					errmsg("로그인 후 예매 가능합니다.");
					return;
				}
				int yn = JOptionPane.showConfirmDialog(null,
						String.format("%02d:%02d", st / 3600, (st % 3600) / 60) + "시간 지하털을 예매할까요?", "메시지",
						JOptionPane.YES_NO_OPTION);

				if (yn == JOptionPane.YES_OPTION) {
					new Purchase(item.this, Reserv.this).addWindowListener(new be(Reserv.this));
				}
			}), "East");

			int tot = et - st;

			cc.add(time = label((tot / 60) + "분 " + (tot % 60) + "초 소요", 0));
			ce.add(label(String.format("%02d:%02d", et / 3600, (et % 3600) / 60), 0, 35));

			time.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));
			time.setForeground(Color.BLUE);

			cc.setBorder(new MatteBorder(2, 0, 0, 0, Color.BLACK));

			cc.setOpaque(false);
			ce.setOpaque(false);
			cw.setOpaque(false);
		}
	}

	void mkTime() {
		int lidx, inner;

		timetable = new int[path.size()][300];
		eidx = path.size() - 1;

		for (int i = 0; i <= eidx - 1; i++) {
			timetable[i][0] = path.get(i);
			timetable[i][1] = line[path.get(i)][path.get(i + 1)];

			if (i == 0) {
				timetable[i][3] = 0;
			} else {
				timetable[i][3] = adj[timetable[i - 1][0]][timetable[i][0]];
				if (timetable[i - 1][1] != timetable[i][1]) {
					timetable[i][2] = 1;
					tcnt++;
				}
			}
		}

		timetable[eidx][0] = path.get(path.size() - 1);
		timetable[eidx][3] = adj[timetable[eidx - 1][0]][timetable[eidx][0]];

		int col = 4;
		lidx = timetable[0][1];
		inner = ((ArrayList) metroStinfo[lidx][0]).indexOf(stNames.get(path.get(0)));
		for (int i = 0; i < 100; i++) {
			if (metroTime[lidx][inner][i] == 0)
				break;
			if (now.toSecondOfDay() <= metroTime[lidx][inner][i]) {
				timetable[0][col++] = metroTime[lidx][inner][i];
			}
		}

		for (int i = 1; i < eidx; i++) {
			lidx = timetable[i][1];
			inner = ((ArrayList) metroStinfo[lidx][0]).indexOf(stNames.get(path.get(i)));

			for (int j = 4; j < 300; j++) {
				if (timetable[i - 1][j] == 0)
					break;

				for (int k = 0; k < 300; k++) {
					if (metroTime[lidx][inner][k] == 0)
						break;

					if (timetable[i - 1][j] + timetable[i][3] <= metroTime[lidx][inner][k]) {
						timetable[i][j] = metroTime[lidx][inner][k];
						break;
					}
				}
			}
		}

		for (int i = 4; i < 300; i++) {
			if (timetable[eidx - 1][i] == 0)
				break;

			timetable[eidx][i] = timetable[eidx - 1][i] + timetable[eidx][3];
		}
	}

}
