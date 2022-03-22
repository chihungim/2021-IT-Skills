package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Ticket extends BaseFrame {
	
	public Ticket(int dep, int arrv, int price, String date, String time, int dist) {
		super("Ƽ��", 800, 400);
		
		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));
		
		n.add(lbl("���� ������ - TRAIN TICKET", 0, 45, Color.white));
		n.setBackground(blue);
		n.setOpaque(true);
		
		c.add(lbl(stNames.get(dep)+ " �� "+stNames.get(arrv) , 0, 30), "North");
		
		String str1 = "<html><left><font face = '���� ���'; size=4>", str2= "<html><font face = '���� ���'; size=4>";
		str1 += (dist / 10.0) +"km<br>";
		str1 += tFormat(LocalDate.parse(date), "yyyy�� MM�� dd��");
		str1 += "<br>"+tFormat(LocalTime.parse(time), "HH�� mm�� ss��");
		c.add(new JLabel(str1, 2), "West");
		
		c.add(new JLabel(str2+iFormat(price)+"��"), "East");
		
		sz(n, 1, 100);
		
		setEmpty(c, 20, 20, 20, 20);
		
		this.setVisible(true);
	}
	
}
