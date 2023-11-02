import os
import imageio

from visualizations.animation.utils import *
import matplotlib.pyplot as plt

save_frames = False
framerate = 30 / 4
save_video = True
frames_folder = "frames"
output_video_filename = "animation.mp4"
show_frame_time = False
show_particle_id = False
show_targets = True

targets = [(-12.8, -6.5), (-9.6, -6.5), (-12.8, 0), (-9.6, 0), (-3.3, -6.5), (-3.3, 0), (-9.6, 6.5),
           (-12.8, 6.5), (-3.3, 6.5), (3.15, 6.5), (10, 6.5), (12.5, 6.5), (3.15, -6.5), (3.15, 0),
           (12.5, 0), (10, 0), (10, -6.5), (12.5, -6.5),
           (0, -3.5), (0, 0), (0, 3.5)]

frames = parse_pedestrian_json()
total_ids = get_ids_count(frames)
state_by_id = {}
images = []
index = 1

if not os.path.exists(frames_folder):
    os.makedirs(frames_folder)
    print(f"Folder '{frames_folder}' created.")
else:
    print(f"Folder '{frames_folder}' already exists.")

fig, ax = plt.subplots()

for frame in frames:
    ax.clear()
    for pedestrian in frame.pedestrians:
        id = pedestrian.get_id()
        x = pedestrian.get_x()
        y = pedestrian.get_y()
        r = pedestrian.get_r()

        if id not in state_by_id:
            state_by_id[id] = {'x': [], 'y': []}

        state_by_id[id]['x'].append(x)
        state_by_id[id]['y'].append(y)

        # Last 5 frames
        state_by_id[id]['x'] = state_by_id[id]['x'][-5:]
        state_by_id[id]['y'] = state_by_id[id]['y'][-5:]

        if id != -1:
            ax.add_patch(plt.Circle((x, y), radius=r, color=f'C{id % total_ids}', alpha=0.9))
            plt.plot(state_by_id[id]['x'], state_by_id[id]['y'], color=f'C{id % total_ids}', alpha=0.9)
        else:
            ax.add_patch(plt.Circle((x, y), radius=r, color="black", alpha=1))
            plt.plot(state_by_id[id]['x'], state_by_id[id]['y'], color="black", alpha=1)

        if show_particle_id:
            plt.text(x, y, f'ID: {id}', fontsize=8, color='black', ha='right', va='bottom')

    plt.xlabel('Coordenada X')
    plt.ylabel('Coordenada Y')

    if show_frame_time:
        plt.text(13, 11, f't={frame.get_time()}s', fontsize=12, color='black', ha='right', va='top')

    plt.xlim(-15, 15)
    plt.ylim(-8, 12) if show_frame_time else plt.ylim(-8, 8)

    if show_targets:
        for idx, target in enumerate(targets):
            x, y = target
            plt.plot(x, y, marker='x', markersize=8, color='red', alpha=0.3)

    plt.savefig(f"{frames_folder}/frame_{index}.png")

    images.append(f'frame_{index}.png')
    index += 1

if save_video:
    try:
        os.remove(output_video_filename)
    except OSError:
        pass

    with imageio.get_writer(output_video_filename, fps=framerate) as writer:
        for image_filename in images:
            image = imageio.imread(f"{frames_folder}/{image_filename}")
            writer.append_data(image)

if not save_frames:
    for image_name in images:
        os.remove(f"{frames_folder}/{image_name}")
    os.rmdir(frames_folder)
