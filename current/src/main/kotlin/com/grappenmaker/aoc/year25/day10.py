import pyperclip
from z3 import Optimize, Int

machines = [([[3], [1, 3], [2], [2, 3], [0, 2], [0, 1]], [3, 5, 4, 7]),
([[0, 2, 3, 4], [2, 3], [0, 4], [0, 1, 2], [1, 2, 3, 4]], [7, 5, 12, 7, 2]),
([[0, 1, 2, 3, 4], [0, 3, 4], [0, 1, 2, 4, 5], [1, 2]], [10, 11, 11, 5, 10, 5])]

def solve(machine):
  solver = Optimize()
  buttons, jolts = machine
  buttons = [set(button) for button in buttons]

  button_vars = [Int(f'b{i}') for i in range(len(buttons))]
  for i in range(len(jolts)):
    lhs = sum(button_vars[j] for j in range(len(buttons)) if i in buttons[j])
    solver.add(lhs == jolts[i])

  for v in button_vars:
    solver.add(v >= 0)

  total = sum(button_vars)
  solver.minimize(total)
  solver.check()
  model = solver.model()

  return model.eval(total).as_long()

ans = 0
for machine in machines:
  ans += solve(machine)

pyperclip.copy(ans)
print(f'Copied "{ans}" to clipboard!')