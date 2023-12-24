stones = []
# PASTE OUTPUT FROM PRINT IN KOTLIN CODE HERE

from z3 import *
solver = Solver()
unknowns = [Int('unknown_time_' + str(i)) for i in range(len(stones))]
x = Int('x')
y = Int('y')
z = Int('z')
dx = Int('dx')
dy = Int('dy')
dz = Int('dz')

for idx in range(len(stones)):
    pos, vel = stones[idx]
    sx, sy, sz = pos
    sdx, sdy, sdz = vel
    unknown = unknowns[idx]
    solver.add(x - sx + unknown * (dx - sdx) == 0)
    solver.add(y - sy + unknown * (dy - sdy) == 0)
    solver.add(z - sz + unknown * (dz - sdz) == 0)

print("solving")
print(solver.check())

model = solver.model()
print(sum(model[v].as_long() for v in [x, y, z]))