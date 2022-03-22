package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import db.DBManager;

public class MagicPass extends BaseFrame {
	
	int cnt;
	int boom;
	Random r = new Random();
	ArrayList<Integer> nums = new ArrayList<Integer>();
	ArrayList<JLabel> lbls = new ArrayList<JLabel>();
	SelThread selTh = new SelThread();
	
	public MagicPass() {
		super("매직패스", 480, 580);
		
		cnt = rei(DBManager.getOne("select count(*) from ride"));
		
		this.add(c = new JPanel(new GridLayout(0, 4, 5, 5)));
		this.add(s = new JPanel(), "South");
		
		this.add(lbl("환상의 매직패스", 0, 20), "North");
		s.add(sz(btn("Stop", a->{
			
			while (selTh.isAlive()) {
				selTh.interrupt();
			}
			
			int i = selTh.getIndex();
			String no = lbls.get(i).getName();
			if (no.contentEquals("0")) {
				eMsg("아쉽네요~ 다음 기회에 다시 도전해주세요.");
				int mag = val(Main.lbl[4].getText())-1;
				Main.lbl[4].setText("매직패스("+mag+")");
				Main.lbl[4].setEnabled(val(Main.lbl[4].getText())!=0);
				this.dispose();
				return;
			} else {
				if (rei(DBManager.getOne("SELECT count(*) FROM ticket where t_date = curdate() and r_no="+no)) >= rei(DBManager.getOne("select r_max from ride where r_no="+no))) {
					eMsg(DBManager.getOne("select r_name from ride where r_no="+no)+"은(는) 만석입니다. 다시 한번 도전해주세요.");
					selTh = new SelThread();
					selTh.start();
				} else {
					iMsg(DBManager.getOne("select r_name from ride where r_no="+no)+" 매직패스에 당첨되셨습니다.");
					int mag = val(Main.lbl[4].getText())-1;
					Main.lbl[4].setText("매직패스("+mag+")");
					Main.lbl[4].setEnabled(val(Main.lbl[4].getText())!=0);
					DBManager.execute("insert into ticket values(0, '"+uno+"',curdate(),'"+no+"',1)");
					this.dispose();
				}
			}
			
		}), 120, 25));
		
		
		boom = (r.nextInt(5)+1);
		for (int i = 0; i < boom; i++) {
			JLabel lbl;
			lbls.add(lbl=new JLabel(img("./datafiles/이미지/꽝.jpg", 100, 100)));
			lbl.setName("0");
		}
		
		
		for (int i = boom; i < 16; i++) {
			int n = r.nextInt(cnt)+1;
			JLabel lbl;
			lbls.add(lbl=new JLabel(img("./datafiles/이미지/"+DBManager.getOne("select r_name from ride where r_no ="+n)+".jpg", 105, 105)));
			lbl.setName(n+"");
			
		}
		
		Collections.shuffle(lbls);
		for (var l : lbls) {
			setLine(l, Color.black);
			c.add(l);
		}
		
		
		s.setOpaque(false);
		c.setOpaque(false);
		
		
		selTh = new SelThread();
		selTh.start();
		setEmpty((JPanel)getContentPane(), 10, 10, 10, 10);
		this.setVisible(true);
	}
	
	class SelThread extends Thread {
		
		int idx = -1;
		
		@Override
		public void run() {
			while (!Thread.interrupted()) {
				
				for (var l : lbls)
					l.setEnabled(false);
				
				if (idx >= lbls.size()-1) {
					idx=0;
				} else idx++;
				lbls.get(idx).setEnabled(true);
			
				try {
					sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		
		int getIndex() {
			return this.idx;
		}
		
	}
	
	public static void main(String[] args) {
		new MagicPass();
	}
	
}
