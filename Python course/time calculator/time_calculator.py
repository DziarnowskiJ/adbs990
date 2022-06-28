def add_time(start, duration, weekDay = None):
    startTime = start.split()[0]
    startH = int(startTime.split(':')[0])
    startM = int(startTime.split(':')[1])
    startType = start.split()[1]

    startD = getDayNumber(weekDay)

    addedH = int(duration.split(':')[0])
    addedM = int(duration.split(':')[1])

    #calculate minutes
    endM = startM + addedM
    if endM >= 60 :
        endM = endM - 60
        addedH = addedH + 1
    if endM < 10 :
        endM = "0" + str(endM)

    #calculate hours
    endH = startH + addedH

    #calculate days
    daysPassed = 0
    endType = startType
    while endH >= 12 :
        endH = endH - 12
        if endType == "AM" :
            endType = "PM"
        else :
            endType = "AM"
            daysPassed = daysPassed + 1
    if endH == 0 :
        endH = 12

    if daysPassed > 1 :
        if startD > 0 :
            return (str(endH) + ':' + str(endM) + ' ' + endType + ', ' + getDayName(startD + daysPassed) + " (" + str(daysPassed) + " days later)")
        else :
            return (str(endH) + ':' + str(endM) + ' ' + endType + " (" + str(daysPassed) + " days later)")
    elif daysPassed == 1 :
        if startD > 0 :
            return (str(endH) + ':' + str(endM) + ' ' + endType + ', ' + getDayName(startD + daysPassed) + " (next day)")
        else :
            return (str(endH) + ':' + str(endM) + ' ' + endType + " (next day)")
    else :
        if startD > 0 :
            return (str(endH) + ':' + str(endM) + ' ' + endType + ', ' + getDayName(startD))
        else :
            return (str(endH) + ':' + str(endM) + ' ' + endType)

    #return new_time

def getDayNumber(dayName) :
    if dayName == None :
        return -1
    elif dayName.upper() == "MONDAY" :
        return 1
    elif dayName.upper() == "TUESDAY" :
        return 2
    elif dayName.upper() == "WEDNESDAY" :
        return 3
    elif dayName.upper() == "THURSDAY" :
        return 4
    elif dayName.upper() == "FRIDAY" :
        return 5
    elif dayName.upper() == "SATURDAY" :
        return 6
    elif dayName.upper() == "SUNDAY" :
        return 0

def getDayName(dayNumber) :
    daysPassed = dayNumber % 7
    if daysPassed == 1 :
        return "Monday"
    elif daysPassed == 2 :
        return "Tuesday"
    elif daysPassed == 3 :
        return "Wednesday"
    elif daysPassed == 4 :
        return "Thursday"
    elif daysPassed == 5 :
        return "Friday"
    elif daysPassed == 6 :
        return "Saturday"
    elif daysPassed == 0 :
        return "Sunday"
    elif daysPassed == -1 :
        return None
