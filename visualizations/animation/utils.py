import json
from functools import reduce

from visualizations.animation.models import Frame, Pedestrian


def parse_pedestrian_json() -> list[Frame]:
    with open("pedestrians.json") as file:
        json_input = json.load(file)

    frames = []
    for frame_data in json_input:
        time = frame_data["time"]
        pedestrians_data = frame_data["pedestrians"]
        pedestrians = [Pedestrian(p["x"], p["y"], p["id"]) for p in pedestrians_data]
        frames.append(Frame(time, pedestrians))
    return frames


def print_occurrences(frames: list[Frame]):
    if frames is None:
        raise Exception("Empty frames")

    mapped_counts = map(lambda frame: [(pedestrian.get_id(), 1) for pedestrian in frame.pedestrians], frames)

    def reducer(acc, val):
        for id, count in val:
            acc[id] = acc.get(id, 0) + count
        return acc

    counts = reduce(reducer, mapped_counts, {})

    for id, count in counts.items():
        print(f"ID {id}: {count} occurrences")