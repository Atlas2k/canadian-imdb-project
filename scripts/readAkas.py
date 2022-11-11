import csv
import sys

""" [0] = titleId
    [1] = ordering
    [2] = title
    [3] = region
    [4] = language
    [5] = types
    [6] = attributes
    [7] = isOriginalTitle
"""
csv.field_size_limit(sys.maxsize)
keptTitlesData = [[0, "", ""]]

with open("title.akas.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        temp = []
        if i != 0 and line[3] == "US" and (int(line[0][2:]) != keptTitlesData[len(keptTitlesData) - 1][0]):
            temp.append(int(line[0][2:]))
            temp.append(line[2])
            if line[4] == "\\N":
                temp.append("null")
            else:
                temp.append(line[4])
            keptTitlesData.append(temp)
        i += 1

sqlFile = open("title.akas.sql", "w")
for value in keptTitlesData:
    preparedSql = "insert into table media (titleid, title, language) values (%s, %s, %s)\n" % (value[0], value[1], value[2])
    sqlFile.write(preparedSql)
