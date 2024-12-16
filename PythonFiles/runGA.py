# -*- coding: utf-8 -*-

import clasesAG
import random

from clasesAG import Cargador

import time

# Parameters of the genetic algorithm
population_size = 10
num_generations = 50
num_padres = 3
mutation_rate = 0.2

# Generate chargers list - adapt the number of chargers
cargadores = [Cargador(1), Cargador(2), Cargador(3), Cargador(4), Cargador(5), Cargador(6),Cargador(7), Cargador(8)]


#Create vehicle objects from file
def create_vehicles_from_file(file_name):
    vehicles = []
    with open(file_name, 'r') as file:
        lines = file.readlines()
        for line in lines:
            data = line.strip().split(', ')
            if len(data) == 8:
                id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate = map(float, data)
                vehicle = clasesAG.ElectricVehicle(id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate)
                vehicles.append(vehicle)
    return vehicles

file_name = 'C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\hello-world\\vehiculos.txt'
vehiculos = create_vehicles_from_file(file_name)

# Example of vehicles and prices per hour - Arrival and departure time with mean and variance
hora_llegada_media = [9, 8, 12, 12, 14, 13, 12, 15, 9, 8]
hora_llegada_varianza = [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]

#, 12, 14, 21, 22, 20, 15, 9, 6, 4, 17    , 1, 1, 1, 1, 1, 1, 1, 1, 1, 1   2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 , 2, 2, 2    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1

hourly_prices = [0.1, 0.2, 0.15, 0.25, 0.2, 0.15, 0.1, 0.1, 0.2, 0.3, 0.35, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.2, 0.3, 0.35, 0.3, 0.25, 0.2, 0.15]

start_time = time.time()
# Generate initial population
poblacion = clasesAG.generar_poblacion_inicial(vehiculos, cargadores, population_size)

'''#Print population - Uncomment if necessary
print("POBLACION")
for sol in poblacion:
  for it in sol:
    print("identificador vehiculo: "+ str(it.vehiculo.id))
    print("inicio "+str(it.tiempo_inicio))
    print("fin "+str(it.tiempo_fin))
    print("cargador "+str(it.cargador.id))
    print("")

  print("-------------------------------------------------")'''
  

poblacion



# Run genetic algorithm - uncomment to execute
'''for generation in range(num_generations):
    evaluaciones = clasesAG.evaluar_poblacion(poblacion, hourly_prices, hora_llegada_media, hora_llegada_varianza)
    padres = clasesAG.seleccionar_padres(evaluaciones, num_padres)

    nueva_poblacion = []
    for i in range(population_size):
        padre1 = random.choice(padres)
        padre2 = random.choice(padres)
        hijo = clasesAG.cruzar_padres(padre1, padre2,cargadores)
        hijo_mutado = clasesAG.mutar_hijo(hijo, mutation_rate,cargadores)
        nueva_poblacion.append(hijo_mutado)

    poblacion = nueva_poblacion

# Get the best solution found
evaluaciones = clasesAG.evaluar_poblacion(poblacion, hourly_prices, hora_llegada_media, hora_llegada_varianza)
evaluaciones.sort(key=lambda x: (x[1], x[2]))
mejor_sol = evaluaciones[0]
end_time = time.time()

print("#########################################################################")

# Print the solution
print("Coste total: " + str(mejor_sol[1]))
print("Tiempo total: " + str(mejor_sol[2]))
print("Timespan " + str(mejor_sol[3]) )
print("Planificación:")'''

""" for item in mejor_sol[0]:
  print(item.vehiculo.id)
  print(item.tiempo_inicio)
  print(item.tiempo_fin)
  print(item.cargador.id)
  print("") """
  
  
'''list_model_vehicles = []

for item in mejor_sol[0]:
    vehicle_data = {
        'id': int(item.vehiculo.id),
        'start_time': int(item.tiempo_inicio*10),
        'end_time': int(item.tiempo_fin*10),
        'charger': int(item.cargador.id)
    }
    list_model_vehicles.append(vehicle_data)

# Imprimir la lista de vehículos
for vehicle in list_model_vehicles:
    print(vehicle)'''
    
# generate PRISM model - uncomment to execute
'''clasesAG.generate_prism_model(mejor_sol[0])

execution_time = end_time - start_time
#print(f"Tiempo de ejecución: {execution_time * 1000:.2f} milisegundos")
print("Tiempo total: " + str(execution_time))'''