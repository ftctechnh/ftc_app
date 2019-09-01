#!/usr/bin/python
import sys
import socket
import threading
import socketserver
import json
from operator import attrgetter
import math
import os
os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = "hide"
import pygame
import queue

import time

JAVA_SERVER_HOST = "localhost"
JAVA_SERVER_PORT = 4445
PYTHON_SERVER_PORT = 4446

SCREEN_DIMS = (800, 800)
FIELD_WIDTH_IN = 12 * 12
ROBOT_WIDTH_IN = 18

SCREEN_CENTER = tuple(x/2 for x in SCREEN_DIMS)
SCALE_FACTOR = SCREEN_DIMS[0] / FIELD_WIDTH_IN

RR2_BACKGROUND_FIELD = pygame.image.load('rr2_field.png')
RR2_BACKGROUND_FIELD = pygame.transform.scale(RR2_BACKGROUND_FIELD, SCREEN_DIMS)

ROBOT_DIMS = tuple(ROBOT_WIDTH_IN * x // FIELD_WIDTH_IN for x in SCREEN_DIMS)
ROBOT = pygame.image.load('robot.png')
ROBOT = pygame.transform.scale(ROBOT, (ROBOT_DIMS))

SECS_BETWEEN_FRAMES = 1 / 20
# Put the code you want to do something based on when you get data here.
class RobotPosition:

    def __init__(self, x, y, h, index, json=""):
        self.x = x
        self.y = y
        self.h = h
        self.index = index
        self.json = json

    @classmethod
    def from_json(cls, json_data):
        attrs = json.loads(json_data)
        return cls(attrs["x"], attrs["y"], attrs["heading"], attrs["index"], json_data)

    def __str__(self):
        return str(self.json)

    # Self's time should always be less than position
    # Frames 105, 103, current time is 104
    # time_frac = 104 - 103 / 105 - 103 = 1/2
    def interpolate(self, position, frac):
        return RobotPosition(
            frac * (position.x - self.x) + self.x,
            frac * (position.y - self.y) + self.y,
            frac * (position.h - self.h) + self.h,
            frac * (position.index - self.index) + self.index,
        )

    def draw(self, screen):
        rotated = pygame.transform.rotate(ROBOT, math.degrees(self.h))
        robot_size = rotated.get_rect().size

        # We flip X and Y to correspond to how we've laid out field
        screen_coords = ( # Coords for center of robot
            SCREEN_CENTER[1] - SCALE_FACTOR * self.y - robot_size[1] / 2,
            SCREEN_CENTER[0] - SCALE_FACTOR * self.x - robot_size[0] / 2,
        )

        screen.blit(rotated, screen_coords)

class FrameQueue:
    def __init__(self):
        self.frames = []
        self.last_frame_update = time.time()
        self.prev_start = None
        self.prev_end = None

    # Frame queue contains only frames with times
    # not yet elapsed. We will often need to peek
    # at earliest element, though
    def add(self, frame: RobotPosition):
        self.frames.append(frame)


    def _min(self):
        return min(self.frames,key=attrgetter('index'))

    def _pop_min(self):
        m = self._min()
        self.frames.remove(m)
        return m

    def _time(self): # Gives time in ms
        return (time.time() - self.start_time) * 1000

    # Block until we have num_frames or more
    def _block_until_frame(self, num_frames=1):
        while len(self.frames) < num_frames:
            pass

    def get(self):
        if not self.prev_start or not self.prev_end:
            self._block_until_frame(2)
            self.prev_start = self._pop_min()
            self.prev_end = self._pop_min()
            self.last_frame_update = time.time()

        # If we should and can frame update, do so
        if (time.time() >= self.last_frame_update + SECS_BETWEEN_FRAMES and
                len(self.frames) >= 1):
            self.prev_start = self.prev_end
            self.prev_end = self._pop_min()
            self.last_frame_update = time.time()

        elapsed_time = time.time() - self.last_frame_update
        interpolate_frac = elapsed_time / SECS_BETWEEN_FRAMES
        return self.prev_start.interpolate(self.prev_end, interpolate_frac)


robot_frames = FrameQueue()

class ThreadedUDPRequestHandler(socketserver.BaseRequestHandler):
    def handle(self):
        data = self.request[0].strip()
        socket = self.request[1]
        robot_frames.add(RobotPosition.from_json(data))

class ThreadedUDPServer(socketserver.ThreadingMixIn, socketserver.UDPServer):
    pass

def main(argv):
    server = ThreadedUDPServer(("localhost", PYTHON_SERVER_PORT), ThreadedUDPRequestHandler)
    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.daemon = True
    server_thread.start()
    pygame.init()
    screen = pygame.display.set_mode(SCREEN_DIMS)

    done = False
    time_offset = None
    while not done:
        for event in pygame.event.get():
                if event.type == pygame.QUIT:
                        done = True

        # We always need to blit game field
        screen.blit(RR2_BACKGROUND_FIELD, [0, 0])
        frame = robot_frames.get().draw(screen)
        pygame.display.flip()

    server.shutdown()
    server.server_close()

if __name__ == '__main__':
    main(sys.argv)
