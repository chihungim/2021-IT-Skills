package 광주;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Shoplist extends Basedialog {
	JScrollPane scr;
	JPanel c, cc, s, sn;
	JLabel tot;
	int p = 0;
	Shop sh;
	
	public Shoplist(Shop sh) {
		super("주문표", 1000, 1000);
		
		this.sh = sh;
		
		add(label("주문표", 2, 20),"North");
		add(c = new JPanel(new BorderLayout()));
		c.add(scr = new JScrollPane(cc = new JPanel(new FlowLayout())));
		add(s =  new JPanel(new BorderLayout()),"South");
		s.add(sn = new JPanel(new BorderLayout()),"North");
		s.add(btn("주문하기", a->{
			if(order.size() == 0) {
				errmsg("주문표에 담신 메뉴가 없습니다.");
				return;
			}
			new Purchase(p,sh).addWindowListener(new be(this));
		}),"South");
		
		c.setBackground(Color.WHITE);
		s.setBackground(Color.WHITE);
		sn.setBackground(Color.WHITE);
		
		set();
		
		sn.add(tot = label(format.format(p)+"원", 4),"East");
		
		sn.add(label("최종 금액", 2),"West");
		scr.setBorder(BorderFactory.createEmptyBorder());
		cc.setBorder(new EmptyBorder(20, 5, 20, 5));
		
		cc.setOpaque(false);
		emp((JPanel)getContentPane(), 5, 5, 5, 5);
		
		setVisible(true);
	}

	private void set() {
		cc.removeAll();
		p = 0;
		int hei = 0;
		
		for (int i = 0; i < order.size(); i++) {
			cc.add(new oder(i,order.get(i)));
			p += (int)order.get(i).get(order.get(i).size() - 1);
			hei +=80;
		}
		
		tot.setText(format.format(p)+"원");
		size(cc, 270, hei+60);
		repaint();
		revalidate();
	}
	
	class oder extends JPanel {
		JPanel c,s;
		JLabel delete;
		JLabel jl;
		
		public oder(int idx, ArrayList<Object> arr) {
			setLayout(new BorderLayout());
			
			add(jl = label(arr.get(0).toString(), 2, 13),"North");
			add(c = new JPanel());
			add(s = new JPanel(new BorderLayout()),"South");
			
			for (int i = 1; i < arr.size() - 2; i++) {
				if(i % 2 == 0) continue;
				c.add(labelP(arr.get(i).toString(), 2, 12));
			}
			
			s.add(labelP(arr.get(arr.size() - 2).toString()+"개, "+format.format(toint(arr.get(arr.size() - 1).toString()))+"원", 2, 12),"West");
			s.add(delete = label("삭제하기", 4),"East");
			delete.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					removee(toint(((JButton)e.getSource()).getName()));
					sh.list.setText("주문표 ("+order.size()+")");
				}
			});
			
			c.setOpaque(false);
			jl.setOpaque(false);
			s.setOpaque(false);
			delete.setBackground(Color.WHITE);
			line(delete);
			delete.setName(idx+"");
			c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
			getContentPane().setBackground(Color.WHITE);
			setPreferredSize(new Dimension(260, 80));
			setBorder(new LineBorder(Color.LIGHT_GRAY));
		}
	}
	
	void removee(int no) {
		order.remove(no);
		set();
	}

}
