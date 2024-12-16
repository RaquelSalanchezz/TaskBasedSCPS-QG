# Task Based CPS Planning Quantitative Guarantees under Multiple Sources of Uncertainty

## Abstract
In smart Cyber-Physical Systems (sCPS), planning under uncertainty is a complex challenge addressed through approaches that handle various constraints and uncertainty sources, such as models, environment, sensing, or the temporal availability of components. These methods often (i) address each source of uncertainty separately, (ii) use scalable techniques like genetic algorithms or reinforcement learning, though without guarantees of success, or (iii) employ methods like probabilistic model checking, which provide quantitative guarantees but are not easily scalable.

This paper introduces a method for task-based CPS planning that combines genetic algorithms with statistical model checking to generate scalable plans offering quantitative guarantees under defined levels of uncertainty from multiple sources. The results demonstrate that the proposed approach surpasses a state-of-the-art uncertainty-aware genetic algorithm baseline, delivering stronger assurances of meeting system objectives with a modest increase in computational cost.

## Dependencies

The project is implemented using the latest version of Python (3.12.1) and Java (JDK23). 
The Java project relies on the following libraries and tools to function correctly:
1. **Jython**: A Java implementation of Python, required to bridge the execution of Python scripts within the Java environment. Ensure that `jython-standalone-X.X.X.jar` is included in the project build path.
2. **PRISM Library**: The project utilizes PRISM, a probabilistic model checker, including its components such as `Prism`, `PrismLog`, and related modules for parsing models (`parser.ast`), simulating modules (`simulator`), and handling properties files. To execute the project you must have the PRISM libraries integrated and accessible in the project setup. For more information check https://github.com/prismmodelchecker/prism-api and https://github.com/prismmodelchecker/prism prism.

Python files require the installation of the following libraries for its development and execution:
1. **NumPy** 1.26.2
2. **Matplotlib** 3.8.2


## Repository structure
This repository contains the following items:
* `Readme.md`: this file explaning the code of the project
* `PythonFiles`: this folder contains four files where we can find the clases and the functions needed to execute the algorithms.  
  * `MILP_Algorithm.py`: this file contains the code of the MILP algorithm, that solve the vehicle charging planning problem without considering uncertainty. This algorithm is used as baseline for the experiments.
  * `Genetic_Algorithm.py `: this file contains the code of the original genetic algorithm, that solve the vehicle charging planning problem considering uncertainty.
  * `ClasesAG.py`: this file contains the functions that the new Genetic Statistical Model Checking Algorithm (GSMCA) needs to work.
  * `ClasesAG.py`: this file contains the code to run the new version of the genetic algorithm using Python.
* `JavaAlgorithms`: this folder contains the Java project where de  is implemented. In the src package we can find three files:
  * `ExecuteGeneticAlgorithm.java`: this file handles the execution of the GSMCA.
  * `ExecuteMILP.java`: this file is responsible for launching the MILP algorithm from Java.
  * `ModelCheckFromFiles.java`: this file is responsible for evaluate the model launching PRISM. It consist on a version of a PRISM API example adapted to our project.
*  `Vehicles_Data`: in this folder we can find the data files used to run the experiments.
    * `example_vehicle_objects.txt`: code that contains the data you can modify and add to the algoritms code to conduct experiments. You can copy and paste all the content or part of it directly in the code.
    * `vehicles.txt`: data files used to run the algorithms in the experiments. These are files external to the code.   
* `evaluation_charts.ypinb`: code used to generate the evaluation charts included in the paper.


## Running the Experiments
To run the code, you need to do it within Eclipse IDE for Java and Visual Studio Code for Python or similar environment.The following explains how to run algorithms, with the environments previously installed:

### Download and import the Java project from GitHub into Eclipse
1. First, go to the GitHub repository, click on Code, and select Download ZIP. Extract the downloaded ZIP file to your preferred folder. Alternatively, if you have Git installed, you can clone the repository by using the command "git clone" <repository URL> in your terminal.

2. Once you have the project files, open Eclipse and navigate to File > Import > Existing Projects into Workspace. In the dialog that appears, choose Select root directory and browse to the folder where you extracted the project. Then, click Finish to import it into Eclipse.

3. If the project is not recognized as a Java Project, right-click on it in the Project Explorer, go to Configure > Convert to Java Project, and Eclipse will set it up as a Java Project. Additionally, make sure all required dependencies are configured in the Build Path to avoid errors.

### Running the MILP algorithm from Java
1. **Select the file ExecuteMILP.java**
   - Ensure the correct file is open.

2. **Verify the Path to the Python File (`execfile`)**
   - Double-check that the line executing the Python script uses the correct path in the following format:
     ```java
     interpreter.execfile("C:\\Users\\usuario\\Escritorio\\project\\MILPAlgorithm.py");
     ```
   - Ensure the file `algoritmoMILP.py` exists in the specified location.

3. **Compile and Run the Program**
   - Select **Run As > Java Application**.
   - Eclipse will automatically compile the project and execute the program.

### Running the Genetic Algorithm from Java
To run the genetic algorithm you shoud follow the same stages described in the previous section but replacing the MILP algorithm path by the Genetic Algorithm path.

### Running the Genetic Statistical Model Checking Algorithm
1. **Select the file ExecuteGeneticAlgorithm.java**
   - Ensure the correct file is open.
     
2. **Verify the Paths to the Python Files**
   - Double-check that the lines executing the Python scripts and functions use the correct paths in the following format:
     ```java
     interpreter.execfile("C:\\Users\\usuario\\Escritorio\\project\\geneticAlgorithm.py");
     ```
   - Ensure the Python files (clasesAG.py and runGA.py) exist in the specified location.
     
3. **Compile and Run the Program**
   - Select **Run As > Java Application**.
  
### Execution of the code to generate the charts
You can also generate the evaluation charts using the code found in evaluation_charts.ypinb. To achieve this, simply run the cells in Google Colab or similar environment in order, and various graphs will be displayed as output. If you wish to modify the data to create new graphs, you can do so by editing the data matrices at the beginning of the code that generates each graph.

## Using the data files and generate your own input data
As already shown, there are three files within the 'Data' folder containing sample data for running the algorithms.
* The file `example_vehicle_object.txt` contains example code for creating different objects of the 'Vehicle' class. You can copy and paste its entire content or parts of it to directly create vehicles within the algorithm code without using external files. The file includes up to 150 vehicles and has been used to run various experiments. Each vehicle is initialized with a set of parameters: the vehicle identifier, the distance it has to travel, the approximate arrival and departure times, current load, battery capacity, charging speed, and discharge rate. The parameters are set in that order. Following this structure, we can create new vehicles with new data.
* On its part, the files `vehicles.txt` and `vehicles_data.txt`. These constitute external data files that you can add to the runtime environment to execute the algorithms. In both files each line represents a vehicle, and each parameter is separated from another by a comma (,). The parameters are defined in the order explained in the previous section. You can create new files following this structure or add new lines to the existing ones to test the algorithms
  
     
