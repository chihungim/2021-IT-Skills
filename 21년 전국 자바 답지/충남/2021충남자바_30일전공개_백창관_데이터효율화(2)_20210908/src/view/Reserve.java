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

import db.stmt;

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

	//2�����迭: �ش� ���� ���� �ð�ǥ
	int timeTable[][], eidx;
			
	public Reserve(ArrayList<Integer> path) {
		super("����", 500, 600);
		this.path= path;
		System.out.println(path.toString());
		
		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		sstation = stations.get(path.get(0));
		estation = stations.get(path.get(path.size()-1));
		
		n.add(lbl(sstation +"��"+estation, 0, 25, Color.blue));
		
		JPanel cn = new JPanel(new FlowLayout(0));
		c.add(cn, "North");
		cn.add(btn("�ð� ����", a->{
			new TimePicker(Reserve.this);
		}));
		
		cn.add(lblTime = new JLabel(date + " "+tformat(time, "HH:mm:ss")+" ž��"));
		cn.add(sz(new JLabel(), 500, 25));
		
		c.add(new JScrollPane(cc = new JPanel(new BorderLayout())));
		
		JPanel ccn = new JPanel(new BorderLayout());
		cc.add(ccc = new JPanel(new GridLayout(0, 1, 5, 5)));
		
		//�ش� ���� ���� �ð�ǥ �ۼ�
		makeTimeTable();
	
		cc.add(ccn, "North");
		ccn.add(lbl(path.size()+"���� ���� / "+trancnt+"ȸ ȯ��", 0, 15, Color.blue), "West");
		ccn.add(lblRead = lbl("�ڼ�������", 0, 15, Color.red), "East");
		
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
	
	private void makeTimeTable() {
		int lineIdx, innerStIdx;

		//2���� �迭: ���� �ð�ǥ timeTable[][]
		timeTable = new int[path.size()][300];
		eidx = path.size()-1;
		
		//[0][0]~[path.size()-2][2]: ������ID, [i][1]: ��Ʈ�� �뼱 ID, [i][1]: �� ���������� cost
		for(int i=0; i<=eidx-1; i++) {
			timeTable[i][0] = path.get(i);
			timeTable[i][1] = lineDim[path.get(i)][path.get(i+1)];
			if(i==0)
				timeTable[i][2] = 0;
			else {
				timeTable[i][2] = adjDim[timeTable[i-1][0]][timeTable[i][0]];
				if(timeTable[i-1][1]!=timeTable[i][1]) trancnt++;
			}
		}
		timeTable[eidx][0] = path.get(path.size()-1);
		timeTable[eidx][2] = adjDim[timeTable[eidx-1][0]][timeTable[eidx][0]];

		//[0][3]~[0][99]
		int col=3;
		lineIdx = timeTable[0][1];
		innerStIdx = ((ArrayList)metroStInfos[lineIdx][0]).indexOf(stNames.get(path.get(0)));
		for(int j=0; j<100; j++) {
			if(metroTimeDim[lineIdx][innerStIdx][j]==0) break;
			if(time.toSecondOfDay() <= metroTimeDim[lineIdx][innerStIdx][j]) {
				timeTable[0][col++] = metroTimeDim[lineIdx][innerStIdx][j];
			}
		}
		
		//[1][3]~[path.size()-2][99]
		for(int i=1; i<=eidx-1; i++) {
			lineIdx = timeTable[i][1];
			innerStIdx = ((ArrayList)metroStInfos[lineIdx][0]).indexOf(stNames.get(path.get(i)));

			for(int j=3; j<100; j++) {
				if(timeTable[i-1][j]==0) break;
				
				for(int k=0; k<300; k++) {
					if(metroTimeDim[lineIdx][innerStIdx][k]==0) break;

					if(timeTable[i-1][j] + timeTable[i][2] <= metroTimeDim[lineIdx][innerStIdx][k]) {
						timeTable[i][j] = metroTimeDim[lineIdx][innerStIdx][k];
						break;
					}
				}
			}
		}
		
		//���� �ð�: [������-1]�� ����ð�+[������]cost
		for(int j=3; j<300; j++) {
			if(timeTable[eidx-1][j]==0) break;
			
			timeTable[eidx][j] = timeTable[eidx-1][j] + timeTable[eidx][2];
		}
	}

//	public Reserve(ArrayList<Integer> path) {
//		super("����", 500, 600);
//		this.path= path;
//		System.out.println(this.path.toString());
//		
//		this.add(n = new JPanel(new BorderLayout()), "North");
//		this.add(c = new JPanel(new BorderLayout()));
//		sstation = stations.get(path.get(0));
//		estation = stations.get(path.get(path.size()-1));
//		
//		n.add(lbl(sstation +"��"+estation, 0, 25, Color.blue));
//		
//		JPanel cn = new JPanel(new FlowLayout(0));
//		c.add(cn, "North");
//		cn.add(btn("�ð� ����", a->{
//			new TimePicker(Reserve.this);
//		}));
//		
//		cn.add(lblTime = new JLabel(date + " "+tformat(time, "HH:mm:ss")+" ž��"));
//		cn.add(sz(new JLabel(), 500, 25));
//		
//		c.add(new JScrollPane(cc = new JPanel(new BorderLayout())));
//		
//		JPanel ccn = new JPanel(new BorderLayout());
//		cc.add(ccc = new JPanel(new GridLayout(0, 1, 5, 5)));
//		
//		
//		for (int i = 1; i < path.size(); i++) {
//			totDis += cost2[path.get(i-1)][path.get(i)];
//			ho.add(lineM.get(path.get(i - 1)+ ","+path.get(i)));
//			System.out.println(ho.get(i-1));
//		}
//		
//		int d = ho.get(0);
//		for (var v : ho) {
//			if (d != v) {
//				trancnt++;
//				d = v;
//			}
//		}
//		
////		trancnt = dist.size()-1;
//		for (int i = 1; i < path.size(); i++) {
//			TTWT = TTWT.plusSeconds(cost2[path.get(i-1)][path.get(i)]*5);
//		}
//		
//		cc.add(ccn, "North");
//		ccn.add(lbl(path.size()+"���� ���� / "+trancnt+"ȸ ȯ��", 0, 15, Color.blue), "West");
//		ccn.add(lblRead = lbl("�ڼ�������", 0, 15, Color.red), "East");
//		
//		lblRead.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				new WayPoint(Reserve.this).setVisible(true);
//			}
//		});
//		
//		sz(cn, 1, 50);
//		sz(n, 1, 80);
//		
//		setEmpty(((JPanel)getContentPane()), 10, 10, 10, 10);
//		
//		setTime();
//		
//		this.setVisible(true);
//	}
	
	void setTime() {
		lblTime.setText(date+" "+tformat(time, "HH:mm:ss")+" ž��");

		ccc.removeAll();
		for(int i=3; i<300; i++) {
			if(timeTable[eidx][i]==0) break;
			ccc.add(new ItemPanel(i));
		}
		repaint();
		revalidate();
	}
	
//	void setTime() {
//		lblTime.setText(date+" "+tformat(time, "HH:mm:ss")+" ž��");
//		
//		ccc.removeAll();
//		try {
//			System.out.println("select * from metro where name like '%"+lineN.get(path.get(0)+","+path.get(1)).substring(0, lineN.get(path.get(0)+","+path.get(1)).length()-2)+"%'");
//			var rs = DBManager.rs("select * from metro where name like '%"+lineN.get(path.get(0)+","+path.get(1)).substring(0, lineN.get(path.get(0)+","+path.get(1)).length()-2)+"%'");
//			if (rs.next()) {
//				start = LocalTime.parse(rs.getString(3));
//				end = LocalTime.parse(rs.getString(4));
//				inter = LocalTime.parse(rs.getString(5)).toSecondOfDay();
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		while (start.isBefore(end)) {
//			start =start.plusSeconds(inter);
//			if (start.isBefore(time)) continue;
//			if (bp) break;
//			ccc.add(new ItemPanel(start));
//		}
//		
//		bp = false;
//		repaint();
//		revalidate();
//		
//	}
	
	class ItemPanel extends JPanel {
		String mname;
//		int end;
		JLabel sstation, estation;
		JPanel ce, cw, c, cc;
//		LocalTime stime, totime;
		JLabel time;
		int startTime, endTime;
		
		public ItemPanel(int startIdx) {
			//���� ���۽ð�, ���� �ð�
			startTime = timeTable[0][startIdx];
			endTime = timeTable[eidx][startIdx];
			
			for(int i=0; i<=eidx; i++) {
				System.out.print(String.format("%02d:%02d", timeTable[i][startIdx]/3600, (timeTable[i][startIdx]%3600)/60) + " -> ");
			}
			System.out.println();
			
			setLayout(new BorderLayout(2, 2));
			
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
			
			cw.add(lbl(String.format("%02d:%02d",  startTime/3600, (startTime%3600)/60), 0, 35));
			this.add(btn("����", a->{
				if (uno.isEmpty()) {
					eMsg("�α��� �� ���� �����մϴ�.");
					return;
				}
				int yn = JOptionPane.showConfirmDialog(null, String.format("%02d:%02d",  startTime/3600, (startTime%3600)/60) + "�ð� ����ö�� �����ұ��?", "�޼���", JOptionPane.YES_NO_OPTION);
				if (yn == JOptionPane.YES_OPTION) {
					new Purchase(ItemPanel.this, Reserve.this).addWindowListener(new Before(Reserve.this));
				}
			}), "East");
			
			//�ҿ�ð� ǥ��
			int tot = endTime-startTime;
			System.out.println(tot + ":" + endTime + "," + startTime);
			cc.add(time = lbl((tot/60) + "�� " + (tot%60) + "�� �ҿ�", 0));
			ce.add(lbl(String.format("%02d:%02d", endTime/3600, (endTime%3600)/60), 4, 35));

			time.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
			time.setForeground(Color.blue);
			
			cc.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
			
			setEmpty(cc, 20, 20, 0, 20);
			setEmpty(c, 5, 5, 5, 5);
		}
//		class ItemPanel extends JPanel {
//			String mname;
//			int end;
//			JLabel sstation, estation;
//			JPanel ce, cw, c, cc;
//			LocalTime stime, totime;
//			JLabel time;
//			
//			public ItemPanel(LocalTime stime) {
//				setLayout(new BorderLayout(2, 2));
//				this.stime= stime;
//				
//				this.add(c = new JPanel(new BorderLayout()));
//				c.add(cc = new JPanel(new BorderLayout()));
//				c.add(ce = new JPanel(new BorderLayout()), "East");
//				c.add(cw = new JPanel(new BorderLayout()), "West");
//				
//				cw.add(sstation= lbl(Reserve.this.sstation, 2, 15), "South");
//				ce.add(estation = lbl(Reserve.this.estation, 4, 15), "South");
//				
//				sstation.setForeground(Color.lightGray);
//				estation.setForeground(Color.lightGray);
//				
//				c.setBackground(Color.white);
//				
//				cc.setOpaque(false);
//				ce.setOpaque(false);
//				cw.setOpaque(false);
//				
//				cw.add(lbl(stime+"", 0, 35));
//				this.add(btn("����", a->{
//					if (uno.isEmpty()) {
//						eMsg("�α��� �� ���� �����մϴ�.");
//						return;
//					}
//					int yn = JOptionPane.showConfirmDialog(null, tformat(this.stime, "HH:mm")+"�ð� ����ö�� �����ұ��?", "�޼���", JOptionPane.YES_NO_OPTION);
//					if (yn == JOptionPane.YES_OPTION) {
//						new Purchase(ItemPanel.this, Reserve.this).addWindowListener(new Before(Reserve.this));
//					}
//				}), "East");
//				
//				if(trancnt > 0) {
//					int from = ho.get(0);
//					int to;
//					LocalTime baseT;
//					for (int i = 1; i < ho.size(); i++) {
//						to = ho.get(i);
//						stime = stime.plusSeconds(cost2[path.get(i - 1)][path.get(i)] * 5);
//						if(from != to) {
//							baseT = stime;
//							mname = lineN.get(path.get(i)+","+path.get(i + 1));
//							
//							ArrayList<Integer> lines = new ArrayList<Integer>();
//							try {
//								ResultSet rs = DBManager.rs("select r.station, m.name from route r, metro m where r.metro = m.serial and name like '%"+mname+"%'");
//								while(rs.next()) {
//									lines.add(rs.getInt(1));
//								}
//							} catch (SQLException e) {
//								e.printStackTrace();
//							}
//							end = lines.indexOf(path.get(i));
//							
//							for (int j = end; j > 1; j--) {
//								baseT = baseT.minusSeconds(cost2[lines.get(j)][lines.get(j - 1)] * 5);
//							}
//							
//							String s = null, inter = null;
//							
//							try {
//								ResultSet rs = DBManager.rs("select * from metro where name like '%"+mname+"%'");
//								if(rs.next()) {
//									s = rs.getString(3);
//									inter = rs.getString(5);
//								}
//							} catch (SQLException e) {
//								e.printStackTrace();
//							}
//							
//							LocalTime start = LocalTime.parse(s);
//							
//							int iv = LocalTime.parse(inter).toSecondOfDay();
//							while(!start.isAfter(baseT)) {
//								start = start.plusSeconds(iv);
//							}
//							
//							LocalTime dif = start.minusSeconds(baseT.toSecondOfDay());
//							totime = TTWT.plusSeconds(dif.toSecondOfDay());
//							
//							if(totime.getHour() > 0) cc.add(time = lbl(totime.getHour()+"�ð� "+totime.getMinute()+"�� "+totime.getSecond()+"�� �ҿ�", 0));
//							else cc.add(time = lbl(totime.getMinute()+"�� "+totime.getSecond()+"�� �ҿ�", 0));
//							
//							ce.add(lbl(tformat(stime.plusSeconds(totime.toSecondOfDay()), "HH:mm"), 4, 35));
//							
//							if(stime.plusSeconds(totime.toSecondOfDay()).isAfter(Reserve.this.end)) {
//								bp = true;
//							}
//							from = to;
//						}
//					}
//				}else {
//					cc.add(time = lbl(TTWT.getMinute()+"�� "+TTWT.getSecond()+"�� �ҿ�", 0));
//					ce.add(lbl(tformat(stime.plusSeconds(TTWT.toSecondOfDay()), "HH:mm"), 4, 35));
//					if(stime.plusSeconds(TTWT.toSecondOfDay()).isAfter(Reserve.this.end)) {
//						bp = true;
//					}
//				}
//				time.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
//				time.setForeground(Color.blue);
//				
//				cc.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));
//				
//				setEmpty(cc, 20, 20, 0, 20);
//				setEmpty(c, 5, 5, 5, 5);
//				
//			}
	}
	
	public static void main(String[] args) {
		new Map();
	}
	
}
