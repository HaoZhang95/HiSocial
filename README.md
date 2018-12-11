# HiSocial

Our core idea is provide information about different kinds of events & activities to the people of Helsinki. Our solution is a mobile-based hobby-finder application, which aims to provide information about different kinds of activities to the users. Additionally, we want to provide a route map for users so they can easily find a way to the place where the hobby event is. The main target group is kids (age 10-17) and their families but others will be free to use it as well. We have also created a web-based application where the user can find all the same information what is in the mobile version. 

Application Author: Hao Zhang

## Requirement
- Android Phone (version 6.0 or higher)
- Android Studio
- Scan the following QR code to download this applciation, or click [Here](https://drive.google.com/file/d/1LnwpXpSZHdh8TokpKyc5GbjrDIA2wjny/view?usp=sharing) to download
- Notice: If you intall this app, map part maybe not work, beacuse of the google map key, if you want it work perfect, please run the code on Android Studio and replace the old key with your own map key. Instruction for changing map api key is below.
![Route](https://i.imgur.com/vvCOgg3.png)

## View demo video to help you understand code
[![Demo video](https://i.imgur.com/O7ZHJcd.png)](https://youtu.be/NJ68TX6HA5Q?list=UU81qa4UW7JBl0wKYlsCR08w)


## Cautions and Project Structure (Click Picture to view more clear version)
It requires Read external storage and Network permissions, the former one is used to add cover image while posting new event, the latter one is used to load data from the remote. And you need a Google Maps API key and paste your key into [google_maps_api.xml] (app/src/debug/res/values/google_maps_api.xml)
![Route](https://i.imgur.com/x3ASGTG.png)

## Application Structure
![Route](https://i.imgur.com/k1pbWPl.png)

+ -- idk.metropolia.fi.myapplication  // parent package
+ -- + adapter                        // Adapter class for list or grid
+ -- + extension                      // Extension for RecyclerView Item 
+ -- + httpService                    // Handle Network Request
+ -- + Model                          // A Bunch of Bean Class
+ -- + utils                          // Snippets code and support method
+ -- + views                          // Activity, Fragment and Widgets 

## Features covered
- [x] Search By event date
- [x] Filter event feature
- [x] Show event location on map as marker
- [x] Display multiple itineraries to the event location
- [x] Create Event and Post it to open data
- [x] Language change: Finnish && English
- [x] Polish the UI, all UI components are following material design
- [x] Categorize event, Ongoing events on the main page
- [x] Event Details show 
- [ ] Dark theme
- [x] Event data from open data which power by *city of helsinki*
- [x] Transportation Itinerary data provided by *helsinki transporation data center*


## Screenshots
|                    Home                     |                  Map               |                  About us               |   
| ------------------------------------------- |--------------------------------------------|--------------------------------------------|
|![Route](https://i.imgur.com/0tnUKVU.gif)     |![History](https://i.imgur.com/mXWenmW.gif)|![BMI](https://i.imgur.com/MrZoDCP.gif)|

## More
|                    Event Details                    |                  OnGoing Event and Map                   |                  New Event               |                  Search page               |      
| ------------------------------------------- |--------------------------------------------|-----------------------------------------|-----------------------------------------|
|![Home](https://i.imgur.com/Gh6xGhc.gif)     |![Details](https://i.imgur.com/yBqkMmT.gif) |![DarkHome](https://i.imgur.com/8nXmxOH.gif)|![DarkDetails](https://i.imgur.com/r5lhPPo.gif)|

## Further Development from here maybe:
- Dark Theme
- Login && Signup for post event
- or add your own logic

## Test
This app was tested successfully on Huawei cell phone (version 6.0) using Android Studio 3.1.1

## Thanks for viewing && welcome to Helsinki :)
![Route](https://i.imgur.com/npIjq1g.jpg)
