package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Util;

public class Purchase extends BaseFrame {
	
	int cfw, cfh;
	
	JPanel p[] = { new JPanel(), new JPanel(), new JPanel(), new JPanel() };
	JPanel subp[] = { new JPanel(new GridLayout(0, 1, 10, 10)), new JPanel(new GridLayout(0, 1, 5, 5)) };
	JLabel lbl[] = { Util.lbl("결제", 0, 25), Util.lbl("P A Y M E N T", 0, 15), new JLabel("날짜"), new JLabel("지점"),
			new JLabel("테마"), new JLabel("시간"), new JLabel("가격"), new JLabel("인원수"), new JLabel("총금액") };
	JLabel idxlbl[] = { new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() };
	JTextField txt = new JTextField(25);
	JButton btn[] = { new JButton("결제"), new JButton("이전") };
	JComponent c[] = { lbl[0], lbl[1], subp[0], subp[1], lbl[2], lbl[3], lbl[4], lbl[5], lbl[6], lbl[7], lbl[8],
			idxlbl[0], idxlbl[1], idxlbl[2], idxlbl[3], idxlbl[4], txt, idxlbl[5], btn[0], btn[1] };
	JPanel addp[] = { p[0], p[1], p[2], p[2], subp[0], subp[0], subp[0], subp[0], subp[0], subp[0], subp[0], subp[1],
			subp[1], subp[1], subp[1], subp[1], subp[1], subp[1], p[3], p[3] };

	public Purchase() {
		super("결제", 350, 400);
		setLayout(new FlowLayout());
		setVisible(true);

		cfw = getContentPane().getSize().width;
		cfh = getContentPane().getSize().height;

		ui();
		data();
		event();
	}

	void event() {
		for (int i = 0; i < btn.length; i++) {
			btn[i].addActionListener(a -> {
				if (a.getActionCommand().equals("다움")) {
					dispose();
				}
			});
		}
	}

	void data() {

	}

	void ui() {
		for (int i = 0; i < p.length; i++) {
			add(p[i]);
		}

		for (int i = 0; i < addp.length; i++) {
			addp[i].add(c[i]);
		}

		p[0].setPreferredSize(new Dimension(cfw, (int) (cfh * 0.1)));
		p[1].setPreferredSize(new Dimension(cfw, (int) (cfh * 0.1)));
		p[2].setPreferredSize(new Dimension(cfw, (int) (cfh * 0.65)));
		p[3].setPreferredSize(new Dimension(cfw, (int) (cfh * 0.15)));
		subp[0].setPreferredSize(new Dimension((int) (cfw * 0.15), (int) (cfh * 0.63)));
		subp[1].setPreferredSize(new Dimension((int) (cfw * 0.75), (int) (cfh * 0.63)));
	}

	public static void main(String[] args) {
		new Purchase();
	}
}
