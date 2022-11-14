import csv
import sys

""" [0] = titleId
    [1] = titleType
    [2] = primaryTitle
    [3] = originalTitle
    [4] = isAdult
    [5] = startYear
    [6] = endYear
    [7] = runTimeMinutes
    [8] = genres
"""
csv.field_size_limit(sys.maxsize)
"""titleId, primaryTitle, isAdult, startYear, endYear, runTimeMinutes"""
titlesData = [[0, "", 0, 0, 0, 0]]
genres = {}
titleGenres = []  # part of

with open("title.basics.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    genreId = 1
    for line in titles:
        temp = []
        if i != 0:
            if (line[1] == "movie" or line[1] == "tvSeries" or line[1] == "tvEpisode") and len(line) == 9:
                temp.append(int(line[0][2:]))  # titleID
                temp.append(line[3])  # title
                temp.append(line[4])  # isAdult
                temp.append(line[5])  # startYear
                temp .append(line[6])  # endYear
                temp.append(line[7])  # runTimeMinutes
                for i in range(1, len(temp)):
                    if temp[i] == "\\N":
                        temp[i] = "null"
                titlesData.append(temp)
                # Take care of genres
                tempGenres = line[8].split(",")
                for genre in tempGenres:
                    if genre == "\\N":
                        testGenre = "null"
                    else:
                        testGenre = genre
                    if testGenre not in genres.keys():
                        genres[testGenre] = genreId
                        genreId += 1
                for genre in tempGenres:
                    if genre == "\\N":
                        titleGenres.append([temp[0], genres["null"]])
                    else:
                        titleGenres.append([temp[0], genres[genre]])
        i += 1

sqlFile = open("title.basics.sql", "w")
for value in titlesData:
    preparedSql = "update media set title=\'%s\', isAdult=%s, startYear=%s, endYear=%s, runtime=%s where titleid = %s;\n" % (
        value[1], value[2], value[3], value[4], value[5], value[0])
    sqlFile.write(preparedSql)

sqlFileGenres = open("genres.title.basics.sql", "w")
for value in genres:
    preparedSql = "insert into genre (genreid, genreName) values (%s, \'%s\');\n" % (
        genres[value], value)
    sqlFileGenres.write(preparedSql)

sqlFilePartOf = open("partOf.title.basics.sql", "w")
for value in titleGenres:
    preparedSql = "inset into partOf (genreid, titleid) value (%s, %s);\n" % (
        value[1], value[0])
    sqlFilePartOf.write(preparedSql)
