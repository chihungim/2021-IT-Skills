package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class UserInfo extends BaseDialog {
	
	String no;
	DefaultTableModel m = model("no,출발지,도착지,도착시간,출발날짜".split(","));
	JTable t = table(m);
	JPanel chart;
	
	public UserInfo(String no) {
		super("예매 정보", 800, 600);
		this.no = no;
		
		this.add(lbl("사용자 예매 정보", 2, 20), "North");
		setChart();
		this.add(sz(new JScrollPane(t), 1, 200), "South"); 
		try {
			var rs = DBManager.rs("select a.name, b.name, right(date_add(s.date, interval s.elapsed_time HOUR_SECOND), 8), s.date from reservation r, schedule s, loc a, loc b where r.user_no="+BaseFrame.uno+" and r.schedule_no=s.no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no order by s.date");
			int i = 1;
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				row[0] = i++;
				for (int j = 1; j < row.length; j++) {
					row[j] = rs.getString(j);
				}
				m.addRow(row);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < m.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(BaseFrame.dtcr);
		}
		setEmpty((JPanel)getContentPane(), 20, 20, 20, 20);
		
		this.setVisible(true);
	}
	
	void setChart() {
		add(chart = new JPanel() {
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
//                g2d.drawLine(20, 30, 20, 250);
//                g2d.drawLine(20, 40, 700, 40);
//                g2d.drawLine(20, 145, 700, 145);
//                g2d.drawLine(20, 250, 700, 250);
                g2.drawLine(30, 30, 30, 250);
                for (int i = 0; i < 3; i++) {
					g2.drawLine(30, 40 + (100 * i) + (5 * i), 700, 40 + (100 * i) + (5*i));
				}
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ArrayList<Integer> values = new ArrayList<>();
                try {
                	System.out.println("select month(s.date) as m, count(*) from reservation r, schedule s, loc a, loc b where r.user_no=1 and r.schedule_no=s.no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no group by m order by m");
                    var rs = DBManager.rs("select month(s.date) as m, count(*) from reservation r, schedule s, loc a, loc b where r.user_no=1 and r.schedule_no=s.no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no group by m order by m");
                    while (rs.next()) {
                        values.add(rs.getInt(2));
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                int xx[] = new int[3];
                int yy[] = new int[3];
                int max = Collections.max(values);
                try {
                	var rs = DBManager.rs("select month(s.date) as m, count(*) from reservation r, schedule s, loc a, loc b where r.user_no=1 and r.schedule_no=s.no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no group by m order by m limit 3");
                    int idx = 0;
                    while (rs.next()) {
                    	
                    	g2.setColor(BaseFrame.blue);
                    	int height = (int) ((Math.ceil((rs.getDouble(2) / max) * 10) / 10) * 210);
                    	if (idx==0) {
                    		g2.fillRect(165 + (idx * 200), 250-20, 100, 20);
                    		g2.setColor(Color.black);
                    		g2.fillOval(210 + (idx * 200), 250-20-5, 10, 10);
                    		xx[idx] = 210 + (idx * 200);
                    		yy[idx] = 250-20;
                    	} else {
                    		g2.fillRect(165 + (idx * 200), 250-height, 100, height);
                    		g2.setColor(Color.black);
                    		g2.fillOval(210 + (idx * 200), 250-height-5, 10, 10);
                    		xx[idx] = 210 + (idx * 200);
                    		yy[idx] = 250-height;
                    	}
                    	
                    	
                    	var wid = g2.getFontMetrics(g2.getFont()).stringWidth(rs.getString(2));
                    	g2.drawString(rs.getString(2), 20-wid, 250 - (100*idx) - (5*idx));
                    	g2.drawString(String.format("%d월", rs.getInt(1)), 200 + (idx * 200), 270);
                    	idx++;
//                    	g2.setColor(BaseFrame.blue);
                    	
//                        g2d.setColor(new Color(0, 123, 255));
//                        int height = (int) ((Math.ceil((rs.getDouble(2) / max) * 10) / 10) * 210);
//                        g2d.fillRect(165 + (idx * 200), 250 - height, 100, height);
//                        g2d.setColor(Color.BLACK);
//                        g2d.fillOval(210 + (idx * 200), 250 - height - 5, 10, 10);
//                        g2d.setStroke(new BasicStroke(2f));
//                        xx[idx] = 210 + (idx * 200);
//                        yy[idx] = 250 - height;
//                        g2d.drawString(String.format("%d월", rs.getInt(1)), 200 + (idx * 200), 270);
//                        System.out.println(xx[idx]+", "+yy[idx]);
//                        idx++;
                        
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                g2.drawPolyline(xx, yy, 3);

            };
        });
    }
	
	public static void main(String[] args) {
		BaseFrame.uno = "1";
		new UserInfo("1");
	}
	
}
