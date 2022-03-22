package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class Reserve extends BaseDialog {
	
	DefaultTableModel m = model("rno,no,출발지,도착지,도착시간,출발날짜,elpsed".split(","));
	JTable t =table(m);
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item = new JMenuItem("취소");
	
	public Reserve() {
		super("예매조회", 700, 650);
		
		pop.add(item);
		item.addActionListener(a->{
			int r = t.getSelectedRow();
			String date= t.getValueAt(r, 5).toString().substring(0, 10);
			if (LocalDate.parse(date).isBefore(LocalDate.now())) {
				new eMsg("취소 불가능한 일정입니다.");
				return;
			}
			
			LocalTime l = LocalTime.parse(t.getValueAt(r, 6)+"");
			int h = l.getHour();
			int point=0;
			
			
			if (h > 3) {
				point = 700;
			} else if (h > 2) {
				point = 500;
			} else if (h > 1) {
				point = 300;
			} else {
				point = 100;
			}
			
			DBManager.execute("delete from reservation where no = '"+t.getValueAt(r, 0)+"'");
			DBManager.execute("update user set point=point+"+point+" where uno ="+BaseFrame.uno);
			
			addRow(m, "select r.no, 1, a.name, b.name, right(date_add(s.date, interval s.elapsed_time HOUR_SECOND), 8), s.date, s.elapsed_time from reservation r, schedule s, loc a, loc b where s.no=r.schedule_no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no and r.user_no="+BaseFrame.uno+" order by s.date");
		});
		
		this.add(lbl("예매조회", 2, 25), "North");
		this.add(new JScrollPane(t));
		
		System.out.println("select r.no, a.name, b.name, right(date_add(s.date, interval s.elapsed_time HOUR_SECOND), 8), s.date from reservation r, schedule s, loc a, loc b where s.no=r.schedule_no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no and r.user_no="+BaseFrame.uno+" order by s.date");
		addRow(m, "select r.no, 1, a.name, b.name, right(date_add(s.date, interval s.elapsed_time HOUR_SECOND), 8), s.date, s.elapsed_time from reservation r, schedule s, loc a, loc b where s.no=r.schedule_no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no and r.user_no="+BaseFrame.uno+" order by s.date");
		
		int width[] = { 30, 100, 100, 100, 100, 100 };
		for (int i = 0; i < m.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(BaseFrame.dtcr);
			if (i >= 1) {
				t.getColumnModel().getColumn(i).setPreferredWidth(width[i-1]);
			}
		}
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
//		t.getColumnModel().getColumn(6).setMinWidth(0);
//		t.getColumnModel().getColumn(6).setMaxWidth(0);
		
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==3) {
					var c = t.columnAtPoint(e.getPoint());
					var r = t.rowAtPoint(e.getPoint());
					t.changeSelection(r, c, false, false);
					
					pop.show(t, e.getX(), e.getY());
				}
			}
		});
		
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}
	
	@Override
	void addRow(DefaultTableModel m, String sql) {
		m.setRowCount(0);
		try {
			var rs= DBManager.rs(sql);
			int k = 1;
			while (rs.next()) {
				Object row[] = new Object[m.getColumnCount()];
				for (int i = 0; i < row.length; i++) {
					row[i] = rs.getString(i+1);
				}
				row[1] = k++;
				m.addRow(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
