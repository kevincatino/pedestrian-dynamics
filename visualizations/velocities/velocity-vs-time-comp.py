import json
import os
from matplotlib import pyplot as plt

prefix = '../../'
file = 'compVelocity-21-23.60-25.00.json'

fig, ax = plt.subplots()


f = open(prefix + file)
json_input = json.load(f)
time = []
vExp = []
vSim = []



for v in json_input['velocities']:
    time.append(v['time'])
    vExp.append(v['vExp'])
    vSim.append(v['vSim'])

#vSim[-1] = vSim[-1] - (vSim[-1] - vExp[-1])/2

ax.plot(time, vExp,   label=f'vExp - id={str(json_input["id"])}', marker='o')
ax.plot(time, vSim,   label=f'vSim - id={str(json_input["id"])}')
ax.axhline(y=1.62, color='red', linestyle='--', label=r'$v^{max}_d$')

ax.set_xlabel(r'Tiempo $[s]$')
ax.set_ylabel(r'Velocidad $[\frac{m}{s}]$')
ax.grid(False)
plt.legend()

file_name = os.path.splitext(file)[0]
nombre_de_archivo = f'tp5-velocity-comp-id-{file_name}.png'

plt.savefig(nombre_de_archivo)

plt.show()