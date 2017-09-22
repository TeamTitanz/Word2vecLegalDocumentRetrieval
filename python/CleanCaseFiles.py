import os, os.path
import errno


def uniqueID(lc, c_id):
    fill_1 = 3 - len(str(lc))
    fill_2 = 7 - len(str(c_id))
    return ('0'*fill_1) + str(lc) + ('0'*fill_2) + str(c_id)

def mkdir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc: # Python >2.5
        if exc.errno == errno.EEXIST and os.path.isdir(path):
            pass
        else: raise

def safe_open_w(path):
    mkdir_p(os.path.dirname(path))
    return open(path, 'w')

def insertIDtoNameGraph(start_db, end_db):
    ngFolder = "ng"
    for lc in range(start_db, end_db+1):
        content = []
        if(lc!=46 and lc!=48):
            fileName = os.path.join(os.path.dirname(os.path.realpath(__file__)), ngFolder, "NameGraph"+str(lc)+".csv")
            with open(fileName, "r") as ins:
                for line in ins:
                    line.strip()
                    if(len(line)>3):
                        content.append(line)

            caseSize = len(content)
            for c_id in range(caseSize):
                content[c_id] = uniqueID(lc, c_id) + "_=id=_" + content[c_id]


            newfileName = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'NGwithID', "NameGraph"+str(lc)+".txt")
            nfp = safe_open_w(newfileName)
            nfp.write(''.join(content))
            nfp.close()

    print "Insert ID to NameGraph Done"


def okToRemoveZero(fileName):
    size = 0
    return (os.path.getsize(fileName)==size)


def copyfile(fType, lc):

    path = os.path.join(os.path.dirname(os.path.realpath(__file__)), "Text", fType, str(lc))
    #print path
    fileNames = [f for f in os.listdir(path) if os.path.isfile(os.path.join(path, f))]

    nameIndex = []
    for fn in fileNames:
        ind = int(fn.split(".")[0][4:])
        nameIndex.append(ind)
    nameIndex.sort()

    caseCount = len(nameIndex[:-10])

    for index in range(caseCount):
        fileName = os.path.join(path, "case"+ str(nameIndex[index]) +".txt")
        if(not okToRemoveZero(fileName)):
            content = []
            with open(fileName, "r") as ins:
                for line in ins:
                    line.strip()
                    if(len(line)>3):
                        content.append(line)

            newFileNPath = os.path.join(os.path.dirname(os.path.realpath(__file__)), "CleanText", fType, str(lc), uniqueID(lc, nameIndex[index]) + ".txt")
            with safe_open_w(newFileNPath) as f1:
                f1.write(''.join(content))


def changeCaseFileName(start_db, end_db):
    print "Change Case File Name..."
    for lc in range(start_db, end_db+1):
        if(lc!=46 and lc!=48):
            print "oblie_" + str(lc)
            copyfile("LemmatizedText", lc)
            copyfile("RawText", lc)
            copyfile("TokenText", lc)


def cleanNgFile(start_db, end_db):
    for lc in range(start_db, end_db+1):
        if(lc!=46 and lc!=48):

            path = os.path.join(os.path.dirname(os.path.realpath(__file__)), "CleanText", "RawText", str(lc))
            #print path
            fileNames = [f for f in os.listdir(path) if os.path.isfile(os.path.join(path, f))]

            nameIndex = []
            for fn in fileNames:
                nameIndex.append(fn.split(".")[0])

            content = []
            readfileName = os.path.join(os.path.dirname(os.path.realpath(__file__)), "NGwithID", "NameGraph"+str(lc)+".txt")
            with open(readfileName, "r") as ins:
                for line in ins:
                    cid = line.split("_=id=_")[0]
                    if(cid in nameIndex):
                        content.append(line)
                    #else:
                        #print cid
            newNGFileWPath = os.path.join(os.path.dirname(os.path.realpath(__file__)), "NewNG", "NameGraph"+str(lc)+".txt")
            with safe_open_w(newNGFileWPath) as f1:
                f1.write(''.join(content))

start_db = 37
end_db = 38
insertIDtoNameGraph(start_db, end_db)
changeCaseFileName(start_db, end_db)
cleanNgFile(start_db, end_db)
