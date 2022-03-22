package 충남;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import 충남.Reserv.item;

public class Purchase extends BaseFrame {
	JTextField tt[] = {
			new JTextField(8),
			new JTextField(3),
			new JTextField(3),
			new JTextField(3),
			new JTextField(3),
			new JTextField(3),
			new JTextField(5)
	};
	ArrayList<Integer> list;
	int rand = 0, tot = 1200;
	LocalTime start, end;
	
	public Purchase(item item, Reserv r) {
		super("결제", 750, 400);
		
		add(w = new JPanel(new GridLayout(0, 1, 0, 10)),"West");
		add(c = new JPanel(new BorderLayout()));
		
		w.setBackground(new Color(50, 100, 255));
		siz(w, 250, 1);
		c.setBackground(Color.WHITE);
		
		w.add(label("결제 정보", 2, 25, Color.WHITE));
		w.add(label("예매 구간", 2, 10, Color.WHITE));
		
		w.add(label(stNames.get(0), 2, 15, Color.WHITE));
		w.add(label(stNames.get(r.path.size() - 1), 2, 15, Color.WHITE));
		w.add(label("구간", 2, 15, Color.WHITE));
		
		start = LocalTime.of(item.st/3600, (item.st % 3600) / 60, item.st % 60);
		
		w.add(label("탑승시간:", 2, 10, Color.WHITE));
		w.add(label(r.date.toString(), 2, 15, Color.WHITE));
		w.add(label(start+" 열차", 2, 15, Color.WHITE));
		
		w.add(label("총 결제 금액:", 2, 10, Color.WHITE));
		
		if(tot < r.tot * 5) {
			tot = r.tot * 5;
		}
		
		if(age <= 13) {
			tot = (int)(tot * 0.9);
		}
		if(age >= 65) {
			tot = (int)(tot * 0.5);
		}
		
		w.add(label(format(tot, "#,##0원"), 2, 15, Color.WHITE));
		
		JPanel cn = new JPanel(new GridLayout(0, 1));
		cn.add(new JLabel(img("logo.png", 180, 30), 2));
		cn.add(label("Seoul Metro Ticket", 2, 20));
		c.add(cn, "North");
		cn.setOpaque(false);
		
		JPanel cc = new JPanel(new GridLayout(0, 1));
		c.add(cc);
		
		JPanel in = new JPanel(new FlowLayout(0));
		in.add(label("안녕하세요, "+NAME+"님.", 2, 15));
		cc.add(in);
		
		JPanel in1 = new JPanel(new FlowLayout(0));
		in1.add(label("탑승권자 이름은 ", 2, 15));
		in1.add(tt[0]);
		in1.add(label("이고,", 2, 15));
		cc.add(in1);
		
		JPanel in2 = new JPanel(new FlowLayout(0));
		in2.add(label("카드번호는 ", 2, 15));
		for (int i = 1; i < 5; i++) {
			in2.add(tt[i]);
		}
		in2.add(label("이고,", 2, 15));
		cc.add(in2);
		
		JPanel in3 = new JPanel(new FlowLayout(0));
		in3.add(label("CVC는 ", 2, 15));
		in3.add(tt[5]);
		in3.add(label("카드 비밀번호는 ", 2, 15));
		in3.add(tt[6]);
		in3.add(label("입니다.", 2, 15));
		cc.add(in3);
		
		setNum();
		
		c.add(btn("결제하기", a->{
			int yn = JOptionPane.showConfirmDialog(null, "구매하시겠습니까?","메시지", JOptionPane.YES_NO_OPTION);
			if(yn == JOptionPane.YES_OPTION) {
				for (int i = 0; i < tt.length; i++) {
					if(tt[i].getText().isEmpty()) {
						errmsg("모든 항목을 입력해야 합니다.");
						return;
					}
				}
				
				for (int i = 1; i < 5; i++) {
					if(tt[i].getText().matches(".*\\D.*")|| tt[i].getText().length() > 4) {
						errmsg("카드 번호는 각 4자리 숫자로 구성해야합니다.");
						return;
					}
				}
				
				String cv = "";
				
				for (int i = 1; i < 4; i++) {
					cv += tt[i].getText().substring(0,1);
				}
				
				if(!tt[5].getText().equals(cv)) {
					errmsg("CVC 코드가 일치하지 않습니다.");
					return;
				}
				if(!tt[6].getText().equals(birth+"")) {
					errmsg("카드 비밀번호가 일치하지 않습니다.");
					return;
				}
				
				String code = format(rand, "000000");
				execute("insert into purchase values('"+code+"','"+NO+"','"+r.path.get(0)+"','"+r.path.get(r.path.size() - 1)+"','"+tot+"','"+tformat(start, "HH:mm:ss")+"','"+r.date+"','"+r.tot+"')");
				msg("결제가 완료되었습니다.\n예매번호:"+code);
				dispose();
			}
		}),"South");
		
		cc.setOpaque(false);
		in.setOpaque(false);
		in1.setOpaque(false);
		in2.setOpaque(false);
		in3.setOpaque(false);
		
		for (int i = 0; i < tt.length; i++) {
			tt[i].setHorizontalAlignment(0);
		}
		
		setVisible(true);
	}

	private void setNum() {
		list = new ArrayList<Integer>();
		try {
			ResultSet rs=  stmt.executeQuery("select * from purchase");
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while(true) {
			int r = (int)(Math.random() * 1000000);
			if(list.contains(rand)) {
				continue;
			}else {
				rand = r;
				break;
			}
		}
	}

}
