class Category:
    def __init__(self, type) :
        self.type = type
        self.ledger = []

    def deposit(self, amount, description="") :
        self.ledger.append({"amount": amount, "description": description})

    def withdraw(self, amount, description="") :
        if (self.check_funds(amount)) :
            self.ledger.append({"amount":(0-amount), "description":description})
            return True
        return False

    def get_balance(self) :
        balance = 0
        for i in self.ledger :
            balance = balance + i["amount"]
        return balance

    def transfer(self, amount, otherType) :
        if (self.check_funds(amount)) :
            self.withdraw(amount, ("Transfer to " + str(otherType.type)))
            otherType.ledger.append({"amount":amount, "description":("Transfer from "+str(self.type))})
            return True
        return False

    def check_funds(self, amount) :
        if self.get_balance() >= amount :
            return True
        return False

    def __str__(self) :
        #top line with ****Type****
        top_line = self.type.center(30, '*') + '\n'
        #middle line with description and amount
        middle_line = ""
        for i in self.ledger :
            price_line = ""
            price_line = price_line + i["description"][0:23]
            for j in range(30 - len(price_line) - len(str(double(i["amount"])))) :
                price_line = price_line + " "
            middle_line = middle_line + price_line + str(double(i["amount"])) + '\n'
        #bottom line with total balance
        bottom_line = "Total: " + str(double(self.get_balance()))

        return top_line + middle_line + bottom_line

def double(amount) :
    if type(amount) is int :
        return str(amount) + ".00"
    if type(amount) is float :
        if (str(amount).find('.') + 2 == len(str(amount))) :
            return str(amount) + "0"
        elif (str(amount).find('.') + 3 == len(str(amount))) :
            return str(amount)

def create_spend_chart(categories):

    total_spendings = 0
    spendings = []
    for type in categories :
        type_spending = 0
        for transaction in type.ledger :
            if transaction["amount"] < 0 :
                type_spending = type_spending - transaction["amount"]
                total_spendings = total_spendings - transaction["amount"]
        spendings.append((convert(type.type), type_spending))

    percentage = []
    for i in spendings :
        value = int((i[1]/total_spendings) * 10) / 10
        percentage.append((i[0], value))

    #title line
    output = "Percentage spent by category\n"

    #graph part
    for l in range(11) :
        #percentage column
        output = output + (f"{str((11-l-1)*10) + '| ' : >5}")
        #indication columns
        for item in percentage :
            if item[1] >= (10-l)/10 :
                output = output + "o  "
            else :
                output = output + "   "
        output = output + '\n'

    #summing line
    output = output + "    -"
    for i in range(len(categories)) :
        output = output + "---"

    #type lines
    for i in range(longest(spendings)) :
        output = output + "\n     "
        for tuple in spendings :
            if tuple[0] :
                output = output + str(tuple[0].pop(0)) + "  "
                continue
            output = output + "   "

    return output

def convert(string) :
    list1=[]
    list1[:0]=string
    return list1

def longest(dict) :
    longest = 0
    for tuple in dict :
        if len(tuple[0]) > longest :
            longest = len(tuple[0])
    return longest
