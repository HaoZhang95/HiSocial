# HiSocial

Our core idea is provide information about different kinds of events & activities to the people of Helsinki. Our solution is a mobile-based hobby-finder application, which aims to provide information about different kinds of activities to the users. Additionally, we want to provide a route map for users so they can easily find a way to the place where the hobby event is. The main target group is kids (age 10-17) and their families but others will be free to use it as well. We have also created a web-based application where the user can find all the same information what is in the mobile version. 

Application Author: Hao Zhang

## Requirement
- Android Phone (version 6.0 or higher)
- Android Studio

## View demo video to help you understand code
[![Demo video](https://i.imgur.com/O7ZHJcd.png)](https://youtu.be/NJ68TX6HA5Q?list=UU81qa4UW7JBl0wKYlsCR08w)


## Cautions and Project Structure
It requires Read external storage and Network permissions, the former one is used to add cover image while posting new event, the latter one is used to load data from the remote. And you need a Google Maps API key and paste your key into [google_maps_api.xml] (app/src/debug/res/values/google_maps_api.xml)
![Route](https://i.imgur.com/y9e5Pyx.png)

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


## Test
This app was tested successfully on Huawei cell phone (version 6.0) using Android Studio 3.1.1

## Thanks for viewing && welcome to Helsinki :)
![Route](https://i.imgur.com/npIjq1g.jpg)
