package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import db.stmt;
import view.Reserve.ItemPanel;

public class Purchase extends BaseFrame {
	JTextField txt[] = { new JTextField(8), new JTextField(3), new JTextField(3), new JTextField(3), new JTextField(3),
			new JTextField(3), new JTextField(5) };
	ArrayList<Integer> list;
	int randumber = 0;
	int totPrice = 1200;

	public Purchase(ItemPanel item, Reserve r) {
		super("결제", 750, 400);

		this.add(w = new JPanel(new GridLayout(0, 1, 0, 10)), "West");
		this.add(c = new JPanel(new BorderLayout()));

		w.setBackground(new Color(50, 100, 255));
		sz(w, 250, 1);
		c.setBackground(Color.WHITE);

		w.add(lbl("결제 정보", 2, 25, Color.WHITE));
		w.add(lbl("예매 구간:", 2, 10, Color.WHITE));

		w.add(lbl(stations.get(0), 2, 15, Color.WHITE));
		w.add(lbl(stations.get(r.path.size() - 1), 2, 15, Color.WHITE));
		w.add(lbl("구간", 2, 15, Color.WHITE));

		w.add(lbl(r.date + ":", 2, 15, Color.WHITE));
		w.add(lbl(item.stime + " 열차", 2, 15, Color.WHITE));
		w.add(lbl("총 결제 금액:", 2, 10, Color.WHITE));

		if (totPrice < r.totDis * 5) {
			totPrice = r.totDis * 5;
		}
		if (uage <= 13) {
			totPrice = (int) (totPrice * 0.9);
		}
		if (uage >= 65) {
			totPrice = (int) (totPrice * 0.5);
		}

		w.add(lbl(new DecimalFormat("#,##0원").format(totPrice), 2, 15, Color.WHITE));
		setEmpty(w, 10, 10, 10, 10);

		JPanel cn = new JPanel(new GridLayout(0, 1));
		cn.add(new JLabel(img("logo.png", 180, 30), 2));
		cn.add(lbl("Seoul Metro Ticket", 2, 20));
		c.add(cn, "North");
		cn.setOpaque(false);

		JPanel cc = new JPanel(new GridLayout(0, 1));
		c.add(cc);

		JPanel in = new JPanel(new FlowLayout(0));
		in.add(lbl("안녕하세요, " + uname + "님.", 2, 15, Color.BLACK));
		cc.add(in);

		JPanel in1 = new JPanel(new FlowLayout(0));
		in1.add(lbl("탑승권자 이름은 ", 2, 15, Color.BLACK));
		in1.add(txt[0]);
		in1.add(lbl("이고,", 2, 15, Color.BLACK));
		cc.add(in1);
		
		JPanel in2 = new JPanel(new FlowLayout(0));
		in2.add(lbl("카드번호는 ", 2, 15, Color.BLACK));
		for (int i = 1; i < 5; i++) {
			in2.add(txt[i]);
		}
		in2.add(lbl("이고,", 2, 15, Color.BLACK));
		cc.add(in2);
		
		JPanel in3 = new JPanel(new FlowLayout(0));
		in3.add(lbl("CVC는 ", 2, 15, Color.BLACK));
		in3.add(txt[5]);
		in3.add(lbl("카드 비밀번호는 ", 2, 15, Color.BLACK));
		in3.add(txt[6]);
		in3.add(lbl("입니다.", 2, 15, Color.BLACK));
		cc.add(in3);
		
		setNum();
		
		c.add(btn("결제하기", a->{
			int yn = JOptionPane.showConfirmDialog(null, "구매하시겠습니까?","메시지",JOptionPane.YES_NO_OPTION);
			if(yn == JOptionPane.YES_OPTION) {
				for (int i = 0; i < txt.length; i++) {
					if(txt[i].getText().isEmpty()) {
						eMsg("모든 항목을 입력해야 합니다.");
						return;
					}
				}
				
				for (int i = 1; i < 5; i++) {
					if(txt[i].getText().matches(".*[^0-9].*") || txt[i].getText().length() > 4) {
						eMsg("카드 번호는 각 4자리 숫자로 구성해야합니다.");
						return;
					}
				}
				
				String cv = "";
				
				for (int i = 1; i < 4; i++) {
					cv += txt[i].getText().substring(0, 1);
				}
				
				if(!txt[5].getText().equals(cv)) {
					eMsg("CVC 코드가 일치하지 않습니다.");
					return;
				}
				
				if(!txt[6].getText().equals(ubirth+"")) {
					eMsg("카드 비밀번호가 일치하지 않습니다.");
					return;
				}
				String code = new DecimalFormat("000000").format(randumber);
				stmt.execute("insert into purchase values('"+code+"','"+uno+"','"+r.path.get(0)+"','"+r.path.get(r.path.size() - 1)+"','"+totPrice+"','"+tformat(item.stime, "HH:mm:ss")+"','"+r.date+"','"+r.totDis+"')");
				iMsg("결제가 완료되었습니다!\n예매번호:"+code);
				dispose();
			}
		}),"South");
		
		cc.setOpaque(false);
		in.setOpaque(false);
		in1.setOpaque(false);
		in2.setOpaque(false);
		in3.setOpaque(false);
		setEmpty(cc, 30, 0, 30, 0);
		setEmpty(c, 10, 20, 10, 10);
		
		for (int i = 0; i < txt.length; i++) {
			txt[i].setHorizontalAlignment(0);
			txt[i].setFont(new Font("맑은 고딕", Font.BOLD, 15));
			txt[i].setBorder(new MatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		}
		
		setVisible(true);
	}

	private void setNum() {
		list = new ArrayList<Integer>();
		try {
			var rs = stmt.rs("select * from purchase");
			while(rs.next()){
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while(true) {
			int ran = (int)(Math.random() * 1000000);
			if(list.contains(randumber)) {
				continue;
			}else {
				randumber = ran;
				break;
			}
		}
	}

}
