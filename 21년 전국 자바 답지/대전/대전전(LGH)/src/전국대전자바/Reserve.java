package 전국대전자바;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Reserve extends BaseFrame {
	Object info[];
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(0)), c = new JPanel(new BorderLayout()), s = new JPanel(new FlowLayout(2));
	JPanel c_w = new JPanel(new BorderLayout()), c_e = new JPanel(new BorderLayout());
	JPanel c_w_w = new JPanel(), c_w_e = new JPanel();
	JPanel c_w_e_w = new JPanel(new GridLayout(0, 1, 30, 30)), c_w_e_e = new JPanel(new GridLayout(0, 1, 30, 30));
	JLabel namelbl = new JLabel(), imglbl = new JLabel(), placelbl = new JLabel("장소 : "),
			appearlbl = new JLabel("출연 : "), pricelbl = new JLabel("가격 : "), datelbl = new JLabel("날짜 : ");
	JLabel idx[] = { new JLabel(), new JLabel(), new JLabel(), new JLabel() };
	DefaultTableModel m = model("p_date,날짜,여유좌석".split(","));
	JTable t = table(m);
	JScrollPane scr = new JScrollPane(t);
	JButton btn = new JButton("예매하기");

	String name = "";

	public Reserve() {
		super("예매", 600, 330);

		tproperty();
		ui();
		data();
		event();
		setVisible(true);
	}

	public Reserve(Object info[]) {
		super("예매", 600, 330);
		this.info = info;

		iproperty();
		ui();
		data();
		event();
		setVisible(true);
	}

	private void tproperty() {
		try {
			ResultSet rs = stmt.executeQuery(
					"select pf_no, p_place, p_actor, p_price from perform where p_name = '" + p_name + "'");
			if (rs.next()) {
				imglbl.setIcon(new ImageIcon(
						Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + rs.getString(1) + ".jpg")
								.getScaledInstance(150, 160, Image.SCALE_SMOOTH)));
				imglbl.setBorder(new LineBorder(Color.black));
				namelbl.setText(p_name);
				idx[0].setText(rs.getString(2));
				idx[1].setText(rs.getString(3));
				idx[2].setText(format.format(rs.getInt(4)));
				idx[3].setText(p_date);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> dates = new ArrayList<String>();
		String p_date = "";
		try {
			ResultSet rs1 = stmt.executeQuery("select p_date from perform where p_name='" + namelbl.getText() + "'");
			while (rs1.next()) {
				dates.add(rs1.getString(1));
			}
			for (int i = 0; i < dates.size(); i++) {
				ResultSet rs = stmt.executeQuery(
						"select p_date, date_format(p_date, '%m. %d.') , t.t_seat from perform p, ticket t where  p.p_no = t.p_no and p.p_name = '"
								+ namelbl.getText() + "' and p.p_date ='" + dates.get(i) + "' order by p_date asc");
				if (rs.next()) {
					int total_seat = 60;
					date = "";
					do {
						total_seat -= rs.getString(3).split(",").length;
						date = rs.getString(2);
						p_date = rs.getString(1);
					} while (rs.next());
					addrow(m, p_date, date, total_seat + "");
				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void iproperty() {
		imglbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./Datafiles/공연사진/" + info[0] + ".jpg")
				.getScaledInstance(150, 160, Image.SCALE_SMOOTH)));
		namelbl.setText(info[1] + "");
		idx[0].setText(info[2] + "");
		idx[1].setText(info[4] + "");
		idx[2].setText(format.format(info[3]));
		idx[3].setText(info[5] + "");

		ArrayList<String> dates = new ArrayList<String>();
		String p_date = "";
		try {
			ResultSet rs = stmt.executeQuery("select p_date from perform where p_name = '" + namelbl.getText() + "'");
			while (rs.next()) {
				dates.add(rs.getString(1));
			}

			for (int i = 0; i < dates.size(); i++) {
				ResultSet rs2 = stmt.executeQuery(
						"select p_date, date_format(p_date, '%m. %d.') , t.t_seat from perform p, ticket t where  p.p_no = t.p_no and p.p_name = '"
								+ info[1] + "' and p.p_date ='" + dates.get(i) + "' order by p_date asc");
				if (rs2.next()) {
					int total_seat = 60;
					date = "";
					do {
						total_seat -= rs2.getString(3).split(",").length;
						date = rs2.getString(2);
						p_date = rs2.getString(1);
					} while (rs2.next());
					addrow(m, p_date, date, total_seat + "");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				idx[3].setText(t.getValueAt(t.getSelectedRow(), 0) + "");
			}
		});

		btn.addActionListener(a -> {
			if (isLogined == false) {
				int yesno = JOptionPane.showConfirmDialog(null, "회원만이 가능한 서비스 입니다.\n로그인 하시겠습니까?", "로그인",
						JOptionPane.YES_NO_OPTION);
				if (yesno == JOptionPane.YES_OPTION) {
					new Login().addWindowListener(new before(Reserve.this));
				}

			} else {
				try {
					ResultSet rs = stmt.executeQuery("select p_no, p_price from perform where p_name = '"
							+ namelbl.getText() + "' and curdate() < '" + t.getValueAt(t.getSelectedRow(), 0) + "'");
					if (rs.next()) {
						p_name = namelbl.getText();
						p_no = rs.getInt(1);
						price = rs.getInt(2);
						date = idx[3].getText();
						p_price = idx[2].getText();
						new Stage().addWindowListener(new before(Reserve.this));
					} else {
						eMsg("종료된 공연입니다.");
						return;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private void data() {
		namelbl.setFont(new Font("굴림", Font.BOLD, 25));
		placelbl.setFont(new Font("굴림", Font.BOLD, 15));
		appearlbl.setFont(new Font("굴림", Font.BOLD, 15));
		pricelbl.setFont(new Font("굴림", Font.BOLD, 15));
		datelbl.setFont(new Font("굴림", Font.BOLD, 15));
		for (int i = 0; i < idx.length; i++) {
			idx[i].setFont(new Font("굴림", Font.BOLD, 15));
		}
		c_w.setBorder(new LineBorder(Color.black));
		scr.setBorder(new LineBorder(Color.black));
		scr.setPreferredSize(new Dimension(200, 0));
		c_w_e.setBorder(new EmptyBorder(0, 0, 0, 10));
		c.setBorder(new EmptyBorder(0, 5, 10, 5));
		s.setBorder(new EmptyBorder(0, 0, 10, 0));
		btn.setPreferredSize(new Dimension(200, 30));
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);

	}

	private void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");
		mainp.add(s, "South");
		c.add(c_w, "West");
		c.add(c_e, "East");
		c_w.add(c_w_w, "West");
		c_w.add(c_w_e, "East");
		c_w_e.add(c_w_e_w, "West");
		c_w_e.add(c_w_e_e, "Eest");
		n.add(namelbl);
		c_w_w.add(imglbl);
		c_w_e_w.add(placelbl);
		c_w_e_w.add(appearlbl);
		c_w_e_w.add(pricelbl);
		c_w_e_w.add(datelbl);
		for (int i = 0; i < idx.length; i++) {
			c_w_e_e.add(idx[i]);
		}
		c_e.add(scr);
		s.add(btn);
	}

	public static void main(String[] args) {
		isLogined = true;
		new Search();
	}
}
