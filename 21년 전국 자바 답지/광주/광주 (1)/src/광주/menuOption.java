package 광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

public class menuOption extends Basedialog {
	JPanel c = new JPanel(new BorderLayout()), s = new JPanel(new GridLayout(1, 0, 10, 0)), sin = new JPanel(new BorderLayout());
	JPanel cn = new JPanel(new BorderLayout()), cc = new JPanel(new FlowLayout());
	JLabel img;
	boolean isOption = false;
	ArrayList<ArrayList<JRadioButton>> arr = new ArrayList<ArrayList<JRadioButton>>();
	JButton jb;
	JLabel jl, expla;
	JScrollPane scr;
	int cnt = 1, h, no, pri;
	
	public menuOption(int no,Shop sg) {
		super("주문표에 추가", 400, 750);
		setModal(true);
		
		this.no = no;
		
		add(img = new JLabel(img("메뉴/"+no+".png", 400, 350)),"North");
		add(scr = new JScrollPane(c));
		add(s,"South");
		
		pri = toint(getone("select price from menu where no = "+no));
		
		s.add(sin);
		s.add(jb = btn("1개를 주문표에 추가하기", a->{
			ArrayList<Object> list=  new ArrayList<Object>();
			list.add(getone("select name from menu where no = "+no));
			int optp = 0, selcnt = 0;
			if(isOption) {
				for (int i = 0; i < arr.size(); i++) {
					selcnt = 0;
					for (int j = 0; j < arr.get(i).size(); j++) {
						if(arr.get(i).get(j).isSelected()) {
							list.add(arr.get(i).get(j).getText());
							list.add(arr.get(i).get(j).getName());
							optp += toint(arr.get(i).get(j).getName());
							selcnt++;
						}
					}
					if(selcnt == 0) {
						errmsg("옵션을 모두 선택해주세요.");
						return;
					}
				}
			}
			list.add(toint(jl.getText()));
			int tot = (pri+optp) * toint(jl.getText());
			list.add(tot);
			order.add(list);
			sg.list.setText("주문표 ("+order.size()+")");
			dispose();
		}));
		
		sin.add(btn("←", a->{
			if(cnt == 1) return;
			cnt--;
			jl.setText(cnt+"");
		}),"West");
		sin.add(jl = label("1", 0));
		sin.add(btn("→", a->{
			if(cnt == 9) return;
			cnt++;
			jl.setText(cnt+"");
			
		}),"East");
		
		c.add(cc);
		
		try {
			ResultSet rs = stmt.executeQuery("select distinct title from options where menu = "+no);
			if(!rs.next()) {
				cc.add(size(expla = label("옵션이 없는 상품입니다.", 0, 15), 400, 200));
				expla.setForeground(Color.RED);
				jl.setVerticalAlignment(JLabel.TOP);
			}
			rs.previous();
			
			ArrayList<opt> list = new ArrayList<opt>();
			while(rs.next()) {
				isOption = true;
				opt o = new opt(rs.getString(1));
				o.setBackground(Color.WHITE);
				list.add(o);
				cc.add(o);
			}
			rs.close();
			
			list.forEach(a->{
				a.setUp();
				arr.add(a.li);
				h += a.hei;
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		size(c, 360, h + 200);
		size(cc, 400, h);
		
		c.add(cn,"North");
		
		cn.add(label(getone("select name from menu where no = "+no), 2, 20));
		cn.add(labelP(getone("select description from menu where no = "+no), 2, 12),"South");
		
		line(img);
		
		emp(s, 10, 10, 10, 10);
		
		s.setOpaque(false);
		c.setOpaque(false);
		cc.setBackground(Color.WHITE);
		cn.setBackground(Color.WHITE);
		scr.setBackground(Color.WHITE);
		sin.setOpaque(false);
		
		setVisible(true);
	}
	
	class opt extends JPanel {
		int hei;
		ArrayList<JRadioButton> li = new ArrayList<JRadioButton>();
		ButtonGroup bg = new ButtonGroup();
		String title;
		JPanel c;
		
		public opt(String title) {
			setLayout(new BorderLayout());
			this.title = title;
			add(label(title, 2, 15),"North");
			add(c = new JPanel(new GridLayout(0, 1)));
		}
		
		void setUp() {
			try {
				ResultSet rs = stmt.executeQuery("Select * from options where title = '"+title.replace("'", "\\'")+"' and menu = "+no);
				while(rs.next()) {
					JPanel temp = new JPanel(new BorderLayout());
					JRadioButton radio = new JRadioButton(rs.getString(3));
					li.add(radio);
					radio.setName(rs.getString(4));
					radio.setBackground(Color.WHITE);
					
					temp.add(radio, "West");
					temp.setBackground(Color.WHITE);
					temp.add(label((rs.getString(4).equals("0")?"추가 비용 없음":"+"+rs.getString(4)), 2),"East");
					bg.add(radio);
					c.add(temp);
					hei += 30;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			setPreferredSize(new Dimension(250, hei+40));
		}
	}
	
}
