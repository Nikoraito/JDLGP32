package net.nikomiko.space.dlgp32;

public class DLGP32 extends DLGComponent
{
	//components ALL:
	
	// Receive 32bit int inputs
	// Put out 32bit int outputs
	
	//WHEN IT WANTS TO DO A COMMAND, SET out TO THE COMMAND
	
	//TODO:
	// MAKE Flag based shit a SWITCH statement instead.
	// When expecting data, do switch flags to 0 when received.
	// When running/loading programs, don't switch flag to 0 UNTIL some trigger is reached in the program, phaps 0x00FF0000 - Processor END OF FILE?
	
	
	private int[] mem;
	private byte subsystem, command, x, y;
	private int out = 0;
	
	//contains a CPU, A stick of 1KB Memory, and a Terminal out
	
	//DeLeGatorProcessor32
	public DLGP32(){	
		mem = new int[0x100];
		subsystem = 0x00;
		mem[0xBE] = 0b00000000; //0xBE is where we will keep our flags.
		mem[0xBF] = 0xC0;	//mem[0xBF] is the last memory value before the STACK 0x00 -> 0xFF
		//since the stack itself doesn't need to be shifted around, the
		//number at 0xBF is reserved for keeping track of the current value
		//being used as the top of the stack, like a cursor.
		//For instance, any value placed on the top of the stack is placed
		//at mem[mem[0xBF]++]. 0xBF points to the value at the top of the stack, so
		//we find the value at mem[0xBF], increment it, and then go to that 
		//location in memory, then store a number there. 
	
		
	
	
	}
	
	public DLGP32(byte address){	
		
		mem = new int[0x100];
		subsystem = address;
		mem[0xBE] = 0b00000000;	//under normal operations, our flags are all blank
		
		//1		: invalid param y 					
		//2		: invalid param x					
		//4		: invalid command					
		//8		: invalid subsystem					
		//16	: waiting for data		
		//32	: loading block
		//64	: passing value						
		//128	: program running					
		
		
		
		mem[0xBF] = 0xC0; // mem[0xBF] is a pointer to the start of the stack at 0xC0
		
	}

	@Override
	public void input(int in){
		out = 0;
		
		if(mem[0xBE] > 0){
			if ((mem[0xBE]&0b00000001) == 0b00000001){	//If incorrect y (previous)
			}
			if ((mem[0xBE]&0b00000010) == 0b00000010){	//If incorrect x (previous)
			}
			if ((mem[0xBE]&0b00000100) == 0b00000100){	//If invalid command (previous)
			}
			if ((mem[0xBE]&0b00001000) == 0b00001000){	//If invalid subsystem (previous)
			}
			if ((mem[0xBE]&0b00010000) == 0b00010000){	//If waiting on a single line
				
				mem[mem[0xBF]++] = in;		//Don't register in as a command, store it as data.
				mem[0xBE] &= 0b11101111;	//the item is stored.		
			
			}
			if ((mem[0xBE]&0b00100000) == 0b00100000){	//If Loading...
				//Continue to read from memory until we reach some marker in the code we're loading. 0x7f7f7f7f will be EOF.
				if(in == 0x7f7f7f7f) mem[0xBE]&=0b01111111;
				else{ 
					mem[mem[0xBF]++] = in;
					out = (mem[0xB0] << 24) | (0x50 << 16) | (mem[0xB1]++ << 8);
				}
				
			}
			if ((mem[0xBE]&0b01000001) == 0b01000000){	//If sending data...
				
			}
			if ((mem[0xBE]&0b10000000) == 0b10000000){	//If running...
				if(mem[mem[0xB0]] == 0x7f7f7f7f){	
					mem[0xB2] = mem[0xB0]; //save the EOF location for whatever reason
					mem[0xBE]&= 0b01111111; // and clear the flag
					
				}
				else {
					input(mem[mem[0xB0]++]); //pick up at, and execute, our next command
				}
				return;	//just return, running a second command or clearing a flag should always be the last thing
			}
		}
		else{
			
			x = (byte)((in >> 8) & 0xFF); 
			y = (byte)((in) & 0xFF); 
			switch((in>>16)&0xFF){
				
				
				//First block is for the literal x and y values
				case 0x00: mem[mem[0xBF]++] = x + y; break;	//every stack command increments the stack pointer.
				case 0x01: mem[mem[0xBF]++] = x - y; break;
				case 0x02: mem[mem[0xBF]++] = x * y; break;
				case 0x03: mem[mem[0xBF]++] = x / y; break;
				case 0x04: mem[mem[0xBF]++] = x << y;break;
				case 0x05: mem[mem[0xBF]++] = x >> y;break;
				case 0x06: mem[mem[0xBF]++] = x ^ y; break;
				case 0x07: mem[mem[0xBF]++] = x | y; break;
				case 0x08: mem[mem[0xBF]++] = x & y; break;
				/*case 0x09: break;
				case 0x0a: break;
				case 0x0b: break;
				case 0x0c: break;
				case 0x0d: break;
				case 0x0e: break;
				case 0x0f: break;*/
				
				//second block is for pointer x and literal y
				case 0x10: mem[mem[0xBF]++] = mem[x] + y; break;
				case 0x11: mem[mem[0xBF]++] = mem[x] - y; break;
				case 0x12: mem[mem[0xBF]++] = mem[x] * y; break;
				case 0x13: mem[mem[0xBF]++] = mem[x] / y; break;
				case 0x14: mem[mem[0xBF]++] = mem[x] << y;break;
				case 0x15: mem[mem[0xBF]++] = mem[x] >> y;break;
				case 0x16: mem[mem[0xBF]++] = mem[x] ^ y; break;
				case 0x17: mem[mem[0xBF]++] = mem[x] | y; break;
				case 0x18: mem[mem[0xBF]++] = mem[x] & y; break;
				/*case 0x19: break;
				case 0x1a: break;
				case 0x1b: break;
				case 0x1c: break;
				case 0x1d: break;
				case 0x1e: break;
				case 0x1f: break;*/
				
				//third block is both pointers to 32bit
				case 0x20: mem[mem[0xBF]++] = mem[x] + mem[y]; break;
				case 0x21: mem[mem[0xBF]++] = mem[x] - mem[y]; break;
				case 0x22: mem[mem[0xBF]++] = mem[x] * mem[y]; break;
				case 0x23: mem[mem[0xBF]++] = mem[x] / mem[y]; break;
				case 0x24: mem[mem[0xBF]++] = mem[x] << mem[y];break;
				case 0x25: mem[mem[0xBF]++] = mem[x] >> mem[y];break;
				case 0x26: mem[mem[0xBF]++] = mem[x] ^ mem[y]; break;
				case 0x27: mem[mem[0xBF]++] = mem[x] | mem[y]; break;
				case 0x28: mem[mem[0xBF]++] = mem[x] & mem[y]; break;
				/*case 0x29: break;
				case 0x2a: break;
				case 0x2b: break;
				case 0x2c: break;
				case 0x2d: break;
				case 0x2e: break;
				case 0x2f: break;*/
				
				
				
				
				//memory commands
				case 0x50: out = mem[x]; break; 								// output a command from a cpu address
				case 0x51: out = mem[mem[0xBF]++]; break; 						// send a command from the top of the stack 
				case 0x52: out = (x<<24) + (mem[y]&0x00FFFFFF); break; 			// select subsystem with x, send it a command defined in mem[y] by replacing the subsystem.
				case 0x53: out = (x<<24) + mem[mem[0xBF]++]&0x00FFFFFF; break; 	// select a subsystem with x, send it a command stored on the stack
				
				//0x60 RUN: 
				case 0x60: 	mem[0xBE] |= 0b10000000; 	//add "running" flag
							//code to run a block of commands from our cpu's memory 
							mem[0xB0] = x; 								// save the address we're running from							
							break; 						
							
				//0x61 XLD: begin loading a block from an external source
				case 0x61: 	mem[0xBE] |= 0b00100000; 					// add "loading" flag
							out = (x << 24) | (0x50 << 16) | (y << 8); 	// send command 0x50 to sub 0xXX to get the data at pointer 0xYY
							mem[0xB0] = x; 								// save the subsystem we're getting information from 
							mem[0xB1] = y;								// save y as the next location we will ask for 
							break;
							
				//0x62 XLN: Load a single int from an external source
				case 0x62: 	mem[0xBE] |= 0b00010000; 					// add "waiting" flag
							out = (x << 24) | (0x50 << 16) | (y << 8); 	// send command 0x50 to sub 0xXX to get the data at pointer 0xYY
							mem[0xB0] = x; 								// save the subsystem we last got information from 
							mem[0xB1] = y;								// save the last address we requested 
							break;	
							
											
				//case 0xff: out = 0xFFFFFF00 | mem[0xBE]; break; 				//SEND FLAGS TO THE SYSTEM

				default: mem[0xBE] = mem[0xBE]|0b100; break;	//saves the flag 0b100 - invalid command
			}
		}	
		
		System.out.println(Integer.toHexString(mem[0xBF]-1) +" is "+ Integer.toHexString(mem[mem[0xBF]-1]));
		
	}
	
	@Override
	public int output(){
	
		
		return out;

		
	}

	@Override
	public byte getSubsystem() {
		
		return subsystem;
		
	}

	@Override
	public byte getFlags() {
		
		return (byte)(mem[0xBE]&0x0000007F);
	
	}
	
}
