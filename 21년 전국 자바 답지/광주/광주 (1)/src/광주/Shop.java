package 광주;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import 광주.Basedialog.be;

public class Shop extends Basedialog {
	JLabel back = label("뒤로가기", 2), list = label("주문표 (0)", 4), like = label("", 4, 20);
	JTabbedPane tab = new JTabbedPane();
	JPanel ta = new JPanel(new FlowLayout(1));
	menu menu;
	review review;
	JPanel n = new JPanel(new BorderLayout()), in, nn = new JPanel(new BorderLayout()), ns = new JPanel(new FlowLayout(2));
	int sno;
	String info;
	
	public Shop(String name, int sno) {
		super(name, 1200, 700);
		
		this.sno = sno;
		
		info = getone("SELECT concat(format(DELIVERYFEE,'#,##0'), '원 배달 수수료 / ', concat(minute(min(cooktime)),' ~ ', minute(max(cooktime)),'분'), ' / 평점 ',format(ifnull(avg(rate),0.0),1) ) FROM menu m, seller s left join review r on r.seller = s.no where m.seller = s.no and s.no = "+sno+" group by s.name order by s.no asc");
		
		add(n = new JPanel(new BorderLayout()) {
			Font f = new Font("맑은 고딕",Font.BOLD, 30);
			Font f1 = new Font("맑은 고딕",Font.PLAIN, 15);
			Font f2 = new Font("맑은 고딕",Font.PLAIN, 12);
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				g2.drawImage(img("배경/"+sno+".png").getImage(), 0, 0, 1200, 350, this);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, 1200, 350);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				
				g2.setColor(Color.WHITE);
				g2.setFont(f);
				g2.drawString(name, 20, 250);
				g2.setFont(f1);
				g2.drawString(info, 20, 280);
				g2.setFont(f2);
				g2.drawString(getone("select about from seller where no = "+sno), 20, 310);
			}
		},"North");
		
		n.add(ns,"South");
		n.add(nn,"North");
		
		nn.add(back,"West");
		nn.add(list,"East");
		
		ns.add(like);
		
		emp(n, 15, 15, 50, 15);
		
		like.setText(getone("select * from favorite where user = "+NO+" and seller = "+sno).equals("")?"♡":"♥");
		
		like.setForeground(Color.RED);
		
		back.setForeground(Color.WHITE);
		list.setForeground(Color.WHITE);
		
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				order.clear();
				dispose();
			}
		});
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Shoplist(Shop.this).addWindowListener(new be(Shop.this));
			}
		});
		
		like.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					//"♡":"♥"
				if(like.getText().equals("♡")) {
					like.setText("♥");
					execute("insert into favorite values(0,"+sno+","+NO+")");
				}else {
					like.setText("♡");
					execute("delete from favorite where seller = "+sno+" and user = "+NO);
				}
			}
		});
		
		add(tab);
		tab.addTab("메뉴", new JScrollPane(menu = new menu()));
		tab.addTab("리뷰", ta);
		
		size(n, 1200, 350);
		
		int hei = 0;
		try {
			ResultSet rs = stmt.executeQuery("select title, content, rate from review where user = "+NO+" and seller = "+sno);
			while(rs.next()) {
				ta.add(size(new review(rs.getString(1), rs.getString(2), rs.getInt(3)), 1150, 150));
				hei += 155;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		size(ta, 1150, hei);
		
		ta.setBackground(Color.WHITE);
		
		nn.setOpaque(false);
		ns.setOpaque(false);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				order.clear();
			}
		});
		
		setVisible(true);
	}
	
	class menu extends JPanel {
		JPanel c = new JPanel(new FlowLayout());
		ArrayList<item> arr = new ArrayList<item>();
		int h = 0;
		
		public menu() {
			add(c);
			
			c.setBackground(Color.WHITE);
			
			try {
				ResultSet rs = stmt.executeQuery("select no,name from type where seller = "+sno);
				while(rs.next()) {
					arr.add(new item(rs.getInt(1),rs.getString(2)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			arr.forEach(a->{
				a.init();
				c.add(a);
				h += a.height;
			});
			
			setBackground(Color.WHITE);
			Basedialog.size(c, 1150, h + 140);
		}
		
		class item extends JPanel {
			int height = 0;
			JPanel c;
			int t;
			
			public item(int no, String name) {
				t = no;
				setLayout(new BorderLayout());
				
				JLabel jl;
				add(jl = label(name, 2, 20),"North");
				jl.setOpaque(false);
				
				add(c = new JPanel(new FlowLayout(0)));
				setOpaque(false);
				c.setOpaque(false);
			}
			
			void init() {
				int cnt = 0;
				try {
					ResultSet rs = stmt.executeQuery("select no, name, description, price, minute(cooktime) from menu where type = "+t+" and seller = "+sno);
					while(rs.next()) {
						JPanel temp = new JPanel(new BorderLayout()), in = new JPanel(new BorderLayout());
						JLabel img;
						JTextArea ta;
						
						temp.setBorder(new LineBorder(Color.GREEN.darker(),2));
						Basedialog.size(temp, 350, 120);
						
						temp.add(in);
						temp.add(img = new JLabel(img("메뉴/"+rs.getInt(1)+".png", 120, 120)),"East");
						
						in.add(label(rs.getString(2), 2, 15),"North");
						in.add(ta = new JTextArea(rs.getString(3)));
						ta.setLineWrap(true);
						ta.setBorder(null);
						in.add(label(format.format(rs.getInt(4))+"원 / "+rs.getString(5)+"분 소요", 2),"South");
						
						int no = rs.getInt(1);
						temp.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								if(e.getClickCount() == 2) {
									new menuOption(no, Shop.this);
								}
							}
						});
						
						temp.setOpaque(false);
						ta.setOpaque(false);
						in.setOpaque(false);
						
						c.add(temp);
						cnt++;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				height = ((cnt / 3) * 120) + 140;
				Basedialog.size(c, 1150, height);
			}
		}
	}
	
	class review extends JPanel {
		JPanel n = new JPanel(new BorderLayout());
		String str = "";
		JTextArea ta;
		JLabel jl;
		
		public review(String name, String expla, int rate) {
			setLayout(new BorderLayout());
			add(n,"North");
			
			n.add(labelP("<html>"+name+"/<b>"+NAME+"</b> 작성함</html>", 2, 15));
			for (int i = 0; i < 5; i++) {
				if(i<rate) {
					str += "★";
				}else {
					str += "☆";
				}
			}
			n.add(jl = label(str, 4, 15),"East");
			jl.setForeground(Color.ORANGE);
			
			add(ta = new JTextArea(expla));
			
			n.setBackground(Color.WHITE);
			ta.setLineWrap(true);
			ta.setBorder(null);
			ta.setEditable(false);
			
			ta.setBackground(Color.WHITE);
			getContentPane().setBackground(Color.WHITE);
			setBorder(new LineBorder(Color.LIGHT_GRAY));
		}
	}

	public static void main(String[] args) {
		NO = 1;
		NAME = "나일론";
		new Shop("MC도날드", 24);
//		,"1,900원 배달 수수료 / 1 ~ 25 분 / 평점 0.0"
	}

}
