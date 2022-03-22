package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.metal.MetalToggleButtonUI;

public class TimePicker extends JDialog {
	JLabel lbl;
	MatteBorder border = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLUE);
	Reserve r;
	Spin s;
	Cal c;

	public TimePicker(Reserve r) {
		this.r = r;
		this.setSize(700, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);

		add(lbl = BaseFrame.lbl("탑승하실 날짜와 시간을 선택해주세요.", JLabel.LEFT, 15), "North");
		border.getBaseline(lbl, lbl.getWidth(), 100);
		lbl.setBorder(border);

		var c = new JPanel(new GridLayout(1, 0));
		var s = new JPanel(new GridLayout(1, 1));
		add(c);
		add(s, "South");

		c.add(this.s = new Spin());
		c.add(this.c = new Cal());

		s.add(BaseFrame.btn("해당 시간으로 변경하기", a -> {
			if (this.c.cday.isBefore(r.date)) {
				BaseFrame.eMsg("이미 지난 날짜는 선택 할 수 없습니다.");
				return;
			}

			if (this.c.cday.isBefore(r.date) && this.s.cTime.isBefore(r.time)) {
				BaseFrame.eMsg("이미 지난 시간은 선택할 수 없습니다.");
				return;
			}

			r.time = this.s.cTime;
			r.date = this.c.cday;

			r.setTime();
			dispose();
		}));

		((JPanel) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	class Spin extends JPanel {

		JLabel hhprev, hhnext, mmprev, mmnext;
		JLabel hhtime, mmtime;
		int hh = r.time.getHour(), mm = r.time.getMinute();
		public LocalTime cTime;

		public Spin() {
			this.setLayout(new FlowLayout(1, 50, 50));
			var hhP = new JPanel(new BorderLayout(50, 50));
			var mmP = new JPanel(new BorderLayout(50, 50));

			this.add(hhP);
			this.add(BaseFrame.lbl(":", 0, 25));
			this.add(mmP);

			hhP.add(hhnext = BaseFrame.lbl("▲", 0, 25), "North");
			hhP.add(hhtime = BaseFrame.lbl(String.format("%02d", hh), 0, 25));
			hhP.add(hhprev = BaseFrame.lbl("▼", 0, 25), "South");

			mmP.add(mmnext = BaseFrame.lbl("▲", 0, 25), "North");
			mmP.add(mmtime = BaseFrame.lbl(String.format("%02d", mm), 0, 25));
			mmP.add(mmprev = BaseFrame.lbl("▼", 0, 25), "South");

			cTime = LocalTime.of(hh, mm);

			hhnext.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (hh >= 23)
						return;
					hhtime.setText(String.format("%02d", ++hh));
					cTime = LocalTime.of(BaseFrame.rei(hhtime.getText()), BaseFrame.rei(mmtime.getText()));
				}
			});

			hhprev.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (hh <= 0)
						return;
					hhtime.setText(String.format("%02d", --hh));
					cTime = LocalTime.of(BaseFrame.rei(hhtime.getText()), BaseFrame.rei(mmtime.getText()));
				}
			});
			mmnext.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (mm >= 59)
						return;
					mmtime.setText(String.format("%02d", ++mm));
					cTime = LocalTime.of(BaseFrame.rei(hhtime.getText()), BaseFrame.rei(mmtime.getText()));
				}
			});
			mmprev.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (mm <= 0)
						return;
					mmtime.setText(String.format("%02d", --mm));
					cTime = LocalTime.of(BaseFrame.rei(hhtime.getText()), BaseFrame.rei(mmtime.getText()));
				}
			});

			BaseFrame.setEmpty(this, 30, 0, 0, 0);
//			BaseFrame.setEmpty(this, 50, 50, 50, 50);
		}
	}

	class Cal extends JPanel {

		JPanel n, c;
		JLabel prev, next, datelbl;
		String week[] = "Sun,Mon,Tue,Wed,Thu,Fri,Sat".split(",");
		LocalDate date = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM월 yyyy");
		JLabel day[] = new JLabel[42];
		LocalDate cday;

		public Cal() {
			this.setLayout(new BorderLayout());

			this.add(n = new JPanel(new BorderLayout()), "North");
			this.add(c = new JPanel(new GridLayout(0, 7)));

			n.add(prev = BaseFrame.lbl("<", 0, 20), "West");
			n.add(datelbl = BaseFrame.lbl(format.format(date), 0, 20));
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

			javax.swing.Timer chk = new javax.swing.Timer(1, a -> {
				for (int i = 0; i < day.length; i++) {
					if (day[i].getBackground() == Color.ORANGE) {
						cday = LocalDate.of(date.getYear(), date.getMonthValue(), BaseFrame.rei(day[i].getText()));
					}
				}
			});

			chk.start();
			drawCal();
			BaseFrame.setEmpty(this, 10, 10, 10, 10);
		}

		void drawCal() {
			for (int i = 0; i < week.length; i++) {
				c.add(BaseFrame.lbl(week[i], 0, 15, Color.blue, Font.PLAIN));
			}

			date = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
			int sday = (date.getDayOfWeek().getValue()) % 7, lday = date.lengthOfMonth();

			for (int i = 0; i < day.length; i++) {
				String d = i - sday + 1 + "";
				if (i < sday) {
					c.add(day[i] = new JLabel());
				} else if (i < sday + lday) {
					c.add(day[i] = new JLabel(d, 0));
					if (i - sday + 1 == r.date.getDayOfMonth())
						day[i].setBackground(Color.orange);
					else
						day[i].setBackground(Color.WHITE);
					day[i].setOpaque(true);
					day[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							for (int j = sday; j < sday + lday; j++) {
								day[j].setBackground(Color.white);
							}
							((JLabel) e.getSource()).setBackground(Color.orange);
						}
					});

				} else {
					c.add(day[i] = new JLabel());
				}
			}
		}

		void setMonth(int month) {
			date = date.plusMonths(month);
			datelbl.setText(format.format(date));
			c.removeAll();
			drawCal();
			repaint();
			revalidate();
		}

	}
}
