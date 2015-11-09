package net.nikomiko.space.dlgp32;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class SystemTester
{

	public static void main(String[] args) {
		
		DLGSystem computer = new DLGSystem();
		
		computer.sys.get(0).input(0x0061014F); //
		
		for(int i = 0; i < 40; i++){ //just do 40 ticks
			computer.tick();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		JTextField textField = new JTextField();
		textField.setEditable(false);
		textField.setFont(new Font("Courier New", Font.PLAIN, 12));
		textField.addKeyListener(new KeyListener(){
			
			@Override
			public void keyTyped(KeyEvent e) {
			
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
				System.out.println(e.getKeyChar());
					  
			}

			@Override
			public void keyReleased(KeyEvent e) {
			
			}
			  
			  
			  
			  
		  });
		  JFrame jframe = new JFrame();
		  jframe.add(textField);
		  jframe.setSize(400, 350);
		  jframe.setVisible(true);
	*/	
	}

}
