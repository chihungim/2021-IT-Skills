package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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

	String time, date = "05:00:00"; //가져올 시간

	public TimePicker(Reserve r) {
		add(lbl = BaseFrame.lbl("탑승하실 날짜와 시간을 선택해주세요.", JLabel.LEFT, 15), "North");
		border.getBaseline(lbl, lbl.getWidth(), 100);
		lbl.setBorder(border);
		setSize(700, 500);
		setLocationRelativeTo(null);

		var c = new JPanel(new GridLayout(1, 0));
		var s = new JPanel(new GridLayout(1, 1));
		add(c);
		add(s, "South");

		c.add(new TimeSetter());
		c.add(new Cal());

		s.add(BaseFrame.btn("해당 시간으로 변경하기", a -> {

		}));
		
		((JPanel)getContentPane()).setBorder(new EmptyBorder(5,5,5,5));
		setVisible(true);
	}

	class Cal extends JPanel {

		String week[] = "Sun,Mon,Tue,Wed,Thu,Fri,Sat".split(",");
		JLabel prev, next;
		JToggleButton btn[] = new JToggleButton[42];
		LocalDate cdate = LocalDate.now();
		JLabel date;
		ButtonGroup BG = new ButtonGroup();
		int year, month;

		void addDate(int n) {
			cdate = cdate.plusMonths(n);
			setDate();
		}

		public Cal() {
			super(new BorderLayout(5, 5));
			var n = new JPanel(new BorderLayout(5, 5));
			var n_s = new JPanel(new GridLayout(1, 0, 5, 5));
			var c = new JPanel(new GridLayout(0, 7));

			add(n, "North");
			n.add(n_s, "South");

			add(c);

			n.add(prev = BaseFrame.lbl("<", JLabel.LEFT, 25), "West");
			n.add(date = BaseFrame.lbl(month + "월 " + year, JLabel.CENTER, 25));
			n.add(next = BaseFrame.lbl(">", JLabel.RIGHT, 25), "East");
			for (int i = 0; i < week.length; i++) {
				n_s.add(BaseFrame.lbl("<html><font color = \"blue\">" + week[i].trim(), 0));
			}

			prev.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					addDate(-1);
				}
			});

			next.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					addDate(1);
				}
			});

			for (int i = 0; i < btn.length; i++) {
				c.add(btn[i] = new JToggleButton());
				btn[i].setBackground(Color.WHITE);
				btn[i].setBorder(new EmptyBorder(0, 0, 0, 0));
				btn[i].setFocusPainted(false);
				btn[i].setUI(new MetalToggleButtonUI() {
					@Override
					protected Color getSelectColor() {
						return Color.ORANGE;
					}
				});
				BG.add(btn[i]);

			}

			setDate();
		}

		void setDate() {
			year = cdate.getYear();
			month = cdate.getMonthValue();

			date.setText(month + "월 " + year);
			LocalDate sdate = LocalDate.of(year, month, 1);
			int sday = sdate.getDayOfWeek().getValue() % 7;

			for (int i = 0; i < 42; i++) {
				LocalDate tmp = sdate.plusDays(i - sday);
				btn[i].setText("<html><font face = \"PLAIN\">" + tmp.getDayOfMonth() + "");
				btn[i].setSelected(false);
				if (tmp.getMonthValue() == cdate.getMonthValue()) {
					btn[i].setVisible(true);
				} else
					btn[i].setVisible(false);
			}

			revalidate();
			repaint();
		}
	}

	class TimeSetter extends JPanel {
		JLabel hhprev, hhnext, mmprev, mmnext;
		JLabel time;

		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		public TimeSetter() {
			setLayout(new BorderLayout());
			var n = new JPanel(new GridLayout(1, 0));
			var s = new JPanel(new GridLayout(1, 0));
			add(n, "North");
			add(s, "South");

			n.add(hhprev = BaseFrame.lbl("▲", 0, 25));
			s.add(hhnext = BaseFrame.lbl("▼", 0, 25));

			n.add(mmprev = BaseFrame.lbl("▲", 0, 25));
			s.add(mmnext = BaseFrame.lbl("▼", 0, 25));

			add(time = BaseFrame.lbl("00 : 00", 0, 25));
		
			setBorder(new EmptyBorder(50, 50, 50, 50));
		}
	}

	public static void main(String[] args) {
			new TimePicker(null);
	}
}
