# -*- coding: utf-8 -*-

import clasesAG
import random
import csv

from generated_classes import Charger
from generated_classes import ElectricVehicle

 
import time

# Parameters of the genetic algorithm
population_size = 10
num_generations = 50
num_parents = 3
mutation_rate = 0.2

# Generate chargers list - adapt the number of chargers
resources = [Charger(1), Charger(2), Charger(3), Charger(4), Charger(5), Charger(6),Charger(7), Charger(8)]
#resources = [Robot(1), Robot(2), Robot(3), Robot(4), Robot(5), Robot(6),Robot(7),Robot(8)]


#Specific methods to create objects from files depending on the scenario
#Create vehicle objects from file
def create_vehicles_from_file(file_name):
    vehicles = []
    with open(file_name, 'r') as file:
        lines = file.readlines()
        for line in lines:
            data = line.strip().split(', ')
            if len(data) == 8:
                id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate = map(float, data)
                vehicle = ElectricVehicle(id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate)
                vehicles.append(vehicle)
    return vehicles

#Path of the data file
file_name = 'C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\general_opt_system\\data\\vehicles.txt'
vehicles2 = create_vehicles_from_file(file_name)
print(vehicles2)

# def create_crops_from_file(file_name):
#     crops = []
#     with open(file_name, 'r') as file:
#         lines = file.readlines()
#         for line in lines:
#             data = line.strip().split(', ')
#             if len(data) == 8:
#                 id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate = map(float, data)
#                 vehicle = Crop(id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate)
#                 crops.append(vehicle)
#     return crops

# file_name = 'C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\general_opt_system\\data\\crops.txt'
# vehicles = create_crops_from_file(file_name)


#General method to create objects from data files


def create_objects_from_csv(file_path, cls, field_mapping=None):
    """
    Crea una lista de objetos de una clase a partir de un archivo CSV (compatible con Python 2).

    :param file_path: Ruta al archivo CSV
    :param cls: Clase a instanciar
    :param field_mapping: Diccionario opcional para renombrar campos
    :return: Lista de objetos instanciados
    """
    objects = []

    with open(file_path, 'rb') as csvfile:  # 'rb' en Python 2
        reader = csv.DictReader(csvfile)
        for row in reader:
            # Mapeo de campos si se especifica
            if field_mapping:
                mapped_row = {field_mapping.get(k, k): v for k, v in row.iteritems()}
            else:
                mapped_row = row

            # Conversión de tipos numéricos
            for k, v in mapped_row.iteritems():
                try:
                    if '.' in v:
                        mapped_row[k] = float(v)
                    else:
                        mapped_row[k] = int(v)
                except (ValueError, TypeError):
                    pass  # conservar como string si no se puede convertir

            obj = cls(**mapped_row)
            objects.append(obj)

    return objects

#Path of the data file
file_name = 'C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\general_opt_system\\vehicles_case\\vehicles_data\\vehicles_20.csv'
vehicles = create_objects_from_csv(file_name,ElectricVehicle)
print(vehicles)

# Example of vehicles and prices per hour - Arrival and departure time with mean and variance
arrival_time_media = [9, 8, 12, 12, 14, 13, 12, 15, 9, 8]
arrival_time_variance = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]

#, 12, 14, 21, 22, 20, 15, 9, 6, 4, 17    , 1, 1, 1, 1, 1, 1, 1, 1, 1, 1   2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 , 2, 2, 2    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1

hourly_prices = [0.1, 0.2, 0.15, 0.25, 0.2, 0.15, 0.1, 0.1, 0.2, 0.3, 0.35, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.2, 0.3, 0.35, 0.3, 0.25, 0.2, 0.15]

start_time = time.time()
# Generate initial population
population = clasesAG.generate_initial_population(vehicles2, resources, population_size)

#Print population - Uncomment if necessary
'''print("population")
for sol in population:
  for it in sol:
    print("identificador vehiculo: "+ str(it.vehiculo.id))
    print("inicio "+str(it.tiempo_inicio))
    print("fin "+str(it.tiempo_fin))
    print("charger "+str(it.charger.id))
    print("")

  print("-------------------------------------------------")'''
  

# Run genetic algorithm - uncomment to execute
for generation in range(num_generations):
    evaluations = clasesAG.evaluate_fitness(population, hourly_prices, arrival_time_media, arrival_time_variance)
    parents = clasesAG.parents_selection(evaluations, num_parents)

    nueva_population = []
    for i in range(population_size):
        parent1 = random.choice(parents)
        parent2 = random.choice(parents)
        son = clasesAG.parents_crossover(parent1, parent2, resources)
        son_mutated = clasesAG.mutate_son(son, mutation_rate,resources)
        nueva_population.append(son_mutated)

    population = nueva_population

# Get the best solution found
evaluations = clasesAG.evaluate_fitness(population, hourly_prices, arrival_time_media, arrival_time_variance)
evaluations.sort(key=lambda x: (x[1], x[2]))
best_sol = evaluations[0]
end_time = time.time()

print("#########################################################################")

# Print the solution
print("Total cost: " + str(best_sol[1]))
print("Total time: " + str(best_sol[2]))
print("Timespan " + str(best_sol[3]) )
print("Plan:")

""" for item in best_sol[0]:
  print(item.vehiculo.id)
  print(item.tiempo_inicio)
  print(item.tiempo_fin)
  print(item.charger.id)
  print("") """
  
  
'''list_model_vehicles = []

for item in best_sol[0]:
    vehicle_data = {
        'id': int(item.vehiculo.id),
        'start_time': int(item.tiempo_inicio*10),
        'end_time': int(item.tiempo_fin*10),
        'charger': int(item.charger.id)
    }
    list_model_vehicles.append(vehicle_data)

# Imprimir la lista de vehículos
for vehicle in list_model_vehicles:
    print(vehicle)'''
  
    
# generate PRISM model - uncomment to execute
#clasesAG.generate_evaluation_prism_model(best_sol[0])
clasesAG.generate_evaluation_model_config(best_sol[0])
'''clasesAG.generate_prism_model(best_sol[0])

execution_time = end_time - start_time
#print(f"Tiempo de ejecución: {execution_time * 1000:.2f} milisegundos")
print("Tiempo total: " + str(execution_time))'''
