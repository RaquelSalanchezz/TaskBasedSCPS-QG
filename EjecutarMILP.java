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


public class EjecutarMILP {
    public static void main(String[] args) {
        // Inicializar el intérprete de Python
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile("C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\hello-world\\algoritmoMILP.py");
        
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