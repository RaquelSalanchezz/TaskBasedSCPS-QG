package demos;
import org.python.util.PythonInterpreter;
import org.python.core.PyObject;

import java.util.ArrayList;
import java.util.List;

import org.python.core.PyList;
import org.python.core.PyTuple;
import org.python.core.PyInteger;
import org.python.core.PyFloat;

import java.util.Random;


public class ExecuteGeneticAlgorithm {
    public static void main(String[] args) {
        // Initialise Python interpreter
        PythonInterpreter interpreter = new PythonInterpreter();

        // Add directory to sys.path - Modify name path
        interpreter.exec("import sys");
        interpreter.exec("sys.path.append('C:\\\\Users\\\\raque\\\\OneDrive\\\\Escritorio\\\\prueba\\\\hello-world')");
        
        // -------------------------------- GENERATE INIITIAL POPULATION ------------------------
        //---------------------------------------------------------------------------------------
        
        // Execute the python script
        //interpreter.execfile("C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\hello-world\\algoritmogenetico.py");
        interpreter.execfile("C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\hello-world\\pruebaGA.py");
        
        // ObtaiN the variable "poblacion"
        PyObject poblacionPy = interpreter.get("poblacion");
        PyObject cargadoresPy = interpreter.get("cargadores");

        //Print initial population
        System.out.println("POBLACION INICIAL");
        String modelo = "modelo";
        
        // Iterate the population
        for (PyObject solucionPy : poblacionPy.asIterable()) { // Each solution is a list
            
        	for (PyObject itemPy : solucionPy.asIterable()) { // For each item in the solution
                PyObject vehiculo = itemPy.__getattr__("vehiculo");
                PyObject cargador = itemPy.__getattr__("cargador");

                // Access to specific attributes
                int idVehiculo = vehiculo.__getattr__("id").asInt();
                int idCargador = cargador.__getattr__("id").asInt();
                double tiempoInicio = itemPy.__getattr__("tiempo_inicio").asDouble();
                double tiempoFin = itemPy.__getattr__("tiempo_fin").asDouble();

                // Print results
                System.out.println("Identificador Vehículo: " + idVehiculo);
                System.out.println("Inicio: " + tiempoInicio);
                System.out.println("Fin: " + tiempoFin);
                System.out.println("Cargador: " + idCargador);
                System.out.println(".....");
            }
        	
        }
        
        
        //------------------------- GENETIC ALGORITHM --------------------------
        //--------------------------------------------------------------------------------
        
        //Obtain Python function
        PyObject clasesAG = interpreter.get("clasesAG");
        PyObject generatePrismModel = clasesAG.__getattr__("generate_prism_model"); 
        
        //Modify number generations: 5
        for (int num = 0; num < 5; num++) {
        	
        	//-----------------------EVALUATE POPULATION---------------------------------
        	
        	// Create Python list for the evaluation
        	PyList evaluacionesPython = new PyList();
        	
        	for (PyObject solucionPython : poblacionPy.asIterable()) { // Each solution is a list
        		
        		// Call model generation function
                PyObject resultadoModelo = generatePrismModel.__call__(solucionPython);
                
                //Obtain properties
                List<Double> propiedades=new ModelCheckFromFiles().run();
                
                double cost=propiedades.get(0);
                System.err.println("El resultado devuelto coste es "+cost);
                
                double timespan=propiedades.get(1);
                System.err.println("El resultado devuelto coste es "+timespan);
                
                // create and add tuples to PyList
                if(cost>=0 && timespan>=0) {//Only if solution is valid
	                evaluacionesPython.add(new PyTuple(new PyObject[]{
	                    solucionPython, new PyFloat(cost), new PyInteger(0), new PyFloat(timespan)
	                }));  
                }
        	} 
        	
        	//In "nuevaPoblación" new solutions are saved
        	PyList nuevaPoblacion = new PyList();
        	PyList listaPoblacion = (PyList)poblacionPy;
        	
        	//For each solution in the population
        	for(int j=0;j<listaPoblacion.size();j++) {
	        	
        		//------------------------- PARENTS SELECTION ----------------------------
        		
	        	// Call parents selection function 
	            PyObject seleccionarPadres = clasesAG.__getattr__("seleccionar_padres");
	            PyObject padres = seleccionarPadres.__call__(evaluacionesPython);
	            
	            // Results to PyList
	            PyList padresList = (PyList) padres;
	
	            // Print parents
	            System.out.println("Padres seleccionados:");
	            for (Object padre : padresList) {
	                System.out.println(padre);
	            }
	            
	            //------------------------ PARENTS CROSSOVER -----------------------------
	            
	            Random random = new Random();
	            PyObject padre1 = (PyObject)padresList.get(random.nextInt(padresList.size()));
	            PyObject padre2 = (PyObject)padresList.get(random.nextInt(padresList.size()));
	            
	            PyObject cruzarPadres = clasesAG.__getattr__("cruzar_padres");
	            PyObject hijo = cruzarPadres.__call__(padre1,padre2,cargadoresPy);
	            
	            //----------------------- MUTATION INTRODUCTION --------------------------
	            PyObject mutarHijo = clasesAG.__getattr__("mutar_hijo");
	            PyObject hijoMutado = mutarHijo.__call__(hijo, new PyFloat(0.2),cargadoresPy);
	            
	            // Print result of the mutation
	            System.out.println("Hijo mutado:");
	            System.out.println(hijoMutado);
	            
	            // Add soon to population
	            nuevaPoblacion.add(hijoMutado);
        	}
        }
        
        
        //------------------------------ SHOW BETTER SOLUTION --------------------------------
        //---------------------------------------------------------------------------------------
        
        PyList evaluacionesPython = new PyList();
    	
    	//-----------------------------EVALUATE POPULATION--------------------------------
    	for (PyObject solucionPython : poblacionPy.asIterable()) { // each solution - a list
    		
    		// Call generate model function
            PyObject resultadoModelo = generatePrismModel.__call__(solucionPython);
            
            // Obtain properties
            List<Double> propiedades=new ModelCheckFromFiles().run();
            
            double cost=propiedades.get(0);
            System.err.println("El resultado devuelto coste ess "+cost);
            
            double timespan=propiedades.get(1);
            System.err.println("El resultado devuelto coste ess "+timespan);
            
            // Create the pylist
            if(cost>=0 && timespan>=0) {//Only if solution is valid
	            evaluacionesPython.add(new PyTuple(new PyObject[]{
	                solucionPython, new PyFloat(cost), new PyInteger(0), new PyFloat(timespan)
	            }));  
            }
    	} 
    	
    	    	
	    //-------------------- BETTER SOLUTION SELECTION ----------------------------
	    
    	// Call to Python funtion to obtain parents
	    PyObject seleccionarPadres = clasesAG.__getattr__("seleccionar_padres");
	    PyObject padres = seleccionarPadres.__call__(evaluacionesPython);
	            
	    // Results to PyList
	    PyList padresList = (PyList) padres;
	
	    // Print parents
	    System.out.println("Padres seleccionados:");
	    for (Object padre : padresList) {
	         System.out.println(padre);
	     }
	    
	    //Obtain better solution
	    PyObject mejorSol = (PyObject)padresList.get(0);
	    
	    //-----------------PRINT BETTER SOLUTION--------------------
	    System.out.println("------------MEJOR SOLUCIÓN--------------");
	    System.out.println("----------------------------------------");
	    for (PyObject itemPy : mejorSol.asIterable()) { // Cada item dentro de una solución
            PyObject vehiculo = itemPy.__getattr__("vehiculo");
            PyObject cargador = itemPy.__getattr__("cargador");

            // Access to attributes
            int idVehiculo = vehiculo.__getattr__("id").asInt();
            int idCargador = cargador.__getattr__("id").asInt();
            double tiempoInicio = itemPy.__getattr__("tiempo_inicio").asDouble();
            double tiempoFin = itemPy.__getattr__("tiempo_fin").asDouble();

            // Print results
            System.out.println("Identificador Vehículo: " + idVehiculo);
            System.out.println("Inicio: " + tiempoInicio);
            System.out.println("Fin: " + tiempoFin);
            System.out.println("Cargador: " + idCargador);
            System.out.println(".....");
        }
	    
	    PyObject resultadoModelo = generatePrismModel.__call__(mejorSol);
	    
	    //Obtain the properties for model evaluation
        List<Double> propiedades=new ModelCheckFromFiles().run();
        
        double cost=propiedades.get(0);
        System.err.println("El coste de la mejor solución es "+cost);
        
        double timespan=propiedades.get(1);
        System.err.println("El timespan de la mejor solución es "+timespan);
              
        // Close the interpreter
        interpreter.close();
    }
}
