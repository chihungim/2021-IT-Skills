package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import additional.Util;
import db.DBManager;

public class MainPage extends BasePage {
	
	JPanel popularP, popularC, eventP, eventC;
	int popCnt, eventCnt;
	JPanel pops[] = new JPanel[3], events[] = new JPanel[3];
	JLabel popl[] = new JLabel[3];
	ArrayList<JLabel> eventl = new ArrayList<JLabel>();
	int popX, eventX;

	public MainPage() {
		super();
		this.setLayout(new GridLayout(0, 1));
		
		this.add(popularP = new JPanel(new BorderLayout()));
		this.add(eventP = new JPanel(new BorderLayout()));
		popularP.add(popularC = new JPanel(new BorderLayout()));
		eventP.add(eventC = new JPanel(new BorderLayout()));
		
		popularP.setOpaque(false);
		eventP.setOpaque(false);
		popularC.setOpaque(false);
		eventC.setOpaque(false);
		
		popularP.setBorder(new TitledBorder(new LineBorder(Color.black, 2), "¿Œ±‚ ªÛ«∞", TitledBorder.LEFT, TitledBorder.TOP, new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20)));
		
		eventP.setBorder(new TitledBorder(new LineBorder(Color.black, 2), "¿Ã¥ﬁ¿« 1+1 «‡ªÁ ªÛ«∞", TitledBorder.LEFT, TitledBorder.TOP, new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20)));
		
		Util.setEmpty(this, 20, 20, 20, 20);
		

		for (int i = 0; i < events.length; i++) {
			pops[i] = new JPanel(new GridLayout(1, 0, 20, 20));
			events[i] = new JPanel(new GridLayout(1, 0, 20, 20));
			pops[i].setOpaque(false);
			events[i].setOpaque(false);
		}
		
		popInit();
		eventInit();
		
		
		popularC.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				popX = e.getX();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				popX = e.getX() - popX;
				if (popX > 150) {
					drawPop(-1);
				} else if (popX < -150) {
					drawPop(1);
				} else {
					drawPop(0);
				}
			}
		});
		
		
		eventC.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				eventX = e.getX();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				eventX = e.getX() - eventX;
				if (eventX > 150) {
					drawEvent(-1);
				} else if (eventX < -150) {
					drawEvent(1);
				} else {
					drawEvent(0);
				}
			}
		});
		
		
		
		this.repaint();
		this.revalidate();
	}
	
	void drawPop(int idx) {
		popCnt += idx;
		if (popCnt > popl.length-1) {
			popCnt = 0;
		} else if (popCnt < 0) {
			popCnt = popl.length-1;
		}
		
		popularC.removeAll();
		popularC.add(pops[popCnt]);
		for (var p : popl) p.setForeground(Color.black);
		popl[popCnt].setForeground(Color.red);
		popularC.repaint();
		popularC.revalidate();
	}
	
	void drawEvent(int idx) {
		eventCnt += idx;
		if (eventCnt > eventl.size()-1) {
			eventCnt = 0;
		} else if (eventCnt < 0) {
			eventCnt = eventl.size()-1;
		}
		
		eventC.removeAll();
		eventC.add(events[eventCnt]);
		for (var e : eventl) e.setForeground(Color.black);
		eventl.get(eventCnt).setForeground(Color.red);
		eventC.repaint();
		eventC.revalidate();
	}
	
	void popInit() {
		int cnt = 0;
		try {
			var rs = DBManager.rs("select pu.p_No, p_Name, count(pu_Price) from product p, purchase pu where p.p_No = pu.p_No group by pu.p_No order by count(pu.p_No) desc, p.p_Name limit 15");
			while (rs.next()) {
				JPanel bord = new JPanel(new BorderLayout());
				JLabel img;
				bord.add(new JLabel(cnt+1+"¿ß", JLabel.LEFT), "North");
				bord.add(img=Util.sz(new JLabel(Util.img("./datafile/image/"+rs.getString(2)+".jpg", 80, 100)), 80, 100));
				Util.setLine(img);
				bord.add(new JLabel(rs.getString(2), 0), "South");
				bord.setOpaque(false);
				Util.setEmpty(bord, 30, 30, 30, 30);
				
				pops[(int)cnt/5].add(bord);
				cnt++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		popularC.add(pops[0]);
		
		var pop_s = new JPanel(new FlowLayout());
		popularP.add(pop_s, "South");
		pop_s.setOpaque(false);
		for (int i = 0; i < 3; i++) {
			
			pop_s.add(popl[i]=new JLabel("°‹"));
			popl[i].setForeground(Color.black);
		}
		popl[0].setForeground(Color.red);
	}
	
	void eventInit() {
		int cnt = 0;
		try {
			var rs = DBManager.rs("select p.p_Name from event e inner join product p on p.p_No=e.p_No where e_Month = month(now())");
			while (rs.next()) {
				JPanel bord = new JPanel(new BorderLayout());
				JLabel img;
				bord.add(img=Util.sz(new JLabel(Util.img("./datafile/image/"+rs.getString(1)+".jpg", 80, 80)), 80, 80));
				Util.setLine(img);
				bord.add(new JLabel(rs.getString(1), 0), "South");
				bord.setOpaque(false);
				Util.setEmpty(bord, 30, 30, 30, 30);
				
				events[(int)cnt/5].add(bord);
				cnt++;
			}
			for (int i = 0; i < events.length; i++) {
				if (events[i].getComponentCount() < 5) {
					for (int j = events[i].getComponentCount(); j < 5; j++) {
						JPanel bord = Util.sz(new JPanel(new BorderLayout()), 100, 100);
						events[i].add(bord);
						bord.setOpaque(false);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eventC.add(events[0]);
		
		var event_s = new JPanel(new FlowLayout());
		eventP.add(event_s, "South");
		event_s.setOpaque(false);
		for (int i = 0; i < ((cnt)/5); i++) {
			eventl.add(new JLabel("°‹"));
			event_s.add(eventl.get(i));
			eventl.get(i).setForeground(Color.black);
			int j = i;
		}
		eventl.get(0).setForeground(Color.red);
	}
	
	
	
	
	
}
