package ������;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import ������.Main.Category;

public class MenuEdit extends BaseFrame {

	int mno = -1;
	String imagePath;
	String saveLocation; // willbesaveto
	JLabel menuImg;

	PlaceHolderTextField txt[] = { new PlaceHolderTextField(15), new PlaceHolderTextField(15),
			new PlaceHolderTextField(15), new PlaceHolderTextField(15) };
	JComboBox<String> type;
	String holders[] = "�޴���,����,�ǸŰ�,�����ð�".split(",");
	DefaultTableModel m;
	JTable t;

	ArrayList<DefaultTableModel> models = new ArrayList<>();

	String bcap[] = "�ɼ� �߰�,�ɼ� ����".split(",");

	JPopupMenu menu = new JPopupMenu();
	JMenuItem item = new JMenuItem("���� �׷� �߰�");

	JPanel c;
	FoodManage fm;

	public MenuEdit(FoodManage fm, boolean isEdit) {
		super(isEdit ? "�޴�����" : "�޴��߰�", 400, 500);
		setLayout(new BorderLayout(5, 5));
		this.fm = fm;
		m = model("��ȣ,�̸�,�ɼǵ�".split(","));

		var n = new JPanel(new BorderLayout(5, 5));
		var n_w = new JPanel(new BorderLayout(5, 10));
		var n_c = new JPanel(new GridLayout(0, 1));

		var s = new JPanel(new BorderLayout(5, 5));
		var s_n = new JPanel(new FlowLayout(FlowLayout.LEFT));

		add(n, "North");
		n.add(n_w, "West");
		n.add(n_c);
		add(c = new JPanel(new BorderLayout()));
		add(s, "South");
		s.add(s_n, "North");

		for (String bc : bcap) {
			s_n.add(btn(bc, a -> {
				if (a.getActionCommand().equals("�ɼ� �߰�")) {
					new AddOption(this).addWindowListener(new Before(this));
				} else {
					if (t.getSelectedRow() == -1) {
						eMsg("������ �ɼ��� �����ؾ��մϴ�.");
					}
				}
			}));
		}

		s.add(btn(isEdit ? "���" : "����", a -> {

		}));

		n_w.add(size(menuImg = new JLabel(), 130, 150));

		for (int i = 0; i < txt.length; i++) {
			n_c.add(txt[i]);
			txt[i].setPlaceHolder(holders[i]);
		}

		n_c.add(type = new JComboBox<>());

		type.setComponentPopupMenu(menu);
		menu.add(item);

		item.addActionListener(a -> {
			String input = JOptionPane.showInputDialog(null, "���� �߰��� ���� �׷� �̸��� �Է��ϼ���.", "�޽���",
					JOptionPane.INFORMATION_MESSAGE);

			if (input == null) {
				return;
			}

			if (input.equals("")) {
				eMsg("�̸��� �Է��ؾ��մϴ�.");
				return;
			}
			if (!input.isEmpty())
				for (int i = 0; i < type.getItemCount(); i++) {
					if (input.equals(type.getItemAt(i))) {
						eMsg("�ߺ��Ǵ� ���� �׷��Դϴ�.");
						return;
					}
				}

			type.addItem(input);
			DefaultTableModel m = model("��ȣ,�̸�,�ɼǵ�".split(","));
			models.add(m);
		});

		try {
			var rs = stmt.executeQuery("SELECT * FROM eats.type where seller = " + sno);
			while (rs.next()) {
				type.addItem(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		menuImg.setBorder(new LineBorder(Color.BLACK));

		menuImg.addMouseListener(new MouseAdapter() {
			int cnt = 0;

			@Override
			public void mouseClicked(MouseEvent e) {
				cnt++;
				if (cnt == 2) {
					FileDialog df = new FileDialog(MenuEdit.this, "", FileDialog.LOAD);
					df.setDirectory(System.getProperty("user.home") + File.separator + "Documents");
					df.setVisible(true);
					File file = new File(df.getDirectory() + df.getFile());
					menuImg.setIcon(new ImageIcon(getImage(file.getPath(), 130, 150)));
					cnt = 0;
				}

				super.mouseClicked(e);
			}
		});

		if (isEdit) {
			mno = toInt(fm.t.getValueAt(fm.t.getSelectedRow(), 0));
			menuImg.setIcon(new ImageIcon(getImage("./�����ڷ�/�޴�/" + mno + ".png", 130, 150)));
			try {
				var rs = stmt.executeQuery("select * from menu where no = " + mno);
				if (rs.next()) {
					txt[0].setText(rs.getString(2));
					txt[1].setText(rs.getString(3));
					txt[2].setText(rs.getString(4));
					txt[3].setText(rs.getString(5));
					type.setSelectedIndex(rs.getInt(7) - 1);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		setData();

		type.addItemListener(a -> {
			c.removeAll();
			t = table(models.get(type.getSelectedIndex()));
			c.add(new JScrollPane(t));
			revalidate();
		});

		t = table(models.get(type.getSelectedIndex()));
		c.add(new JScrollPane(t));
		revalidate();

		((JPanel) (getContentPane())).setBorder(new EmptyBorder(5, 5, 5, 5));
		setVisible(true);
	}

	void setData() {

		if (mno != -1)
			for (int i = 0; i < type.getItemCount(); i++) {
				try {
					var rs = stmt.executeQuery(
							"SELECT m.no, t.name, o.name FROM menu m ,type t, options o where m.type = t.no and o.menu = m.NO and t.name like '%"
									+ type.getItemAt(i) + "%' and m.no=" + mno);

					DefaultTableModel m = model("��ȣ,�̸�,�ɼǵ�".split(","));
					Object row[] = { "", "", "" };
					if (!rs.next()) {
						models.add(m);
						continue;
					}

					rs.previous();

					while (rs.next()) {
						row[0] = rs.getString(1);
						row[1] = rs.getString(2);
						row[2] += "," + rs.getString(3);
					}

					row[2] = row[2].toString().substring(1, row[2].toString().length() - 1);
					m.addRow(row);
					models.add(m);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		else
			for (int i = 0; i < type.getItemCount(); i++) {
				DefaultTableModel m = model("��ȣ,�̸�,�ɼǵ�".split(","));
				models.add(m);
			}

	}
}
