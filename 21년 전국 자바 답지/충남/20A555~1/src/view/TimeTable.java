package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import db.DBManager;

public class TimeTable extends BaseFrame {
	JScrollPane scr, scr2;
	JComboBox<String> mcombo = new JComboBox<String>(), tcombo = new JComboBox<String>();
	ArrayList<String> mlist = new ArrayList<String>(), tlist = new ArrayList<String>();
	String format = "HH:mm:ss";
	JLabel jl;
	JPanel escp;
	
	public TimeTable() {
		super("열차 시간표", 1000, 700);
		dataInit();
		
		this.add(w = new JPanel(new FlowLayout(0)), "West");
		this.add(scr = new JScrollPane(c = new JPanel(new GridLayout(0, 1, 5, 5))));
		this.add(e = new JPanel(new BorderLayout()),"East");
		
		scr.setBorder(BorderFactory.createEmptyBorder());
		setWest();
		setCenter();
		
		sz(w, 250, 150);
		sz(e, 350, 1);
		setEmpty(c, 10, 10, 10, 10);
		
		setVisible(true);
	}

	void setCenter() {
		ArrayList<ItemPanel> pa = new ArrayList<ItemPanel>();
		c.removeAll();
		try {
			ResultSet rs = DBManager.rs("Select r.serial, r.station, s.name, r.metro from route r, station s where r.station = s.serial and metro = "+(mcombo.getSelectedIndex() + 1));
			int i = 1;
			LocalTime time = LocalTime.parse(tcombo.getSelectedItem()+ "");
			LocalTime atime = LocalTime.of(0, 0, 0);
			while(rs.next()) {
				ItemPanel p = new ItemPanel(rs.getInt(2), rs.getString(3));
				c.add(sz(p, 1, 50));
				p.add(lbl(i + ". "+rs.getString(3), 0, 13), "West");
				setEmpty(p, 0, 10, 0, 10);
				if(i == 1) {
					p.add(lbl(tcombo.getSelectedItem()+"도착", 0, 13), "East");
				}else {
					time =time.plusSeconds(cost[rs.getInt(1) - 2][2] * 5);
					atime = atime.plusSeconds(cost[rs.getInt(1) - 2][2] * 5);
					p.add(lbl(tformat(time, format)+"도착", 0, 13),"East");
				}
				i++;
				pa.add(p);
			}
			jl.setText("<html><b>"+mcombo.getSelectedItem()+"</b><br>소요 시간:"+tformat(atime, format));
			for (int j = 0; j < pa.size(); j++) {
				pa.get(j).addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent a) {
						for (int j2 = 0; j2 < pa.size(); j2++) {
							pa.get(j2).onClicked(false);
						}
						ItemPanel p = ((ItemPanel)a.getSource());
						p.onClicked(true);
						e.removeAll();
						e.add(scr2 = new JScrollPane(escp = new JPanel(new GridLayout(0, 1, 5, 5))),"Center");
						scr2.setBorder(BorderFactory.createEmptyBorder());
						setEmpty(e, 10, 10, 10, 10);
						try {
							ResultSet rs = DBManager.rs("Select r.metro, r.station, s.name, m.name from route r, station s, metro m where s.serial = r.station and m.serial = r.metro and s.name like '%"+map[mcombo.getSelectedIndex() + 1].get(p.serial)+"%' and r.metro <> "+(mcombo.getSelectedIndex() + 1));
							while(rs.next()) {
								e.add(lbl("<html>"+p.name+"에서<br>환승 가능한 노선", 2, 15), "North");
								ItemPanel pp = new ItemPanel(rs.getInt(1), rs.getString(4));
								pp.add(lbl(rs.getString(4), 2, 13));
								setEmpty(pp, 0, 10, 0, 10);
								escp.add(sz(pp, 1, 50));
							}
							if(escp.getComponentCount()<10) {
								for (int k = escp.getComponentCount(); k < 10; k++) {
									escp.add(sz(new JPanel(), 1, 50));
								}
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						repaint();
						revalidate();
					}
				});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		repaint();
		revalidate();
	}
	
	class ItemPanel extends JPanel {

		boolean chk;
		int serial;
		String name;

		public ItemPanel(int serial, String name) {
			this.serial = serial;
			this.name = name;
			this.setLayout(new BorderLayout());
		}

		void onClicked(boolean chk) {
			this.chk = chk;

			repaint();
			revalidate();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(chk ? Color.orange : Color.white);
			RoundRectangle2D rec = new RoundRectangle2D.Float(1.5f, 1.5f, getWidth() - 3, getHeight() - 3, 15, 15);
			g2.fill(rec);
		}
	}

	private void setWest() {
		w.add(sz(mcombo, 230, 25));
		w.add(sz(tcombo, 230, 25));
		w.add(jl = sz(lbl("<html><b>1호선 - 경원선</b><br>소요 시간 : 00:35:45", 2, 12, Color.BLACK), 230, 35));
		try {
			ResultSet rs = DBManager.rs("select name from metro");
			while(rs.next()) {
				mcombo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mcombo.addItemListener(a->{
			jl.setText("<html><b>"+mcombo.getSelectedItem()+"</b><br>소요 시간 : 00:35:45");
			setCenter();
			e.removeAll();
		});
		mcombo.setSelectedIndex(0);
		jl.setText("<html><b>"+mcombo.getSelectedItem()+"</b><br>소요 시간 : 00:35:45");
		
		tcombo.addItemListener(a->{
			setCenter();
			e.removeAll();
		});
		
		LocalTime start, end, inter;
		start = LocalTime.parse(DBManager.getOne("select start from metro where serial = "+(mcombo.getSelectedIndex() + 1)));
		end = LocalTime.parse(DBManager.getOne("select end from metro where serial = "+(mcombo.getSelectedIndex() + 1)));
		inter = LocalTime.parse(DBManager.getOne("select `interval` from metro where serial = "+(mcombo.getSelectedIndex() + 1)));
		
		LocalTime time = start;
		while(end.isAfter(time)) {
			tcombo.addItem(tformat(time, format));
			time = time.plusMinutes(inter.getMinute());
		}
	}
	
	public static void main(String[] args) {
		new TimeTable();
	}

}

