import json
import math
from matplotlib import pyplot as plt

prefix = '../../'
file = 'expVelocity20.json'

fig, ax = plt.subplots()


f = open(prefix + file)
json_input = json.load(f)
time = []
vel = []

vd_min = 0.25
vd_max = 1.62

ts = [11.1, 12.4,12.5, 14.5,
      22.8, 23.8, 24, 25.2]

colors = ["red", "blue", "green", "brown", "orange", "purple", "cyan", "brown", "gray", "blue"]
vertical_lines = True
horizontal_line = True

for v in json_input['velocities']:
    time.append(v['time'])
    vel.append(v['vExp'])



ax.plot(time, vel,   label=f'id={str(json_input["id"])}')

if horizontal_line:
    ax.axhline(y=vd_min, color='red', linestyle='--', label=f'$v^{{min}}$ = {vd_min}$\\frac{{m}}{{s}}$')
    ax.axhline(y=vd_max, color='red', linestyle='--', label=f'$v^{{max}}$ = {vd_max}$\\frac{{m}}{{s}}$')

if vertical_lines:
    for i in range(0, len(ts), 2):
        print(i)
        ax.axvline(x=ts[i], color=colors[math.trunc(i/2)], linestyle=':', label=f't{i}={ts[i]}s')
        ax.axvline(x=ts[i+1], color=colors[math.trunc(i/2)], linestyle=':', label=f't{i+1}={ts[i+1]}s')

ax.set_xlabel(r'Tiempo $[s]$')
ax.set_ylabel(r'Velocidad $[\frac{m}{s}]$')
ax.grid(False)
# ax.legend(loc='center left', bbox_to_anchor=(0, 0.4))
ax.set_title(f'id={str(json_input["id"])}')


if vertical_lines:
    nombre_de_archivo = f'tp5-asc-des-time-id-{json_input["id"]}.png'
else:
    nombre_de_archivo = f'tp5-vel-vs-time-min-id-{json_input["id"]}.png'

plt.savefig(nombre_de_archivo)

plt.show()