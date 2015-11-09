package net.nikomiko.space.dlgp32;

public class DLGTerminal extends DLGComponent
{

	private int[] mem;
	private byte subsystem, command, x, y;
	private int out;
	
	DLGTerminal(){
		
		subsystem = 0x02;
		mem = new int[256];
		
	}
	
	@Override
	public void input(int in) {
		x = (byte)((in >> 8) & 0xFF); 
		y = (byte)((in) & 0xFF); 
		
		switch((in>>16)&0xFF){
			
			case 0x00:	break;
			case 0x01: 	System.out.println("0x" + Integer.toHexString(in)); 
						break;
						
			case 0x02: 	break;
			
			default: 	break;
			
		}
	}

	@Override
	public int output() {

		return out;
	}

	@Override
	public byte getSubsystem() {

		return subsystem;
	}
	
	@Override
	public byte getFlags() {
		return 0;
	}

}
