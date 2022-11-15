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
    [8] = Genre
"""
csv.field_size_limit(sys.maxsize)
"""titleId: Language"""
keptTitlesData = {}


with open("title.akas.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        temp = []
        if i != 0 and line[3] == "CA" and (line[4] == "en" or line[4] == "\\N") and int(line[0][2:]) not in keptTitlesData:
            temp.append(int(line[0][2:]))
            if line[4] == "\\N":
                temp.append("null")
            else:
                temp.append(line[4])
            keptTitlesData[temp[0]] = [temp[1]]
        i += 1

sqlFile = open("title.akas.sql", "w")
for value in keptTitlesData:
    preparedSql = "insert into media (titleId, title, language) values (%s, NULL, \'%s\');\n" % (
        value, keptTitlesData[value][0])
    sqlFile.write(preparedSql)
