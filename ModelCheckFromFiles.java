//==============================================================================
//	
//	Copyright (c) 2017-
//	Authors:
//	* Dave Parker <d.a.parker@cs.bham.ac.uk> (University of Birmingham)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PRISM.
//	
//	PRISM is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	PRISM is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License
//	along with PRISM; if not, write to the Free Software Foundation,
//	Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//	
//==============================================================================

package demos;


import prism.Prism;
import prism.PrismDevNullLog;
import prism.PrismException;
import prism.PrismFileLog;
import prism.PrismLog;
import prism.Result;
import prism.UndefinedConstants;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

import javax.swing.event.ListSelectionEvent;

import parser.ast.ModulesFile;
import parser.ast.PropertiesFile;
import simulator.ModulesFileModelGenerator;
import simulator.SimulatorEngine;
import simulator.method.CIwidth;
import simulator.method.SimulationMethod;

import java.util.List;
import java.util.ArrayList;




/**
 * An example class demonstrating how to control PRISM programmatically,
 * through the functions exposed by the class prism.Prism.
 * 
 * This shows how to load a model from a file and model check some properties,
 * either from a file or specified as a string, and possibly involving constants. 
 * 
 * See the README for how to link this to PRISM.
*/
//public class ModelCheckFromFiles
//{
//
//	public static void main(String[] args)
//	{
//		new ModelCheckFromFiles().run();
//	}
//
//	public void run()
//	{
//		try {
//			// Create a log for PRISM output (hidden or stdout)
//			PrismLog mainLog = new PrismDevNullLog();
//			//PrismLog mainLog = new PrismFileLog("stdout");
//
//			// Initialise PRISM engine 
//			Prism prism = new Prism(mainLog);
//			prism.initialise();
//
//			// Parse and load a PRISM model from a file "C:\Users\raque\OneDrive\Escritorio\modelo.pm"  C:\\Users\\raque\\Downloads\\prism-api-master\\prism-api-master\\examples\\dice.pm
//			ModulesFile modulesFile = prism.parseModelFile(new File("C:\\Users\\raque\\OneDrive\\Escritorio\\modelo.prism"));
//			prism.loadPRISMModel(modulesFile);
//
//			// Parse and load a properties model for the model "C:\Users\raque\OneDrive\Escritorio\propiedades.pctl"  C:\\Users\\raque\\Downloads\\prism-api-master\\prism-api-master\\examples\\dice.pctl
//			PropertiesFile propertiesFile = prism.parsePropertiesFile(modulesFile, new File("C:\\Users\\raque\\OneDrive\\Escritorio\\propiedades.pctl"));
//
//			// Model check the first property from the file
//			System.out.println(propertiesFile.getPropertyObject(0));
//			Result result = prism.modelCheck(propertiesFile, propertiesFile.getPropertyObject(0));
//			System.out.println(result.getResult());
// 
//			// Model check the second property from the file
//			// (which has an undefined constant, whose value we set to 3)
//			List<String> consts = propertiesFile.getUndefinedConstantsUsedInProperty(propertiesFile.getPropertyObject(1));
//			String constName = consts.get(0);
//			Values vals = new Values();
//			vals.addValue(constName, Integer.valueOf(3));
//			propertiesFile.setSomeUndefinedConstants(vals);
//			System.out.println(propertiesFile.getPropertyObject(1) + " for " + vals);
//			result = prism.modelCheck(propertiesFile, propertiesFile.getPropertyObject(1));
//			System.out.println(result.getResult());
//
//			// Model check the second property from the file
//			// (which has an undefined constant, which we check over a range 0,1,2)
//			UndefinedConstants undefConsts = new UndefinedConstants(modulesFile, propertiesFile, propertiesFile.getPropertyObject(1));
//			undefConsts.defineUsingConstSwitch(constName + "=0:2");
//			int n = undefConsts.getNumPropertyIterations();
//			for (int i = 0; i < n; i++) {
//				Values valsExpt = undefConsts.getPFConstantValues();
//				propertiesFile.setSomeUndefinedConstants(valsExpt);
//				System.out.println(propertiesFile.getPropertyObject(1) + " for " + valsExpt);
//				result = prism.modelCheck(propertiesFile, propertiesFile.getPropertyObject(1));
//				System.out.println(result.getResult());
//				undefConsts.iterateProperty();
//			}
//
//			// Model check a property specified as a string propertiesFile, propertiesFile.getPropertyObject(0)
//			propertiesFile = prism.parsePropertiesString(modulesFile, "P=?[F<=5 s=7]");
//			System.out.println(propertiesFile.getPropertyObject(0));
//			result = prism.modelCheckSimulator();
//			System.out.println(result.getResult());
//
//			// Model check an additional property specified as a string
//			String prop2 = "R=?[F s=7]";
//			System.out.println(prop2);
//			result = prism.modelCheck(prop2);
//			System.out.println(result.getResult());
//
//			// Close down PRISM
//			prism.closeDown();
//
//		} catch (FileNotFoundException e) {
//			System.out.println("Error: " + e.getMessage());
//			System.exit(1);
//		} catch (PrismException e) {
//			System.out.println("Error: " + e.getMessage());
//			System.exit(1);
//		}
//	}
//}



//    public static void main(String[] args) {
//        new ModelCheckFromFiles().run();
//    }
//
//    public void run() {
//        try {
//            // Create a log for PRISM output (hidden or stdout)
//            PrismLog mainLog = new PrismDevNullLog();
//            // PrismLog mainLog = new PrismFileLog("stdout");
//
//            // Initialise PRISM engine
//            Prism prism = new Prism(mainLog);
//            prism.initialise();
//
//            // Parse and load a PRISM model from a file
//            ModulesFile modulesFile = prism.parseModelFile(new File("C:\\Users\\raque\\OneDrive\\Escritorio\\modelo.prism"));
//            prism.loadPRISMModel(modulesFile);
//
//            // If there are undefined constants, set their values
////            Values vals = new Values();
////            vals.addValue("N1", 3);  // Adjust or add more constants as needed
////            prism.setPRISMModelConstants(vals);
//
//            // Load the model into the simulator
//            prism.loadModelIntoSimulator();
//            SimulatorEngine sim = prism.getSimulator();
//
//            // Create a new path and take several random steps
//            sim.createNewPath();
//            sim.initialisePath(null);
//            sim.automaticTransition();
//            sim.automaticTransition();
//            sim.automaticTransition();
//            System.out.println("A random path (3 steps):");
//            System.out.println(sim.getPath());
//
//            // Create a new path up to 0.01 time units (example of continuous-time simulation)
//            sim.initialisePath(null);
//            sim.automaticTransitions(0.01, false);
//            System.out.println("A random path (until 0.01 time units elapse):");
//            sim.getPathFull().exportToLog(new PrismPrintStreamLog(System.out), true, ",", null);
//
//            // Create a new path up until a target expression is satisfied
//            // Adjust the target property as needed for your model
////            Expression target = prism.parsePropertiesString("na=2").getProperty(0);
////            sim.initialisePath(null);
////            while (!target.evaluateBoolean(sim.getCurrentState())) {
////                sim.automaticTransition();
////            }
////            System.out.println("\nA random path reaching " + target + ":");
////            sim.getPathFull().exportToLog(new PrismPrintStreamLog(System.out), true, ",", null);
////            System.out.println("Path time is: " + sim.getPath().getTotalTime());
//            
//            
//            
//            
//            
//            
//            
//            
//            // Load and evaluate properties from the PCTL file
//            System.out.println("\nEvaluating properties from PCTL file:");
//            File propertiesFile = new File("C:\\Users\\raque\\OneDrive\\Escritorio\\propiedades.pctl");
//            PropertiesFile props = prism.parsePropertiesFile(propertiesFile);
//
//       
//            // Iterate over each property in the file and evaluate it
//            for (int i = 0; i < props.getNumProperties(); i++) {
//            	Expression property = props.getProperty(i);
//                try {
//                    //property.evaluate();
//                    System.out.println("Property: " + property + " Result: " + property.evaluate());
//                } catch (PrismException e) {
//                    System.out.println("Error evaluating property " + property + ": " + e.getMessage());
//                }
//            }
//            
//           
//            // Close down PRISM
//            prism.closeDown();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("Error: " + e.getMessage());
//            System.exit(1);
//        } catch (PrismException e) {
//            System.out.println("Error: " + e.getMessage());
//            System.exit(1);
//        }
//    }

public class ModelCheckFromFiles {	
	public static void main(String[] args)
	{
		new ModelCheckFromFiles().run();
	}

	public List<Double> run()
	{
		double cost = -1;
		double timespan = -1;
		List<Double> propiedades = new ArrayList<>();
		
		try {
			
			PrismLog mainLog = new PrismFileLog("stdout");
			Prism prism = new Prism(mainLog);
			prism.initialise();
			
			ModulesFile modulesFile = prism.parseModelFile(new File("C:\\Users\\raque\\OneDrive\\Escritorio\\prueba/hello-world\\modelo.prism"));
			
			PropertiesFile propertiesFile = prism.parsePropertiesFile(modulesFile, new File("C:\\Users\\raque\\OneDrive\\Escritorio\\propiedades.pctl"));
			
			SimulatorEngine simEngine = new SimulatorEngine(prism);
			ModulesFileModelGenerator modelGen = new ModulesFileModelGenerator(modulesFile, prism);
			simEngine.loadModel(modelGen, modelGen);
			
			SimulationMethod simMethod = new CIwidth(0.01, 10);
			
			//ESTO ES LO QUE CAMBIA
			//simEngine.modelCheckMultipleProperties(propertiesFile, Collections.singletonList(propertiesFile.getProperty(0)), null, 10000000, simMethod); 
			
			
			// ESTO ES EL CODIGO NUEVO
			
			// Evaluar las propiedades (por ejemplo, la primera propiedad)
            Object result = simEngine.modelCheckMultipleProperties(
                propertiesFile, 
                Collections.singletonList(propertiesFile.getProperty(0)), 
                null, 
                10000000, 
                simMethod
            );

            // Procesar el resultado
            if (result instanceof Object[]) {
                Object[] resultsArray = (Object[]) result;

                // Verifica que la lista no esté vacía y procesa el primer elemento
                if (resultsArray.length > 0 && resultsArray[0] instanceof prism.Result) {
                    prism.Result propertyResult = (prism.Result) resultsArray[0];

                    // Extraer el valor numérico del resultado
                    Object rawValue = propertyResult.getResult(); // Este es el valor del resultado
                    if (rawValue instanceof Number) {
                        cost = ((Number) rawValue).doubleValue();
                        System.out.println("Costo evaluado: " + cost);
                    } else {
                        System.err.println("El valor del resultado no es numérico: " + rawValue);
                    }
                } else {
                    System.err.println("El resultado no contiene un objeto Result válido.");
                }
            } else {
                System.err.println("El resultado devuelto no es un array.");
            }
            
            //--------------------------------------------------
            
         // Evaluar las propiedades (por ejemplo, la segunda propiedad)
            Object result2 = simEngine.modelCheckMultipleProperties(
                propertiesFile, 
                Collections.singletonList(propertiesFile.getProperty(1)), 
                null, 
                10000000, 
                simMethod
            );

            // Procesar el resultado
            if (result2 instanceof Object[]) {
                Object[] resultsArray = (Object[]) result2;

                // Verifica que la lista no esté vacía y procesa el primer elemento
                if (resultsArray.length > 0 && resultsArray[0] instanceof prism.Result) {
                    prism.Result propertyResult = (prism.Result) resultsArray[0];

                    // Extraer el valor numérico del resultado
                    Object rawValue = propertyResult.getResult(); // Este es el valor del resultado
                    if (rawValue instanceof Number) {
                        timespan = ((Number) rawValue).doubleValue();
                        System.out.println("Timespan evaluado: " + timespan);
                    } else {
                        System.err.println("El valor del resultado no es numérico: " + rawValue);
                    }
                } else {
                    System.err.println("El resultado no contiene un objeto Result válido.");
                }
            } else {
                System.err.println("El resultado devuelto no es un array.");
            }
            
            //--------------------------------------------------
            
            
			simEngine.createNewOnTheFlyPath();
			simEngine.initialisePath(null);
			simEngine.automaticTransitions(100, false);

			
		} catch (PrismException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		propiedades.add(cost);
		propiedades.add(timespan);
		
		return propiedades;
	}	
	
}










