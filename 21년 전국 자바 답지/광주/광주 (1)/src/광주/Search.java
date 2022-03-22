package 광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Search extends Basedialog {
	PlaceH tt[] = {
			new PlaceH(50),
			new PlaceH(15),
			new PlaceH(15)
	};
	String str[] = "검색할 음식점 이름을 입력하세요.,최소 가격,최대 가격".split(","); 
	JComboBox<String> combo = new JComboBox<String>();
	JRadioButton radio[] = {
			new JRadioButton("기본 정렬"),
			new JRadioButton("평점 정렬"),
			new JRadioButton("배달 비용 정렬")
	};
	JScrollPane scr;
	JPanel base = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new BorderLayout()), w = new JPanel(new GridLayout(0, 1, 0, 10)), c = new JPanel(new GridLayout(0, 3, 10, 10));
	ButtonGroup bg = new ButtonGroup();
	String opt = "order by s.no asc";
	HashMap<Integer, JLabel> hash = new HashMap<Integer, JLabel>();
	
	public Search(int cate) {
		super("검색", 1000, 560);
		
		add(n,"North");
		add(base);
		base.add(scr = new JScrollPane(c));
		base.add(w,"West");
		
		n.add(tt[0]);
		
		tt[0].addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER) {
					search();
				}
			}
		});
		
		w.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(10, 10, 150, 10)));
		w.add(combo);
		w.add(label("평점", 2));
		for (int i = 0; i < radio.length; i++) {
			w.add(radio[i]);
			radio[i].setOpaque(false);
			bg.add(radio[i]);
			radio[i].addActionListener(a->{
				if(a.getSource().equals(radio[0])) {
					opt = "order by s.no asc";
				}
				if(a.getSource().equals(radio[1])) {
					opt = "order by aver desc";
				}
				if(a.getSource().equals(radio[2])) {
					opt = "order by deliveryfee asc";
				}
			});
		}
		
		radio[0].setSelected(true);
		
		w.add(label("가격 필터", 2));
		
		for (int i = 0; i < tt.length; i++) {
			if(i>0) {
				w.add(tt[i]);
			}
			tt[i].setPlace(str[i]);
		}
		
		w.add(btn("적용", a->{ 
			if(tt[1].getText().matches(".*\\D.*") || tt[2].getText().matches(".*\\D.*")) {
				errmsg("가격 필터에는 숫자만 입력 가능합니다.");
				return;
			}
			search();
		}));
		
		combo.addItem("모두");
		try {
			ResultSet rs = stmt.executeQuery("select name from category");
			while(rs.next()) {
				combo.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		combo.setSelectedIndex(cate);
		
		emp((JPanel)getContentPane(), 10, 10, 10, 10);
		emp(base, 10, 0, 0, 0);
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT s.no, format(DELIVERYFEE,'#,##0'), concat(minute(min(cooktime)),' ~ ', minute(max(cooktime)),'분'),format(ifnull(avg(rate),0.0),1) as aver FROM menu m, seller s left join review r on r.seller = s.no where m.seller = s.no group by s.name order by s.no asc");
			while(rs.next()) {
				hash.put(rs.getInt(1), new JLabel(rs.getString(2)+"원 배달 수수료 / "+rs.getString(3)+" / 평점 "+rs.getString(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		c.setOpaque(true);
		n.setOpaque(false);
		w.setOpaque(false);
		base.setOpaque(false);
		scr.setOpaque(true);
		c.setBackground(Color.WHITE);
		scr.setBackground(Color.WHITE);
		scr.setBorder(new LineBorder(Color.WHITE));
		
		search();
		
		setVisible(true);
	}
	
	private void search() {
		int min = tt[1].getText().isEmpty()?0:toint(tt[1].getText());
		int max = tt[2].getText().isEmpty()?100000000:toint(tt[2].getText());
		
		if(min > max) {
			errmsg("최저 가격이 최대 가격보다 클 수 없습니다.");
			return;
		}
		c.removeAll();
		try {
			ResultSet rs = stmt.executeQuery("SELECT s.no,s.name, DELIVERYFEE, concat(minute(min(cooktime)),' ~ ', minute(max(cooktime)),'분'),format(ifnull(avg(rate),0.0),1) as aver FROM menu m, seller s left join review r on r.seller = s.no where m.seller = s.no and s.CATEGORY like '%%' and (price >= "+min+" and price <= "+max+") and s.name like '%"+tt[0].getText()+"%' and s.category like '%"+(combo.getSelectedIndex()==0?"":combo.getSelectedIndex())+"%' group by s.name "+opt);
//			ResultSet rs = stmt.executeQuery("SELECT s.no, s.name FROM menu m, seller s left join review r on r.seller = s.no where m.seller = s.no and s.CATEGORY like '%%' and (price >= "+min+" and price <= "+max+") and s.name like '%"+tt[0].getText()+"%' and s.category like '%"+(combo.getSelectedIndex()==0?"":combo.getSelectedIndex())+"%' group by s.name "+opt);
			while(rs.next()) {
				JPanel temp = new JPanel(new BorderLayout()), in = new JPanel(new BorderLayout());
				temp.add(in,"South");
				temp.add(new JLabel(img("배경/"+rs.getInt(1)+".png", 240, 110)));
				
				in.add(label(rs.getString(2),2),"North");
//				in.add(new JLabel(format.format(rs.getInt(3))+"원 배달 수수료 / "+rs.getString(4)+" / 평점"+rs.getString(5)));
				in.add(hash.get(rs.getInt(1)));
				
				size(temp, 240, 150);
				temp.setOpaque(false);
				in.setOpaque(false);
				temp.setName(rs.getString(2)+","+rs.getInt(1));
				
				temp.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 2) {
							String aa[] = temp.getName().split(",");
							new Shop(aa[0],toint(aa[1])).addWindowListener(new be(Search.this));
							//,hash.get(toint(aa[1])).getText()
						}
					}
				});
				
				c.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		new Search(0);
	}

}
