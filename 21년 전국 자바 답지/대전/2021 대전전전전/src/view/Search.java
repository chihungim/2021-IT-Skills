package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class Search extends BaseFrame {
	
	JMenuBar bar;
	JMenu menu;
	
	String types = "";
	String items[] = "전체,뮤지컬,오페라,콘서트".split(",");
	JTextField txt;
	
	DefaultTableModel m = model("p_no,공연날짜,공연명,공연가격".split(","));
	JTable t = new JTable(m);
	JPanel cw = new JPanel(new BorderLayout());
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	
	
	public Search() {
		super("검색", 650, 530);
		this.setJMenuBar(bar = new JMenuBar());
		bar.add(menu = new JMenu("분류"));
		
		for (var it : items) {
			var item = new JMenuItem(it);
			item.addActionListener(a->{
				if (a.getActionCommand().equals(items[0])) {
					types = "";
					search();
				} else if (a.getActionCommand().equals(items[1])) {
					types = "M";
					search();
				} else if (a.getActionCommand().equals(items[2])) {
					types = "O";
					search();
				} else {
					types = "C";
					search();
				}
			});
			menu.add(item);
			if (it.equals("전체")) menu.addSeparator();
		}
		
		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		
		var ne = new JPanel(new FlowLayout(2));
		var cc = new JPanel(new BorderLayout());
		
		n.add(ne);
		c.add(cw, "West");
		c.add(cc);
		
		ne.add(lbl("공연명 : ", 0));
		ne.add(txt = new JTextField(15));
		ne.add(btn("검색", a->{
			search();
		}));
		
		cw.add(new ImagePanel());
		
		cc.add(lbl("현재 예매 가능 공연", JLabel.RIGHT, 13), "North");
		cc.add(new JScrollPane(t));
		
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				var n = t.getValueAt(t.getSelectedRow(), 0).toString();
				new Reserve(n).addWindowListener(new Before(Search.this));
			}
			
		});
		
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		for (int i = 0; i < t.getColumnCount(); i++) {
			t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
		}
		
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
		
		setEmpty(cw, 50, 0, 50, 0);
		setEmpty((JPanel)getContentPane(), 5, 5, 5, 5);
		
		this.setVisible(true);
	}
	
	void search() {
		cw.removeAll();
		cw.add(new ImagePanel());
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) {
		new Search();
	}
	
	class ImagePanel extends JPanel {
		JLabel type ;
		CardLayout card;
		int pageCnt=1, pageIdx=1;
		
		JLabel next, prev, first, last;
		ArrayList<JLabel> lbls = new ArrayList<JLabel>();
		
		public ImagePanel() {
			super(new BorderLayout());
			
			var n = new JPanel(new BorderLayout());
			var c = new JPanel(new BorderLayout());
			var cc = new JPanel(card = new CardLayout());
			var cs = new JPanel();
			var s = new JPanel();
			
			this.add(n, "North");
			this.add(c);
			c.add(cc);
			c.add(cs, "South");
			c.add(prev = lbl("◀", 2, 30), "West");
			c.add(next= lbl("▶", 2, 30), "East");
			this.add(s, "South");
			
			switch (Search.this.types) {
			case "M":
				n.add(type = lbl("분류: 뮤지컬", JLabel.RIGHT, 15));
				break;
			case "O":
				n.add(type = lbl("분류: 오페라", JLabel.RIGHT, 15));
				break;
			case "C":
				n.add(type = lbl("분류: 콘서트", JLabel.RIGHT, 15));
				break;
			default:
				n.add(type = lbl("분류: 전체", JLabel.RIGHT, 15));
				break;
			}
			
			addRow(m, "select p_no, p_date, p_name, format(p_price, '#,##0') from perform where p_date >= '2021-10-06' and pf_no like '%"+Search.this.types+"%' and p_name like '%"+txt.getText()+"%' order by p_date, p_price desc");
			System.out.println("select p_no, p_date, p_name, format(p_price, '#,##0') from perform where p_date >= curdate() and pf_no like '%"+Search.this.types+"%' and p_name like '%"+txt.getText()+"%' order by p_date, p_price desc");
			try {
				var rs = DBManager.rs("select * from perform where p_date >= '2021-10-06' and pf_no like '%"+Search.this.types+"%' and p_name like '%"+txt.getText()+"%' group by p_name order by p_date, p_price desc");
				while (rs.next()) {
					JLabel img = new JLabel(img("./Datafiles/공연사진/"+rs.getString(2)+".jpg", 100, 100));
					img.setName(rs.getString(1));
					img.setToolTipText(rs.getString(3) +" / "+ rs.getString(7));
					setLine(img, Color.black);
					img.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							var no = ((JLabel)e.getSource()).getName();
							new Reserve(no).addWindowListener(new Before(Search.this));
						}
					});
					lbls.add(img);
				}
				if (lbls.size() == 0) {
					iMsg("검색 결과가 없습니다.");
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (lbls.size() % 4 != 0) {
				for (int i = lbls.size()%4; i < 4; i++) {
					var l = sz(new JLabel(), 100, 100);
					lbls.add(l);
					setLine(l, Color.black);
					l.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							eMsg("공연 정보가 없습니다.");
							return;
						}
					});
				}
			}
			
			pageCnt = lbls.size()/4;
			int cnt=0;
			var tmp = new JPanel(new GridLayout(0, 2, 5, 5));
			
			for (int i = 1; i <= pageCnt; i++) {
				for (int j = cnt; j < cnt+4; j++) {
					tmp.add(lbls.get(j));
				}
				cnt+=4;
				cc.add(tmp, i+"");
				tmp = new JPanel(new GridLayout(0, 2, 5, 5));
			}
			
			JLabel lbl[] = new JLabel[pageCnt];
			
			for (int i = 0; i < lbl.length; i++) {
				cs.add(lbl[i] = lbl("●", 0, 10));
				lbl[i].setName(i+1+"");
				
				lbl[i].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						for (int j = 0; j < lbl.length; j++) {
							lbl[j].setForeground(Color.black);
						}
						
						var c = ((JLabel)e.getSource());
						c.setForeground(Color.red);
						card.show(cc, c.getName());
					}
				});
			}
			
			lbl[0].setForeground(Color.red);
			
			s.add(first = lbl("《처음으로", 0, 15));
			s.add(last = lbl("마지막으로》", 0, 15));
			
			prev.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (pageIdx - 1 == 0)
						return;
					else
						pageIdx--;
					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[pageIdx - 1].setForeground(Color.red);
					card.show(cc, pageIdx + "");
				}
			});

			next.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (pageIdx + 1 == pageCnt + 1)
						return;
					else
						pageIdx++;

					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[pageIdx - 1].setForeground(Color.red);
					card.show(cc, pageIdx + "");
				}
			});

			first.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[0].setForeground(Color.RED);
					card.first(cc);
					pageIdx = 1;
				}
			});

			last.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {

					for (int i = 0; i < lbl.length; i++) {
						lbl[i].setForeground(Color.BLACK);
					}

					lbl[pageCnt - 1].setForeground(Color.RED);
					card.last(cc);
					pageIdx = pageCnt;
				}
			});
			
					
			
			setEmpty(this, 20, 20, 20, 20);
		}
	}
	
	
}
