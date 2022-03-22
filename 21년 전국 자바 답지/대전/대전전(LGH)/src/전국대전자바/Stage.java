package ���������ڹ�;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Stage extends BaseFrame {
	JPanel mainp = new JPanel(new BorderLayout());
	JPanel w = new JPanel(new BorderLayout()), e = new JPanel(new BorderLayout());
	JPanel w_n = new JPanel(), w_c = new JPanel(), w_s = new JPanel(new BorderLayout());
	JPanel e_n = new JPanel(new BorderLayout()), e_c = new JPanel(new BorderLayout()),
			e_s = new JPanel(new BorderLayout());
	JPanel w_s_w = new JPanel(new GridLayout(0, 1, 20, 20)), w_s_e = new JPanel(new GridLayout(0, 10, 3, 3)),
			w_s_n = new JPanel(new FlowLayout(2));
	JPanel e_n_n = new JPanel(new FlowLayout(0)), e_n_c = new JPanel(), e_n_s = new JPanel(new FlowLayout(0)),
			e_c_sub = new JPanel(), e_s_n = new JPanel(new FlowLayout(0)),
			e_s_s = new JPanel(new GridLayout(0, 2, 5, 5));
	JLabel stagelbl = new JLabel("STAGE", JLabel.CENTER), datelbl = new JLabel("��¥ : "), dateidx = new JLabel(),
			namelbl = new JLabel("����"), poplbl = new JLabel("�ο� �� : "),
			popidx[] = { new JLabel("��"), new JLabel("��"), new JLabel("��"), new JLabel("��"), new JLabel("��") },
			totallbl = new JLabel("�ѱݾ� : ");
	JLabel alphalbl[] = { new JLabel("A"), new JLabel("B"), new JLabel("C"), new JLabel("D"), new JLabel("E"),
			new JLabel("F") };
	JButton btn[] = { new JButton("��������"), new JButton("��������") };
	char alpha = 65;
	int cnt = 0;
	ArrayList<JPanel> stagep = new ArrayList<JPanel>();
	int i = 0;

	public Stage() {
		super("�¼�", 950, 500);

		ui();
		data();
		event();
		setVisible(true);
	}

	private void event() {
		for (int i = 0; i < popidx.length; i++) {
			int idx = i;
			popidx[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					for (int j = 0; j < 5; j++) {
						popidx[j].setText("��");
					}
					for (int j = 0; j < idx + 1; j++) {
						if (popidx[j].getText().equals("��")) {
							popidx[j].setText("��");
							popidx[j].setName((j + 1) + "");
						} else {
							popidx[j].setText("��");
						}
					}

					for (int j = 0; j < 5; j++) {
						if (popidx[0].getText().equals("��")) {
							count = 1;
						}
						if (popidx[1].getText().equals("��")) {
							count = 2;
						}
						if (popidx[2].getText().equals("��")) {
							count = 3;
						}
						if (popidx[3].getText().equals("��")) {
							count = 4;
						}
						if (popidx[4].getText().equals("��")) {
							count = 5;
						}
					}
				}
			});
		}

		for (int i = 0; i < 6; i++, alpha++) {
			for (int j = 0; j < 10; j++) {
				JLabel idx = new JLabel((j + 1) + "");
				idx.setBorder(new LineBorder(Color.black));
				idx.setPreferredSize(new Dimension(55, 55));
				idx.setOpaque(true);
				idx.setBackground(Color.white);
				idx.setHorizontalAlignment(SwingConstants.CENTER);
				idx.setName(alpha + "" + String.format("%02d", j + 1));
				w_s_e.setBorder(new EmptyBorder(0, 10, 5, 0));
				w_s_e.add(idx);

				try {
					ResultSet rs = stmt.executeQuery(
							"select ticket.t_seat from ticket, perform where ticket.p_no = perform.p_no and perform.p_date = '"
									+ date + "' and perform.p_name = '" + p_name + "'");
					while (rs.next()) {
						String seat[] = rs.getString(1).split(",");
						for (int k = 0; k < seat.length; k++) {
							if (idx.getName().equals(seat[k])) {
								idx.setBackground(Color.LIGHT_GRAY);
							}
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				idx.addMouseListener(new MouseAdapter() {
					boolean bool = false;

					public void mousePressed(MouseEvent e) {
						if (idx.getBackground() == Color.LIGHT_GRAY) {
							eMsg("�̹� ���ŵ� �¼��Դϴ�.");
							return;
						}

						if (bool == false) {
							idx.setOpaque(true);
							idx.setBackground(Color.orange);
							cnt++;
							if (cnt > 5) {
								eMsg("�� �̻� ������ �Ұ��մϴ�.");
								idx.setBackground(Color.white);
								cnt = 5;
								return;
							} else {
								p += price;
								totalidx.setText(format.format(p));
								updatePanel(idx.getName(), p_price);
								Stage.this.i++;
							}
						} else {
							idx.setOpaque(true);
							idx.setBackground(Color.white);
							p -= price;
							totalidx.setText(format.format(p));
							cnt--;
							if (cnt == 0)
								cnt = 0;
							removePanel();
							Stage.this.i--;
						}

						bool = !bool;
					}
				});
			}
		}

		btn[1].addActionListener(a -> {
			if (a.getActionCommand().equals("��������")) {
				if (cnt == 0) {
					eMsg("�¼��� �������ּ���.");
					return;
				} else if (cnt != count) {
					eMsg("�ο����� �°� �¼��� �������ּ���.");
					return;
				} else {
					total_price = totalidx.getText();
					new Purchase().addWindowListener(new before(Stage.this));
				}
			} else {
				if (cnt == 0) {
					eMsg("�¼��� �������ּ���.");
					return;
				} else if (cnt != count) {
					eMsg("�ο����� �°� �¼��� �������ּ���.");
					return;
				} else {
//					execute("update perform set p_price = " + totalidx + " where p_name = '" + namelbl.getText()
//							+ "'");
//					execute("update ticket set t_seat = '" + idx.getName() + "'");
					new MyPage().addWindowListener(new before(Stage.this));
				}
			}
		});
		btn[0].addActionListener(a -> {
			dispose();
		});

	}

	private void data() {
		e.setPreferredSize(new Dimension(300, 500));
		stagelbl.setFont(new Font("����", Font.BOLD, 25));
		dateidx.setText(date);

		try {
			ResultSet rs = stmt.executeQuery("select p_place from perform where p_name = '" + p_name + "'");
			while (rs.next()) {
				p_place = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		namelbl.setText(p_name);
		namelbl.setFont(new Font("���� ���", Font.BOLD, 20));

		if (bool == true) {
			btn[0].setText("����ϱ�");
			btn[1].setText("�����ϱ�");
		} else {
			btn[0].setText("��������");
			btn[1].setText("��������");
		}

	}

	private void ui() {
		add(mainp);
		mainp.add(e, "East");
		mainp.add(w, "West");
		e.add(e_c, "Center");
		e.add(e_n, "North");
		e.add(e_s, "South");
		e_n.add(e_n_c, "Center");
		e_n.add(e_n_n, "North");
		e_n.add(e_n_s, "South");
		e_s.add(e_s_n, "North");
		e_s.add(e_s_s, "South");
		w.add(w_c, "Center");
		w.add(w_n, "North");
		w.add(w_s, "South");
		w_s.add(w_s_e, "East");
		w_s.add(w_s_w, "West");
		w_s.add(w_s_n, "North");
		e_c.add(e_c_sub);

		w_n.add(stagelbl);
		w_s_n.add(datelbl);
		w_s_n.add(dateidx);
		for (int i = 0; i < alphalbl.length; i++) {
			alphalbl[i].setFont(new Font("����", Font.BOLD, 15));
			w_s_w.add(alphalbl[i]);
		}
		w_s.setBorder(new EmptyBorder(5, 20, 5, 5));
		w_s_w.setBorder(new EmptyBorder(0, 0, 0, 10));
		e_n_n.add(namelbl);
		e_n_c.add(BaseFrame.size(new JLabel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.drawLine(710, 50, 980, 50);
			}
		}, 280, 10));
		e_n_s.add(poplbl);
		e.setBorder(new LineBorder(Color.black));
		for (int i = 0; i < popidx.length; i++) {
			e_n_s.add(popidx[i]);
		}
		Choose choose = new Choose();
		e_c_sub.add(choose);
		e_s_n.add(totallbl);
		e_s_n.add(totalidx);
		e_s_s.add(btn[0]);
		e_s_s.add(btn[1]);
		e_s_s.setBorder(new EmptyBorder(0, 0, 5, 0));
	}

	void updatePanel(String name, String price) {
		JLabel lbl = new JLabel(name, JLabel.RIGHT), index = new JLabel(price, JLabel.LEFT),
				down = new JLabel("��", JLabel.RIGHT);
		JCheckBox chk[] = { new JCheckBox("û�ҳ� ���� 20%"), new JCheckBox("��� ���� 40%"), new JCheckBox("����� ���� 50%") };
		JPanel gridP = new JPanel(new GridLayout(0, 1));

		stagep.add(new JPanel(new FlowLayout(FlowLayout.LEFT)));
		stagep.get(stagep.size() - 1).add(size(lbl, 100, 20));
		stagep.get(stagep.size() - 1).add(size(index, 100, 20));
		stagep.get(stagep.size() - 1).add(size(down, 20, 20));
		stagep.get(stagep.size() - 1).add(gridP);
		stagep.get(stagep.size() - 1).setBorder(new LineBorder(Color.black));
		stagep.get(stagep.size() - 1).setPreferredSize(new Dimension(265, 30));
		stagep.get(stagep.size() - 1).setName(Stage.this.i + "");
		e_c_sub.add(stagep.get(stagep.size() - 1));

		down.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (down.getText().equals("��")) {
					down.setText("��");
					stagep.get(stagep.size() - 1).setPreferredSize(new Dimension(265, 110));
					for (int i = 0; i < chk.length; i++) {
						gridP.add(chk[i]);
						chk[0].addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent e) {
								if (e.getStateChange() == ItemEvent.SELECTED) {
									index.setText(format.format(p * 0.8) + "");
								} else {
									index.setText(price);
								}
							}
						});
						chk[1].addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent e) {
								if (e.getStateChange() == ItemEvent.SELECTED) {
									index.setText(format.format(p * 0.6) + "");
								} else {
									index.setText(price);
								}
							}
						});
						chk[2].addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent e) {
								if (e.getStateChange() == ItemEvent.SELECTED) {
									index.setText(format.format(p * 0.5) + "");
								} else {
									index.setText(price);
								}
							}
						});
					}
				} else {
					down.setText("��");
					stagep.get(stagep.size() - 1).setPreferredSize(new Dimension(265, 30));
					for (int i = 0; i < chk.length; i++) {
						gridP.remove(chk[i]);
					}
				}
			}
		});
		repaint();
		revalidate();
	}

	void removePanel() {
		if (stagep.size() != 1) {
			e_c_sub.remove(stagep.remove(stagep.size() - 1));
		} else {
			e_c_sub.removeAll();
		}
		repaint();
		revalidate();
	}

	public static void main(String[] args) {
		isLogined = true;
		new Search();
	}

	class Choose extends JPanel {
		TitledBorder border = new TitledBorder(new LineBorder(Color.black), "������ �¼�");

		public Choose() {
			setLayout(new BorderLayout());
			border.setTitleColor(Color.black);
			border.setTitleFont(new Font("����", Font.BOLD, 15));
			e_c_sub.setBorder(border);
		}

	}

}
