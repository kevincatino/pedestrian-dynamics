

import json
import os
import matplotlib.pyplot as plt

prefix = '../../'
file = 'tauError-21-23.60-25.00.json'

f = open(prefix + file)
data = json.load(f)

# Extraer valores de tau y error del JSON
tau_values = [item['tau'] for item in data['errors']]
error_values = [item['error'] for item in data['errors']]

# Graficar tau vs error
# plt.figure(figsize=(8, 6))  # Tamaño del gráfico
plt.plot(tau_values, error_values, marker='o', linestyle='-')  # Crear el gráfico de dispersión

minimo_valor = tau_values[error_values.index(min(error_values))]

print("El valor mínimo en el array es:", minimo_valor)

plt.xlabel('Tau[s]')  # Etiqueta del eje x
plt.ylabel('Error')  # Etiqueta del eje y

# Obtener el nombre del archivo de datos sin la extensión
file_name = os.path.splitext(file)[0]  # Elimina la extensión .json

minimo_valor = round(minimo_valor, 2)
# Guardar la imagen en un archivo con el nombre deseado (tp5-img-datos.png)
output_file = f"tp5-img-{file_name}-err{minimo_valor}.png"  # Nombre del archivo de imagen
plt.savefig(output_file)  # Guardar el gráfico como imagen PNG

plt.show()