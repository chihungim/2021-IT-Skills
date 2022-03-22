package View;

import java.util.ArrayList;

public class Reserve extends BaseFrame {
	String s, e; // 시작점 끝점
	// 다익스트라로 경로 안보내주면 못할듯
	ArrayList serial;
	String maps[];
	public Reserve(Map m) {
		super("예매", 400, 500);
		serial = m.serials;
		this.s = s;
		this.e = e;
		setVisible(true);
	}

	public static void main(String[] args) {

	}
}
