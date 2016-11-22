/*
 * 定义枚举变量，用来表示上下左右四个方向
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
