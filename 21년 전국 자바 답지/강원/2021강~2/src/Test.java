import java.awt.Dimension;
import java.util.Comparator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class Test extends javax.swing.JFrame {
	private DefaultTableModel tableModel;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable1;

	public Test() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jScrollPane1.setViewportView(jTable1);
		getContentPane().add(jScrollPane1);

		setPreferredSize(new Dimension(500, 300));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		pack();

		Object[] headers = { "Item", "Fruit" };
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(headers);
		Vector row1 = new Vector();
		row1.add("Item1");
		row1.add("Papaya");
		Vector row2 = new Vector();
		row2.add("Item2");
		row2.add("Orange");
		Vector row3 = new Vector();
		row3.add("Item3");
		row3.add("Apple");
		tableModel.addRow(row1);
		tableModel.addRow(row2);
		tableModel.addRow(row3);

		jTable1.setModel(tableModel);

		jTable1.setAutoCreateRowSorter(true);
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(tableModel);
		sorter.setComparator(1, new SorterComparator<String>());
		jTable1.setRowSorter(sorter);
	}

	private class SorterComparator<E> implements Comparator {
		public int compare(Object s1, Object s2) {
			return ((String) s1).compareTo((String) s2);
		}
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Test().setVisible(true);
			}
		});
	}
}