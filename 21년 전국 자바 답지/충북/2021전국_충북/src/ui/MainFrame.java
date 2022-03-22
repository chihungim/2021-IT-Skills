package ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import base.BasePage;

public class MainFrame extends JFrame{
	
	ArrayList<RankItem> queue = new ArrayList<RankItem>();
	int idx = 0;
	
	public MainFrame() {
		super("Music");
		setSize(1300, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(2);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./지급자료/images/logo.png"));
		setBackground(Color.DARK_GRAY);
	}

}
