package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;


public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		Scanner scanner = new Scanner(System.in);
		String line = null;
		while (true) {
			line = scanner.nextLine();
			switch (line) {
			case "start":
				s.raiseStart();
				s.runCycle();
				print(s);
				break;
			case "white":
				s.raiseWhite();
				s.runCycle();
				print(s);
				break;
			case "black":
				s.raiseBlack();
				s.runCycle();
				print(s);
				break;
			case "exit":
				scanner.close();
				print(s);
				System.exit(0);
				break;
			default:
				break;
			}
		}
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}
