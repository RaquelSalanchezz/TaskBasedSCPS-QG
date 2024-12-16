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
    def __init__(self, vehiculo, begin_time, end_time, cargador ):
        self.vehiculo = vehiculo
        self.tiempo_inicio = begin_time
        self.tiempo_fin = end_time
        self.cargador = cargador


# Class representing a charger
class Cargador:
    def __init__(self, id):
        self.id = id
        self.periodos_ocupado = []

    def set_estado(self, hora_inicio, hora_fin):
        self.periodos_ocupado.append((hora_inicio, hora_fin))

    def eliminar_estado(self, hora_inicio, hora_fin):
        self.periodos_ocupado = [(inicio, fin) for inicio, fin in self.periodos_ocupado if (inicio, fin) != (hora_inicio, hora_fin)]

    def liberar_cargador(self):
        self.periodos_ocupado = []

    def esta_ocupado(self, hora_inicio, hora_fin):
        for inicio, fin in self.periodos_ocupado:
            if inicio <= hora_fin and fin >= hora_inicio:
                return True
        return False

    def tiene_horas_ocupadas(self):
        return bool(self.periodos_ocupado)




# Restricciones lineales para asegurar que los cargadores no se solapan
def check_cargador_constraints(assignment):
    for i in range(len(assignment)):
        for j in range(i + 1, len(assignment)):
            if assignment[i].cargador == assignment[j].cargador:
                if assignment[i].tiempo_fin > assignment[j].tiempo_inicio and assignment[j].tiempo_fin > assignment[i].tiempo_inicio:
                    return False
    return True

# Función para resolver el problema mediante programación lineal
def solve_optimization_problem(vehiculos, cargadores, hourly_prices):
    # Crear un problema de programación lineal
    prob = LpProblem("Carga_de_vehiculos", LpMinimize)

    # Variables de decisión
    assignment_vars = LpVariable.dicts("Assignment", [(vehiculo.id, cargador.id) for vehiculo in vehiculos for cargador in cargadores], 0, 1, LpBinary)

    # Función objetivo (minimizar el coste total de carga)
    prob += lpSum([assignment_vars[(vehiculo.id, cargador.id)] * vehiculo.get_charging_cost(vehiculo.available_time_start, vehiculo.available_time_end, hourly_prices) for vehiculo in vehiculos for cargador in cargadores])

    # Restricciones: cada vehículo es asignado a exactamente un cargador
    for vehiculo in vehiculos:
        prob += lpSum([assignment_vars[(vehiculo.id, cargador.id)] for cargador in cargadores]) == 1

    # Restricciones: cada cargador solo puede cargar un vehículo a la vez
    for cargador in cargadores:
        prob += lpSum([assignment_vars[(vehiculo.id, cargador.id)] for vehiculo in vehiculos]) <= 1

    # Restricciones adicionales para asegurar que los cargadores no se solapen
    for vehiculo1 in vehiculos:
        for vehiculo2 in vehiculos:
            if vehiculo1 != vehiculo2:
                for cargador in cargadores:
                    prob += assignment_vars[(vehiculo1.id, cargador.id)] + assignment_vars[(vehiculo2.id, cargador.id)] <= 1

    # Resolver el problema de programación lineal
    prob.solve()

    # Extraer la solución
    best_assignment = []
    for vehiculo in vehiculos:
        for cargador in cargadores:
            if assignment_vars[(vehiculo.id, cargador.id)].value() == 1:
                best_assignment.append(item_planlist(vehiculo, vehiculo.available_time_start * assignment_vars[(vehiculo.id, cargador.id)].value(), vehiculo.available_time_end * assignment_vars[(vehiculo.id, cargador.id)].value(), cargador))

    # Calcular el coste total de la solución encontrada
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
vehiculo1 = ElectricVehicle(1, 100, 8, 9, 2, 80, 10, 5)
vehiculo2 = ElectricVehicle(2, 200, 8, 9, 3, 100, 8, 4)
vehiculo3 = ElectricVehicle(3, 150, 12, 13, 1, 120, 12, 6)
vehiculos = [vehiculo1, vehiculo2, vehiculo3]

vehicles = create_vehicles_from_file("vehiculos.txt")

# Ejemplo de cargadores
cargadores = [Cargador(1), Cargador(2), Cargador(3)]

for i in range(len(cargadores)):
  num = random.uniform(0, 1)
  t_ini = random.uniform(0, 23)
  t_fin = t_ini+random.uniform(0.5,3)

  #spoil probability
  if num < 0.1:
    cargadores[i].set_estado(t_ini, t_fin)


# Solve the probloem
best_assignment, best_cost = solve_optimization_problem(vehiculos, cargadores, hourly_prices)



# Print best solution
print("Coste total: " + str(best_cost))
print("Planificación:")
for item in best_assignment:
    print("Vehículo " + str(item.vehiculo.id))
    print("Inicio: " + str(item.tiempo_inicio))
    print("Fin: " + str(item.tiempo_fin))
    print("Cargador " + str(item.cargador.id))
    print("")

clasesAG.generate_prism_model(best_assignment)