package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import additional.Util;
import db.Tools;

public class SearchPage extends BasePage {

	JScrollPane scr1, scr2;
	JRadioButton r = new JRadioButton("<html><font color=\"blue\">drf</font>");
	JPanel scr_p = new JPanel(), orderPanel;
	JScrollPane scr;
	JTextField txt;
	JCheckBox eventChk = new JCheckBox();
	JComboBox<String> cateCombo = new JComboBox<String>(), sellCombo = new JComboBox<String>();
	String cap[] = "영업점명,카테고리,행사 여부".split(",");
	HashSet<Integer> idSet = new HashSet<Integer>();
	int count;
	
	public SearchPage() {
		super();

		this.add(n = new JPanel(new GridLayout(0, 1, 0, 0)), "North");
		var n_f = new JPanel(new BorderLayout());
		n_f.add(txt = new JTextField());
		n_f.add(Util.sz(Util.btn("<html>&#x1F50D", a->{
			count = 0;
			scr.getVerticalScrollBar().setValue(0);
			c.removeAll();
			search();
		}), 40, 1), "East");
		Util.sz(n_f, 1, 40);
		n.add(n_f);
		var n_l = new JPanel(new GridLayout(1, 0, 5, 5));
		n.add(n_l);
		n.setOpaque(false);
		n_f.setOpaque(false);
		n_l.setOpaque(false);
		cateCombo.setBackground(Color.white);
		sellCombo.setBackground(Color.white);
		
		for (int i = 0; i < cap.length; i++) {
			var tmp = new JPanel();
			tmp.add(Util.sz(new JLabel(cap[i]), 60, 25));
			if (i==0) {
				tmp.add(sellCombo);
			} else if (i==1) {
				tmp.add(cateCombo);
			} else {
				tmp.add(eventChk);
				eventChk.setOpaque(false);
			}
			tmp.setOpaque(false);
			n_l.add(tmp);
		}
		
		this.add(scr=scr1 = new JScrollPane(c = new JPanel(new GridLayout(0, 3, 5, 5))));
		this.add(scr_p = new JPanel(new BorderLayout()), "East");
		scr_p.add(Util.lbl("장바구니", 0, 20), "North");
		scr_p.add(scr2 = new JScrollPane(orderPanel = new JPanel(new FlowLayout(0))));
		scr_p.add(Util.sz(Util.btn("구매하기", a->{
			if (idSet.size()==0) {
				Util.eMsg("장바구니가 비어있습니다.");
				return;
			}
			mf.addPage(new PurchasePage((sellCombo.getSelectedIndex()+1), orderPanel));

		}), 1, 25), "South");
		Util.sz(scr2, 230, 25);
		Util.setEmpty(scr_p, 0, 10, 0, 0);
		
		scr1.setOpaque(false);
		scr2.setOpaque(false);
		scr_p.setOpaque(false);
		Util.setLine(scr1);
		Util.setLine(scr2);
		
		c.setBackground(Color.white);
		orderPanel.setBackground(Color.white);
		Util.setEmpty(this, 10, 10, 10, 10);
		
		cateCombo.addItem("전체");
		try {
			var rs = Tools.rs("select c_Name from category");
			while (rs.next()) {
				cateCombo.addItem(rs.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			var rs = Tools.rs("select s_Name from seller");
			while (rs.next()) {
				sellCombo.addItem(rs.getString(1));
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sellCombo.setSelectedIndex(-1);
		sellCombo.addItemListener(a->{
			count = 0;
			txt.setText("");
			cateCombo.setSelectedIndex(0);
			eventChk.setSelected(false);
			c.removeAll();
			search();
		});
		
		c.removeAll();
		
		var scroll = scr.getVerticalScrollBar();
		scroll.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (scroll.getValue()!=0 && (scroll.getValue() + scroll.getHeight() == scroll.getMaximum())) {
					count += 9;
					search();
				}
			}
		});
	}
	
	void search() {
		String sql = "SELECT p.p_No, c.c_Name, p.p_Name, p.p_Price, if(e.e_Month = month(now()), e.e_Month, 0) state, st.st_Count FROM product p inner join category c on c.c_No=p.c_No left join event e on e.p_No=p.p_No inner join stock st on st.p_No=p.p_No where p_Name like '%"+txt.getText()+"%' and st.s_No='"+(sellCombo.getSelectedIndex()+1)+"'";
		if (cateCombo.getSelectedIndex() != 0) 
			sql += " and c.c_No = "+Util.rei(Tools.getOneRs("select c_No from category where c_Name ='"+cateCombo.getSelectedItem()+"'"));;
		
		if (eventChk.isSelected())
			sql += " and if(e.e_Month = month(now()), e.e_Month, 0) <> 0";
		
		sql += " order by p.p_No limit "+count+", 9";
	
		System.out.println(sql);
		try {
			var rs = Tools.rs(sql);
			while (rs.next()) {
				int no = rs.getInt(1);
				String name = rs.getString(3);
				int price = rs.getInt(4);
				int sale = rs.getInt(5);
				int stock = rs.getInt(6);
				JButton btn;
				JPanel bord = new JPanel(new BorderLayout());
				
				JPanel b_c = new JPanel(new FlowLayout(0)), b_w = new JPanel(), b_s = new JPanel(new FlowLayout(2));
				JLabel lbl;
				bord.add(b_c);
				bord.add(b_w, "West");
				bord.add(b_s, "South");
				
				b_w.add(lbl=Util.sz(new JLabel(Util.img("./datafile/image/"+name+".jpg", 80, 80)), 80, 80));
				Util.setLine(lbl);
				
				var e = ((sale!=0)?"O":"X");
				b_c.add(new JLabel("<html><left>"+name+"<br>가격 : "+price+"원<br>재고 : "+rs.getString(6)+"개<br>행사상품 "+e));
				
				b_s.add(btn=Util.sz(Util.btn("장바구니에 추가", a->{
					if (idSet.contains(no)) {
						Util.eMsg("이미 추가된 상품입니다.");
						return;
					}
					orderPanel.add(new OrderItem(no, name, price, stock, sale));
					idSet.add(no);
					updateOrderPanel();
				}), 120, 25));
				btn.setEnabled(stock!=0);
				
				c.add(bord);
				Util.setLine(bord);
				bord.setBackground(Color.white);
				b_c.setOpaque(false);
				b_w.setOpaque(false);
				b_s.setOpaque(false);
				Util.sz(bord, 1, 150);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		repaint();
		revalidate();
		
	}
	
	void updateOrderPanel() {
		orderPanel.setPreferredSize(new Dimension(1, 50 * orderPanel.getComponentCount()));
		orderPanel.repaint();
		orderPanel.revalidate();
	}
	
	class OrderItem extends JPanel {
		int no;
		String name;
		int price, stock, state;
		public OrderItem(int no, String name, int price, int stock, int state) {
			this.no = no;
			this.name = name;
			this.price = price;
			this.stock = stock;
			this.state = state;
			
			setLayout(new BorderLayout());
			setBorder(new LineBorder(Color.black));
			setPreferredSize(new Dimension(200,30));
			setBackground(Color.white);
			setOpaque(false);
			
			var cp = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
			JLabel lbl;
			
			String txt= name.length()<=10 ? name : name.substring(0,10)+"...";
			cp.add(Util.sz(Util.lbl(txt, JLabel.LEFT), 150, 25));
			add(lbl=Util.sz(new JLabel(Util.img("./datafile/image/"+name+".jpg", 30, 30)), 30, 2), "West");
			add(Util.sz(Util.btn("x", a->{
				idSet.remove(this.no);
				orderPanel.remove(OrderItem.this);
				updateOrderPanel();
			}), 30, 30), "East");
			add(cp);
			cp.setOpaque(false);
			
			Util.setLine(lbl);
			this.repaint();
			this.revalidate();
		}
	}
	
	

}
