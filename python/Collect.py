caseID = []

def collect(start_db, end_db):
    mf = open('mapAll.txt', 'w')
    for lc in range(start_db, end_db+1):
        try:
            with open(str(lc)+".txt", "r") as f:
                content = f.readlines()

            content = [x.strip() for x in content]
            for raw in content:
                ind = raw.split(";")[0]
                if(ind not in caseID):
                    caseID.append(ind)
                    mf.write(raw+'\n')
        except Exception:
            pass
    mf.close()

collect(1, 78)
