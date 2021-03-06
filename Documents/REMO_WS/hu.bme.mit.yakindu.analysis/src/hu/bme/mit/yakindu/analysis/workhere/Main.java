package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		List<String> proposednames = new ArrayList<String>();
		
		List<VariableDefinition> variabledefinitions = new ArrayList<VariableDefinition>();
		List<EventDefinition> eventDefinitions = new ArrayList<EventDefinition>();
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof VariableDefinition) {
				VariableDefinition variabledefinition = (VariableDefinition) content;
				/* 4.3 feladat megoldasa
				 * System.out.println(firstCharToUpper(variabledefinition.getName()));*/
				variabledefinitions.add(variabledefinition);

			}
			if(content instanceof EventDefinition) {
				EventDefinition eventDefinition = (EventDefinition) content;
				/* 4.3-as feladat megoldasa
				 * System.out.println(firstCharToUpper(eventDefinition.getName()));*/
				eventDefinitions.add(eventDefinition);

			}
			/* 2. feladat megoldasa
			if(content instanceof State) {
				State state = (State) content;
				
				EList<Transition> transitions = state.getIncomingTransitions();
				
				if (state.getName() == null) {
					String name = namePropose(state);
					if (proposednames.contains(name)) {
						name = name+proposednames.size();
						System.out.println("N??v javaslat: "+name);
						proposednames.add(name);
					}
					else {
						System.out.println("N??v javaslat "+name);
						proposednames.add(name);
					}
				}
				else {
					System.out.println(state.getName());
					proposednames.add(state.getName());
				}
				
				if (state.getOutgoingTransitions().isEmpty()) {
					System.out.println("["+state.getName()+"] ez egy csapda/nyel?? ??llapot \n");
				}
				else {
					for (int i = 0; i < state.getOutgoingTransitions().size(); i++) {
						System.out.println(state.getName()+"->"+state.getOutgoingTransitions().get(i).getTarget().getName());
					}
					System.out.println();
				}
			}
			*/
		}
		
		System.out.println();
		
		printStartofRunStatechart();	
		printWithEvents(eventDefinitions);		
		printEndofRunstatechartMain();
		printWithVariables(variabledefinitions);
		
		
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}


	public static void printStartofRunStatechart() {
		System.out.println("public class RunStatechart {");
		System.out.println("");
		System.out.println("\tpublic static void main(String[] args) throws IOException {");
		System.out.println("\t\tExampleStatemachine s = new ExampleStatemachine();");
		System.out.println("\t\ts.setTimer(new TimerService());");
		System.out.println("\t\tRuntimeService.getInstance().registerStatemachine(s, 200);");
		System.out.println("\t\ts.init();");
		System.out.println("\t\ts.enter();");
		System.out.println("\t\tScanner scanner = new Scanner(System.in);");
		System.out.println("\t\tString line = null;");
		System.out.println("\t\twhile (true) {");
		System.out.println("\t\t\tline = scanner.nextLine();");	
		System.out.println("\t\t\tswitch (line) {");
	}

	public static void printWithEvents(List<EventDefinition> eventDefinitions) {
		for (int i = 0; i < eventDefinitions.size(); i++) {
			System.out.println("\t\t\tcase \""+eventDefinitions.get(i).getName()+"\":");
			System.out.println("\t\t\t\ts.raise"+firstCharToUpper(eventDefinitions.get(i).getName())+"();");		
			System.out.println("\t\t\t\ts.runCycle();");		
			System.out.println("\t\t\t\tprint(s);");		
			System.out.println("\t\t\tbreak;");
		}
	}
	
	public static void printEndofRunstatechartMain() {
		System.out.println("\t\t\tcase \"exit\":");
		System.out.println("\t\t\t\tscanner.close();");
		System.out.println("\t\t\t\tprint(s);");	
		System.out.println("\t\t\t\tSystem.exit(0);");	
		System.out.println("\t\t\t\tbreak;");	
		System.out.println("\t\t\tdefault:");
		System.out.println("\t\t\t\tbreak;");	
		System.out.println("\t\t\t}");
		System.out.println("\t\t}");
		System.out.println("\t}");
	}

	public static void printWithVariables(List<VariableDefinition> variabledefinitions) {
		System.out.println("public static void print(IExampleStatemachine s) {");
		for (int i = 0; i < variabledefinitions.size(); i++) {
			System.out.println("System.out.println(\""+getfirstCharInUpper(variabledefinitions.get(i).getName())+" = \" + s.getSCInterface().get"+firstCharToUpper(variabledefinitions.get(i).getName())+"());");	
		}
		System.out.println("}");
	}
	
	public static String firstCharToUpper(String string) {
		String temp = string.substring(0,1).toUpperCase() + string.substring(1);
		return temp;
	}
	
	public static String getfirstCharInUpper(String string) {
		String temp = string.substring(0,1).toUpperCase();
		return temp;
	}

	public static String namePropose(State state) {
		return "From"+state.getIncomingTransitions().get(0).getSource().getName()+"With"+state.getIncomingTransitions().get(0).getSpecification();
	}
}
