# -*- coding: utf-8 -*-
import json

config_dir="C:\\Users\\raque\\OneDrive\\Escritorio\\prueba\\general_opt_system\\config_files\\"
general_dir='C:/Users/raque/OneDrive/Escritorio/prueba/general_opt_system/'

# Base classes code compatible with Python 2
base_classes_code = '''
# -*- coding: utf-8 -*-

import random

class Consumer:
    def __init__(self, id, distance, available_time_start, available_time_end):
        self.id = id
        self.distance = distance
        self.available_time_start = available_time_start
        self.available_time_end = available_time_end
        
    def get_process_time(self):
        raise NotImplementedError("This method should be implemented by subclasses")

class Resource:
    def __init__(self, id):
        self.id = id
        self.occupied_periods = []

    def set_state(self, ini_hour, final_hour):
        self.occupied_periods.append((ini_hour, final_hour))

    def delete_state(self, ini_hour, final_hour):
        self.occupied_periods = [
            (ini, fin) for ini, fin in self.occupied_periods
            if (ini, fin) != (ini_hour, final_hour)
        ]

    def release(self):
        self.occupied_periods = []

    def is_occupied(self, ini_hour, final_hour):
        for inicio, fin in self.occupied_periods:
            if (ini_hour >= inicio and ini_hour <= fin) or (final_hour >= inicio and final_hour <= fin):
                return True
        return False

    def has_occupied_hours(self):
        return bool(self.occupied_periods)
'''


def generate_class_code(class_info):
    name = class_info["name"]
    base = class_info["base_class"]
    attributes = class_info["attributes"]
    methods = class_info.get("methods", [])

    init_params = ", ".join(["self"] + attributes)
    init_body = ""
    for attr in attributes:
        init_body += "        self.%s = %s\n" % (attr, attr)

    init_code = "    def __init__(%s):\n%s" % (init_params, init_body)

    method_code = ""
    for method in methods:
        if method == "get_attending_time":
            method_code += '''
    def get_process_time(self):
        if not self.has_food:
            return 2 * (self.distance / 5.0)
        return 0
    '''
        elif method == "get_charging_time":
            method_code += '''
    def get_process_time(self):
        required_charge = self.distance * (self.discharge_rate/100)  # Calculation of the required charge for the distance to be covered

        # If the car does not have enough charge
        if self.current_charge < required_charge:
            required_charge -= self.current_charge  # Subtract the current charge
            return required_charge / self.charge_speed  # Time in hours required to charge the vehicle

        else:
            return 0
    '''
        elif method == "get_charging_cost":
            method_code += '''
    def get_charging_cost(self, begin_time, end_time, hourly_prices):
        total_cost = 0
        current_time = begin_time
        while current_time < end_time:
            total_cost += self.charge_speed * hourly_prices[int(current_time)]
            current_time += 1
        return total_cost
    '''
        elif method == "get_irrigation_cost":
                method_code += '''
        def get_irrigation_cost(self, begin_time, end_time, water_price, irrigation_speed):
            total_cost = water_price*irrigation_speed*(end_time-begin_time)
            return total_cost
        '''

    return "\nclass %s(%s):\n%s%s\n" % (name, base, init_code, method_code)

def main():
    with open(config_dir+"configuracion_vehiculos.json", "r") as f:
        config = json.load(f)

    output = base_classes_code
    for cls in config["classes"]:
        output += generate_class_code(cls)

    with open(general_dir+'generated_classes.py', "w") as f:
        f.write(output)

if __name__ == "__main__":
    main()
