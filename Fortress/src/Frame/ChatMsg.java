package Frame;


// ChatMsg.java ä�� �޽��� ObjectStream ��.

import java.io.Serializable;
import java.util.Vector;

public class ChatMsg implements Serializable {
	private static final long serialVersionUID = 1L;
	public String code; // 100:�α���, 400:�α׾ƿ�, 200:ä�ø޽���, 300:Image, 500: Mouse Event
	public String UserName;
	public String data;
	public Vector<String> UserList;
	public int keycode;
	public int startX[];
	public int startY[];
	public int gaugeCount;
	public double missileAngleX;
	public double missileAngleY;
	public String missileSort;
	
	public ChatMsg(String UserName, String code, String msg) {
		this.code = code;
		this.UserName = UserName;
		this.data = msg;
	}
}