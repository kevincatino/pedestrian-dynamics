import os
import imageio

from visualizations.animation.utils import *
import matplotlib.pyplot as plt

save_frames = False
framerate = 20
save_video = True
frames_folder = "frames"
output_video_filename = "animation.mp4"
show_frame_time = False

frames = parse_pedestrian_json()
total_ids = get_ids_count(frames)

fig, ax = plt.subplots()

state_by_id = {}
images = []
index = 1

if not os.path.exists(frames_folder):
    os.makedirs(frames_folder)
    print(f"Folder '{frames_folder}' created.")
else:
    print(f"Folder '{frames_folder}' already exists.")

for frame in frames:
    for pedestrian in frame.pedestrians:
        id = pedestrian.get_id()
        x = pedestrian.get_x()
        y = pedestrian.get_y()

        if id not in state_by_id:
            state_by_id[id] = {'x': [], 'y': []}

        state_by_id[id]['x'].append(x)
        state_by_id[id]['y'].append(y)

        # Last 5 frames
        state_by_id[id]['x'] = state_by_id[id]['x'][-5:]
        state_by_id[id]['y'] = state_by_id[id]['y'][-5:]

        plt.plot(state_by_id[id]['x'], state_by_id[id]['y'], color=f'C{id % total_ids}', alpha=0.5)
        plt.plot(x, y, 'o', markersize=5, color=f'C{id % total_ids}')
        plt.text(x, y, f'ID: {id}', fontsize=8, color='black', ha='right', va='bottom')

    plt.xlabel('Coordenada X')
    plt.ylabel('Coordenada Y')

    if show_frame_time:
        plt.text(13, 11, f't={frame.get_time()}s', fontsize=12, color='black', ha='right', va='top')

    plt.xlim(-15, 15)
    plt.ylim(-8, 12) if show_frame_time else plt.ylim(-8, 8)

    plt.savefig(f"{frames_folder}/frame_{index}.png")
    plt.close()

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
