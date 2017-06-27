package Utils;

public class Triplet {
	
	private int a;
	private int b;
	private int c;
	
	public Triplet(int x, int y, int z) {
		a = x;
		b = y;
		c = z;
	}
	
	public boolean isSame(Triplet t)	{
		if (this.a != t.a)	{return false;}
		if (this.b != t.b)	{return false;}
		if (this.c != t.c)	{return false;}
		return true;
	}
}
