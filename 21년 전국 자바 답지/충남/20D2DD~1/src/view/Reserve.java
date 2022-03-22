package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import db.DBManager;

public class Reserve extends BaseFrame {
	
	ArrayList<Integer> path, ho = new ArrayList<Integer>(), dist = new ArrayList<Integer>();
	String sstation, estation;
	LocalDate date = LocalDate.now();
	LocalTime time = LocalTime.now().plusMinutes(30);
	JLabel lblTime, lblRead;
	JPanel cc, ccc;
	int totDis, trancnt, inter;
	LocalTime TTWT = LocalTime.of(0, 0, 0), start, end;
	boolean bp;
	
	public Reserve(ArrayList<Integer> path) {
		super("예매", 500, 600);
		this.path= path;
		
		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		sstation = stations.get(path.get(0));
		estation = stations.get(path.get(path.size()-1));
		
		n.add(lbl(sstation +"→"+estation, 0, 25, Color.blue));
		
		JPanel cn = new JPanel(new FlowLayout(0));
		c.add(cn, "North");
		cn.add(btn("시간 변경", a->{
			new TimePicker(Reserve.this);
		}));
		
		cn.add(lblTime = new JLabel(date + " "+tformat(time, "HH:mm:ss")+" 탑승"));
		cn.add(sz(new JLabel(), 500, 25));
		
		c.add(new JScrollPane(cc = new JPanel(new BorderLayout())));
		
		JPanel ccn = new JPanel(new BorderLayout());
		cc.add(ccc = new JPanel(new GridLayout(0, 1, 5, 5)));
		
		for (int i = 1; i < path.size(); i++) {
			totDis += cost2[path.get(i-1)][path.get(i)];
			ho.add(lineM.get(path.get(i - 1)+ ","+path.get(i)));
			System.out.println(ho.get(i-1));
		}
		
		int d = ho.get(0);
		for (var v : ho) {
			if (d != v) {
				trancnt++;
				d = v;
			}
		}
		for (int i = 1; i < path.size(); i++) {
			TTWT = TTWT.plusSeconds(cost2[path.get(i-1)][path.get(i)]*5);
		}
		
		cc.add(ccn, "North");
		ccn.add(lbl(path.size()+"개역 정차 / "+trancnt+"회 환승", 0, 15, Color.blue), "West");
		ccn.add(lblRead = lbl("자세히보기", 0, 15, Color.red), "East");
		
		lblRead.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new WayPoint(Reserve.this).setVisible(true);
			}
		});
		
		sz(cn, 1, 50);
		sz(n, 1, 80);
		
		setEmpty(((JPanel)getContentPane()), 10, 10, 10, 10);
		
		setTime();
		
		this.setVisible(true);
	}
	
	void setTime() {
		lblTime.setText(date+" "+tformat(time, "HH:mm:ss")+" 탑승");
		
		ccc.removeAll();
		try {
			System.out.println("select * from metro where name like '%"+lineN.get(path.get(0)+","+path.get(1)).substring(0, lineN.get(path.get(0)+","+path.get(1)).length()-2)+"%'");
			var rs = DBManager.rs("select * from metro where name like '%"+lineN.get(path.get(0)+","+path.get(1)).substring(0, lineN.get(path.get(0)+","+path.get(1)).length()-2)+"%'");
			if (rs.next()) {
				start = LocalTime.parse(rs.getString(3));
				end = LocalTime.parse(rs.getString(4));
				inter = LocalTime.parse(rs.getString(5)).toSecondOfDay();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (start.isBefore(end)) {
			start =start.plusSeconds(inter);
			if (start.isBefore(time)) continue;
			if (bp) break;
			ccc.add(new ItemPanel(start));
		}
		 
		bp = false;
		repaint();
		revalidate();
		
	}
	
	class ItemPanel extends JPanel {
		String mname;
		int end;
		JLabel sstation, estation;
		JPanel ce, cw, c, cc;
		LocalTime stime, totime;
		JLabel time;
		
		public ItemPanel(LocalTime stime) {
			setLayout(new BorderLayout(2, 2));
			this.stime= stime;
			
			this.add(c = new JPanel(new BorderLayout()));
			c.add(cc = new JPanel(new BorderLayout()));
			c.add(ce = new JPanel(new BorderLayout()), "East");
			c.add(cw = new JPanel(new BorderLayout()), "West");
			
			cw.add(sstation= lbl(Reserve.this.sstation, 2, 15), "South");
			ce.add(estation = lbl(Reserve.this.estation, 4, 15), "South");
			
			sstation.setForeground(Color.lightGray);
			estation.setForeground(Color.lightGray);
			
			c.setBackground(Color.white);
			
			cc.setOpaque(false);
			ce.setOpaque(false);
			cw.setOpaque(false);
			
			cw.add(lbl(stime+"", 0, 35));
			this.add(btn("선택", a->{
				if (uno.isEmpty()) {
					eMsg("로그인 후 예매 가능합니다.");
					return;
				}
				int yn = JOptionPane.showConfirmDialog(null, tformat(this.stime, "HH:mm")+"시간 지하철을 예매할까요?", "메세지", JOptionPane.YES_NO_OPTION);
				if (yn == JOptionPane.YES_OPTION) {
					new Purchase(ItemPanel.this, Reserve.this).addWindowListener(new Before(Reserve.this));
				}
			}), "East");
			
			if(trancnt > 0) {
				int from = ho.get(0);
				int to;
				LocalTime baseT;
				for (int i = 1; i < ho.size(); i++) {
					to = ho.get(i);
					stime = stime.plusSeconds(cost2[path.get(i - 1)][path.get(i)] * 5);
					if(from != to) {
						baseT = stime;
						mname = lineN.get(path.get(i)+","+path.get(i + 1));
						
						ArrayList<Integer> lines = new ArrayList<Integer>();
						try {
							ResultSet rs = DBManager.rs("select r.station, m.name from route r, metro m where r.metro = m.serial and name like '%"+mname+"%'");
							while(rs.next()) {
								lines.add(rs.getInt(1));
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						end = lines.indexOf(path.get(i));
						
						for (int j = end; j > 1; j--) {
							baseT = baseT.minusSeconds(cost2[lines.get(j)][lines.get(j - 1)] * 5);
						}
						
						String s = null, inter = null;
						
						try {
							ResultSet rs = DBManager.rs("select * from metro where name like '%"+mname+"%'");
							if(rs.next()) {
								s = rs.getString(3);
								inter = rs.getString(5);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						LocalTime start = LocalTime.parse(s);
						
						int iv = LocalTime.parse(inter).toSecondOfDay();
						while(!start.isAfter(baseT)) {
							start = start.plusSeconds(iv);
						}
						
						LocalTime dif = start.minusSeconds(baseT.toSecondOfDay());
						totime = TTWT.plusSeconds(dif.toSecondOfDay());
						
						if(totime.getHour() > 0) cc.add(time = lbl(totime.getHour()+"시간 "+totime.getMinute()+"분 "+totime.getSecond()+"초 소요", 0));
						else cc.add(time = lbl(totime.getMinute()+"분 "+totime.getSecond()+"초 소요", 0));
						
						ce.add(lbl(tformat(stime.plusSeconds(totime.toSecondOfDay()), "HH:mm"), 4, 35));
						
						if(stime.plusSeconds(totime.toSecondOfDay()).isAfter(Reserve.this.end)) {
							bp = true;
						}
						from = to;
					}
				}
			}else {
				cc.add(time = lbl(TTWT.getMinute()+"분 "+TTWT.getSecond()+"초 소요", 0));
				ce.add(lbl(tformat(stime.plusSeconds(TTWT.toSecondOfDay()), "HH:mm"), 4, 35));
				if(stime.plusSeconds(TTWT.toSecondOfDay()).isAfter(Reserve.this.end)) {
					bp = true;
				}
			}
			time.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
			time.setForeground(Color.blue);
			
			cc.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
			
			setEmpty(cc, 20, 20, 0, 20);
			setEmpty(c, 5, 5, 5, 5);
			
		}
	}
	
	public static void main(String[] args) {
		new Map();
	}
	
}
