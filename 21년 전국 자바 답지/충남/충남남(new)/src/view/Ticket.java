package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JPanel;

public class Ticket extends BaseFrame {

	public Ticket(int departure, int arrive, LocalDate date, LocalTime time, double distance, int price) {
		super("티켓", 950, 450);

		this.add(n = new JPanel(new BorderLayout()), "North");
		this.add(c = new JPanel(new BorderLayout()));

		n.setBackground(new Color(50, 100, 255));
		n.add(lbl("열차 승차권 - TRAIN TICKET", 0, 40, Color.white, Font.BOLD));
		sz(n, 1, 120);

		c.add(lbl(stations.get(departure) + " ▶ " + stations.get(arrive), 0, 30, Color.black), "North");
		c.add(lbl("<html><left>" + (distance / 10.0) + "km<br>" + date.getYear() + "년 "
				+ String.format("%02d월 ", date.getMonthValue()) + String.format("%02d일 ", date.getDayOfMonth()) + "<br>"
				+ String.format("%02d시 ", time.getHour()) + String.format("%02d분 ", time.getMinute())
				+ String.format("%02d초 ", time.getSecond()), 0, 15, Color.black, Font.PLAIN), "West");
		c.add(lbl(new DecimalFormat("#,##0").format(price) + "원", 0, 15, Color.black, Font.PLAIN), "East");

		setEmpty(c, 30, 30, 30, 30);

		this.setVisible(true);
	}

}
