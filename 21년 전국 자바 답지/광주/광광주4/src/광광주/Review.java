package ±¤±¤ÁÖ;

import java.awt.Menu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class Review extends BaseFrame {

	DefaultTableModel m = model("ÁÖ¹®¹øÈ£,ÁÖ¹®ÀÏ½Ã,ÁÖ¹®±Ý¾×,À½½Ä,À½½ÄÁ¡,¸®ºä¹øÈ£,¸®ºä".split(","));
	JTable t = table(m);
	JPopupMenu menu = new JPopupMenu();
	JMenuItem menuItem = new JMenuItem("¸®ºä ¼öÁ¤");

	public Review() {
		super("¸®ºä", 500, 300);
		add(new JScrollPane(t));
		setData();

		menu.add(menuItem);

		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getColumnModel().getColumn(5).setMinWidth(0);
		t.getColumnModel().getColumn(5).setMaxWidth(0);

		t.getColumnModel().getColumn(3).setMinWidth(0);
		t.getColumnModel().getColumn(3).setMaxWidth(0);

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (t.getSelectedRow() == -1)
						return;

					menu.show(t, e.getX(), e.getY());

					if (t.getValueAt(t.getSelectedRow(), 6) != null)
						menuItem.setText("¸®ºä ¼öÁ¤");
					else
						menuItem.setText("¸®ºä ÀÛ¼º");
				}
			}
		});

		menuItem.addActionListener(a -> {
			new ReviewEdit(this).addWindowListener(new Before(this));
		});

		setVisible(true);
	}

	public void setData() {
		addrow("SELECT \r\n" + "    r.no, r.time, r.price, s.no, s.name, r.rno, r.title\r\n" + "FROM\r\n"
				+ "    seller s,\r\n" + "    (SELECT \r\n" + "        rc.seller AS sell,\r\n"
				+ "            rc.NO AS no,\r\n" + "            rc.receipt_time AS time,\r\n"
				+ "            rc.price AS price,\r\n" + "            r.title AS title,\r\n"
				+ "            r.No as rno\r\n" + "    FROM\r\n" + "        receipt rc\r\n"
				+ "    LEFT OUTER JOIN review r ON rc.NO = r.RECEIPT\r\n" + "    WHERE\r\n"
				+ "        rc.STATUS = 3\r\n" + "        and rc.user = " + uno + ") r\r\n" + "WHERE\r\n"
				+ "    r.sell = s.No", m);
	}

	

}
