[![](https://jitpack.io/v/huburt-Hu/NewbieGuide.svg)](https://jitpack.io/#huburt-Hu/NewbieGuide)


# NewbieGuide

An library Quickly implement the novice boot layer library for Android

This library can be displayed by simple chain calls,
a single line of code implementing the guidance layer,
and automatically implementing the first display,

and of course, the parameter configuration can be used to satisfy different display logic and requirements.

It is very convenient to adjust the position of the text and image through the custom layout.

## Document

* [中文](https://github.com/huburt-Hu/NewbieGuide/blob/master/doc/README-zh.md)


## Change Log

**v1.2.0**

Modify the implementation to support more than one guide page display.

**v1.1.1**

Optimize the destruction time of listenerFragment

**v1.1.0 pre-release**

Add fragment support and monitor fragment's onDestroyView to destroy the NewbieGuide layer


## Effect

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

If you use appcompat-v7 on your project, you can exclude the library's references to v7 and avoid conflict

```
 dependencies {
	  compile ('com.github.huburt-Hu:NewbieGuide:v1.1.1') {
            exclude group: 'com.android.support'
      }
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
        NewbieGuide.with(this)
                .setLabel("page")//Set the guide layer labeling to distinguish different guide layers
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //when guide layer display
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //when guide layer dismiss（Multi-page switching will not be triggered）
                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {
                        Log.e(TAG, "NewbieGuide  onPageChanged: " + page);
                        //The boot page switch, page for the current page position, starting at 0
                    }
                })
                .alwaysShow(true)//If the boot layer is displayed each time, the default false is displayed only once
                /*-------------The above element is the boot layer attribute--------------*/

                .addHighLight(textView)//Set the highlighted view
                .setLayoutRes(R.layout.view_guide)
                .asPage()
                /*------------- The properties of the first page boot page --------------*/

                .addHighLight(button)
                .setLayoutRes(R.layout.view_guide)
                .asPage()
                /*------------- The second page of the boot page properties --------------*/

                .addHighLight(textView)
                .setLayoutRes(R.layout.view_guide_custom, R.id.iv)
                .setEveryWhereCancelable(false)
                .fullScreen(true)
                .setBackgroundColor(getResources().getColor(R.color.testColor))
//                .asPage()//If only one or the last page can be omitted
                /*------------- The third page of the boot page properties --------------*/

                .show();
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
