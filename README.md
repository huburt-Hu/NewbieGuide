[![](https://img.shields.io/badge/release-v1.1.0-brightgreen.svg)](https://github.com/huburt-Hu/NewbieGuide/archive/v1.1.0.zip)



# NewbieGuide

An library Quickly implement the novice boot layer library for Android

This library can be displayed by simple chain calls,
a single line of code implementing the guidance layer,
and automatically implementing the first display,

and of course, the parameter configuration can be used to satisfy different display logic and requirements.

It is very convenient to adjust the position of the text and image through the custom layout.

## Change Log

v1.1.0 pre-release Add fragment support and monitor fragment's onDestroyView to destroy the NewbieGuide layer

## Document

* [中文](https://github.com/huburt-Hu/NewbieGuide/blob/master/doc/README-zh.md)

## 效果

Change the size of the highlighted view need't adjusts the code where showing the guide layer

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-08-09-161703.png)  
![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/change_size.png)

The guide layer's XML can be fully customizable, just you like

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-11-03-151550.png)


## Download

Project build.gradle adds
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```
 
build.gradle of module adds
 ```
 dependencies {
	  compile 'com.github.huburt-Hu:NewbieGuide:v1.1.0'
	}
 ```

## Usage
 
### The basic use：

```
NewbieGuide.with(this)//activity or fragment
                .setLabel("guide1")//Set guide layer labeling to distinguish different guide layers, must be passed! Otherwise throw an error
                .addHighLight(textView, HighLight.Type.RECTANGLE)//Add the view that needs to be highlighted
                .setLayoutRes(R.layout.view_guide)//Custom guide layer layout, do not add background color, the boot layer background color is set by setBackgroundColor()
                .show();
```
### More parameter configuration

```
Controller controller = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListener() {//add listener
                    @Override
                    public void onShowed(Controller controller) {
                        //when guide layer display
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        //when guide layer dismiss
                    }
                })
                .setBackgroundColor(Color.BLACK)//Set the background color of the guide layer and suggest translucent. The default background color is: 0xb2000000
                .setEveryWhereCancelable(false)//The Settings click anywhere to dismiss, and default is true
                .setLayoutRes(R.layout.view_guide, R.id.textView)//The second variable parameter is to click on the view's id of the hidden guide layer view
                .alwaysShow(true)//Show the boot layer each time,default is false
                .build();//Build the controller for the guide layer
        controller.resetLabel("guide1");
        controller.remove();//remove the guide layer
        controller.show();//show the guide layer
```

## License

 ```
Copyright 2017 huburt-Hu

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
