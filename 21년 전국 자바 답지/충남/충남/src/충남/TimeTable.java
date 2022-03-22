package 충남;

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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

public class TimeTable extends BaseFrame {
	JScrollPane scr, scr2;
	JComboBox<String> mcombo = new JComboBox<String>(), tcombo = new JComboBox<String>();
	ArrayList<String> mlist = new ArrayList<String>(), tlist = new ArrayList<String>();
	String format = "HH:mm:ss";
	JLabel jl;
	JPanel jp;
	
	public TimeTable() {
		super("열차 시간표", 1000, 700);
		
		try {
			data();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		add(w = new JPanel(new FlowLayout(0)),"West");
		add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 1, 5, 5))));
		add(e = new JPanel(new BorderLayout()), "East");
		
		w.add(siz(mcombo, 230, 25));
		w.add(siz(tcombo, 230, 25));
		w.add(jl = siz(label("<html><b>1호선 - 경원선</b><br>소요 시간 : 00:35:45", 2, 12, Color.BLACK), 230, 35));
		
		for (int i = 1; i < metroNames.size(); i++) {
			mcombo.addItem(metroNames.get(i));
		}
		
		mcombo.addItemListener(a->{
			jl.setText("<html><b>"+mcombo.getSelectedItem()+"</b><br>소요 시간 : 00:35:45");
			setCenter();
			e.removeAll();
		});
		
		mcombo.setSelectedIndex(0);
		
		tcombo.addItemListener(a->{
			setCenter();
			e.removeAll();
		});
		
		LocalTime start, end, inter;
		start = LocalTime.parse((CharSequence) metroStinfo[mcombo.getSelectedIndex()+1][1]);
		end = LocalTime.parse((CharSequence) metroStinfo[mcombo.getSelectedIndex()+1][2]);
		inter = LocalTime.parse((CharSequence) metroStinfo[mcombo.getSelectedIndex()+1][3]);
		
		LocalTime time = start;
		
		while(end.isAfter(time)) {
			tcombo.addItem(tformat(time, format));
			time = time.plusMinutes(inter.getMinute());
		}
		
		setCenter();
		
		siz(w, 250, 150);
		siz(e, 350, 0);
		setVisible(true);
	}

	private void setCenter() {
		ArrayList<item> p = new ArrayList<item>();
		ArrayList<String> arr = (ArrayList<String>)metroStinfo[mcombo.getSelectedIndex()+1][0];
		c.removeAll();
		
		LocalTime time = LocalTime.parse(tcombo.getSelectedItem()+"");
		LocalTime a = LocalTime.of(0, 0, 0);
		
		for (int i = 0; i < arr.size() - 1; i++) {
			item item = new item(stNames.indexOf(arr.get(i+1)), arr.get(i+1));
			c.add(siz(item, 0, 50));
			item.add(label((i+1)+". "+arr.get(i+1),2, 13),"West");
			if(i == 0) {
				item.add(label(tcombo.getSelectedItem()+"도착",4, 13),"East");
			}else {
				time = time.plusSeconds(adj[stNames.indexOf(arr.get(i))][stNames.indexOf(arr.get(i+1))]);
				a = a.plusSeconds(adj[stNames.indexOf(arr.get(i))][stNames.indexOf(arr.get(i+1))]);
				item.add(label(tformat(time, format)+"도착", 0, 13),"East");
			}
			item.setBackground(Color.WHITE);
			p.add(item);
			item.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e1) {
					for (int j2 = 0; j2 < p.size(); j2++) {
						p.get(j2).onClick(false);
					}
					item item = (item) e1.getSource();
					item.setBackground(Color.ORANGE);
					item.onClick(true);
					e.removeAll();
					e.add(label("<html><b>"+item.name+"에서<br>환승 가능한 노선", 2, 15),"North");
					e.add(scr2 = new JScrollPane(jp = new JPanel(new GridLayout(0, 1, 0, 5))));
					for (int j2 = 1; j2 < 31; j2++) {
						if(((ArrayList)metroStinfo[j2][0]).contains(item.name) && j2 != mcombo.getSelectedIndex() + 1) {
							item item2 = new item(stNames.indexOf(arr.get(j2)), arr.get(j2));
							item2.add(label(metroNames.get(j2), 2));
							item2.setBackground(Color.WHITE);
							jp.add(item2);
						}
					}
					if(jp.getComponentCount() < 10) {
						for (int j = jp.getComponentCount(); j < 10; j++) {
							jp.add(new JLabel());
						}
					}
					repaint();
					revalidate();
				}
			});
		}
		
//		try {
//			ResultSet rs = stmt.executeQuery("select r.serial, r.station, s.name, r.metro from route r, station s where r.station = s.serial and metro = "+(mcombo.getSelectedIndex() + 1));
//			int i = 1;
//			LocalTime time = LocalTime.parse(tcombo.getSelectedItem()+"");
//			LocalTime a = LocalTime.of(0, 0, 0);
//			while(rs.next()) {
//				item item = new item(rs.getInt(2), rs.getString(3));
//				c.add(siz(item, 0, 50));
//				item.add(label(i+". "+rs.getString(3), 0, 13),"West");
//				if(i == 0) {
//					item.add(label(tcombo.getSelectedItem()+"도착", 0, 13),"East");
//				}else {
//					time = time.plusSeconds(adj[rs.getInt(1)][rs.getInt(1)+1]);
//					a = a.plusSeconds(adj[rs.getInt(1)][rs.getInt(1)+1]);
//					item.add(label(tformat(time, format)+"도착", 0, 13),"East");
//				}
//				i++;
//				p.add(item);
//			}
//			jl.setText("<html><b>"+mcombo+"<br>소요 시간 : "+tcombo);
//			
//			for (int j = 0; j < p.size(); j++) {
//				p.get(i).addMouseListener(new MouseAdapter() {
//					@Override
//					public void mouseClicked(MouseEvent e1) {
//						for (int j2 = 0; j2 < p.size(); j2++) {
//							p.get(j2).setBackground(Color.WHITE);
//						}
//						item item = (item) e1.getSource();
//						item.setBackground(Color.ORANGE);
//						item.onClick();
//						e.removeAll();
//						e.add(scr2 = new JScrollPane(jp = new JPanel(new GridLayout(0, 1, 0, 5))));
//						for (int j2 = 1; j2 < 31; j2++) {
//							if(((ArrayList)metroStinfo[j2][0]).contains(item.name) && j2 != mcombo.getSelectedIndex() + 2) {
//								System.out.println(metroNames.get(j2));
//							}
//						}
//					}
//				});
//				
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
	
	class item extends JPanel {
		
		String name;
		
		public item(int sta, String name) {
			setLayout(new BorderLayout());
			this.name = name;
		}
		
		void onClick(boolean chk) {
			if(chk) {
				setBackground(Color.ORANGE);
			}else {
				setBackground(Color.WHITE);
			}
		}
	}
	
	public static void main(String[] args) {
		new TimeTable();
	}

}
