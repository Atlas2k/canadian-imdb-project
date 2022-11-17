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
keptPeopleData = {}
keptPeopleDataPrincipals = {}
keptTitlesDataAkas = {}
keptTitlesData = {}


with open("title.akas.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        if i != 0 and line[3] == "CA" and (line[4] == "en" or line[4] == "\\N") and int(line[0][2:] not in keptTitlesDataAkas):
            keptTitlesDataAkas[(int(line[0][2:]))] = ""
        i += 1

with open("title.basics.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        temp = []
        if i != 0:
            if (line[1] == "movie" or line[1] == "tvSeries" or line[1] == "tvEpisode") and len(line) == 9 and int(line[0][2:]) in keptTitlesDataAkas:
                keptTitlesData[int(line[0][2:])] = ""
        i += 1

with open("title.principals.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        if i != 0 and int(line[0][2:]) in keptTitlesData and int(line[2][2:] not in keptPeopleData):
            keptPeopleDataPrincipals[(int(line[2][2:]))] = ""
        i += 1

with open("name.basics.tsv") as file:
    people = csv.reader(file, delimiter="\t")
    i = 0
    for line in people:
        temp = []
        tempKnownFor = []
        if i != 0 and int(line[0][2:]) in keptPeopleDataPrincipals:
            keptPeopleData[(int(line[0][2:]))] = ""
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
    if value[0] != 0:
        prepareSql = "insert into workedOn (titleId, personId, position) values (%s, %s, \'%s\');\n" % (
            value[0], value[1], value[2])
        sqlFile.write(prepareSql)

sqlFile = open("characters.title.principals.sql", "w")
for value in charactersData:
    if value[0] != 0:
        prepareSql = "insert into characters (titleId, personId, character) values (%s, %s, \'%s\');\n" % (
            value[0], value[1], value[2])
        sqlFile.write(prepareSql)
