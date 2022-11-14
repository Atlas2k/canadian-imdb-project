import csv
import sys

""" [0] = titleId
    [1] = parentTitleId
    [2] = seasonNumber
    [3] = episodeNumber
"""
csv.field_size_limit(sys.maxsize)
"""titleId, parentTitleId, seasonNumber, episodeNumber"""
titlesData = [[0, 0, 0, 0]]
keptTitlesData = {}


with open("title.akas.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        if i != 0 and line[3] == "CA" and (line[4] == "en" or line[4] == "\\N") and int(line[0][2:] not in keptTitlesData):
            keptTitlesData[(int(line[0][2:]))] = ""
        i += 1


with open("title.episode.tsv") as file:
    titles = csv.reader(file, delimiter="\t")
    i = 0
    for line in titles:
        temp = []
        if i != 0 and int(line[0][2:]) in keptTitlesData and int(line[1][2:]) in keptTitlesData:
                temp.append(int(line[0][2:]))  # titleID
                temp.append(int(line[1][2:]))  # parentTitleId
                temp.append(line[2])  # seasonNumber
                temp.append(line[3])  # episodeNumber
                for i in range(1, len(temp)):
                    if temp[i] == "\\N":
                        temp[i] = "null"
                titlesData.append(temp)
        i += 1

sqlFile = open("title.episode.sql", "w")
for value in titlesData:
    preparedSql = "update media set episodeNumber=%s, seasonNumber=%s where titleid = %s;\n" % (value[3], value[2], value[0])
    sqlFile.write(preparedSql)
    prepareSql= "insert into have (titleidEpisode, titleidShow) values (%s, %s);\n" % (value[0], value[1])
    sqlFile.write(prepareSql)
