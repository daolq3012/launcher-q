# Launcher Q (LaQ)
Opensource home launcher app with special features

![00](https://user-images.githubusercontent.com/32695143/57983982-0b01ec00-7a92-11e9-826d-a8a00febd805.jpg) ![01](https://user-images.githubusercontent.com/32695143/57983988-0c331900-7a92-11e9-82b7-5606115f6e95.jpg) ![02](https://user-images.githubusercontent.com/32695143/57983990-0ccbaf80-7a92-11e9-92ed-f0e42d096c93.jpg) ![03](https://user-images.githubusercontent.com/32695143/57983993-0d644600-7a92-11e9-9983-0ad4e7214624.jpg) ![04](https://user-images.githubusercontent.com/32695143/57983994-0d644600-7a92-11e9-900c-edc00b52a98c.jpg) ![05](https://user-images.githubusercontent.com/32695143/57984004-0f2e0980-7a92-11e9-89ad-be19160ef333.jpg)


<br><br>
## Typical features of LaQ
1. So clean : home page has only one page with configurable widgets, but doesn't have any application icon
2. Quick launch app : simply launch up to 64 apps (with folder * 6) through only one touch in only one home page
3. Quick run app's functino : run specific app's page(regist app badge or third party library or expert mode) through only two touch<br>
third party library : ( https://github.com/seose/launcher-q/tree/dev/TwoStepExample )
4. Deocrate home page : Easily decorate home screen through screen setting or theme store <br>
( https://play.google.com/store/apps/details?id=seoft.co.kr.laq_store )
5. Detailed setting : shortcut window (size, time interval, opendistance, ...), drawer (reorder, hide, row col, ...), widget (size, color, font ...), icon (pixel, image) more...


<br><br>
# How to use 
** top, bottom, left, right = TBLR

<br><br>
## How to launch app ( in home page )

![06](https://user-images.githubusercontent.com/32695143/57983982-0b01ec00-7a92-11e9-826d-a8a00febd805.jpg) ![07](https://user-images.githubusercontent.com/32695143/57983983-0b01ec00-7a92-11e9-9c61-6f3ded4ffc60.jpg)  

#### step 1
show mark TBLR on touch position when tap to screen<br>
( caution : touch top edge, bottom edge is not work )<br>
( you can setting mark distance on touch position in launcher setting )

#### step 2
Immediately move scroll to TBLR, show grid shape shortcut window that has 0~16 app icons on your finger position

#### step 3
if touching status -> launch app when you touch up in app icon <br>
if touch up status -> launch app when you touch down in app icon <br>


<br><br>
## How to run app's function (same to folder type's app, in home page )

![08](https://user-images.githubusercontent.com/32695143/57983988-0c331900-7a92-11e9-82b7-5606115f6e95.jpg) ![09](https://user-images.githubusercontent.com/32695143/57983989-0c331900-7a92-11e9-9059-c129fbff1bd1.jpg) ![10](https://user-images.githubusercontent.com/32695143/57983990-0ccbaf80-7a92-11e9-92ed-f0e42d096c93.jpg)

#### pre coundition : 
```
1. app has short cut badge ( https://developer.android.com/training/notify-user/badges )
2. app has third party library logic ( https://github.com/seose/launcher-q/tree/dev/TwoStepExample )
3. expert type that set from short cut setting - expert
```

#### step 1
show mark TBLR on touch position when tap to screen<br>
( caution : touch top edge, bottom edge is not work )<br>
( you can setting mark distance on touch position in launcher setting )

#### step 2
Immediately move scroll to TBLR, show grid shape shortcut window that has 0~16 app icons on your finger position

#### step 3
waiting gage that placed on screen top

#### step 4
after complete gage, show app's function list window

#### step 5
finally, run function in app when touch item in function list window


<br><br>
## Another commands in home page

![11](https://user-images.githubusercontent.com/32695143/57984158-e4dd4b80-7a93-11e9-8f46-dba6794b1a12.jpg)

```
1. enter drawer, swiping bottom to top in bottom boundary
2. open status bar, swiping top to bottom in bottom boundary
( you can setting swiping boundary in launcher setting )
3. screen off immediately, double tap to screen
4. settings [screen, shortcut, theme, launcher], long tap to screen -> setting menu you want
```

<br><br>
## How to enter shortcut setting (in home page )

![12](https://user-images.githubusercontent.com/32695143/57983986-0b9a8280-7a92-11e9-8d65-a86fcd463815.jpg) ![13](https://user-images.githubusercontent.com/32695143/57983988-0c331900-7a92-11e9-82b7-5606115f6e95.jpg) ![14](https://user-images.githubusercontent.com/32695143/57983993-0d644600-7a92-11e9-9983-0ad4e7214624.jpg)

### method 1
long tap to screen -> Shrotcut setting menu

### method 2

#### step 1
show mark TBLR on touch position when tap to screen<br>
( caution : touch top edge, bottom edge is not work )<br>
( you can setting mark distance on touch position in launcher setting )

#### step 2
Immediately move scroll to TBLR, show grid shape shortcut window that has 0~16 app icons on your finger position

#### step 3
Long tap to empty grid

### method 3 ( open shortcut setting with set app to shortcut window )

#### step 1
open draw, swiping bottom to top in bottom boundary 
( you can setting bottom boundary in launcher setting )

#### step 2
long tap to inserting app to shortcut window

#### step 3
select insertion dircetion


<br><br>
## What is shortcut setting ( in shortcut setting )

![15](https://user-images.githubusercontent.com/32695143/57983994-0d644600-7a92-11e9-900c-edc00b52a98c.jpg) ![16](https://user-images.githubusercontent.com/32695143/57983996-0dfcdc80-7a92-11e9-88cb-77012406cb98.jpg) ![17](https://user-images.githubusercontent.com/32695143/57983997-0dfcdc80-7a92-11e9-85bb-6d7b1248941e.jpg) ![18](https://user-images.githubusercontent.com/32695143/57983998-0dfcdc80-7a92-11e9-8305-4dcc2d15ebf6.jpg) ![19](https://user-images.githubusercontent.com/32695143/57983999-0e957300-7a92-11e9-8f46-601637e75f60.jpg) 

### you can manage shortcut window from shortcut setting

#### after manage position after choose TBLR 
```
add app - insert specific app to position
delete - delete app at position
move app - reorder app's position
folder - create folder or check app's in folder
set icon - set app's icon to default icon or from gallery
expert mode - 
  adjust command to one step or in two step list
  set android intent's detail options and start this intent
  set shortcut [webpage, call, app] using completed format in title bar's [...] icon
```
### export mode senario

![21](https://user-images.githubusercontent.com/32695143/57983989-0c331900-7a92-11e9-9059-c129fbff1bd1.jpg) ![22](https://user-images.githubusercontent.com/32695143/57983990-0ccbaf80-7a92-11e9-92ed-f0e42d096c93.jpg) ![23](https://user-images.githubusercontent.com/32695143/57983996-0dfcdc80-7a92-11e9-88cb-77012406cb98.jpg) ![24](https://user-images.githubusercontent.com/32695143/57983987-0c331900-7a92-11e9-976e-3f3371278e00.jpg) 

#### example, call senario
one step : call
two step : mother's call
           father's call
           friend jack's call
           friend mike's call
           
#### example, web brower senario
one step : chrome
two step : google.com
           weather site
           stock site
           new site
    
           
###### you can save/load shortcut arrange info from launcher setting


<br><br>
## What is screen setting

![25](https://user-images.githubusercontent.com/32695143/57984000-0e957300-7a92-11e9-85ac-3dfa7ca1e74a.jpg) ![26](https://user-images.githubusercontent.com/32695143/57984001-0e957300-7a92-11e9-8004-b359d6435758.jpg) ![27](https://user-images.githubusercontent.com/32695143/57984002-0f2e0980-7a92-11e9-9eb3-40f10523df66.jpg) ![28](https://user-images.githubusercontent.com/32695143/57984003-0f2e0980-7a92-11e9-8450-7e55c91bbee9.jpg) 

### you can manage main page's screen info

#### how to enter
long tap to screen -> Shrotcut setting menu

#### background setting
```
solid color - Set backgorun as solid color
user image - Set background as suer image
```

#### widget setting
```
widget type : time, am/pm, date, date of week, text 
setting type : X,Y position, show/hide, color, text format type, font (with user font), reset
```

###### you can save/load widget formats from launcher setting
###### you can set all widget's info with screen info at once using theme store ( https://play.google.com/store/apps/details?id=seoft.co.kr.laq_store )


<br><br>
## What is launcher setting

![29](https://user-images.githubusercontent.com/32695143/57984004-0f2e0980-7a92-11e9-89ad-be19160ef333.jpg) ![30](https://user-images.githubusercontent.com/32695143/57984005-0fc6a000-7a92-11e9-872c-b6876065085f.jpg)

#### set shourtcut, boundary
```
window row, column count - Set shortcut window row, column count
window size - Set shortcut window size
distance for open window - Set distance for open shorcut window
maintain time for open two step - Set maintain time for open shortcut window
icon resolution - Set user icon resolution
top drag touch boundray - Set top drag touch boundary for task off status bar
bottom drag touch boundary - Set bottom drag touch boundary for open app list drawer
```

#### save/load setting file
```
save Launcher Q setting file - Save setting file that has widget, shortcut info to user's location
load Launcher Q setting file - Load and Adjust setting file that has widget, shortcut info
```

#### check another info
```
share app - Share Launcher Q Application
opensource - Confirm Opensource License
launcer Q - Confirm Launcher Q's detail information
SEOFT - Guide developer's site
```


<br><br>
# License
```
Copyright 2019 Seoft

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
