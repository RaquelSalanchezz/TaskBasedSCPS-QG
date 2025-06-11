# -*- coding: utf-8 -*-

import random
from pulp import *
import clasesAG

# Class representing an electric vehicle
class ElectricVehicle:
    def __init__(self, id, distance, available_time_start, available_time_end, current_charge, battery_capacity, charge_speed, discharge_rate):
        self.id = id  # Vehicle identifier
        self.distance = distance  # Distance the vehicle needs to travel
        self.available_time_start = available_time_start  # Start time available for charging
        self.available_time_end = available_time_end  # End time available for charging
        self.current_charge = current_charge  # Current charge level
        self.battery_capacity = battery_capacity  # Battery capacity
        self.charge_speed = charge_speed  # Charging speed
        self.discharge_rate = discharge_rate  # KW discharged per 100 km (efficiency)

    # Calculates and returns the time needed to charge the vehicle enough for the specified distance
    def get_charging_time(self):
        required_charge = self.distance * (self.discharge_rate/100)  # Calculation of the required charge for the distance to be covered

        #Si el coche no tiene carga suficiente
        if self.current_charge < required_charge:
          required_charge -= self.current_charge  # Subtract the current charge
          return required_charge / self.charge_speed  # Time in hours required to charge the vehicle

        else:
          return 0

    # Calculates the time required to charge the vehicle
    def get_charging_cost(self, begin_time, end_time, hourly_prices):
        total_cost = 0
        current_time = begin_time
        while current_time < end_time:
            total_cost += self.charge_speed * hourly_prices[int(current_time)]
            current_time += 1
        return total_cost

    def __str__(self):
      return "Vehículo"+ self.id

# Class representing a element of solution list
class item_planlist:
    def __init__(self, vehicle, begin_time, end_time, charger ):
        self.vehicle = vehicle
        self.time_begin = begin_time
        self.time_end = end_time
        self.charger = charger


# Class representing a charger
class charger:
    def __init__(self, id):
        self.id = id
        self.occupied_periods = []

    def set_estado(self, hour_begin, hour_end):
        self.occupied_periods.append((hour_begin, hour_end))

    def eliminar_estado(self, hour_begin, hour_end):
        self.occupied_periods = [(begin, end) for begin, end in self.occupied_periods if (begin, end) != (hour_begin, hour_end)]

    def liberar_charger(self):
        self.occupied_periods = []

    def esta_ocupado(self, hour_begin, hour_end):
        for begin, end in self.occupied_periods:
            if begin <= hour_end and end >= hour_begin:
                return True
        return False

    def tiene_hours_ocupadas(self):
        return bool(self.occupied_periods)




# Restricciones lineales para asegurar que los chargeres no se solapan
def check_charger_constraints(assignment):
    for i in range(len(assignment)):
        for j in range(i + 1, len(assignment)):
            if assignment[i].charger == assignment[j].charger:
                if assignment[i].time_end > assignment[j].time_begin and assignment[j].time_end > assignment[i].time_begin:
                    return False
    return True





# Función para resolver el problema mediante programación lineal
def solve_optimization_problem(vehicles, chargeres, hourly_prices):
    # Crear un problema de programación lineal
    prob = LpProblem("Carga_de_vehicles", LpMinimize)

    # Variables de decisión
    assignment_vars = LpVariable.dicts("Assignment", [(vehicle.id, charger.id) for vehicle in vehicles for charger in chargeres], 0, 1, LpBinary)

    # Función objetivo (minimizar el cost total de carga)
    prob += lpSum([assignment_vars[(vehicle.id, charger.id)] * vehicle.get_charging_cost(vehicle.available_time_start, vehicle.available_time_end, hourly_prices) for vehicle in vehicles for charger in chargeres])

    # Restricciones: cada vehículo es asignado a exactamente un charger
    for vehicle in vehicles:
        prob += lpSum([assignment_vars[(vehicle.id, charger.id)] for charger in chargeres]) == 1

    # Restricciones: cada charger solo puede cargar un vehículo a la vez
    for charger in chargeres:
        prob += lpSum([assignment_vars[(vehicle.id, charger.id)] for vehicle in vehicles]) <= 1

    # Restricciones adicionales para asegurar que los chargeres no se solapen
    for vehicle1 in vehicles:
        for vehicle2 in vehicles:
            if vehicle1 != vehicle2:
                for charger in chargeres:
                    prob += assignment_vars[(vehicle1.id, charger.id)] + assignment_vars[(vehicle2.id, charger.id)] <= 1

    # Resolver el problema de programación lineal
    prob.solve()

    # Extraer la solución
    best_assignment = []
    for vehicle in vehicles:
        for charger in chargeres:
            if assignment_vars[(vehicle.id, charger.id)].value() == 1:
                best_assignment.append(item_planlist(vehicle, vehicle.available_time_start * assignment_vars[(vehicle.id, charger.id)].value(), vehicle.available_time_end * assignment_vars[(vehicle.id, charger.id)].value(), charger))

    # Calcular el cost total de la solución encontrada
    best_cost = value(prob.objective)

    return best_assignment, best_cost


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

# Parámetros del algoritmo
hourly_prices = [0.1, 0.2, 0.15, 0.25, 0.2, 0.15, 0.1, 0.1, 0.2, 0.3, 0.35, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.2, 0.3, 0.35, 0.3, 0.25, 0.2, 0.15]

# Ejemplo de vehículos
vehicle1 = ElectricVehicle(1, 100, 8, 9, 2, 80, 10, 5)
vehicle2 = ElectricVehicle(2, 200, 8, 9, 3, 100, 8, 4)
vehicle3 = ElectricVehicle(3, 150, 12, 13, 1, 120, 12, 6)
vehicles = [vehicle1, vehicle2, vehicle3]

#vehicles = create_vehicles_from_file("vehicles.txt")

# Ejemplo de chargeres
chargeres = [charger(1), charger(2), charger(3)]

for i in range(len(chargeres)):
  num = random.uniform(0, 1)
  t_ini = random.uniform(0, 23)
  t_end = t_ini+random.uniform(0.5,3)

  #spoil probability
  if num < 0.1:
    chargeres[i].set_estado(t_ini, t_end)


# Solve the probloem
best_assignment, best_cost = solve_optimization_problem(vehicles, chargeres, hourly_prices)



# Print best solution
print("cost total: " + str(best_cost))
print("Planificación:")
for item in best_assignment:
    print("Vehicle " + str(item.vehicle.id))
    print("begin: " + str(item.time_begin))
    print("end: " + str(item.time_end))
    print("Charger " + str(item.charger.id))
    print("")

clasesAG.generate_prism_model(best_assignment)


# STATISTICS TO EVALUATE SOLUTION QUALITY - Uncomment to execute
file_name = 'C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\general_opt_system\\data\\vehiclesprueba.txt'
vehicles2 = create_vehicles_from_file(file_name)

i=0
cont=0
vehicles_noc=0
time_real_carga=0
cost_real_total = 0
cost_real=0
cost_penalizacion=0
timespan_penalizacion=0
for item in best_assignment:

  if item.vehicle.id == vehicles2[i].id:
    print("Hola")
    if item.time_begin<vehicles2[i].available_time_start:
      cont=cont+1
      time_real_carga = item.time_end - vehicles2[i].available_time_start
      time_real_ini= vehicles2[i].available_time_start

      if vehicles[i].get_charging_time()>time_real_carga:
          vehicles_noc=vehicles_noc+1
          timespan_penalizacion=vehicles[i].get_charging_time()-time_real_carga
          #Para los vehicles que por llegar tarde no se carguen lo suficiente, se añade una penalización sumando el cost de cargar lo que les falta en el momento más caro del día
          #time que queda x velocidad de carga x precio de hour más cara
          cost_penalizacion=cost_penalizacion+(timespan_penalizacion*vehicles[i].charge_speed)*0.35

    else:
      time_real_carga=item.time_end-item.time_begin
      time_real_ini=item.time_begin

    cost_real=vehicles[i].get_charging_cost(item.time_begin, item.time_end, hourly_prices)


    timespan_begin=0
    timespan_end=0

    if i==0:
      timespan_begin=time_real_ini

    else: timespan_end=item.time_end+timespan_penalizacion

  i=i+1
  cost_real_total = cost_real_total + cost_real/2 ++cost_penalizacion
  timespan=timespan_end-timespan_begin
