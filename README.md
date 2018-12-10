# HiSocial

This application is kind of linke a sport app where families can find a different kind of hobbies in the selected area and they have also possibility to see all the sports clubs and the fields/halls in that area (provided that there is exist). For example. Family's dad wants to know how many basketball clubs are in the Helsinki area. At the same time he will see all the basketball fields/halls, camps, and their locations. 

Application Author: Hao Zhang

## Requirement
- Android Phone (version 6.0 or higher)
- Android Studio

## View demo video to help you understand code
[![Demo video](https://i.imgur.com/IPYnKJj.png)](https://youtu.be/qKfWnml2-UQx)


## Cautions and Project Structure
It requires GPS and Network permissions, the former one is used to locate yourself while running, the latter one is used to upload your running data to web database. And you need a Google Maps API key and paste your key into [google_maps_api.xml] (app/src/debug/res/values/google_maps_api.xml)
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


## Features
- GPS location, measure distance
- Draw real-time path on the map while running
- Lock screen using proximity internal sensor
- Upload data to web database
- data persistence using roomDatabase and LiveData
- Beautiful UI design
- And more...

## Screenshots
|                    Home                     |                  Map               |                  About us               |   
| ------------------------------------------- |--------------------------------------------|--------------------------------------------|
|![Route](https://i.imgur.com/0tnUKVU.gif)     |![History](https://i.imgur.com/mXWenmW.gif)|![BMI](https://i.imgur.com/MrZoDCP.gif)|

## More
|                    Event Details                    |                  OnGoing Event and Map                   |                  New Event               |                  Search page               |      
| ------------------------------------------- |--------------------------------------------|-----------------------------------------|-----------------------------------------|
|![Home](https://i.imgur.com/Gh6xGhc.gif)     |![Details](https://i.imgur.com/yBqkMmT.gif) |![DarkHome](https://i.imgur.com/8nXmxOH.gif)|![DarkDetails](https://i.imgur.com/r5lhPPo.gif)|

## UI Documentation
More details can be found in [Here](https://drive.google.com/drive/u/1/folders/1Pis_Fp-WW-sY4XwzR1-8VR4oKVCQva-k?usp=sharingx)

## Test
This app was tested successfully on Huawei cell phone (version 6.0) using Android Studio 3.1.1

## Thanks for viewing && welcome to Helsinki :)
![Route](https://i.imgur.com/npIjq1g.jpg)
