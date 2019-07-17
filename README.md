# JsoupCrawler
Simple java crwaler

1. fisrt we finding url  from the Home Page  using jsoup.
2. then we getting all urls at second level.
3. and putting it in the set for avoiding duplicacies.
4.and getting element from set one by one and adding to valurange and writting at one in google sheet.
5. And then reading url from google sheet and here we getting checked that response of url is other than 200 and containg DAMN! and Page not working then writing failed url in the different G sheet. 
