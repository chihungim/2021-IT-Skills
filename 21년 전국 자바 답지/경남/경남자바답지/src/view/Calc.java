package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Calc extends BaseFrame {

	String num[] = "0,��,��,��,��,��,��,ĥ,��,��".split(",");
	String unit[] = { "", "��", "��", "õ", "��", "�ʸ�", "�鸸", "õ��", "��", "�ʾ�", "���", "õ��" };
	JComboBox[] combo = {
		new JComboBox(),	
		new JComboBox(),	
		new JComboBox(),	
		new JComboBox(),	
		new JComboBox()	
	};
	JTextField txt;
	JPanel panels[] = new JPanel[9];
	
	
	public Calc() {
		super("�޿�����", 450, 600);

		this.add(n = new JPanel(), "North");
		this.add(c = new JPanel(new GridLayout(0, 1)));
		n.add(lbl("<html><center>2021�� �����ñ���<br><font color=\"red\";>8,720<font color=\"black\">�� �Դϴ�.", 0, 20));
		n.setBackground(Color.yellow);
		
		for (int i = 0; i < panels.length; i++) {
			c.add(panels[i] = new JPanel(new BorderLayout()));
		}
		panels[0].setLayout(new FlowLayout());
		panels[7].setLayout(new GridLayout());
		
		panels[0].add(combo[0]);
		panels[0].add(new JLabel(">>"));
		panels[0].add(combo[1]);
		
		panels[1].add(new JLabel("�޿�"), "West");
		panels[1].add(txt = new JTextField(15));
		var lbl = new JLabel("��");
		panels[1].add(lbl, "South");
		lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		txt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!isNumeric(e.getKeyChar())) {
					e.consume();
				} 
			}
			@Override
			public void keyReleased(KeyEvent e) {
				lbl.setText(price2Kor(txt.getText()));
			}
		});
		
		
		this.setVisible(true);
	}

	String price2Kor(String strNum) {
		int j = strNum.length() - 1;
		String str = "";
		
		// ���ڿ��� ���� ��ŭ �ݺ��� ����
		for (int i = 0; i < strNum.length(); i++) {
			int n = strNum.charAt(i) - '0'; // ���ڿ��� �ִ� ���ڸ� �ϳ��� �����ͼ� int������ ��ȯ
			if (!num[n].contentEquals("0")) { // ���ڰ� 0�� ���� ������� ����
				str += num[n]; // ���ڸ� �ѱ۷� �о ���
				str += unit[j]; // ���� ���
			}
			j--;
		}
		return str;
	}

	public static void main(String[] args) {
		new Calc();
	}

}
