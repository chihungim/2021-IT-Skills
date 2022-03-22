package 충남;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

public class Station extends BaseFrame {
	JComboBox<String> combo = new JComboBox<String>();
	JLabel next, prev, cur;
	JPanel nc, cc, ccc, cn;
	JScrollPane scr;
	int mnum, inner;
	String st, selst;

	public Station(int sno) {
		super("역 정보", 600, 700);
		try {
			data();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		selst = st = stNames.get(sno);

		add(n = new JPanel(new FlowLayout(1)), "North");
		add(c = new JPanel(new BorderLayout(10, 10)));

		c.add(cn = new JPanel(new BorderLayout()), "North");
		c.add(scr = new JScrollPane(cc = new JPanel(new BorderLayout())));
		cc.add(ccc = new JPanel(new GridLayout(0, 1, 5, 5)));

		n.add(prev = label("", 0, 25));
		n.add(cur = label("", 0, 25));
		n.add(next = label("", 0, 25));

		cur.setForeground(Color.BLUE);

		cn.add(combo);
		cc.add(label("열차 시간표", 0, 20), "North");

		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (prev.getText().equals("기점")) {
					errmsg("해당 노선의 마지막 위치입니다.");
					return;
				}
				change(-1);
			}
		});

		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (next.getText().equals("종점")) {
					errmsg("해당 노선의 마지막 위치입니다.");
					return;
				}
				change(1);
			}
		});

		combo.addItemListener(a -> cevnt());
		
		setCombo();
		cevnt();
		
		change(0);

		setVisible(true);
	}

	private void cevnt() {
		mnum = metroNames.indexOf(combo.getSelectedItem().toString());
		st = selst;
		inner = ((ArrayList) metroStinfo[mnum][0]).indexOf(st);
		change(0);
	}

	private void setCombo() {
		combo.removeAllItems();
		
		for (int i = 1; i <= 30; i++) {
			if (((ArrayList) metroStinfo[i][0]).contains(st)) {
				combo.addItem(metroNames.get(i));
			}
		}
	}

	void update() {
		int hour0=0, hour1=0;
		String timeStr = "";
		
		ccc.removeAll();
		for(int i=0; i<300; i++) {
			int sec = metroTime[mnum][inner][i];
			
			if (sec==0) break;
			
			hour1=sec/3600;
			if((hour0!=0 && hour0!=hour1) || hour1==0) {
				JPanel temp = new JPanel(new FlowLayout());
				temp.add(new TimeLine(timeStr, hour0));
				ccc.add(temp);
				
				timeStr=String.format("%02d:%02d:%02d",sec/3600, (sec%3600)/60, sec%60) + "<br>";
			} else {
				timeStr += String.format("%02d:%02d:%02d",sec/3600, (sec%3600)/60, sec%60) + "<br>";
			}
			
			hour0=hour1;
		}
		
		ccc.repaint();
		ccc.revalidate();
	}

	class TimeLine extends JPanel {
		JLabel tl;

		public TimeLine(String time, int hour) {
			super(new BorderLayout());

			add(siz(label(hour + "시", 2, 18), 50, 50),"West");
			add(siz(tl = label("<html>" + time, 0), 470, time.split("<br>").length * 18));

			tl.setVerticalAlignment(JLabel.TOP);
			tl.setBorder(new MatteBorder(0, 1, 0, 0, Color.BLUE));
		}
	}

	void change(int i) {
		inner += i;
		
		
		cur.setText("←" + ((ArrayList) metroStinfo[mnum][0]).get(inner) + "→");
		st = ((ArrayList) metroStinfo[mnum][0]).get(inner).toString();

		if (inner-1 == 0) {
			prev.setText("기점");
		} else {
			prev.setText(((ArrayList) metroStinfo[mnum][0]).get(inner - 1).toString());
		}

		if (inner + 1 == ((ArrayList) metroStinfo[mnum][0]).size()) {
			next.setText("종점");
		} else {
			next.setText(((ArrayList) metroStinfo[mnum][0]).get(inner + 1).toString());
		}

		update();
	}
}
