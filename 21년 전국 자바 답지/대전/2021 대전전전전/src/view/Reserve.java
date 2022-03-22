package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class Reserve extends BaseFrame {
	
	DefaultTableModel m = model("pno,날짜,여유좌석,d".split(","));
	JTable t = new JTable(m);
	JLabel lbl;
	String date;
	DecimalFormat dec = new DecimalFormat("#,##0");
	
	public Reserve(String pno) {
		super("예매", 600, 300);
		
		try {
			System.out.println("select * from perform where p_no="+pno);
			var rs = DBManager.rs("select * from perform where p_no="+pno);
			if (rs.next()) {
				for (int i = 0; i < pinfo.length; i++) {
					pinfo[i] = rs.getString(i+1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		date = pinfo[6];
		
		this.add(n = new JPanel(new FlowLayout(0)), "North");
		this.add(c = new JPanel(new BorderLayout(20, 20)));
		this.add(e = new JPanel(new BorderLayout()), "East");
		this.add(s = new JPanel(new FlowLayout(2)), "South");
		
		n.add(lbl(pinfo[2], 2, 20));
		
		c.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 5, 5, 5)));
		c.add(new JLabel(img("./Datafiles/공연사진/"+pinfo[1]+".jpg", 120, 150)), "West");
		c.add(lbl = new JLabel("<html><left>장소 : "+pinfo[3]+"<br><br>출연 : "+pinfo[5]+"<br><br>가격 : "+dec.format(rei(pinfo[4]))+"<br><br>날짜 : "+date));
		addRow(m, "select p.p_no, date_format(p.p_date, '%m. %d.'), 60 - count(*), p.p_date from perform p, ticket t where p.p_no=t.p_no and p.p_name like '%"+pinfo[2]+"' group by p.p_no");
		
		var scr = new JScrollPane(t);
		e.add(scr);
		setLine(scr, Color.black);
		
		s.add(sz(btn("예매하기", a->{
			if (t.getSelectedRow() == -1) return;
			
			if (uno.isEmpty()) {
				int yesno = JOptionPane.showConfirmDialog(null, "회원만이 가능한 서비스 입니다.\n로그인 하시겠습니까?", "로그인", JOptionPane.YES_NO_OPTION);
				if (yesno==JOptionPane.YES_OPTION) {
					new Login().addWindowListener(new Before(this));
					return;
				}
			}
			
			if (LocalDate.parse(date).isBefore(LocalDate.now())) {
				eMsg("종료된 공연입니다.");
				return;
			}
			
			pdate= date;
			new Stage();
			
		}), 150, 30));
		
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
		t.getColumnModel().getColumn(3).setMinWidth(0);
		t.getColumnModel().getColumn(3).setMaxWidth(0);
		sz(e, 200, 1);
		
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					var rs = DBManager.rs("select * from perform where p_no="+t.getValueAt(t.getSelectedRow(), 0));
					if (rs.next()) {
						for (int i = 0; i < pinfo.length; i++) {
							pinfo[i] = rs.getString(i+1);
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				date = t.getValueAt(t.getSelectedRow(), 3).toString();
				System.out.println(date);
				lbl.setText("<html><left>장소 : "+pinfo[3]+"<br><br>출연 : "+pinfo[5]+"<br><br>가격 : "+dec.format(rei(pinfo[4]))+"<br><br>날짜 : "+date);
				repaint();
				revalidate();
			}
		});
		
		setEmpty(e, 0, 20, 0, 0);
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		uno = "1";
		uname = "강찬석";
		
		new Reserve("77");
	}
	
}
