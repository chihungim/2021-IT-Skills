package 충남;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.MatteBorder;

public class TimePick extends JDialog {
	JLabel jl;
	MatteBorder border = new MatteBorder(0, 0, 2, 0, Color.BLUE);
	Reserv r;
	spin spin;
	Cal cal;
	JPanel c = new JPanel(new GridLayout(1, 0)), s = new JPanel(new GridLayout(0, 1));

	public TimePick(Reserv r) {
		setSize(700, 500);
		setTitle("시간 선택");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
		setModal(true);
		
		this.r = r;
		
		add(jl = BaseFrame.label("탑승하실 날짜와 시간을 선택해주세요.", 2, 15),"North");
		border.getBaseline(jl, jl.getWidth(), 100);
		jl.setBorder(border);
		
		add(c);
		add(s,"South");
		
		c.add(spin = new spin());
		c.add(cal = new Cal());
		
		s.add(BaseFrame.btn("해당 시간으로 변경하기", a->{
			if(cal.day.isBefore(LocalDate.now())) {
				BaseFrame.errmsg("이미 지난 날짜는 선택할 수 없습니다.");
				return;
			}
			if((cal.day.equals(LocalDate.now()) || cal.day.isBefore(LocalDate.now())) && spin.t.isBefore(LocalTime.now().plusMinutes(30))) {
				BaseFrame.errmsg("이미 지난 시간은 선택할 수 없습니다.");
				return;
			}
			
			r.now = spin.t;
			r.date = cal.day;
			r.mkTime();
			r.setTime();
			dispose();
		}));
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				cal.chk.stop();
			}
		});

		setVisible(true);
	}
	
	class spin extends JPanel {
		JLabel pr1, ne1, pr2, ne2;
		JLabel hour, min;
		int h = r.now.getHour(), m = r.now.getMinute();
		public LocalTime t;
		JPanel hp = new JPanel(new BorderLayout(50, 50));
		JPanel mp = new JPanel(new BorderLayout(50, 50));
		
		public spin() {
			super(true);
			setLayout(new FlowLayout(1, 50, 50));
			
			add(hp);
			add(BaseFrame.label(":", 0, 25));
			add(mp);
			
			hp.add(ne1 = BaseFrame.label("▲", 0, 25),"North");
			hp.add(hour = BaseFrame.label(String.format("%02d", h), 0, 25));
			hp.add(pr1 = BaseFrame.label("▼", 0, 25),"South");
			
			mp.add(ne2 = BaseFrame.label("▲", 0, 25),"North");
			mp.add(min = BaseFrame.label(String.format("%02d", m), 0, 25));
			mp.add(pr2 = BaseFrame.label("▼", 0, 25),"South");
			
			t = LocalTime.of(h, m,0);
			
			ne1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(h >= 23) return;
					h++;
					hour.setText(String.format("%02d", h));
					t = LocalTime.of(h, m,0);
				}
			});
			pr1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(h <= 0) return;
					h--;
					hour.setText(String.format("%02d", h));
					t = LocalTime.of(h, m,0);
				}
			});
			
			ne2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(m >= 59) return;
					m++;
					min.setText(String.format("%02d", m));
					t = LocalTime.of(h, m,0);
				}
			});
			
			pr2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(m <= 0) return;
					m--;
					min.setText(String.format("%02d", m));
					t = LocalTime.of(h, m,0);
				}
			});
		}
		
	}
	
	class Cal extends JPanel {
		JPanel n, c;
		JLabel prev, next, date;
		String str[] = "Sun,Mon,Tue,Wed,Thu,Fri,Sat".split(",");
		LocalDate da = LocalDate.now(), day;
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM월 yyyy");
		JLabel jl[] = new JLabel[42];
		Timer chk;
		
		public Cal() {
			setLayout(new BorderLayout());
			
			add(n = new JPanel(new BorderLayout()),"North");
			add(c = new JPanel(new GridLayout(0, 7)));
			
			n.add(prev = BaseFrame.label("<", 0, 20),"West");
			n.add(date = BaseFrame.label(format.format(da), 0, 20));
			n.add(next = BaseFrame.label(">", 0, 20),"East");
			
			prev.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setMon(-1);
				}
			});
			next.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setMon(1);
				}
			});
			
			draw();
			
			chk = new Timer(1, a->{
				for (int i = 0; i < jl.length; i++) {
					if(jl[i].getBackground().equals(Color.ORANGE)) {
						day = LocalDate.of(da.getYear(), da.getMonth(), BaseFrame.toint(jl[i].getText()));
					}
				}
			});
			
			chk.start();
		}
		private void draw() {
			for (int i = 0; i < str.length; i++) {
				JLabel jl;
				c.add(jl = BaseFrame.label(str[i], 0, 15));
				jl.setForeground(Color.BLUE);
			}
			
			da = LocalDate.of(da.getYear(), da.getMonth(), 1);
			
			int sday = da.getDayOfWeek().getValue() % 7, lday = da.lengthOfMonth()+sday;
			
			for (int i = 0; i < jl.length; i++) {
				String d = i - sday + 1+"";
				if(i < sday) {
					c.add(jl[i] = new JLabel());
				}else if (i<lday) {
					c.add(jl[i] = new JLabel(d, 0));
					jl[i].setOpaque(true);
					day = LocalDate.of(da.getYear(), da.getMonth(), BaseFrame.toint(d));
					if(day.toEpochDay() == r.date.toEpochDay()) {
						jl[i].setBackground(Color.ORANGE);
					}else {
						jl[i].setBackground(Color.WHITE);
					}
					jl[i].addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							for (int j = 0; j < jl.length; j++) {
								jl[j].setBackground(Color.WHITE);
							}
							JLabel j = (JLabel)e.getSource();
							j.setBackground(Color.ORANGE);
						}
					});
				}else {
					c.add(jl[i] = new JLabel());
				}
			}
			
		}
		private void setMon(int mon) {
			da = da.plusMonths(mon);
			date.setText(format.format(da));
			c.removeAll();
			draw();
			repaint();
			revalidate();
		}
	}

}
