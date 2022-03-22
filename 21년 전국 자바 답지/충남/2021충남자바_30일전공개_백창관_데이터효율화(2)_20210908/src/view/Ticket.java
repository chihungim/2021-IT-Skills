package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JPanel;

public class Ticket extends BaseFrame {
	
	public Ticket(int dep, int arv, LocalDate date, LocalTime time, double dis, int price) {
		super("Ƽ��", 950, 450);
		dataInit();
		
		this.add(n = new JPanel(new BorderLayout()),"North");
		this.add(c = new JPanel(new BorderLayout()));
		
		n.setBackground(Color.blue);
		n.add(lbl("���� ������ - TRAIN TICKET", 0, 40, Color.WHITE));
		sz(n, 1, 120);
		
		c.add(lbl(stations.get(dep)+" �� "+stations.get(arv), 0, 30, Color.BLACK),"North");
		c.add(lbl("<html><left>"+(dis / 10.0)+"km<br>"+date.getYear()+"�� "+String.format("%02d�� ", date.getMonthValue())+String.format("%02d��", date.getDayOfMonth())+"<br>"+String.format("%02d�� ", time.getHour())+String.format("%02d�� ",time.getMinute())+String.format("%02d��", time.getSecond()), 0, 15, Color.BLACK),"West");
		c.add(lbl(new DecimalFormat("#,##0��").format(price), 0, 15, Color.BLACK),"East");
		
		setEmpty(c, 30, 30, 30, 30);
		
		setVisible(true);
	}


}
