import os
from selenium import webdriver
import config

chromedriver = config.CHROME_DRIVER_PATH
os.environ["webdriver.chrome.driver"] = chromedriver
driver = webdriver.Chrome(chromedriver)
driver.get("http://lawcrawler.findlaw.com/LCsearch.html?restrict=lp&client=lp&entry=harvey+v.+veneman")
result = driver.page_source
driver.get("https://stackoverflow.com/questions/5370762/how-to-hide-firefox-window-selenium-webdriver")
print result
