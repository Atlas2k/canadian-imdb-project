import csv
import sys

""" [0] = showId
    [1] = type
    [2] = title
    [3] = director
    [4] = cast
    [5] = country
    [6] = date_added
    [7] = release_year
    [8] = rating
    [9] = duration
    [10] = listed_in
    [11] = description
"""
csv.field_size_limit(sys.maxsize)
"""titleId, date_added"""
sqlFile = open("availableOn.sql", "w")
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
                keptTitlesData[line[2]] = (int(line[0][2:]))
        i += 1



def readPlatformFile(fileName, platformId):
    foundTitles = [[0, ""]]
    with open(fileName) as file:
        entry = csv.reader(file, delimiter=",")
        i = 0
        for line in entry:
            if i != 0 and line[2] in keptTitlesData:
                # titleId, dateAdded
                foundTitles.append([keptTitlesData[line[2]], line[6]])
            i += 1

    for value in foundTitles:
        if value[0] != 0:
            prepareSql = "insert into availableOn (platformId, titleId, dateAdded) values (%s, %s, \'%s\');\n" % (
                platformId, value[0], value[1])
            sqlFile.write(prepareSql)


readPlatformFile("amazon_prime_titles.csv", 2)
readPlatformFile("disney_plus_titles.csv", 3)
readPlatformFile("hulu_titles.csv", 4)
readPlatformFile("netflix_titles.csv", 1)


sqlFile = open("platform.sql", "w")
prepareSql = "insert into platform (platformId, platformName) values (%s, \'%s\');\n" % (1, "Netflix")
sqlFile.write(prepareSql)
prepareSql = "insert into platform (platformId, platformName) values (%s, \'%s\');\n" % (2, "Amazon Prime")
sqlFile.write(prepareSql)
prepareSql = "insert into platform (platformId, platformName) values (%s, \'%s\');\n" % (3, "Disney+")
sqlFile.write(prepareSql)
prepareSql = "insert into platform (platformId, platformName) values (%s, \'%s\');\n" % (4, "hulu")
sqlFile.write(prepareSql)
