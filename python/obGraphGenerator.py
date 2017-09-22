import MySQLdb

caseContent = []

def uniqueID(temID):
    prefixLen = 10 - len(temID)
    prefix = '0' * prefixLen
    return prefix + temID

def getContent(cur):
    global caseContent
    case_id = caseContent[-1][0]
    cur.execute("SELECT content FROM cases_content WHERE case_id=" + str(case_id))
    content = ""

    for row in cur.fetchall():
        content += row[0].strip() + '\n'

    caseContent[case_id].append(content)


def getCase(dbName):

    global caseContent

    print "Fetch", dbName
    con = MySQLdb.connect(host='localhost', user='root', passwd='root', db=dbName)
    cur = con.cursor()
    cur.execute("SELECT case_id, Name FROM cases")

    for row in cur.fetchall():
        caseContent.append([row[0], row[1].strip()])
        getContent(cur)

    con.close()

def createPostingList():

    global caseContent
    postingList = []

    count = 0
    numberOfCases = len(caseContent)
    for x in range(numberOfCases):
        name = (caseContent[x][1]).lower()
        postingStr = ""
        for y in range(numberOfCases):
            if( (x!=y) and (name in (caseContent[y][2]).lower())):
                postingStr = uniqueID(str(y)) + ","
                count += 1

        postingList.append(uniqueID(str(x))+":"+postingStr[:-1])

    f = open('obGraph.txt','a+')

    for text in postingList:
        f.write(text+'\n')
    f.close()


#Get data
for x in range(1, 78, 1):
    getCase('oblie_' + str(x))

#Create Posting List
print "Starting posting list creator..."
createPostingList()
print "Done"