package net.nikomiko.space.dlgp32;

public abstract class DLGComponent
{
	//private int[] mem;
	//private byte subsystem, command, x, y;
	
	public abstract void input(int in);
	public abstract int output();
	public abstract byte getSubsystem(); 
	public abstract byte getFlags();
	
}
