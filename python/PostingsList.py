import MySQLdb

caseID = []
caseContent = []
caseName = []
uniqueIdIndex = 0

def uniqueID(temID):
    prefixLen = 10 - len(temID)
    prefix = '0' * prefixLen
    return prefix + temID

def getFootNotes(index, case_id, cur):
    global caseContent
    cur.execute("SELECT * FROM foot_notes where case_id=" + str(case_id))

    footNotes = ""
    for row in cur.fetchall():
        footNotes += str(row)
        
    caseContent[index] = caseContent[index] + footNotes
    

def getCase(dbName):
    
    global caseID
    global caseContent
    global caseName
    global uniqueIdIndex

    postingList = []

    print dbName
    con = MySQLdb.connect(host='localhost', user='root', passwd='root', db=dbName)
    cur = con.cursor()
    
    cur.execute("SELECT id, party_1, party_2, content, summary FROM cases")

    index = 0
    for row in cur.fetchall():
        
        caseID.append(row[0])
        caseContent.append(row[3] + '\n' + row[4])
        caseName.append(row[1].strip() + ' v. ' + row[2].strip())

        getFootNotes(index, row[0], cur)
        index += 1

    count = 0
    caseLength = len(caseID)
    for x in range(caseLength):
        name = caseName[x]
        #print name
        postingStr = ""
        for y in range(caseLength):
            if( (x!=y) and (name in caseContent[y])):
                #print "*"
                count += 1
                postingStr = uniqueID(str(y+uniqueIdIndex)) + ","
                
        postingList.append(uniqueID(str(x+uniqueIdIndex))+":"+postingStr[:-1])
    
    con.close()

    caseID = []
    caseContent = []
    caseName = []

    f = open('obGraph.txt','a+')

    for text in postingList:
        f.write(text+'\n')
    f.close()

for x in range(1, 51, 1):
    #print 'oblie_' + str(x)
    getCase('oblie_' + str(x))
    uniqueIdIndex += 1
