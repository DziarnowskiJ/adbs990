class Rectangle:
    width = 0
    height = 0

    #constuctor
    def __init__(self, width, height) :
        self.width = width
        self.height = height
    #width setter
    def set_width(self, width) :
        self.width = width
    #height setter
    def set_height(self, height) :
        self.height = height
    #get area
    def get_area(self) :
        return (self.width * self.height)
    #get perimiter
    def get_perimeter(self) :
        return 2 * self.width + 2 * self.height
    #get diagonal
    def get_diagonal(self) :
        return (self.width ** 2 + self.height ** 2) ** .5
    #paint picture
    def get_picture(self) :
        line = ""
        if (self.height > 50 or self.width > 50) :
            return "Too big for picture."
        for i in range(self.height) :
            for j in range(self.width) :
                line = line + '*'
            line = line + '\n'
        return line
    #check how many shapes fits inside the rectangle
    def get_amount_inside(self, shape) :
        #find how many times shape fits horizontally
        hori = (self.width - (self.width % shape.width)) / shape.width
        #find how many times shape fits vertically
        vert = (self.height - (self.height % shape.height)) / shape.height
        return int(hori * vert)
    #when represented as a string "Rectangle(width=X,height=Y)"
    def __str__(self) :
        return "Rectangle(width=" + str(self.width) + ", height=" + str(self.height) + ")"

class Square(Rectangle):
    #constructor
    def __init__(self, width) :
        self.width = self.height = width
    #set height and width
    def set_side(self, side) :
        self.width = self.height = side
    #modify height setter to affect width
    def set_height(self, height) :
        self.width = self.height = height
    #modify width setter to affect width
    def set_width(self, width) :
        self.width = self.height = width
    #when represented as a string "Rectangle(side=X)"
    def __str__(self) :
        return "Square(side=" + str(self.width) + ")"
