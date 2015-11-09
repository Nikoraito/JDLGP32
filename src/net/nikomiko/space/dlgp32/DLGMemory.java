package net.nikomiko.space.dlgp32;

public class DLGMemory extends DLGComponent
{
	
	private int[] mem;
	private byte subsystem, x, y;
	private int out;

	DLGMemory(){
		
		subsystem = 0x01;
		mem = new int[0x100];
		mem[0x4F] = 0xCAFEBABE;
		mem[0x50] = 0xDEADBEEF;
		mem[0x51] = 0x7f7f7f7f;
		
	}
	
	@Override
	public void input(int in) {
		mem[0xBE] &= 0b10111111;
		x = (byte)((in >> 8) & 0xFF); 
		y = (byte)((in) & 0xFF); 	
		
		switch((in>>16)&0xFF){
			case 0x00: break;
			case 0x01: break;
			
			case 0x50: 	mem[0xBE] |= 0b01000000;
						out = mem[x];
						break;
			
			//case 0xff: out = 0xFFFFFF00 | mem[0xBE];  break; //SEND FLAGS TO THE SYSTEM
			
			default: mem[0xFF] = 0b00000100; break;		//send an ERROR flag to the system ("unhandled command in this subsystem")	
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
		return (byte)(mem[0xBE]&0x000000FF);
	}

}
