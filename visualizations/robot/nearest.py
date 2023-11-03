import json
import statistics

import matplotlib.pyplot as plt


prefix = '../../'
file = 'minDistance.json'

fig, ax = plt.subplots()

f = open(prefix + file)
datos_json = json.load(f)


dmin_values = [element["dMin"] for element in datos_json]
time_values = [element["time"] for element in datos_json]


indices_colorear = []

if dmin_values[0] < dmin_values[1]:
    indices_colorear.append(0)


for i in range(1, len(dmin_values) - 1):
    if dmin_values[i] < dmin_values[i - 1] and dmin_values[i] < dmin_values[i + 1]:
        indices_colorear.append(i)

if dmin_values[len(dmin_values) - 1] < dmin_values[len(dmin_values) - 2]:
    indices_colorear.append(len(dmin_values) - 1)

dmin_values_colorear = [dmin_values[i] for i in indices_colorear]
time_values_colorear = [time_values[i] for i in indices_colorear]


# Graficar
plt.figure(figsize=(10, 6))

data_by_id = {}

for index in indices_colorear:
    current_id = datos_json[index]["id"]
    if current_id not in data_by_id:
        data_by_id[current_id] = []
    if current_id in data_by_id and len(data_by_id[current_id]) > 0:
        last_time = data_by_id[current_id][-1][0]
        last_dmin = data_by_id[current_id][-1][1]
        if time_values[index] - last_time < 5 and dmin_values[index] < last_dmin:
            data_by_id[current_id][-1] = (time_values[index], dmin_values[index])  # Reemplazar el último punto
        elif time_values[index] - last_time >= 1:
            data_by_id[current_id].append((time_values[index], dmin_values[index]))  # Agregar si la diferencia de tiempo es mayor o igual a 1 segundo
    else:  # Si es el primer punto del ID, añadirlo directamente
        data_by_id[current_id].append((time_values[index], dmin_values[index]))

# Graficar
plt.figure(figsize=(10, 6))

plt.plot(time_values, dmin_values, marker='o', linestyle='-', markersize=2, color='blue')

ordered_ids = sorted(data_by_id.keys())

# Definir colores para cada ID
colores = ['red', 'green', 'blue', 'orange', 'purple', 'cyan', 'magenta', 'yellow', 'brown', 'pink', 'olive', 'teal', 'coral', 'skyblue', 'lime', 'lavender', 'gold', 'peru', 'indigo', 'salmon', 'orchid', 'aqua', 'lightgreen', 'khaki', 'slategray', 'violet']

for id in ordered_ids:
    if id in data_by_id:
        x_vals, y_vals = zip(*data_by_id[id])
        plt.scatter(x_vals, y_vals, color=colores[id % len(colores)], s=50, label=f'ID {id}')

plt.xlabel('Time [s]')
plt.ylabel('Dmin [m]')
plt.legend()

plt.savefig("nearest.png")

plt.show()


# Obtener todos los valores de dmin de todos los IDs en una sola lista
all_dmin_values = [value[1] for values in data_by_id.values() for value in values]

# Calcular el promedio y la desviación estándar de todos los dmin juntos
if all_dmin_values:
    average_all_dmin = sum(all_dmin_values) / len(all_dmin_values)
    std_dev_all_dmin = statistics.stdev(all_dmin_values) if len(all_dmin_values) > 1 else 0
else:
    average_all_dmin = 0
    std_dev_all_dmin = 0

print(f"Promedio de todos los dMin = {average_all_dmin:.2f}, Desviación estándar = {std_dev_all_dmin:.2f}")