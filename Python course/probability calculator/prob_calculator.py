import copy
import random
# Consider using the modules imported above.

class Hat() :
    contents = []

    #constructor, converts parameters passed in class to list of strings of colors
    def __init__(self, **kwargs) :
        self.contents = []
        for key, value in kwargs.items() :
            for i in range(value) :
                self.contents.append(key)

    def getContents(self) :
        return self.contents

    def setContents(self, newContent) :
        self.contents = newContent

    #get randomly drawn balls
    def draw(self, number) :
        pickedList = []
        #number of balls is higher than hat's content
        if number > len(self.contents) :
            return self.draw(len(self.contents))
        #draw number of balls
        for i in range(number) :
            #pick random index of Contents
            pick = random.randint(0, len(self.contents) - 1)
            #remove value from Contents and put it in the list
            pickedList.append(self.contents.pop(pick))
        return pickedList

def experiment(hat, expected_balls, num_balls_drawn, num_experiments):
    #number of succesfull draws
    successful = 0;

    for i in range(num_experiments) :
        #create copy of the hat and experiment on it
        copyHat = copy.deepcopy(hat)
        pickedBalls = copyHat.draw(num_balls_drawn)
        pickedHist = makeHistogram(pickedBalls)
        #check whether expected_balls are included in pickedHis
        final = True
        for key in expected_balls :
            if key not in pickedHist.keys() or pickedHist[key] < expected_balls[key] :
                final = False
        if final :
            successful = successful + 1

    return successful/num_experiments

def makeHistogram(list) :
    dir = {}
    for item in list :
        dir[item] = dir.get(item, 0) + 1
    return dir
