
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyPage extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel n = new JPanel(new FlowLayout(0)), c = new JPanel(new GridLayout());
	JLabel name = new JLabel("회원 : "), nameidx = new JLabel(u_name);
	DefaultTableModel m = model("t_no,날짜,공연명,좌석,금액,p_no".split(","));
	JTable t = table(m);
	JScrollPane jsc = new JScrollPane(t);
	JPopupMenu menu = new JPopupMenu();
	JMenuItem item[] = { new JMenuItem("취소"), new JMenuItem("수정") };
	int price = 0;

	public MyPage() {
		super("마이페이지", 600, 500);

		ui();
		data();
		event();

		setVisible(true);
	}

	void event() {
		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e == null)
					return;
				// TODO Auto-generated method stub
				if (e.getButton() == 3) {
					menu.show(t, e.getX(), e.getY());
				}
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				data();
				super.windowActivated(e);
			}
		});

		item[1].addActionListener(a -> {
			if (t.getSelectedRow() == -1)
				return;
			p_no = toInt(t.getValueAt(t.getSelectedRow(), 5).toString());
			p_name = t.getValueAt(t.getSelectedRow(), 2).toString();
			try {
				BaseFrame.price = toInt(
						new DecimalFormat("#,##0").parse(t.getValueAt(t.getSelectedRow(), 4).toString()).toString());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			p_date = t.getValueAt(t.getSelectedRow(), 1).toString();
			isEdit = true;
			new Stage(t.getValueAt(t.getSelectedRow(), 0).toString()).addWindowListener(new before(this));
		});
		item[0].addActionListener(a -> {
			if (t.getSelectedRow() == -1)
				return;
			iMsg("취소되었습니다.");
			execute("delete from ticket where t_no = " + t.getValueAt(t.getSelectedRow(), 0));
			data();
		});
	}

	void data() {
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		addrow(m,
				"SELECT t_no, p_date, p_name, t_seat, format(p_price, '#,##0') ,p.p_no FROM user u, perform p, ticket t where u.u_no = t.u_no and p.p_no = t.p_no and u.u_name = '"
						+ nameidx.getText() + "' order by p.p_date asc");

	}

	void ui() {
		add(mainp);
		mainp.add(n, "North");
		mainp.add(c, "Center");

		n.add(name);
		n.add(nameidx);
		c.add(jsc);
		menu.add(item[0]);
		menu.add(item[1]);

		c.setBorder(new EmptyBorder(0, 15, 15, 15));
		name.setFont(new Font("굴림", Font.BOLD, 20));
		nameidx.setFont(new Font("굴림", Font.BOLD, 20));
		t.getColumnModel().getColumn(0).setMinWidth(0);
		t.getColumnModel().getColumn(0).setMaxWidth(0);
		t.getColumnModel().getColumn(5).setMinWidth(0);
		t.getColumnModel().getColumn(5).setMaxWidth(0);
	}

	public static void main(String[] args) {
		u_no = 1;
		u_name = "강찬석";
		new MyPage().setVisible(true);
	}
}
