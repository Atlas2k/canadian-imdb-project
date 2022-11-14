import csv
import sys

""" [0] = personId
    [1] = name
    [2] = birthYear
    [3] = deathYear
    [4] = professions
    [5] = knownForTitles
"""
csv.field_size_limit(sys.maxsize)
"""personId, name, birthYear, deathYear"""
peopleData = [[0, "", 0, 0]]
knownForData = []
jobs = {}
peopleJobs = []

with open("name.basics.tsv") as file:
    people = csv.reader(file, delimiter="\t")
    i = 0
    jobId = 1
    for line in people:
        temp = []
        tempKnownFor = []
        if i != 0:
            temp.append(int(line[0][2:]))  # peopleId
            temp.append(line[1])  # name
            temp.append(line[2])  # birthYear
            temp.append(line[3])  # deathYear
            for i in range(1, len(temp)):
                if temp[i] == "\\N":
                    temp[i] = "null"
            peopleData.append(temp)

            # Handling known for
            tempKnownFor.append(int(line[0][2:]))  # peopleId
            knownFor = line[5].split(",")
            if knownFor[0] != "\\N":
                for title in knownFor:
                    knownForData.append([int(line[0][2:]), int(title[2:])])
            else:
                knownForData.append([int(line[0][2:]), "null"])

            # Handling jobs
            tempJobs = line[4].split(",")
            for job in tempJobs:
                if job == "\\N":
                    tempJob = "null"
                else:
                    tempJob = job
                if tempJob not in jobs.keys():
                    jobs[tempJob] = jobId
                    jobId += 1
            for job in tempJobs:
                if job == "\\N":
                    peopleJobs.append([temp[0], jobs["null"]])
                else:
                    peopleJobs.append([temp[0], jobs[job]])

        i += 1

sqlFile = open("name.basics.sql", "w")
for value in peopleData:
    prepareSql = "insert into people (personId, name, birthYear, deathYear) values (%s, \'%s\', %s, %s);\n" % (
        value[0], value[1], value[2], value[3])
    sqlFile.write(prepareSql)

sqlFile = open("knownFor.name.basics.sql", "w")
for value in knownForData:
    prepareSql = "insert into knownFor (personId, titleId) value (%s, %s);\n" % (
        value[0], value[1])
    sqlFile.write(prepareSql)

sqlFileGenres = open("jobs.name.basics.sql", "w")
for value in jobs:
    preparedSql = "insert into jobs (jobId, jobName) values (%s, \'%s\');\n" % (
        jobs[value], value)
    sqlFileGenres.write(preparedSql)

sqlFilePartOf = open("works.name.basics.sql", "w")
for value in peopleJobs:
    preparedSql = "inset into works (jobId, personId) value (%s, %s);\n" % (
        value[1], value[0])
    sqlFilePartOf.write(preparedSql)
