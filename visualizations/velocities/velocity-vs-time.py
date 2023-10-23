import json

from matplotlib import pyplot as plt

prefix = '../../'
file = 'expVelocity.json'

fig, ax = plt.subplots()


f = open(prefix + file)
json_input = json.load(f)
time = []
vel = []



for v in json_input['velocities']:
    time.append(v['time'])
    vel.append(v['vExp'])



ax.plot(time, vel,   label=f'id={str(json_input["id"])}')
ax.axhline(y=1.6, color='red', linestyle='--', label=r'$v^{max}_d$')

ax.set_xlabel(r'Tiempo $[s]$')
ax.set_ylabel(r'Velocidad $[\frac{m}{s}]$')
ax.grid(False)
plt.legend()
plt.show()