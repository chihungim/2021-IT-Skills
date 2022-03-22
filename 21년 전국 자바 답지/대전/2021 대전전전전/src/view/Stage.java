package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import db.DBManager;

public class Stage extends BaseFrame {
	
	HashMap<String, SeatPanel> seats = new HashMap<String, SeatPanel>();
	HashMap<String, ItemPanel> items = new HashMap<String, ItemPanel>();
	JPanel selP;
	JLabel lblPeople[] = new JLabel[5], lblPrice;
	int people=1, sumprice, cnt;
	DecimalFormat f=  new DecimalFormat("#,##0");
	JButton next, prev;
	
	
	public Stage() {
		super("좌석", 1050, 600);
		
		this.add(c = new JPanel(new BorderLayout()));
		this.add(e = new JPanel(new BorderLayout()), "East");
		sz(e, 320, 1);
		e.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 5, 5, 5)));
		
		var cn = new JPanel(new BorderLayout());
		var cc = new JPanel(new BorderLayout());
		var ccc = new JPanel(new GridLayout(0,1,5,5));
		
		c.add(cn, "North");
		c.add(cc);
		cc.add(ccc);
		
		setEmpty(cc, 10, 10, 10, 10);
		setEmpty(ccc, 10, 0, 0, 0);
		
		sz(cn, 1, 100);
		
		cn.add(lbl("STAGE", 0, 30));
		cn.setBorder(new MatteBorder(0,0,2,0,Color.black));
		
		cc.add(lbl("날짜 : "+pdate, JLabel.RIGHT, 15), "North");
		
		for (var s : "A,B,C,D,E,F".split(",")) {
			seats.put(s, new SeatPanel(s));
			ccc.add(seats.get(s));
		}
		
		var en = new JPanel(new FlowLayout(0));
		var ec = new JPanel(new BorderLayout());
		var es = new JPanel(new GridLayout());
		
		e.add(en, "North");
		e.add(ec);
		e.add(es, "South");
		
		en.add(lbl(pinfo[2], 0, 15));
		en.setBorder(new MatteBorder(0, 0, 2, 0, Color.black));
		
		var ecn = new JPanel(new FlowLayout(0));
		
		var ecs = new JPanel(new FlowLayout(0));
		
		ec.add(ecn, "North");
		
		JScrollPane scr;
		ec.add(scr=new JScrollPane(selP= new JPanel(new FlowLayout(0))));
		scr.setBorder(BorderFactory.createEmptyBorder());
		
		ec.add(ecs, "South");
		
		ecn.add(new JLabel("인원수 : "));
		for (int i = 0; i < lblPeople.length; i++) {
			ecn.add(lblPeople[i] = new JLabel("○"));
			int j = i;
			lblPeople[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for (int k = 0; k < lblPeople.length; k++) {
						lblPeople[k].setText("○");
					}
					people= j+1;
					for (int k = 0; k <= j; k++) {
						lblPeople[k].setText("●");
					}
				}
			});
		}
		lblPeople[0].setText("●");
		
		selP.setBorder(new TitledBorder(new LineBorder(Color.black), "선택한 좌석"));
		
		ecs.add(lblPrice = new JLabel("총금액 : 0"));
		
		es.add(prev=btn("이전으로", a->{
			this.dispose();
		}));
		es.add(next=btn("다음으로", a->{
			if (items.size()==0) {
				eMsg("좌석을 선택해주세요.");
				return;
			}
			if (people != items.size()) {
				eMsg("인원수에 맞게 좌석을 선택해주세요.");
				return;
			}
			
			if (next.getText().equals("다음으로")) {
				new Purchase(items, sumprice).addWindowListener(new Before(this));
			} else {
				// db수정
			}
		}));
		
		try {
			System.out.println("select * from ticket where p_no="+pinfo[0]);
			var rs = DBManager.rs("select * from ticket where p_no="+pinfo[0]);
			while (rs.next()) {
				var ts= rs.getString("t_seat").split(",");
				for (int i = 0; i < ts.length; i++) {
					var o = ts[i].substring(0, 1);
					var t = ts[i].substring(1);
					seats.get(o).lbl[rei(t)-1].setBackground(Color.LIGHT_GRAY);

				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.setVisible(true);
	}
	
	public Stage(String tno) {
		this();
		
		next.setText("수정하기");
		prev.setText("취소하기");
		
		try {
			System.out.println("select * from ticket where t_no="+tno);
			var rs = DBManager.rs("select * from ticket where t_no="+tno);
			while (rs.next()) {
				var ts = rs.getString("t_seat").split(",");
				var dis = rs.getString("t_discount").split(",");
				for (int k = 0; k < ts.length; k++) {
					lblPeople[k].setText("●");
				}
				for (int i = 0; i < ts.length; i++) {
					var o = ts[i].substring(0, 1);
					var t = ts[i].substring(1);
					seats.get(o).lbl[rei(t)-1].setBackground(Color.orange);
					
					var item = new ItemPanel(ts[i], rei(pinfo[4]));
					selP.add(item);
					if (!dis[i].equals("0")) {
						item.chk[rei(dis[i])-1].setSelected(true);
					}
						
					items.put(ts[i], item);
				}
			}
			selPsize();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (var k : items.keySet()) {
			items.get(k).setPrice();
		
		}
		
	}
	
	class SeatPanel extends JPanel {
		
		JLabel lbl[] = new JLabel[10];
		
		public SeatPanel(String s) {
			super(new GridLayout(1,0,5,5));
			
			this.add(lbl(s, 0, 15));
			for (int i = 0; i < lbl.length; i++) {
				this.add(lbl[i] = lbl(i+1+"", 0, 15));
				setLine(lbl[i], Color.black);
				int j = i;
				String n = s+String.format("%02d", (j+1));
				lbl[i].setOpaque(true);
				
				lbl[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (lbl[j].getBackground().equals(Color.lightGray)) {
							eMsg("이미 예약된 좌석입니다.");
							return;
						} else if (lbl[j].getBackground().equals(Color.orange)) {
							lbl[j].setBackground(null);
							cnt--;
							
							items.remove(n);
							selP.removeAll();
							
							sumprice = 0;
							for (var k : items.keySet()) {
								selP.add(items.get(k));
								sumprice += items.get(k).price;
							}
							lblPrice.setText("총금액 : " + f.format(sumprice));
							selPsize();
							
							selP.repaint();
							selP.revalidate();
							
						} else {
							if (cnt>=5) {
								eMsg("더 이상 선택이 불가합니다.");
								return;
							}
							
							var item = new ItemPanel(n, rei(pinfo[4]));
							selP.add(item);
							items.put(n, item);
							
							lbl[j].setBackground(Color.orange);
							cnt++;
							selPsize();
							
							repaint();
							revalidate();
						}
					}
				});
			}
		}
	}
	
	void selPsize() {
		selP.setPreferredSize(new Dimension(280, 130* (selP.getComponentCount())+10));
	}
	
	class ItemPanel extends JPanel {
		JPanel n, c;
		JLabel lbl, arrow;
		String s;
		int price, dis;
		JCheckBox chk[] = { new JCheckBox("청소년 할인 20%"), new JCheckBox("어린이 할인 40%"), new JCheckBox("장애인 할인 50%") };
		
		public ItemPanel(String s, int price) {
			super(new BorderLayout());
			this.setPreferredSize(new Dimension(270, 30));
			
			this.s =s;
			this.price=price;
			dis = 0;
			sumprice += this.price;
			lblPrice.setText("총금액 : " + f.format(sumprice));
			this.add(n = new JPanel(new BorderLayout()), "North");
			this.add(c = new JPanel(new FlowLayout(0)));
			
			n.add(lbl=lbl(s + " : "+f.format(price), 0, 15));
			n.add(arrow=lbl("▼", 0, 15), "East");
			arrow.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (arrow.getText().equals("▼")) {
						c.setVisible(true);
						sz(ItemPanel.this, 270, 120);
						arrow.setText("▲");
						selPsize();
					} else {
						c.setVisible(false);
						sz(ItemPanel.this, 270, 30);
						arrow.setText("▼");
						selPsize();
					}
				}
			});
			
			for (int i = 0; i < chk.length; i++) {
				c.add(sz(chk[i], 150, 25));
				chk[i].addActionListener(a->{
					setPrice();
				});
			}
			
			setLine(this, Color.black);
			
		}
		
		void setPrice() {
			var p = rei(pinfo[4]);
			if(chk[0].isSelected()) {
				p = (int) (p * 0.8);
				dis = 1;
			} else if(chk[1].isSelected()) {
				p = (int) (p * 0.6);
				dis = 2;
			} else if (chk[2].isSelected()){
				p = (int) (p * 0.5);
				dis = 3;
			}
			
			this.price = p;
			sumprice=0;
			
			for (var k : items.keySet()) {
				sumprice += items.get(k).price;
			}
			
			lbl.setText(s + " : "+f.format(this.price));
			lblPrice.setText("총금액 : " + f.format(sumprice));
			
			repaint();
			revalidate();
			
		}
		
		
	}
	
}
