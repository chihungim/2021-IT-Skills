package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import additional.Util;
import db.DBManager;
import view.SearchPage.OrderItem;

public class PurchasePage extends BasePage {

	DefaultTableModel model = new DefaultTableModel(null, "이미지,상품이름,행사 여부,가격,재고,수량,총합".split(",")) {
		public boolean isCellEditable(int row, int column) {
			return column == 5;
		};

		public java.lang.Class<?> getColumnClass(int columnIndex) {
			return JComponent.class;
		};
	};
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer() {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (value instanceof JComponent) {
				return (JComponent) value;
			} else
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	};

	JTable t = new JTable(model);
	JTextField txt = new JTextField(10);
	DecimalFormat format = new DecimalFormat("#,##0");

	public static class SpinnerEditor extends DefaultCellEditor {
		JSpinner spinner;
		JSpinner.DefaultEditor editor;
		boolean valueSet;
		int up=1;
//        JTextField txt;

		// Initializes the spinner.
		public SpinnerEditor() {
			super(new JTextField());
			spinner = new JSpinner(new SpinnerNumberModel(1, 1, null, up));
//            editor = ((JSpinner.DefaultEditor)spinner.getEditor());
//            textField = editor.getTextField();
//            textField.addFocusListener(new FocusListener() {
//                public void focusGained( FocusEvent fe ) {
//                    System.err.println("Got focus");
//                    //textField.setSelectionStart(0);
//                    //textField.setSelectionEnd(1);
//                    SwingUtilities.invokeLater( new Runnable() {
//                        public void run() {
//                            if ( valueSet ) {
//                                textField.setCaretPosition(1);
//                            }
//                        }
//                    });
//                }
//                public void focusLost(FocusEvent fe) {
//                }
//            });
//            textField.addActionListener(a->stopCellEditing());
		}

		// Prepares the spinner component and returns it.
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			up = (table.getValueAt(row, 2).toString().contentEquals("O"))?2:1;
			int max = Util.rei(table.getValueAt(row, 4).toString());
			spinner.setModel(new SpinnerNumberModel(1, 1, max, up));
			
			spinner.setValue(value);
//            SwingUtilities.invokeLater( new Runnable() {
//                public void run() {
//                    textField.requestFocus();
//                }
//            });
			return spinner;
		}

//        public boolean isCellEditable( EventObject eo ) {
//            System.err.println("isCellEditable");
//            if ( eo instanceof KeyEvent ) {
//                KeyEvent ke = (KeyEvent)eo;
//                System.err.println("key event: "+ke.getKeyChar());
////                textField.setText(String.valueOf(ke.getKeyChar()));
//                //textField.select(1,1);
//                //textField.setCaretPosition(1);
//                //textField.moveCaretPosition(1);
//                valueSet = true;
//            } else {
//                valueSet = false;
//            }
//            return true;
//        }

		// Returns the spinners current value.
		public Object getCellEditorValue() {
			return spinner.getValue();
		}

//        public boolean stopCellEditing() {
//            try {
////                editor.commitEdit();
//                spinner.commitEdit();
//            } catch ( java.text.ParseException e ) {
//                JOptionPane.showMessageDialog(null,
//                    "Invalid value, discarding.");
//            }
//            return super.stopCellEditing();
//        }
	}

	public PurchasePage(int selcNo, JPanel p) {
		super();
		this.add(c = new JPanel(new BorderLayout()));
		this.add(s = new JPanel(new GridLayout(0, 1)), "South");

		JPanel s_t = new JPanel(new FlowLayout(2));
		JPanel s_s = new JPanel(new FlowLayout(2));

		s.add(s_t);
		s.add(s_s);

		c.setOpaque(false);
		s.setOpaque(false);
		s_t.setOpaque(false);
		s_s.setOpaque(false);

		c.add(new JScrollPane(t));
		s_t.add(new JLabel("금액"));
		s_t.add(txt);
		s_t.add(new JLabel("원"));
		txt.setEditable(false);
		txt.setHorizontalAlignment(SwingConstants.RIGHT);
		txt.setBackground(Color.white);

		t.setDefaultRenderer(JComponent.class, dtcr);
		t.getColumnModel().getColumn(5).setCellEditor(new SpinnerEditor());

		s_s.add(Util.sz(Util.btn("구매하기", a -> {
			int sum = 0;
			for (int i = 0; i < t.getRowCount(); i++) {
				sum += Util.rei(t.getValueAt(i, 6));
				int pno = Util
						.rei(DBManager.getOneRs("select p_No from product where p_Name ='" + t.getValueAt(i, 1) + "'"));
				DBManager.execute("insert into purchase values(0, '" + selcNo + "','" + pno + "','" + uno + "','"
						+ t.getValueAt(i, 5) + "','" + t.getValueAt(i, 6) + "', curdate())");
				DBManager.execute(
						"update stock set st_Count = st_Count - '" + t.getValueAt(i, 4) + "' where p_No =" + pno +" and s_No = "+selcNo);
			}

			Util.iMsg("총 " + format.format(sum) + "원 결제 완료되었습니다.");
			mf.addPage(new MainPage());

		}), 60, 25));

		Util.setEmpty(this, 50, 50, 50, 50);

		for (var comp : p.getComponents()) {
			var item = (OrderItem) comp;
			Object row[] = { new JLabel(Util.img("./datafile/image/" + item.name + ".jpg", 80, 80)), item.name, (item.state!=0)?"O":"X",
					item.price, item.stock, (item.state!=0)?2:1, item.price };
			model.addRow(row);
		}

		t.setRowHeight(80);

		t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		t.getTableHeader().setReorderingAllowed(false);
		t.getTableHeader().setResizingAllowed(false);
		
		
		
		t.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getType() == TableModelEvent.UPDATE) {
					final int r = e.getFirstRow();
					final int c = e.getColumn();
					if (c == 5) {
						if (Util.rei(t.getValueAt(r, c)) > Util.rei(t.getValueAt(r, c - 1))) {
							Util.eMsg("재고 개수를 초과하여 구매할 수 없습니다.");
							t.setValueAt(Util.rei(t.getValueAt(r, c - 1)), r, c);
						}
						double st = (t.getValueAt(r, c-3).toString().contentEquals("O"))?0.5:1;
						int price = (int) (Util.rei(t.getValueAt(r, c - 2)) * Util.rei(t.getValueAt(r, c)) * st);
						t.setValueAt(price, r, c + 1);
						int sum = 0;
						for (int i = 0; i < t.getRowCount(); i++) {
							sum += Util.rei(t.getValueAt(i, c + 1));
						}
						txt.setText(format.format(sum));
					}
				}
			}
		});
		int sum = 0;
		for (int i = 0; i < t.getRowCount(); i++) {
			sum += Util.rei(t.getValueAt(i, 6));
		}
		txt.setText(format.format(sum));
	}


	public static void main(String[] args) {
		uno = 1;
		BasePage.mf.addPage(new SearchPage());
		BasePage.mf.setVisible(true);
	}
}
