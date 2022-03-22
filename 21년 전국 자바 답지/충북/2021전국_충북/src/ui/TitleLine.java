package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import base.BasePage;

public class TitleLine extends BasePage {

	final static int vertical = 0;
	final static int horizontal= 1;
	
	String cap1, cap2;

	JPanel title = new JPanel(new GridLayout(1, 0));
	JPanel line = new JPanel(new GridLayout());

	JLabel t1;
	JLabel t2;

	public TitleLine(String cap1, int w) {
		this.cap1 = cap1;

		setOpaque(false);
		setLayout(new BorderLayout());
		setBackground(Color.black);

		title.setOpaque(false);

		add(title);
		add(line, "South");

		t1 = lbl(cap1, JLabel.LEFT, Font.BOLD, 15);

		title.add(t1, "West");

		BasePage.size(line, w, 1);
	}
	
	public TitleLine(String cap1, int w, int ali) {
		this(cap1, w);
		if(ali == 1) {
			BasePage.size(line, 1, w);
		}
	}
	
	public TitleLine(String cap1, String cap2, int w, MouseListener a) {
		this(cap1, w);
		this.cap2 = cap2;

		t2 = lbl(cap2, JLabel.RIGHT, Font.BOLD, 15);
		t2.addMouseListener(a);

		title.add(t2);
	}
	
	
}
