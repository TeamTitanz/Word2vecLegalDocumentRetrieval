from selenium import webdriver
from selenium.common.exceptions import WebDriverException, NoSuchElementException, StaleElementReferenceException
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from bs4 import BeautifulSoup
import config
import time

driver = None
sleep_time = 1
caseURL = []
caseNames = []

def loadDriver():
    global driver

    try:
        profile = webdriver.FirefoxProfile()
        profile.accept_untrusted_certs = True
        profile.set_preference('permissions.default.stylesheet', 2)
        profile.set_preference('permissions.default.image', 2)
        profile.set_preference('dom.ipc.plugins.enabled.libflashplayer.so', 'false')

        firefox_capabilities = DesiredCapabilities.FIREFOX
        firefox_capabilities['marionette'] = True

        driver = webdriver.Firefox(executable_path=config.GECKO_DRIVER_PATH)
        driver.set_window_size(1124, 850)

    except WebDriverException:
        print("Web driver error")
        return False


def getUrl(url):
    global driver, sleep_time

    # Navigate to the hostel form
    #'http://lawcrawler.findlaw.com/LCsearch.html?restrict=lp&client=lp&entry=harvey+v.+veneman'
    driver.get(url)
    time.sleep(1)
    result = driver.page_source
    soup = BeautifulSoup(result, 'html.parser')
    return soup.find(id="gsa_n_1").find("div", class_="gsa_result_url").getText()


def getCaseURL(name):
    oldCharList = [" ", "'", ",", "$", "&", ":", "/", "?"]
    newCharList = ["+", "%27", "%2C", "%24", "%26", "%3A", "%2F", "%3F"]

    searchURL = "http://lawcrawler.findlaw.com/LCsearch.html?restrict=lp&client=lp&entry="
    for char in name:
        if(char in oldCharList):
            searchURL += newCharList[oldCharList.index(char)]
        else:
            searchURL += char
    #print searchURL
    return getUrl(searchURL)

def uniqueID(temID):
    temID = str(temID)
    prefixLen = 10 - len(temID)
    prefix = '0' * prefixLen
    return prefix + temID

def createGraph():
    global caseURL
    loadDriver()

    with open("NameGraph.csv", "r") as ins:
        for line in ins:
            caseURL.append(line.split("_=r=_")[0])
            caseNames.append(line.split("_=;=_")[-1])

    newCf = open('newCases.txt','w')
    graphf = open('obGraph.txt','w')

    index = 0
    for mentionNames in caseNames:
        graphf.write(uniqueID(index)+':')
        plist = []
        temName = mentionNames.split("_=,=_")
        for name in temName:
            try:
                newURL = getCaseURL(name)
            except AttributeError:
                continue
            
            print newURL
            if(newURL in caseURL):
                temID = uniqueID(caseURL.index(newURL))
                if(temID not in plist):
                    plist.append(temID)
            else:
                newCf.write(newURL+'\n')
                temID = uniqueID(len(caseURL))
                plist.append(temID)
                caseURL.append(newURL)
        graphf.write(",".join(plist)+'\n')
        index += 1
    newCf.close()
    graphf.close()
    

createGraph()

