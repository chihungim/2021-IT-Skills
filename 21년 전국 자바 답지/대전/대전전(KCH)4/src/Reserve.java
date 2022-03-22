import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Reserve extends BaseFrame {

	JLabel img, datelbl;
	JTable t;
	DefaultTableModel m;
	String pf_no;
	String info[] = new String[4];
	String cap[] = "장소,출연,가격,날짜".split(",");

	public Reserve(String p_no) {
		super("예매", 600, 300);
		setLayout(new BorderLayout(5, 5));
		setData(p_no);

		t = table(m = model("p_no,날짜,여유좌석,d".split(",")));

		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);

		t.getColumnModel().getColumn(3).setMinWidth(0);
		t.getColumnModel().getColumn(3).setMaxWidth(0);

		addrow(m,
				"SELECT \r\n" + "    p.p_no, date_format(p.p_date, '%m. %d.'), 60 - COUNT(*), p.p_date\r\n" + "FROM\r\n"
						+ "    2021전국.perform p\r\n" + "        INNER JOIN\r\n" + "    ticket t ON p.p_no = t.p_no\r\n"
						+ "WHERE\r\n" + "    p.p_name LIKE '%" + info[0] + "%'\r\n" + "GROUP BY p.p_no");

		var c = new JPanel(new BorderLayout(10, 5));
		var c_c = new JPanel(new BorderLayout());
		var c_e = new JPanel(new BorderLayout());
		var c_c_c = new JPanel(new GridLayout(0, 1));
		var s = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		add(lbl(info[0], JLabel.LEFT, 25), "North");

		add(c);
		add(s, "South");

		c.add(c_c);
		c.add(c_e, "East");
		c_c.add(c_c_c);

		c_c.add(img = imgLabel("./Datafiles/공연사진/" + pf_no + ".jpg", 120, 160), "West");

		for (int i = 0; i < info.length; i++) {
			var tmp = new JPanel(new FlowLayout(FlowLayout.LEFT));
			var l = lbl(cap[i] + ":" + info[i], JLabel.LEFT, 20);
			if (i == 3) {
				datelbl = l;
			}
			tmp.add(l);
			c_c_c.add(tmp);
		}

		for (int i = 0; i < t.getRowCount(); i++) {
			if (t.getValueAt(i, 3).toString().equals(info[3])) {
				t.addRowSelectionInterval(i, i);
			}
		}

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (t.getSelectedRow() == -1)
					return;

				datelbl.setText("날짜:" + t.getValueAt(t.getSelectedRow(), 3).toString());
				info[3] = t.getValueAt(t.getSelectedRow(), 3).toString();
				p_date = info[3];

				super.mousePressed(e);
			}
		});

		s.add(size(btn("예매하기", a -> {
			if (t.getSelectedRow() == -1)
				return;

			if (!isLogined) {
				if (JOptionPane.showConfirmDialog(null, "회원만 가능한 서비스 입니다.\n로그인 하시겠습니까?", "로그인",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					new Login(null).addWindowListener(new before(this));
					return;
				} else {
					return;
				}
			}

			new Stage().addWindowListener(new before(this));
		}), 150, 30));

		c_e.add(size(new JScrollPane(t), 150, 1), "East");
		c_c.setBorder(new EmptyBorder(2, 2, 2, 2));
		c_c_c.setBorder(new LineBorder(Color.BLACK));
		img.setBorder(new LineBorder(Color.BLACK));
		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	void setData(String p_no) {
		try {
			var rs = stmt.executeQuery("select * from perform where p_no=" + p_no);
			if (rs.next()) {
				pf_no = rs.getString(2);
				info[0] = rs.getString(3);
				p_name = rs.getString(3);
				BaseFrame.p_no = rs.getInt(1);
				info[1] = rs.getString(4);
				price = rs.getInt(5);
				info[2] = new DecimalFormat("#,##0").format(rs.getInt(5));
				info[3] = rs.getString(7);
				p_date = info[3];
				BaseFrame.p_place = rs.getString(4);
				System.out.println(price);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		isLogined = true;
		u_no = 1;
		new Reserve("100");
	}
}
