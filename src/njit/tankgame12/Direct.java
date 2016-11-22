/*
 * ����ö�ٱ�����������ʾ���������ĸ�����
 */

package njit.tankgame12;

public enum Direct{
	
	UP,
	DOWN,
	LEFT,
	RIGHT;
	public static Direct valueOf(int ordinal) 
	{
	    if (ordinal < 0 || ordinal >= values().length) 
	    {
	        throw new IndexOutOfBoundsException("Invalid ordinal");
	    }
	    return values()[ordinal];
	}

	public static Direct parse(String string) {
		
		if(string.equals("UP"))
			return UP;
		if(string.equals("DOWN"))
			return DOWN;
		if(string.equals("LEFT"))
			return LEFT;
		if(string.equals("RIGHT"))
			return RIGHT;
		
		return null;
	}
}
