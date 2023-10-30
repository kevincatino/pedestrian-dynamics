import json

from matplotlib import pyplot as plt

prefix = '../../'
file = 'compVelocity-10-8.80-9.90.json'

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



ax.plot(time, vExp,   label=f'vExp - id={str(json_input["id"])}', marker='o')
ax.plot(time, vSim,   label=f'vSim - id={str(json_input["id"])}')
ax.axhline(y=1.6, color='red', linestyle='--', label=r'$v^{max}_d$')

ax.set_xlabel(r'Tiempo $[s]$')
ax.set_ylabel(r'Velocidad $[\frac{m}{s}]$')
ax.grid(False)
plt.legend()
plt.show()