package View;

import java.util.ArrayList;

public class Reserve extends BaseFrame {
	String s, e; // ������ ����
	// ���ͽ�Ʈ��� ��� �Ⱥ����ָ� ���ҵ�
	ArrayList serial;
	String maps[];
	public Reserve(Map m) {
		super("����", 400, 500);
		serial = m.serials;
		this.s = s;
		this.e = e;
		setVisible(true);
	}

	public static void main(String[] args) {

	}
}
