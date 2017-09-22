import os, os.path

def remove_non_ascii(text):
    return ''.join([i if ord(i) < 128 else '' for i in text])

for x in range(1, 79):
    if (x != 46 and x != 48):
        path = os.path.join(os.path.dirname(os.path.realpath(__file__)), "RawText", str(x))
        fileNames = [f for f in os.listdir(path) if os.path.isfile(os.path.join(path, f))]

        for file_name in fileNames:
            inFile = open("RawText/" + str(x)+"/" + file_name, "r")
            outFile = open("Cases/" + file_name, "w")
            outFile.write(remove_non_ascii(inFile.read()))
            inFile.close()
            outFile.close()
