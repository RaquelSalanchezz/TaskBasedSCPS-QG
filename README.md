# Task Based CPS Planning Quantitative Guarantees under Multiple Sources of Uncertainty

## Abstract
In smart Cyber-Physical Systems (sCPS), planning under uncertainty is a complex challenge addressed through approaches that handle various constraints and uncertainty sources, such as models, environment, sensing, or the temporal availability of components. These methods often (i) address each source of uncertainty separately, (ii) use scalable techniques like genetic algorithms or reinforcement learning, though without guarantees of success, or (iii) employ methods like probabilistic model checking, which provide quantitative guarantees but are not easily scalable.

This paper introduces a method for task-based CPS planning that combines genetic algorithms with statistical model checking to generate scalable plans offering quantitative guarantees under defined levels of uncertainty from multiple sources. The results demonstrate that the proposed approach surpasses a state-of-the-art uncertainty-aware genetic algorithm baseline, delivering stronger assurances of meeting system objectives with a modest increase in computational cost.

## Dependencies

## Repository structure
This repository contains the following items:
* `Readme.md`: this file explaning the code of the project
* `PythonFiles`: this folder contains four files where we can find the clases and the functions needed to execute the algorithms.  
  * `MILP_Algorithm.py`: this file contains the code of the MILP algorithm, that solve the vehicle charging planning problem without considering uncertainty. This algorithm is used as the basis for the experiments.
  * `Genetic_Algorithm.py  `: this file contains the code of the original genetic algorithm, that solve the vehicle charging planning problem considering uncertainty.
  * `ClasesAG.py  `: this file contains the functions that the new genetic algorithm needs to work.
  * `ClasesAG.py  `: this file contains the code to run the new version of the genetic algorithm.
* `JavaAlgorithms`: this folder contains the project.
*  `Vehicles_Data`: in this folder we can find the data files used to run the experiments.
    * `example_vehicle_objects.txt`: code that contains the data you can modify and add to the algoritms code to conduct experiments. You can copy and paste all the content or part of it directly in the code.
    * `vehicles.txt`: data files used to run the algorithms in the experiments. These are files external to the code.
    
* `evaluation_charts.ypinb`: code used to generate the evaluation charts included in the paper.
* `correlation.ypinb`: code used to evaluate the correlation analysis between variables.
* `Test_results`: this folder contains other two folders that include the results of the executions for the algorithm tests in the two case studies.



## Running the Experiments
