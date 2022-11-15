import csv
import sys

""" [0] = titleId
    [1] = order
    [2] = personid
    [3] = role/position
    [4] = job
    [5] = characters
"""
csv.field_size_limit(sys.maxsize)
"""titleId, personId, position"""
peopleData = [[0, 0, ""]]
"""titleId, personId, character"""
charactersData = [[0, 0, "character"]]
keptTitlesData = {}
keptPeopleData = {}

with open("title.akas.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        if i != 0 and line[3] == "CA" and (line[4] == "en" or line[4] == "\\N") and int(line[0][2:] not in keptTitlesData):
            keptTitlesData[(int(line[0][2:]))] = ""
        i += 1

with open("title.principals.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        if i != 0 and int(line[0][2:]) in keptTitlesData and int(line[2][2:] not in keptPeopleData):
            keptPeopleData[(int(line[2][2:]))] = ""
        i += 1
    

with open("title.principals.tsv") as file:
    role = csv.reader(file, delimiter="\t")
    i = 0
    for line in role:
        tempPeople = []
        if i != 0 and int(line[2][2:]) in keptPeopleData and int(line[0][2:]) in keptTitlesData:
            tempPeople.append(int(line[0][2:]))  # titleId
            tempPeople.append(int(line[2][2:]))  # personId
            tempPeople.append(line[3].replace("'", "\""))  # position
            for i in range(1, len(tempPeople)):
                if tempPeople[i] == "\\N":
                    tempPeople[i] = "null"
            peopleData.append(tempPeople)

            # Handling characters 
            if line[5] != "\\N":
                tempCharacters = line[5]
                for character in "[]\"":
                    tempCharacters = tempCharacters.replace(character, "")
                tempCharacters = tempCharacters.split(",")
                for character in tempCharacters:
                    if tempCharacters != "\\N":
                        charactersData.append([int(line[0][2:]), int(line[2][2:]), character.replace("'", "\"")])
                    else:
                        charactersData.append([int(line[0][2:]), int(line[2][2:]), "null"])
        i += 1

sqlFile = open("title.principals.sql", "w")
for value in peopleData:
    prepareSql = "insert into workedOn (titleId, personId, position) values (%s, %s, \'%s\');\n" % (
        value[0], value[1], value[2])
    sqlFile.write(prepareSql)

sqlFile = open("characters.title.principals.sql", "w")
for value in charactersData:
    prepareSql = "insert into characters (titleId, personId, character) values (%s, %s, \'%s\');\n" % (
        value[0], value[1], value[2])
    sqlFile.write(prepareSql)
