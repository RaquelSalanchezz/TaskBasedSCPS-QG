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


public class EjecutarPython {
    public static void main(String[] args) {
        // Inicializar el intérprete de Python
        PythonInterpreter interpreter = new PythonInterpreter();

        // Agregar el directorio al sys.path
        interpreter.exec("import sys");
        interpreter.exec("sys.path.append('C:\\\\Users\\\\raque\\\\OneDrive\\\\Escritorio\\\\prueba\\\\hello-world')");
        
        // --------------------------------GENERAR POBLACIÓN INICIAL------------------------
        //----------------------------------------------------------------------------------
        
        // Cargar y ejecutar un script de Python
        //interpreter.execfile("C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\hello-world\\algoritmogenetico.py");
        interpreter.execfile("C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\hello-world\\pruebaGA.py");
        
        // Obtener la variable "poblacion"
        PyObject poblacionPy = interpreter.get("poblacion");
        PyObject cargadoresPy = interpreter.get("cargadores");

        //Imprimir la población inicial
        System.out.println("POBLACION INICIAL");
        String modelo = "modelo";
        // Iterar sobre la población
        for (PyObject solucionPy : poblacionPy.asIterable()) { // Cada solución es una lista
            
        	for (PyObject itemPy : solucionPy.asIterable()) { // Cada item dentro de una solución
                PyObject vehiculo = itemPy.__getattr__("vehiculo");
                PyObject cargador = itemPy.__getattr__("cargador");

                // Acceder a atributos específicos
                int idVehiculo = vehiculo.__getattr__("id").asInt();
                int idCargador = cargador.__getattr__("id").asInt();
                double tiempoInicio = itemPy.__getattr__("tiempo_inicio").asDouble();
                double tiempoFin = itemPy.__getattr__("tiempo_fin").asDouble();

                // Formatear la salida
                System.out.println("Identificador Vehículo: " + idVehiculo);
                System.out.println("Inicio: " + tiempoInicio);
                System.out.println("Fin: " + tiempoFin);
                System.out.println("Cargador: " + idCargador);
                System.out.println(".....");
            }
        	
        }
        
        
        //-------------------------CUERPO DEL ALGORITMO GENÉTICO--------------------------
        //--------------------------------------------------------------------------------
        
        //Obtener los métodos del algoritmo para su uso
        PyObject clasesAG = interpreter.get("clasesAG");
        PyObject generatePrismModel = clasesAG.__getattr__("generate_prism_model"); 
        
        //10 es el número de generaciones, modificar si es necesario
        for (int num = 0; num < 5; num++) {
        	
        	//-----------------------EVALUAR POBLACIÓN---------------------------------
        	
        	// Crear una lista Python para las evaluaciones
        	PyList evaluacionesPython = new PyList();
        	
        	for (PyObject solucionPython : poblacionPy.asIterable()) { // Cada solución es una lista
        		
        		// Llamar a la función generar_modelo
                PyObject resultadoModelo = generatePrismModel.__call__(solucionPython);
                
                //Obtener las propiedades que se quieren evaluar con el modelo
                List<Double> propiedades=new ModelCheckFromFiles().run();
                
                double cost=propiedades.get(0);
                System.err.println("El resultado devuelto coste es "+cost);
                
                double timespan=propiedades.get(1);
                System.err.println("El resultado devuelto coste es "+timespan);
                
                // Crear y añadir tuplas a la PyList
                if(cost>=0 && timespan>=0) {//Solo si la solución es válida
	                evaluacionesPython.add(new PyTuple(new PyObject[]{
	                    solucionPython, new PyFloat(cost), new PyInteger(0), new PyFloat(timespan)
	                }));  
                }
        	} 
        	
        	//En "nuevaPoblación" se guardan las nuevas soluciones recombinadas y mutadas
        	PyList nuevaPoblacion = new PyList();
        	PyList listaPoblacion = (PyList)poblacionPy;
        	
        	//Para cada solución de la población
        	for(int j=0;j<listaPoblacion.size();j++) {
	        	
        		//-------------------------SELECCIÓN DE PADRES----------------------------
        		
	        	// Llamar a la función Python con la lista en Python
	            PyObject seleccionarPadres = clasesAG.__getattr__("seleccionar_padres");
	            PyObject padres = seleccionarPadres.__call__(evaluacionesPython);
	            
	            // Convertir el resultado a PyList
	            PyList padresList = (PyList) padres;
	
	            // Imprimir los padres seleccionados
	            System.out.println("Padres seleccionados:");
	            for (Object padre : padresList) {
	                System.out.println(padre);
	            }
	            
	            //------------------------CRUZAMIENTO DE PADRES-----------------------------
	            
	            Random random = new Random();
	            PyObject padre1 = (PyObject)padresList.get(random.nextInt(padresList.size()));
	            PyObject padre2 = (PyObject)padresList.get(random.nextInt(padresList.size()));
	            
	            PyObject cruzarPadres = clasesAG.__getattr__("cruzar_padres");
	            PyObject hijo = cruzarPadres.__call__(padre1,padre2,cargadoresPy);
	            
	            //-----------------------INTRODUCCIÓN DE MUTACIONES--------------------------
	            PyObject mutarHijo = clasesAG.__getattr__("mutar_hijo");
	            PyObject hijoMutado = mutarHijo.__call__(hijo, new PyFloat(0.2),cargadoresPy);
	            
	            // Imprimir el hijo modificado
	            System.out.println("Hijo mutado:");
	            System.out.println(hijoMutado);
	            
	            //Añadir el hijo a la nueva poblacion
	            nuevaPoblacion.add(hijoMutado);
        	}
        }
        
        
        //------------------------------MOSTRAR LA MEJOR SOLUCIÓN--------------------------------
        //---------------------------------------------------------------------------------------
        
        PyList evaluacionesPython = new PyList();
    	
    	//-----------------------------EVALUAR POBLACIÓN--------------------------------
    	for (PyObject solucionPython : poblacionPy.asIterable()) { // Cada solución es una lista
    		
    		// Llamar a la función generar_modelo
            PyObject resultadoModelo = generatePrismModel.__call__(solucionPython);
            
            //Obtener las propiedades que se quieren evaluar con el modelo
            List<Double> propiedades=new ModelCheckFromFiles().run();
            
            double cost=propiedades.get(0);
            System.err.println("El resultado devuelto coste ess "+cost);
            
            double timespan=propiedades.get(1);
            System.err.println("El resultado devuelto coste ess "+timespan);
            
            // Crear y añadir tuplas a la PyList
            if(cost>=0 && timespan>=0) {//Solo si la solución es válida
	            evaluacionesPython.add(new PyTuple(new PyObject[]{
	                solucionPython, new PyFloat(cost), new PyInteger(0), new PyFloat(timespan)
	            }));  
            }
    	} 
    	
    	    	
	    //-------------------- SELECCIÓN DE LA MEJOR SOLUCIÓN----------------------------
	    
    	// Llamar a la función Python con la lista en Python
	    PyObject seleccionarPadres = clasesAG.__getattr__("seleccionar_padres");
	    PyObject padres = seleccionarPadres.__call__(evaluacionesPython);
	            
	    // Convertir el resultado a PyList
	    PyList padresList = (PyList) padres;
	
	    // Imprimir los padres seleccionados
	    System.out.println("Padres seleccionados:");
	    for (Object padre : padresList) {
	         System.out.println(padre);
	     }
	    
	    //Obtención de la mejor solución
	    PyObject mejorSol = (PyObject)padresList.get(0);
	    
	    //-----------------IMPRIMIR MEJOR SOLUCIÓN--------------------
	    System.out.println("------------MEJOR SOLUCIÓN--------------");
	    System.out.println("----------------------------------------");
	    for (PyObject itemPy : mejorSol.asIterable()) { // Cada item dentro de una solución
            PyObject vehiculo = itemPy.__getattr__("vehiculo");
            PyObject cargador = itemPy.__getattr__("cargador");

            // Acceder a atributos específicos
            int idVehiculo = vehiculo.__getattr__("id").asInt();
            int idCargador = cargador.__getattr__("id").asInt();
            double tiempoInicio = itemPy.__getattr__("tiempo_inicio").asDouble();
            double tiempoFin = itemPy.__getattr__("tiempo_fin").asDouble();

            // Formatear la salida
            System.out.println("Identificador Vehículo: " + idVehiculo);
            System.out.println("Inicio: " + tiempoInicio);
            System.out.println("Fin: " + tiempoFin);
            System.out.println("Cargador: " + idCargador);
            System.out.println(".....");
        }
	    
	    PyObject resultadoModelo = generatePrismModel.__call__(mejorSol);
	    //Obtener las propiedades que se quieren evaluar con el modelo
        List<Double> propiedades=new ModelCheckFromFiles().run();
        
        double cost=propiedades.get(0);
        System.err.println("El coste de la mejor solución es "+cost);
        
        double timespan=propiedades.get(1);
        System.err.println("El timespan de la mejor solución es "+timespan);
              
        // Cerrar el intérprete
        interpreter.close();
    }
}
