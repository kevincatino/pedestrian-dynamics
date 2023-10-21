class Pedestrian:
    def __init__(self, x: float, y: float, id: int):
        self.x = x
        self.y = y
        self.id = id

    def get_x(self):
        return self.x

    def get_y(self):
        return self.y

    def get_id(self):
        return self.id

    def __str__(self):
        return f"\nPedestrian = ID: {self.id}, Posición: ({self.x}, {self.y})"

    def __repr__(self):
        return self.__str__()


class Frame:
    def __init__(self, time: float, pedestrians: list[Pedestrian]):
        self.time = time
        self.pedestrians = pedestrians

    def get_pedestrians(self):
        return self.pedestrians

    def get_time(self):
        return self.time

    def __str__(self):
        return f"\nFrame = Time: {round(self.time, 2)}, Total: {len(self.pedestrians)}, Pedestrians: {[pedestrian for pedestrian in self.pedestrians]}\n"

    def __repr__(self):
        return self.__str__()