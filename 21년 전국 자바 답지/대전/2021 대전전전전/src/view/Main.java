package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import db.DBManager;

public class Main extends BaseFrame {
	
	String sqls[] = {
		"select p.p_no, p.p_name, count(*) from perform p, ticket t where p.p_no=t.p_no and left(p.pf_no, 1)='M' group by p.p_name order by count(*) desc limit 5",
		"select p.p_no, p.p_name, count(*) from perform p, ticket t where p.p_no=t.p_no and left(p.pf_no, 1)='O' group by p.p_name order by count(*) desc limit 5",
		"select p.p_no, p.p_name, count(*) from perform p, ticket t where p.p_no=t.p_no and left(p.pf_no, 1)='C' group by p.p_name order by count(*) desc limit 5"
	};
	String cap[] = "TICKETING,MONTH SCHEDULE,CHART,LOGIN,MYPAGE".split(",");
	JLabel login, img;
	static Timer re;
	CardLayout card;
	JPanel p;
	JPanel pop[] = new JPanel[3];
	String tits[] = "뮤지컬,오페라,콘서트".split(","), types[] = "M,O,C".split(",");
	int cardIdx=0;
	
	public Main() {
		super("메인", 750, 340);
		
		this.add(n = new JPanel(new FlowLayout(0)), "North");
		
		setNorth();
		
		
		this.add(c = new JPanel(card = new CardLayout()));
		for (int i = 0; i < pop.length; i++) {
			c.add(pop[i] = new JPanel(new BorderLayout()), i+"");
			pop[i].setBorder(new TitledBorder(new LineBorder(Color.black), "인기공연("+tits[i]+")", TitledBorder.LEFT, TitledBorder.TOP, new Font("", Font.BOLD, 20)));
			var c = new JPanel(new GridLayout(1, 0));
			var s = new JPanel(new FlowLayout(1, 5, 5));
			pop[i].add(c);
			pop[i].add(s, "South");
			
			try {
				var rs = DBManager.rs("select p.p_name, p.pf_no, count(*) from perform p, ticket t where p.p_no=t.p_no and left(p.pf_no, 1)='"+types[i]+"' group by p.p_name order by count(*) desc limit 5");
				int rank = 1;
				while (rs.next()) {
					var bord = new JPanel(new BorderLayout());
					var lblImg = new JLabel(img("./Datafiles/공연사진/"+rs.getString(2)+".jpg", 120, 120));
					bord.add(lbl(rank+"위", 2, 15), "North");
					bord.add(lblImg);
					bord.add(lbl(rs.getString(1), 0), "South");
					c.add(bord);
					setLine(lblImg, Color.black);
					setEmpty(bord, 10, 10, 10, 10);
					rank++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String[] page = "1,2,3".split(",");
			for (int j = 0; j < page.length; j++) {
				var l = new JLabel(page[j], 0);
				if (i==j) l.setForeground(Color.red);
				s.add(l);
			}
		}
		
		re = new Timer(1000, a->{
			cardIdx = (cardIdx == 3 ? 0 : cardIdx);
			card.show(c, cardIdx+"");
			cardIdx++;
		});
		re.setDelay(2000);
		re.start();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				re.restart();
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				re.stop();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setEmpty((JPanel)getContentPane(), 0, 10, 10, 10);
		setEmpty(c, 15, 0, 0, 0);
	}
	
	void setNorth() {
		for (int i = 0; i < cap.length; i++) {
			var l = lblP(cap[i], 0, 14);
			if (i==3) login = l;
			
			n.add(l);
			if (i==2) {
				n.add(Box.createHorizontalStrut(270));
				n.add(img= sz(new JLabel(), 30, 30));
			} else n.add(Box.createHorizontalStrut(5));
			
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					var name = ((JLabel)e.getSource()).getText();
					if (name.equals(cap[0])) {
						new Search().addWindowListener(new Before(main));
					} else if (name.equals(cap[1])) {
						new MonthSchedule().addWindowListener(new Before(main));
					} else if (name.equals(cap[2])) {
						new Chart().addWindowListener(new Before(main));
					} else if (name.equals(cap[3])) {
						new Login().addWindowListener(new Before(main));
					} else if (name.equals(cap[4])) {
						new MyPage().addWindowListener(new Before(main));
					} else if (name.equals("LOGOUT")) {
						uno ="";
						uname="";
						login.setText("LOGIN");
						img.setBorder(null);
						img.setVisible(false);
					}
				} 
			});
		}
	}
	
	
	
	public static void main(String[] args) {
		main.setVisible(true);
	}
	
	
}
