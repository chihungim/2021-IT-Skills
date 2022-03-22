package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import db.DBManager;

public class Reserve extends BaseFrame {
	DefaultTableModel m = new DefaultTableModel(null, ",��ȣ,�ⱸ��,����,ž���ο�,Ű ����,���� ����,�ݾ�,�ⱸ����".split(",")) {
		public boolean isCellEditable(int row, int column) {
			return column == 0;
		};

		public Class<?> getColumnClass(int columnIndex) {
			return getValueAt(0, columnIndex).getClass();
		};
	};

	JTable t = new JTable(m) {
		public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
			var comp = (JComponent) super.prepareRenderer(renderer, row, column);
			if (t.getSelectedRow() == row) {
				comp.setBackground(Color.yellow);
			} else
				comp.setBackground(Color.white);
			return comp;
		};
	};
	JTextField txt = new JTextField();
	DecimalFormat f = new DecimalFormat("#,##0");
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	int sum = 0;

	public Reserve() {
		super("����", 650, 500);

		this.add(n = new JPanel(new FlowLayout(2)), "North");
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new FlowLayout(2)), "South");

		n.add(btn("����", a -> {
			int cnt = 0;
			for (int k = 0; k < m.getRowCount(); k++) {
				if (t.getValueAt(k, 0).equals(true)) {

					if (rei(DBManager.getOne("SELECT count(*) FROM ticket where t_date = curdate() and r_no='"
							+ t.getValueAt(k, 1) + "'")) > rei(t.getValueAt(k, 4))) {
						eMsg(t.getValueAt(k, 2) + "��(��) �����Դϴ�.");
						return;
					}

					cnt++;
				}
			}

			if (cnt == 0) {
				eMsg("������ ���̱ⱸ�� �������ּ���.");
				return;
			}

			if (cnt >= 5) {
				iMsg("�����н��� ��÷�Ǿ����ϴ�.");
				int mag = val(Main.lbl[4].getText()) + 1;
				Main.lbl[4].setText("�����н�(" + mag + ")");
				Main.lbl[4].setEnabled(true);
			}

			iMsg("���Ű� �Ϸ�Ǿ����ϴ�.");
			for (int k = 0; k < m.getRowCount(); k++) {
				if (t.getValueAt(k, 0).equals(true)) {
					DBManager.execute(
							"insert into ticket values(0, '" + uno + "',curdate(),'" + t.getValueAt(k, 1) + "',0)");
				}
			}
			this.dispose();
		}));

		c.add(new JScrollPane(t));

		s.add(new JLabel("�ѱݾ� : "));
		s.add(sz(txt, 200, 30));
		txt.setEnabled(false);
		txt.setHorizontalAlignment(SwingConstants.RIGHT);
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		m.setRowCount(0);
		try {
			var rs = DBManager.rs("select * from ride");
			while (rs.next()) {
				if ((uheight >= val(rs.getString("r_height")) && uold >= val(rs.getString("r_old"))
						&& !(val(rs.getString("r_disable")) == 1 && udisable == 1))) {
					Object row[] = new Object[m.getColumnCount()];
					row[0] = false;
					for (int i = 1; i < m.getColumnCount(); i++) {
						t.getColumnModel().getColumn(i).setCellRenderer(dtcr);
						row[i] = rs.getString(i);
					}
					row[row.length - 1] = rs.getString("r_explation");
					m.addRow(row);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int width[] = { 50, 150, 50, 100, 100, 100, 100, 150 };
		for (int j = 1; j < m.getColumnCount(); j++) {
			t.getColumnModel().getColumn(j).setPreferredWidth(width[j - 1]);
		}

		t.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sum = 0;
				for (int k = 0; k < m.getRowCount(); k++) {
					if (t.getValueAt(k, 0).equals(true)) {
						sum += rei(t.getValueAt(k, 7));
					}
				}
				txt.setText(f.format(sum) + "��");
			}
		});

		this.setVisible(true);
	}

	public static void main(String[] args) {
		BaseFrame.setLogin(1);
		new Reserve();
	}

}
