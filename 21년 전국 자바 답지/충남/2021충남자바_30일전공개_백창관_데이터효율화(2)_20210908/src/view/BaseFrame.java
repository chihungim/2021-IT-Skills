package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.stmt;

public class BaseFrame extends JFrame {
	
	JPanel c, w, e, s, n;
	static int sno;
	static String uno="", uname, sname;
	static int ubirth, uage;
	static LocalTime tnow = LocalTime.now();
	static Home h = new Home();
	
	static HashMap<Integer, ArrayList<Integer>> route = new HashMap<Integer, ArrayList<Integer>>();
	static HashMap<String, Integer> metro = new HashMap<String, Integer>(), lineM = new HashMap<String, Integer>();
	static HashMap<Integer, String> stations = new HashMap<Integer, String>(), map[] = new HashMap[31];
	static HashMap<String, String> lineN = new HashMap<String, String>();
	static int cost[][], cost2[][] = new int[276][276];
	
	//
	static ArrayList<String> metroNames = new ArrayList<String>();
	static ArrayList<String> stNames = new ArrayList<String>();
	static Object metroStInfos[][] = new Object[30+1][4];
	static int adjDim[][] = new int[275+1][275+1];
	static int lineDim[][] = new int[275+1][275+1];
	static int metroTimeDim[][][] = new int[30+1][100][300];
	int INF = 10000000;
	ResultSet rs;
	
	public BaseFrame(String tit, int w, int h) {
		super("�����Ʈ�� - " + tit);
		this.setSize(w, h);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(2);
	}
	
	void dataInit() {
		for (int i = 1; i <= 30; i++) {
			ArrayList<Integer> l = new ArrayList<Integer>();
			try {
				var rs = stmt.rs("select * from route where metro= "+i);
				while (rs.next()) {
					l.add(rs.getInt(2));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			route.put(i, l);
		}
		
		try {
			var rs= stmt.rs("select * from metro");
			while (rs.next()) {
				metro.put(rs.getString(2), rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			var rs = stmt.rs("select * from station");
			while (rs.next()) {
				stations.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 1; i <= 30; i++) {
			map[i] = new HashMap<Integer, String>();
			try {
				var rs= stmt.rs("select r.station, s.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and r.metro=  "+i);
				while (rs.next()) {
					map[i].put(rs.getInt(1), rs.getString(2));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			var rs= stmt.rs("select * from path");
			cost = new int[584][3];
			int i = 0;
			while (rs.next()) {
				cost[i][0] = rs.getInt(2);
				cost[i][1] = rs.getInt(3);
				cost[i][2] = rs.getInt(4);
				i++;
				
				cost2[rs.getInt(2)][rs.getInt(3)] = rs.getInt(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			var rs = stmt.rs("select p.departure, p.arrive, sub1.ho from `path` p, (select distinct r.station st, left(m.name,1) ho, m.name from metro m, route r where r.metro = m.serial) sub1, (select distinct r.station st, left(m.name, 1) ho from metro m, route r where r.metro = m.serial) sub2 where (sub1.st<>sub2.st and sub1.ho=sub2.ho) and (p.departure = sub1.st and p.arrive = sub2.st)");
			while(rs.next()) {
				lineM.put(rs.getInt(1)+","+rs.getInt(2), rs.getInt(3));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			var rs = stmt.rs("select p.departure, p.arrive, sub1.name from `path` p, (select distinct r.station st, left(m.name,1) ho, m.name from metro m, route r where r.metro = m.serial) sub1, (select distinct r.station st, left(m.name, 1) ho from metro m, route r where r.metro = m.serial) sub2 where (sub1.st<>sub2.st and sub1.ho=sub2.ho) and (p.departure = sub1.st and p.arrive = sub2.st)");
			while(rs.next()) {
				lineN.put(rs.getInt(1)+","+rs.getInt(2), rs.getString(3));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void dataInit2() throws SQLException {
		//��ü �뼱 ���
		//ArrayList: �뼱 ��ȣ <-> �뼱 ��
		rs = stmt.rs("select * from metro");
		metroNames.add("");
		while (rs.next()) {
			metroNames.add(rs.getString(2));
		}
		
		//��ü ������ ���
		//ArrayList: ������ ��ȣ <-> ������ ��
		rs = stmt.rs("select * from station");
		stNames.add("");
		while (rs.next()) {
			stNames.add(rs.getString(2));
		}
		
		//��ü �뼱�� ���������� 2���� �迭 
		//metroStInfos[�뼱�� ������ ArrayList(�뼱�� ������idx <-> ������ ��)][���� �����ð�][�� ���� �ð�][���� ����]
		for(int i=1; i<=30; i++) {
			metroStInfos[i][0] = new ArrayList<String>();
			((ArrayList)metroStInfos[i][0]).add("");
			
			rs = stmt.rs("SELECT s.name FROM route r,  station s where r.metro=" + i + " and r.station=s.serial");
			while (rs.next()) {
				((ArrayList)metroStInfos[i][0]).add(rs.getString(1));
			}
			
			rs = stmt.rs("select * from metro where serial=" + i);
			rs.next();
			metroStInfos[i][1]=rs.getString(3);
			metroStInfos[i][2]=rs.getString(4);
			metroStInfos[i][3]=rs.getString(5);
		}
		
		//������-������ ����뼱: lineDim[��� ������ ��ȣ][���� ������ ��ȣ]
		for(int i=1; i<=30; i++) {
			ArrayList al = (ArrayList)metroStInfos[i][0];
			for(int j=1; j<=al.size()-2; j++) {
				lineDim[stNames.indexOf(al.get(j))][stNames.indexOf(al.get(j+1))]=i;
			}
		}
		
		//���� �迭 for ���ͽ�Ʈ��
		//dijDim[��� ������ ��ȣ][���� ������ ��ȣ]
		for (int i = 1; i < 276; i++) {
			for (int j = i+1; j < 276; j++) {
				adjDim[i][j] = adjDim[j][i] = INF;
			}
		}
		
		rs = stmt.rs("SELECT * FROM metro.path");
		while (rs.next()) {
			adjDim[rs.getInt(2)][rs.getInt(3)] = rs.getInt(4)*5;
		}
		
		//��ü �뼱 ����ð�ǥ 3���� �迭: "00:00:00"�� �� ������ ȯ��
		//metroTimeDim[�뼱 ��ȣ][�뼱�� ������idx][���� �ð�]
		for(int i=1; i<=30; i++) {
			for(int j=1; j<100; j++) {
				ArrayList thisMetro = (ArrayList)metroStInfos[i][0];
				if(j>=thisMetro.size()) break;
				
				int sTime = LocalTime.parse((CharSequence) metroStInfos[i][1]).toSecondOfDay();
				int eTime = LocalTime.parse((CharSequence) metroStInfos[i][2]).toSecondOfDay();
				int timeGap = LocalTime.parse((CharSequence) metroStInfos[i][3]).toSecondOfDay();
				
				metroTimeDim[i][j][0] = j==1 ? sTime : metroTimeDim[i][j-1][0]+adjDim[stNames.indexOf(thisMetro.get(j-1))][stNames.indexOf(thisMetro.get(j))];
				for(int k=1; k<300; k++) {
					if(j>1 && metroTimeDim[i][j-1][k] == 0) break;
					if(metroTimeDim[i][j][k-1] + timeGap > eTime) break;
					
					metroTimeDim[i][j][k] = metroTimeDim[i][j][k-1] + timeGap;
				}
			}
		}
	}
	
	String tformat(LocalTime t, String format) {
		return DateTimeFormatter.ofPattern(format).format(t);
	}
	
	static <T extends JComponent> T sz(T c, int w, int h) {
		c.setPreferredSize(new Dimension(w, h));
		return c;
	}
	
	static void eMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "���", JOptionPane.ERROR_MESSAGE);
	}
	
	static void iMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "����", JOptionPane.INFORMATION_MESSAGE);
	}
	
	static JLabel lbl(String text, int alig) {
		JLabel l = new JLabel(text, alig);
		return l;
	}
	
	static JLabel lbl(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("���� ���", Font.BOLD, size));
		return l;
	}
	
	static JLabel lblP(String text, int alig, int size) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("���� ���", Font.PLAIN, size));
		return l;
	}
	
	static JLabel lbl(String text, int alig, int size, Color col) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("���� ���", Font.BOLD, size));
		return l;
	}
	
	static JLabel lblP(String text, int alig, int size, Color col) {
		JLabel l = new JLabel(text, alig);
		l.setFont(new Font("���� ���", Font.PLAIN, size));
		return l;
	}
	
	static JButton btn(String text, ActionListener a) {
		JButton b = new JButton(text);
		b.addActionListener(a);
		b.setBackground(new Color(50, 100, 255));
		b.setForeground(Color.white);
		return b;
	}
	
	static void setLine(JComponent c) {
		c.setBorder(new LineBorder(Color.black));
	}
	
	static void setLine(JComponent c, Color col) {
		c.setBorder(new LineBorder(col));
	}
	
	static void setEmpty(JComponent c, int t, int l, int b, int r) {
		c.setBorder(new EmptyBorder(t, l, b, r));
	}
	
	static ImageIcon img(String path, int w, int h) {
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage("./�����ڷ�/images/"+path).getScaledInstance(w, h, Image.SCALE_SMOOTH));
	}
	
	static int rei(Object obj) {
		if (obj.toString().isEmpty()) return 0;
		return Integer.parseInt(obj.toString());
	}
	
	class Before extends WindowAdapter {
		BaseFrame b;
		
		public Before(BaseFrame b) {
			this.b = b;
			b.setVisible(false);
		}
		
		@Override
		public void windowClosed(WindowEvent e) {
			if (b instanceof Home) {
//				h.re.start();
			}
			b.setVisible(true);
		}
	}
	
}
