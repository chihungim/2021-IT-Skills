package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import db.DBManager;

public class Main extends BaseFrame {
	
	HolderField txt[] = new HolderField[3];
	JLabel img[] = new JLabel[5];
	JPopupMenu pop[] = new JPopupMenu[3];
	JPanel list1 = new JPanel(new GridLayout(0, 1)), list2 = new JPanel(new GridLayout(0, 1)), date;
	int sel;
	String name;
	LocalDate now = LocalDate.now();
	
	
	public Main() {
		super("버스예매", 1080, 600);
		
		this.add(c = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(new ImageIcon("./지급파일/images/main.jpg").getImage(), 0, 0, 1200, 600, null);
			}
		});
		this.add(s = pnl(null), "South");
		
		drawView();
		setPopup();
		setDatePopup();
		setImgPopup();
		
		this.setVisible(true);
	}
	
	void setImgPopup() {
		
		for (var s : "상세설명,예매".split(",")) {
			var item = new JMenuItem(s);
			pop[2].add(item);
			
			item.addActionListener(a->{
				if (a.getActionCommand().equals("상세설명")) {
					new Detail(rei(DBManager.getOne("select * from location where name ='"+name+"'")));
				} else {
					sel = 1;
					pop[0].show(txt[1], txt[1].getX()-200, txt[1].getY());
					for (var items : list1.getComponents()) {
						
						var btn = (JButton) items;
						if (btn.getText().equals(name)) {
							btn.doClick();
							break;
						}
					}
				}
			});
		}
		
		for (int i = 0; i < img.length; i++) {
			img[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					var l = ((JLabel)e.getSource());
					if (e.getButton()==3) {
						name = l.getName();
						System.out.println(name);
						pop[2].show(l, e.getX(), e.getY());
					}
				}
			});
		}
	}
	
	void setDatePopup() {
		var grid = new JPanel(new GridLayout(1, 0));
		pop[1].add(grid);
		
		JButton[] up = new JButton[3], down = new JButton[3];
		JLabel[] time = new JLabel[3];
		
		for (int i = 0; i < time.length; i++) {
			String asd[] = { now.getYear() +"", now.getMonthValue()+"", now.getDayOfMonth() +""};
			JPanel tmp = new JPanel(new GridLayout(0, 1));
			tmp.add(up[i] = new JButton("▲"));
			tmp.add(time[i] = lbl(asd[i], 0));
			tmp.add(down[i] = new JButton("▼"));
			int k = i;

			up[i].addActionListener(a->{
				if (a.getSource().equals(up[0])) {
					now = now.plusYears(1);
				} else if (a.getSource().equals(up[1])) {
					now = now.plusMonths(1);
				} else if (a.getSource().equals(up[2])) {
					now = now.plusDays(1);
				}
				String qwq[] = { now.getYear() +"", now.getMonthValue()+"", now.getDayOfMonth() +""};
				for (int j = 0; j < time.length; j++) {
					time[j].setText(qwq[j]);
				}
			});
			down[i].addActionListener(a->{
				if (a.getSource().equals(down[0])) {
					now = now.plusYears(-1);
				} else if (a.getSource().equals(down[1])) {
					now = now.plusMonths(-1);
				} else if (a.getSource().equals(down[2])) {
					now = now.plusDays(-1);
				}
				String qwq[] = { now.getYear() +"", now.getMonthValue()+"", now.getDayOfMonth() +""};
				for (int j = 0; j < time.length; j++) {
					time[j].setText(qwq[j]);
				}
			});
			grid.add(tmp);
		}
		
		txt[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton()==3) {
					now = LocalDate.now();
					String qwq[] = { now.getYear() +"", now.getMonthValue()+"", now.getDayOfMonth() +""};
					for (int j = 0; j < time.length; j++) {
						time[j].setText(qwq[j]);
					}
					pop[1].show(txt[2], e.getX(), e.getY());
				}
			}
		});
		
		pop[1].addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				txt[2].setText(now.toString());
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	void setPopup() {
		for (int i = 0; i < pop.length; i++) {
			pop[i] = new JPopupMenu();
		}
		var grid= sz(new JPanel(new GridLayout()), 260, 400);
		pop[0].add(grid);
		grid.add(new JScrollPane(list1));
		grid.add(new JScrollPane(list2));
		
		try {
			var rs = DBManager.rs("select * from location");
			int i = 1;
			while (rs.next()) {
				var b = new JButton(rs.getString(2));
				list1.add(b);
				int k = i;
				b.addActionListener(a->{
					list2.removeAll();
					try {
						var rs1 =DBManager.rs("select * from location2 where location_no = "+k);
						while (rs1.next()) {
							var l = new JButton(rs1.getString(2));
							l.setName(b.getText() + " "+l.getText());
							list2.add(l);
							l.addActionListener(c->{
								txt[sel].setText(((JButton)c.getSource()).getName());
							});
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pop[0].repaint();
					pop[0].revalidate();
				});
				
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < 2; i++) {
			int k = i;
			System.out.println(i);
			txt[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton()==3) {
						sel = k;
						pop[0].show(txt[k], e.getX(), e.getY());
					}
				}
			});
		}
		
	}
	
	void drawView() {
		var cn = new JPanel(new FlowLayout(2));
		cn.setOpaque(false);
		
		c.add(cn, "North");
		cn.add(theme());
		for (var s : "계정,예매,로그아웃".split(",")) {
			cn.add(btn(s, a->{
				if (a.getActionCommand().equals("계정")) {
					String input = JOptionPane.showInputDialog("비밀번호를 입력해주세요.");
					if (input == null) return;
					if (input.equals(upw)) new Account();
				} else if (a.getActionCommand().equals("예매")) {
					new Reserve();
				} else {
					uno="";
					this.dispose();
				}
			}));
		}
		
		var p = new JPanel(new GridBagLayout());
		c.add(p);
		
		var cc = pnl(new BorderLayout());
		p.add(cc);
		
		cc.add(lbl("예매", 2, 25), "North");
		var ins = new JPanel();
		cc.add(ins);
		
		ins.add(txt[0] = sz(new HolderField(15, "출발지"), 150, 30));
		ins.add(btn("<html>←<br>→", a->{
			if (txt[0].getText().isEmpty() || txt[1].getText().isEmpty()) return;
			var tmp = txt[0].getText();
			txt[0].setText(txt[1].getText());
			txt[1].setText(tmp);
		}));
		ins.add(txt[1] = sz(new HolderField(15, "도착지"), 150, 30));
		ins.add(txt[2] = sz(new HolderField(15, "날짜"), 150, 30));
		ins.add(sz(btn("조회", a->{
			String dep = txt[0].getText(), arrv = txt[1].getText(), date = txt[2].getText();
			if (dep.isEmpty() || arrv.isEmpty() || date.isEmpty()) {
				new eMsg("출발지, 도착지, 날짜 중 공란이 있습니다.");
				return;
			}
			
			System.out.println("SELECT * FROM loc a, loc b, schedule s where s.departure_location2_no=a.no and s.arrival_location2_no=b.no and a.name = '"+dep+"' and b.name='"+arrv+"' and s.date = '"+date+"'");
			if (DBManager.getOne("SELECT * FROM loc a, loc b, schedule s where s.departure_location2_no=a.no and s.arrival_location2_no=b.no and a.name = '"+dep+"' and b.name='"+arrv+"' and s.date = '"+date+"'").isEmpty()) {
				new eMsg("예매 가능한 일정이 없습니다.");
				return;
			}
			
			new Reservation("SELECT a.name, b.name, right(date,8), right(date_add(date, interval elapsed_time HOUR_SECOND),8), s.no, date, elapsed_time FROM loc a, loc b, schedule s where s.departure_location2_no=a.no and s.arrival_location2_no=b.no and a.name = '"+dep+"' and b.name='"+arrv+"' and s.date = '"+date+"'");
			
		}), 120, 30));
		
		addComp(s, lbl("추천 여행지", 2, 20), 30, 30, 200, 30);
		try {
			var rs = DBManager.rs("SELECT name, img, title FROM location l, recommend r, recommend_info ri where ri.recommend_no =r.no and r.location_no=l.no group by location_no order by recommend_no, title");
			int i = 0;
			while (rs.next()) {
				JPanel bord;
				addComp(s, bord = new JPanel(new BorderLayout()), 30 + (i * 200), 80 + ((i % 2) * 20), 150, 150);
				System.out.println("./지급파일/recommend/"+hash.get(rs.getString(1)) + "/"+ rs.getString(3)+".jpg");
				bord.add(img[i] = new JLabel(img("./지급파일/images/recommend/"+hash.get(rs.getString(1)) + "/"+ rs.getString(3)+".jpg", 300, 150)));
				img[i].setName(rs.getString(1));
				titled[i].setTitle(rs.getString(1));
				bord.setBorder(titled[i]);
				bord.setOpaque(false);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ins.setOpaque(false);
		p.setOpaque(false);
		setEmpty(cc, 5, 5, 5, 5);
		sz(s, 1200, 300);
	}
	
	public static void main(String[] args) {
		uno = "1";
		uname = "심임송";
		upw = "QzCp4vb=&yzFzN8";
		new Main();
	}
	
}
