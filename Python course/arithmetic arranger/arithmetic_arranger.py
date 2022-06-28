def arithmetic_arranger(problems, answered=False):

#split equations into subparts
    parts = []
    for x in problems :
        parts.append(x.split())

#show errors
    #too many problems
    if (len(problems) > 5) :
        return "Error: Too many problems."


    for part in parts :
        #multiplication or division
        if (part[1] != "+" and part[1] != "-") :
            return "Error: Operator must be '+' or '-'."
        #nubers contain letters
        if (not part[0].isdecimal() or not part[2].isdecimal()) :
            return "Error: Numbers must only contain digits."
        #numbers have more than 4 digits
        if (len(part[0]) > 4 or len(part[2]) > 4) :
            return "Error: Numbers cannot be more than four digits."

#create top line of the assignemnts
    top_line = "  "

    for i in range(len(parts)) :
        for j in range(len(str(parts[i][2])) - len(str(parts[i][0]))) :
            top_line = top_line + " "
        top_line = top_line + str(parts[i][0])
        if (i != len(parts) - 1) :
            top_line = top_line + "      "

    #print(top_line)

#create bottom line of the assignemnts
    middle_line = ""

    for i in range(len(parts)) :
        middle_line = middle_line + parts[i][1] + " "
        for j in range(len(parts[i][0]) - len(parts[i][2])) :
            middle_line = middle_line + " "
        middle_line = middle_line + parts[i][2]
        if (i != len(parts) - 1 ) :
            middle_line = middle_line + "    "

    #print(middle_line)

#create 'sum' line
    sum_line = ""

    for i in range(len(parts)) :
        for j in range(2 + len(longer(parts[i][0], parts[i][2]))) :
            sum_line = sum_line + "-"
        if (i != len(parts) - 1 ) :
            sum_line = sum_line + "    "

    #print(sum_line)

#create answer sum_line
    answer_line = ""

    for i in range(len(parts)) :
        answer = eval(parts[i][0] + parts[i][1] + parts[i][2])
        for j in range(2 + len(longer(parts[i][0], parts[i][2])) - len(str(answer))) :
            answer_line = answer_line + " "
        answer_line = answer_line + str(answer)
        if (i != len(parts) - 1 ) :
            answer_line = answer_line + "    "

    #print(answer_line)

    arranged_problems = top_line + '\n' + middle_line + '\n' + sum_line

    #add answers
    if (answered) :
        arranged_problems = arranged_problems + '\n' + answer_line

    return arranged_problems

def longer(elm1, elm2) :
    if (len(elm1) > len(elm2)) :
        return elm1
    return elm2
