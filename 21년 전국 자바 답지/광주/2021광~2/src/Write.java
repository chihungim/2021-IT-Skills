

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Write extends Basedialog {
	PlaceH tt = new PlaceH(30);
	PlaceTA ta = new PlaceTA();
	JPanel s = new JPanel(new BorderLayout()), sw = new JPanel(new FlowLayout(1));
	JLabel star[] = {
			labelB("��", 0,15),
			labelB("��", 0,15),
			labelB("��", 0,15),
			labelB("��", 0,15),
			labelB("��", 0,15)
	};
	int sel;
	JButton jb;
	
	public Write(String mode, int no, int sno) {
		super(mode.equals("���")?"���� ���":"���� ����", 400, 500);
		tt.setPlace("����");
		ta.setPlace("����");
		
		add(tt,"North");
		add(ta);
		add(s,"South");
		
		s.add(sw,"West");
		s.add(jb = btn("���", a->{
			if(tt.getText().isEmpty() || ta.getText().isEmpty()) {
				errmsg("������ �Է����ּ���.");
				return;
			}
			if(sel == 0) {
				errmsg("������ �������ּ���.");
				return;
			}
			if(a.getActionCommand().equals("���")) {
				msg("���並 ����߽��ϴ�.");
				execute("insert into review values(0,'"+tt.getText()+"','"+ta.getText()+"','"+sel+"','"+NO+"','"+sno+"','"+no+"')");
			}else {
				msg("���並 �����߽��ϴ�.");
				execute("update review set title = '"+tt.getText()+"', content = '"+ta.getText()+"', rate = "+sel+" where receipt = "+no);
			}
		}),"East");
		
		emp((JPanel)getContentPane(), 10, 10, 10, 10);
		
		ta.setBorder(new LineBorder(Color.LIGHT_GRAY));
		
		for (int i = 0; i < star.length; i++) {
			sw.add(star[i]);
			star[i].setForeground(Color.ORANGE);
			int k = i;
			star[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for (int j = 0; j < star.length; j++) {
						if(j <= k) {
							star[j].setText("��");
						}else {
							star[j].setText("��");
						}
						sel = k + 1;
					}
				}
			});
		}
		
		if(mode.equals("����")) {
			jb.setText("����");
			tt.setText(getone("select title from review where receipt = "+no));
			ta.setText(getone("select content from review where receipt = "+no));
			sel = toint(getone("select rate from review where receipt = "+no));
			for (int i = 0; i < sel; i++) {
				star[i].setText("��");
			}
		}
		
		sw.setOpaque(false);
		s.setOpaque(false);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		NO = 1;
		new Write("����", 66,3);
		
	}

}
