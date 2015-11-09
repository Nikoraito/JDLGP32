package net.nikomiko.space.dlgp32;

import java.util.ArrayList;

public class DLGSystem
{
	ArrayList<DLGComponent> sys = new ArrayList<DLGComponent>();
	
	long timei, timej, tickTime;
	private int[] commands;
	private byte[] flags;
		//1		: invalid param y 
		//2		: invalid param x
		//4		: invalid command
		//8		: invalid subsystem
		//16	: waiting for line
		//32	: loading block
		//64	: passing value
		//128	: program running
	private boolean running = false;
	
	public DLGSystem(){		
		
							
		//THERE HAS TO BE A BETTER WAY THAN ARRAYLISTS GOOD GOD THEY'RE SO SLOW
		//Is that maybe okay though if I make ticks only happen intermittently
		
		sys.add(new DLGP32()); //always 0x00
		sys.add(new DLGMemory()); //always 0x01
		sys.add(new DLGTerminal()); //always 0x02

		commands = new int[sys.size()]; 	//create a list of commands to be executed. 
											//whenever the system ticks, it iterates through this list.
											//NOTE: ints being passed between modules also are put into this list -
											//For instance, 0x0060
		
		flags = new byte[sys.size()];		//a list of our flags from our subsystems. We can use this for shit like
											//turning error lights on or feeding that info to the terminal.
		
	}
	
	public void tick(){
		timei = System.nanoTime();
		if(commands.length != sys.size()) commands = new int[sys.size()];	
			//During each tick, each subsystem may execute only one command, so we ensure 
			//our array and list of components are equivalent in length
		
		

		
		for(int i = 0; i < commands.length; i++){	//iterate through the commands...
			
			if((flags[i]&0b01000000) == 0b01000000){
				//If command[i] is actually marked as a value, don't check the subsystem byte in the command - 
				//instead, check to see if any subsystems are expecting to receive an int. 
				//All receipt-flagged subsystems will receive it.
				
				for(DLGComponent g : sys){	//check each subsystem in our list...
					if((g.getFlags()&0b00100000) == 0b00100000 || (g.getFlags()&0b00010000) == 0b00010000 ) {	//and if that subsystem's flagged to receive DATA,
						g.input(commands[i]);	//Send the int to the subsystem.
					} 
				}
			} 
			else if(commands[i] != 0) check:{
				
				for(DLGComponent g : sys){	//check each subsystem in our list...
					if(g.getSubsystem() == (byte)(((commands[i]>>24)&0xFF)) ) {	//and if that subsystem's signature matches the subsystem in the command...
						g.input(commands[i]);	//Send the command to the subsystem...
						break check;	//and stop checking through the list.
					} 
				
				}
			
				System.out.println("Subsystem " + (((commands[i]>>24)&0xFF)) + " is invalid!"); //address must be invalid if we don't break before this point.
				
			}
			
		}
		
		for(int i = 0; i < commands.length; i++){	
			commands[i] = sys.get(i).output();	//collect the outputs from each sub and put them into a list 
		}
		
		for(int i = 0; i < flags.length; i++){	
			flags[i] = sys.get(i).getFlags();	//collect the flags from each sub after their respective operations and put them into a list 
		}
		
		
		timej = System.nanoTime();
		System.out.println(timej-timei);
	
	}

	
}
