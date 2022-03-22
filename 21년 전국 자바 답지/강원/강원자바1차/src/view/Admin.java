package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class Admin extends BaseFrame {
	JTabbedPane pane = new JTabbedPane();
	String cap[] = "사용자 관리,추천 여행지 관리,일정 관리,예매 관리".split(",");
	
	JPanel p[] = new JPanel[4];
	
	public Admin() {
		super("관리자", 1100, 700);
		
		this.add(pane);
		pane.setTabPlacement(2);
		for (int i = 0; i < cap.length; i++) {
			pane.add(p[i] = pnl(new BorderLayout()));
			pane.setTitleAt(i, cap[i]);
		}
		UserManage m;
		p[0].add(m=new UserManage());
		m.setOpaque(false);
		
//		Recommend r;
//		p[1].add(r=new Recommend());
//		r.setOpaque(false);
		
		Schedule s;
		p[2].add(s=new Schedule());
		s.setOpaque(false);
		
		Chart ch;
		p[3].add(ch=new Chart());
		ch.setOpaque(false);
		
		
		pane.add("테마", null);
		pane.add("로그아웃", null);
		
		pane.addChangeListener(e->{
			if (pane.getSelectedIndex()==4) {
				col = col.equals(Color.white) ? Color.DARK_GRAY : Color.white;
				fcol = fcol.equals(Color.black) ? Color.white : Color.black;
				
				for (var l : titled) {
					l.setTitleColor(fcol);
				}
				
				this.getContentPane().setBackground(col);
				this.repaint();
				this.revalidate();
				pane.setBackgroundAt(4, (col.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE);
				pane.setForegroundAt(4, (fcol.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE);
			}
			if (pane.getSelectedIndex()==5) {
				dispose();
			}
		});
		pane.setBackgroundAt(4, (col.equals(Color.WHITE)) ? Color.DARK_GRAY : Color.WHITE);
		pane.setForegroundAt(4, (fcol.equals(Color.WHITE)) ? Color.BLACK : Color.WHITE);
		
		this.setVisible(true);
	}
	
	class UserManage extends JPanel {
		DefaultTableModel m = new DefaultTableModel(null, "순번,아이디,비밀번호,성명,이메일,포인트,예매수".split(","));
		JTable t = table(m);
		HolderField txt = new HolderField(15, "성명");
		JPopupMenu pop = new JPopupMenu();
		JMenuItem item = new JMenuItem("예매 조회");
		
		boolean isChange;
		
		JPanel n =new JPanel(new BorderLayout()), ne = new JPanel(), s = new JPanel(new FlowLayout(2));
		
		public UserManage() {
			super(new BorderLayout());
			
			this.add(n, "North");
			n.add(ne, "East");
			this.add(new JScrollPane(t));
			this.add(s, "South");
			
			n.add(lbl("사용자 관리", 2, 20), "West");
			ne.add(sz(txt, 150, 30));
			ne.add(btn("사용자조회", a->search()));
			
			for (int i = 0; i < t.getColumnCount(); i++) {
				t.getColumnModel().getColumn(i).setCellRenderer(BaseFrame.dtcr);
			}
			
			for (var bcap : "저장,삭제".split(",")) {
				s.add(btn(bcap, a->{
					if (a.getActionCommand().equals("저장")) {
						for (int i = 0; i < t.getRowCount(); i++) {
							int r= i;
							try {
								var rs = DBManager.rs("select * from user where no ='"+r);
								while (rs.next()) {
									for (int c = 0; c < t.getColumnCount(); c++) {
										if (t.getValueAt(r, c) != rs.getString(c+1)) {
											new iMsg("수정내용을 저장 완료하였습니다.");
											DBManager.execute("update user set id = '"+t.getValueAt(r, 1)+"', pwd ='"+t.getValueAt(r, 2)+"', name= '"+t.getValueAt(r, 3)+"', email='"+t.getValueAt(r, 4)+"', point = '"+t.getValueAt(r, 5)+"'");
											return;
										}
									}
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} else {
						new eMsg("삭제를 완료하였습니다.");
						DBManager.execute("delete from user where no='"+t.getValueAt(t.getSelectedRow(), 0)+"'");
						search();
					}
				}));
			}
			
			pop.add(item);
			item.addActionListener(a->{
				new UserInfo(t.getValueAt(t.getSelectedRow(), 0)+"");
			});
			t.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton()==3) {
						int r = t.rowAtPoint(e.getPoint()), c = t.columnAtPoint(e.getPoint());
						t.changeSelection(r, c, false, false);
						pop.show(t, e.getX(), e.getY());
					}
				};
			});
			
			n.setOpaque(false);
			ne.setOpaque(false);
			s.setOpaque(false);
			search();
			
			setEmpty(this, 20, 20, 20, 20);
		}
		
		void search() {
			addRow(m, "select u.no, u.id, u.pwd, u.name, u.email, u.point, count(user_no) from user u, reservation r where r.user_no=u.no and name like '%"+txt.getText()+"%' group by user_no order by u.no");
		}
		

	}
	
	class Recommend extends JPanel {
		JPanel rP, iP;
		JPanel rc, ic;
		
		int srnum;
		
		public Recommend() {
			super(new GridLayout(0, 1));
			
			this.add(rP = pnl(new BorderLayout(5,5)));
			rP.add(lbl("추천 여행지 관리", 2, 15), "North");
			rP.add(rc = pnl(new FlowLayout(0)));
			rP.setOpaque(false);
			rc.setOpaque(false);
			setEmpty(rc, 50, 0, 50, 0);
			
			try {
				var rs = DBManager.rs("SELECT * FROM recommend_info ri inner join recommend r on ri.recommend_no = r.no inner join location l on r.location_no = l.no group by name order by r.no");
				int i =0;
				while (rs.next()) {
					JLabel img = sz(new JLabel(img("./지급파일/images/recommend/"+hash.get(rs.getString("name")) + "/" + rs.getString(2) +".jpg", 170, 170)), 170, 170);
					titled[i].setTitle(rs.getString("name"));
					img.setName(rs.getString("name"));
					final int num = rs.getInt(1);
					img.setBorder(titled[i]);
					rc.add(img);
					
					var a= DBManager.getOne("select * from location l, recommend r where l.no = r.location_no and name like '%"
											+ titled[i].getTitle() + "%'");
					
					final JPopupMenu menu = new JPopupMenu();
					JPopupMenu pop = new JPopupMenu();
					img.setComponentPopupMenu(pop);
					for (var bcap : "이미지 설정,설명설정".split(",")) {
						JMenuItem item = new JMenuItem(bcap);
						pop.add(item);
					}
					
					menu.add(new PosItem(i, img, pop, a));
					img.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount()==2) {
								menu.show(img, e.getX(), e.getY());
							}
						}
					});
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.add(iP = pnl(new BorderLayout()));
			ic = pnl(new FlowLayout(0));
			
			var n = pnl(new FlowLayout(0));
			
			iP.add(n, "North");
			iP.add(ic);
			
			n.add(lbl("설명 설정", 2, 15));
			n.add(btn("추가", a->{
				JFileChooser file = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & png Images", "jpg,png".split(","));
				file.setFileFilter(filter);
				file.setMultiSelectionEnabled(false);
				if (file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					JLabel img = new JLabel(img(file.getSelectedFile().toString(), 170, 170));
					ic.add(img);
					setLine(img, Color.black);
					revalidate();
					repaint();
					
					try {
						ImageIO.write(ImageIO.read(new File(file.getSelectedFile().toString())), ".jpg", new File("./datafiles/지급파일/"+file.getSelectedFile().getName()+".jpg"));
//						DBManager.execute("insert into );
//						이거 기준 뭐임?
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					var title = file.getSelectedFile().getName();
					System.out.println(title);
					
					JPopupMenu pop = new JPopupMenu();
					for (var s : "삭제,설명 텍스트 입력".split(",")) {
						JMenuItem item = new JMenuItem(s);
						pop.add(item);
						
						item.addActionListener(b->{
							// 뭘지우고 뭘추가해??
						});
					}
				}
			}));
			
			setEmpty(this, 20, 20, 20, 20);
			
		}
		
		class PosItem extends JPanel {
			
			JScrollPane pane;

			HashMap<String, String> map = new HashMap<>();
			
			public PosItem(int idx, JLabel img, JPopupMenu pop, String rNum) {
				super(new BorderLayout());

				var c = new JPanel(new GridLayout(0, 1));

				add(pane = new JScrollPane(c));

				try {
					var rs = DBManager.rs("select * from location");
					while (rs.next()) {
						map.put(rs.getString(1), rs.getString(2));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (String v : map.keySet()) {
					JButton b = new JButton(map.get(v));
					b.setName(v);
					c.add(b);
					b.addActionListener(a -> {

						DBManager.execute("update recommend set location_no = " + ((JButton) (a.getSource())).getName()
								+ " where no = " + rNum);
						img.setIcon(new ImageIcon(Toolkit.getDefaultToolkit()
								.getImage("지급파일/images/recommend/" + hash.get(a.getActionCommand()) + "/1.jpg")
								.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
						System.out.println("지급파일/images/recommend/" + hash.get(
								a.getActionCommand() + "/" + hash.get(((JButton) (a.getSource())).getName()) + ".jpg"));
						titled[idx].setTitle(a.getActionCommand());

						pop.setVisible(false);
					});
				}

			}
		}
		
	}
	
	class Schedule extends JPanel {
		
		DefaultTableModel m = model("sno,순번,출발지,도착지,출발날짜,이용시간".split(","));
		JTable t = table(m);
		JPopupMenu pop = new JPopupMenu();
		JPanel list1 = new JPanel(new GridLayout(0, 1)), list2 = new JPanel(new GridLayout(0, 1));
		
		public Schedule() {
			super(new BorderLayout());
			
			this.add(lbl("일정 관리", 2, 20), "North");
			this.add(new JScrollPane(t));
			this.add(s = pnl(new FlowLayout(2)), "South");
			
			try {
				var rs = DBManager.rs("select s.no, 1, a.name, b.name, s.date, s.elapsed_time from reservation r, schedule s, loc a, loc b where r.schedule_no=s.no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no order by s.no");
				int k = 1;
				while (rs.next()) {
					Object row[] = new Object[m.getColumnCount()];
					row[0] = rs.getInt(1);
					row[1] = k++;
					for (int i = 2; i < row.length; i++) {
						row[i] = rs.getString(i+1);
					}
					m.addRow(row);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t.getColumnModel().getColumn(0).setMinWidth(0);
			t.getColumnModel().getColumn(0).setMaxWidth(0);
			
			t.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(t.getSelectedColumn());
					if (e.getButton()==3 && (t.getSelectedColumn()==2 || t.getSelectedColumn()==3)) {
						int r = t.rowAtPoint(e.getPoint()), c = t.columnAtPoint(e.getPoint());
						t.changeSelection(r, c, false, false);
						pop.show(t, e.getX(), e.getY());
					}
				}
			});
			
			s.add(btn("저장", a->{
				new iMsg("수정내용을 저장 완료하였습니다.");
				for (int i = 0; i < t.getRowCount(); i++) {
					int dep = rei(DBManager.getOne("select no from loc where name ='"+t.getValueAt(i, 2)+"'"));
					int arrv = rei(DBManager.getOne("select no from loc where name ='"+t.getValueAt(i, 3)+"'"));
					DBManager.execute("update schedule set departure_location2_no = " + dep + ", arrival_location2_no = " + arrv
							+ " where no = " + rei(t.getValueAt(i, 0)));
				}
			}));
			s.add(btn("삭제", a->{
				new iMsg("삭제를 완료하였습니다.");
				DBManager.execute("delete from reservation where r_no="+t.getValueAt(t.getSelectedRow(), 0));
			}));
			
			setPopup();
			
			setEmpty(this, 20, 20, 20, 20);
		}
		
		void setPopup() {
			var grid= sz(new JPanel(new GridLayout()), 260, 400);
			pop.add(grid);
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
									t.setValueAt(b.getText() + " "+l.getText(), t.getSelectedRow(), t.getSelectedColumn());
								});
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pop.repaint();
						pop.revalidate();
					});
					
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	class Chart extends JPanel {
		JPanel n, c, s, ch;
		JComboBox<String> combo = new JComboBox<String>("2차원 영역형,방사형".split(","));
		DefaultTableModel m = model("순번,예약자,출발지,도착지,출발날짜,도착시간".split(","));
		JTable t = table(m);
		
		public Chart() {
			super(new BorderLayout());
			
			this.add(n = new JPanel(new FlowLayout(0)), "North");
			this.add(c = new JPanel(new BorderLayout()));
			this.add(s = new JPanel(new BorderLayout()), "South");
			
			n.add(lbl("예매 관리", 0, 20));
			n.add(combo);
			n.add(Box.createHorizontalStrut(100));
			n.add(lbl("<가장 예매가 많은 일정 TOP 6>", 0, 11));
			
			var ss = new JPanel(new FlowLayout(2));
			s.add(ss, "South");
			
			s.add(new JScrollPane(t));
			ss.add(btn("저장", a->{
				
			}));
			ss.add(btn("삭제", a->{
				
			}));
			
			BaseDialog.sz(s, 1, 210);
			
			try {
				var rs = DBManager.rs("SELECT r.no, u.name, a.name, b.name, s.date, right(date_add(s.date, interval s.elapsed_time HOUR_SECOND), 8) FROM reservation r, schedule s, user u, loc a, loc as b where r.user_no=u.no and s.departure_location2_no=a.no and s.arrival_location2_no=b.no and r.schedule_no = s.no order by r.no");
				while (rs.next()) {
					Object row[] = new Object[6];
					for (int i = 0; i < row.length; i++) {
						row[i] = rs.getString(i+1);
					}
					m.addRow(row);
				} 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			combo.addActionListener(a->{
				if (combo.getSelectedIndex()==0) {
					c.remove(ch);
					areaChart();
				} else {
					c.remove(ch);
					radialChart();
				}
			});
			
			areaChart();
			
		}
		
		void areaChart() {
			ch = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					Graphics2D g2 = (Graphics2D)g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
					g2.drawLine(50, 20, 50, 255);
					for (int i = 0; i < 5; i++) {
						g2.drawLine(50, 20 + (i * 59), 600, 20 + (i * 59));
					}
					
					Polygon p = new Polygon();
					try {
						var rs = DBManager.rs("select *, count(schedule_no) as cnt from reservation group by schedule_no order by cnt desc, schedule_no limit 6");
						int i = 0, max = 0;
						while (rs.next()) {
							if (i==0) max = rs.getInt(4);
							if (i<5) g2.drawString(10 - (i*2)+"", 30, 25 + (i * 59));
							g2.drawString(rs.getString(3), 45 + (i*109), 280);
							p.addPoint(50 + (i*110), 20 + ((max - rs.getInt(4))* 29));
							i++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					g2.setColor(BaseFrame.blue);
					
					p.addPoint(600, 255);
					p.addPoint(50, 255);
					
					g2.fillPolygon(p);
					
				}
			};
			c.add(ch);
			ch.setOpaque(true);
			ch.setBackground(col);
			repaint();
			revalidate();
		}
		
		void radialChart() {
			ch = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					Graphics2D g2 = (Graphics2D)g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
					int po[][] = new int[6][2];
					int num[] = new int[6];
					Polygon p[] = { new Polygon(), new Polygon(), new Polygon(), new Polygon(), new Polygon(), new Polygon() };
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < p.length; j++) {
							p[i].addPoint((int)(380+(100-i*20) * Math.cos(j * Math.PI / 3 + (Math.PI / 6))), (int) (150 + (100-i*20) * Math.sin(j * Math.PI / 3 + (Math.PI / 6))));
						}
						g2.drawPolygon(p[i]);
					}
					
					for (int i = 0; i < p.length; i++) {
						po[i][0] = (int) (370 + 110 * Math.cos(i * Math.PI /3 - (Math.PI / 2)));
						po[i][1] = (int) (150 + 110 * Math.sin(i * Math.PI /3 - (Math.PI/2)));
					}
					
					int basex = 0, basey=0;
					try {
						var rs = DBManager.rs("select *, count(schedule_no) as cnt from reservation group by schedule_no order by cnt desc, schedule_no limit 6");
						int i = 0, max = 0;
						while (rs.next()) {
							if (i==0) {
								max = rs.getInt(4);
								basex = (int) (po[0][0] / max);
								basey = (int) (po[0][1] / max);
							}
							if (i < 5) g2.drawString(max-2*i+"", 370, 55 + (i*20));
							g2.drawString(rs.getString(3), po[i][0], po[i][1]);
							num[i] = rs.getInt(4);
							i++;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					g2.setColor(BaseFrame.blue);
					Polygon poly = new Polygon();
					for (int i = 0; i < num.length; i++) {
						poly.addPoint((int)(380+(100-(num[0]-num[i])/2.0*20) * Math.cos(i * Math.PI/3 - (Math.PI/2))), (int)(150+(100-(num[0]-num[i])/2.0*20)*Math.sin(i * Math.PI/3 - (Math.PI/2))));
					}
					g2.drawPolygon(poly);
				}
			};
			c.add(ch);
			ch.setOpaque(true);
			ch.setBackground(col);
			repaint();
			revalidate();
		}
		
	}
	
	public static void main(String[] args) {
		new Admin();
	}
	
}
