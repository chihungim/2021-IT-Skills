package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;

public class TimePicker extends JDialog {
	JLabel jl;
	MatteBorder border = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLUE);
	Reserve r;
	Spin spin;
	Cal cal;

	public TimePicker(Reserve r) {
		this.r = r;
		setSize(700, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);

		add(jl = BaseFrame.lbl("탑승하실 날짜와 시간을 선택해주세요.", 2, 15), "North");
		border.getBaseline(jl, jl.getWidth(), 100);
		jl.setBorder(border);

		JPanel c = new JPanel(new GridLayout(1, 0));
		JPanel s = new JPanel(new GridLayout(1, 1));
		add(c);
		add(s, "South");

		c.add(spin = new Spin());
		c.add(cal = new Cal());

		s.add(BaseFrame.btn("해당 시간으로 변경하기", a -> {
			if (this.cal.day.isBefore(LocalDate.now())) {
				BaseFrame.eMsg("이미 지난 날짜는 선택할 수 없습니다.");
				return;
			}
			if ((this.cal.day.equals(LocalDate.now()) || this.cal.day.isBefore(LocalDate.now()))
					&& spin.ti.isBefore(LocalTime.now().plusMinutes(30))) {
				BaseFrame.eMsg("이미 지난 시간는 선택할 수 없습니다.");
				return;
			}

			r.time = spin.ti;
			r.date = cal.date;

			r.setTime();
			
			dispose();
		}));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cal.chk.stop();
			}
		});

		setVisible(true);
	}

	class Spin extends JPanel {
		JLabel hprv, hnext, mprv, mnext;
		JLabel htime, mtime;
		int h = r.time.getHour(), m = r.time.getMinute();
		public LocalTime ti;

		public Spin() {
			super(true);
			setLayout(new FlowLayout(1, 50, 50));
			JPanel hp = new JPanel(new BorderLayout(50, 50));
			JPanel mp = new JPanel(new BorderLayout(50, 50));

			add(hp);
			add(BaseFrame.lbl(":", 0, 25));
			add(mp);

			hp.add(hnext = BaseFrame.lbl("▲", 0, 25), "North");
			hp.add(htime = BaseFrame.lbl(String.format("%02d", h), 0, 25));
			hp.add(hprv = BaseFrame.lbl("▼", 0, 25), "South");

			mp.add(mnext = BaseFrame.lbl("▲", 0, 25), "North");
			mp.add(mtime = BaseFrame.lbl(String.format("%02d", m), 0, 25));
			mp.add(mprv = BaseFrame.lbl("▼", 0, 25), "South");

			ti = LocalTime.of(h, m);

			hnext.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (h >= 23)
						return;
					htime.setText(String.format("%02d", ++h));
					ti = LocalTime.of(BaseFrame.rei(htime.getText()), BaseFrame.rei(mtime.getText()));
				}
			});

			hprv.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (h <= 0)
						return;
					htime.setText(String.format("%02d", --h));
					ti = LocalTime.of(BaseFrame.rei(htime.getText()), BaseFrame.rei(mtime.getText()));
				}
			});
			mnext.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (m >= 59)
						return;
					mtime.setText(String.format("%02d", ++m));
					ti = LocalTime.of(BaseFrame.rei(htime.getText()), BaseFrame.rei(mtime.getText()));
				}
			});

			mprv.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (m <= 0)
						return;
					mtime.setText(String.format("%02d", --m));
					ti = LocalTime.of(BaseFrame.rei(htime.getText()), BaseFrame.rei(mtime.getText()));
				}
			});

			BaseFrame.setEmpty(this, 30, 0, 0, 0);
		}
	}

	class Cal extends JPanel {
		JPanel n, c;
		JLabel prev, next, datel;
		String str[] = "Sun,Mon,Tue,Wed,Thu,Fri,Sat".split(",");
		LocalDate date = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM월 yyyy");
		JLabel jl[] = new JLabel[42];
		LocalDate day;
		Timer chk;

		public Cal() {
			setLayout(new BorderLayout());

			add(n = new JPanel(new BorderLayout()), "North");
			add(c = new JPanel(new GridLayout(0, 7)));

			n.add(prev = BaseFrame.lbl("<", 0, 20), "West");
			n.add(datel = BaseFrame.lbl(format.format(date), 0, 20));
			n.add(next = BaseFrame.lbl(">", 0, 20), "East");

			prev.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setMonth(-1);
				}
			});

			next.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setMonth(1);
				}
			});

			chk = new Timer(1, a -> {
				for (int i = 0; i < jl.length; i++) {
					if (jl[i].getBackground().equals(Color.ORANGE)) {
						day = LocalDate.of(date.getYear(), date.getMonthValue(), BaseFrame.rei(jl[i].getText()));
					}
				}
			});
			chk.start();
			drawCal();
			BaseFrame.setEmpty(this, 10, 10, 10, 10);

		}

		private void drawCal() {
			for (int i = 0; i < str.length; i++) {
				c.add(BaseFrame.lbl(str[i], 0, 15, Color.BLUE));
			}

			date = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
			int sday = (date.getDayOfWeek().getValue()) % 7, lday = date.lengthOfMonth();

			for (int i = 0; i < jl.length; i++) {
				String d = i - sday + 1 + "";
				if (i < sday) {
					c.add(jl[i] = new JLabel());
				} else if (i < sday + lday) {
					c.add(jl[i] = new JLabel(d, 0));
					if (i - sday + 1 == r.date.getDayOfMonth())
						jl[i].setBackground(Color.ORANGE);
					else
						jl[i].setBackground(Color.WHITE);
					jl[i].setOpaque(true);
					jl[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							for (int j = sday; j < sday + lday; j++) {
								jl[j].setBackground(Color.WHITE);
							}
							date = LocalDate.of(date.getYear(), date.getMonth(),
									BaseFrame.rei(((JLabel) e.getSource()).getText()));
							((JLabel) e.getSource()).setBackground(Color.ORANGE);
						}
					});
				} else {
					c.add(jl[i] = new JLabel());
				}
			}
		}

		void setMonth(int i) {
			date = date.plusMonths(i);
			datel.setText(format.format(date));
			c.removeAll();
			drawCal();
			repaint();
			revalidate();
		}
	}

	public static void main(String[] args) {
		new Map();
	}

}
