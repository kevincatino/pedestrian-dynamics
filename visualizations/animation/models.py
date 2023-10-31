class Pedestrian:
    def __init__(self, x: float, y: float, id: int, r=0.3):
        self.x = x
        self.y = y
        self.id = id
        self.r = r

    def get_x(self):
        return self.x

    def get_y(self):
        return self.y

    def get_id(self):
        return self.id

    def get_r(self):
        return self.r

    @property
    def get_diameter(self):
        return 2 * self.r

    def __str__(self):
        return f"\nPedestrian = ID: {self.id}, position: ({self.x}, {self.y}), radius: {self.r}m"

    def __repr__(self):
        return self.__str__()


class Frame:
    def __init__(self, time: float, pedestrians: list[Pedestrian]):
        self.time = time
        self.pedestrians = pedestrians

    def get_pedestrians(self):
        return self.pedestrians

    def get_time(self):
        return round(self.time, 1)

    def __str__(self):
        return f"\nFrame = Time: {round(self.time, 1)}, Total: {len(self.pedestrians)}, Pedestrians: {[pedestrian for pedestrian in self.pedestrians]}\n"

    def __repr__(self):
        return self.__str__()