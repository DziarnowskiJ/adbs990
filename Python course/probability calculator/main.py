# This entrypoint file to be used in development. Start by reading README.md
import prob_calculator
#from unittest import main

prob_calculator.random.seed(95)
hat = prob_calculator.Hat(yellow=5, red=1, green=3, blue=9, test=1)
probability = prob_calculator.experiment(
    hat=hat,
    expected_balls={"blue": 2},
    num_balls_drawn=2,
    num_experiments=3)
print("Probability1:", probability)

prob = prob_calculator.experiment(
    hat = prob_calculator.Hat(yellow=5, red=1, green=3, blue=9, test=1),
    expected_balls={"yellow":2,"blue":3,"test":1},
    num_balls_drawn=20,
    num_experiments=4)
print("Probability2:", prob)

# Run unit tests automatically
#main(module='test_module', exit=False)
