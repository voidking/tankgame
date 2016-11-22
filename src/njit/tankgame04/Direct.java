/*
 * ����ö�ٱ�����������ʾ���������ĸ�����
 */

package njit.tankgame04;

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

}
