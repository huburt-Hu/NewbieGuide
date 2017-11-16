[![](https://jitpack.io/v/huburt-Hu/NewbieGuide.svg)](https://jitpack.io/#huburt-Hu/NewbieGuide)


# NewbieGuide
Android 快速实现新手引导层的库

这是一款可以通过简洁链式调用，一行代码实现引导层的显示，自动判断首次显示，当然也可以通过参数配置来满足不同的显示逻辑和需求。
通过自定义layout.xml实现文本及image的添加，非常方便位置的调整，避免代码调整各种不好控制的情况：实验5，6次才最终确定文字等的位置。

## 更新日志

**v1.2.0**

修改实现细节以支持多张引导页的显示，现在可以在一个引导层中显示多张引导页，不用再通过监听引导层的消失显示下一张引导层，可以只通过一行链式调用实现多页引导页的显示和切换。


**v1.1.1**

优化listenerFragment的销毁时机，原本为依附的fragment销毁才销毁。现在是只要引导层消失，listenerFragment就会销毁。
新增参数设置：`fullScreen()`，用于设置是否全屏显示


**v1.1.0 pre-release**

新增fragment支持，监听fragment的onDestroyView销毁引导层


## 效果

改变高亮view的尺寸，并不用调整显示引导层的代码

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-08-09-161703.png)  
![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/change_size.png)

引导层的xml可以完全自定义，像怎样显示就怎样显示

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-11-03-151550.png)


## 此库依赖

```
    compile 'com.android.support:appcompat-v7:25.3.1'
``` 

## 导入

项目的build.gradle添加

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

module的build.gradle添加

```
 dependencies {
	  compile 'com.github.huburt-Hu:NewbieGuide:v1.2.0'
	}
```

如果你的项目中使用了appcompat-v7，可以排除此库对v7的引用，避免版本混淆

```
 dependencies {
	  compile ('com.github.huburt-Hu:NewbieGuide:v1.2.0') {
            exclude group: 'com.android.support'
      }
 }
```

## 使用

### 基本使用：

```
NewbieGuide.with(this)//传入activity
                .setLabel("guide1")//设置引导层标示，用于区分不同引导层，必传！否则报错
                .addHighLight(textView, HighLight.Type.RECTANGLE)//添加需要高亮的view
                .setLayoutRes(R.layout.view_guide)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                .show();//显示引导层
```

### 更多参数


```
        //新增多页模式，即一个引导层显示多页引导内容
        NewbieGuide.with(this)
                .setLabel("page")//设置引导层标示区分不同引导层，必传！否则报错
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        Log.e(TAG, "NewbieGuide onShowed: ");
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Log.e(TAG, "NewbieGuide  onRemoved: ");
                        //引导层消失（多页切换不会触发）
                    }
                })
                .setOnPageChangedListener(new OnPageChangedListener() {
                    @Override
                    public void onPageChanged(int page) {
                        Log.e(TAG, "NewbieGuide  onPageChanged: " + page);
                        //引导页切换，page为当前页位置，从0开始
                    }
                })
                .alwaysShow(true)//是否每次都显示引导层，默认false，只显示一次
                /*-------------以上元素为引导层属性--------------*/

                .addHighLight(textView)//设置高亮的view
                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                .asPage()//保存参数为第一页
                /*------------- 第一页引导页的属性 --------------*/

                .addHighLight(button)//从新设置第二页的参数
                .setLayoutRes(R.layout.view_guide)
                .asPage()
                /*------------- 第二页引导页的属性 --------------*/

                .addHighLight(textView)
                .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件id
                .setEveryWhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                .fullScreen(true)//是否全屏，即是否包含状态栏，默认false，设置为true需要Activity设置为全屏或者沉浸式状态栏
                .setBackgroundColor(getResources().getColor(R.color.testColor))//设置引导页背景色，建议使用有透明度的颜色，默认背景色为：0xb2000000
//                .asPage()//只有一页或者最后一页可以省略
                /*------------- 第三页引导页的属性 --------------*/

                .show();//显示引导层
```

## 流程控制

### NewbieGuide

`NewbieGuide.with()` 为入口方法，返回Builder对象，用于构建引导层

### Builder

引导层的建造者对象，v1.2.0版本新增引导页（GuidePage）的概念，一个引导层可以包含多个引导页。

| 方法    |  含义    | 归属 |
| ------- | :--------:| :--------:|
| setLabel    |  设置引导层标示区分不同引导层，必传！否则报错 | 引导层  |
| setOnGuideChangedListener    |  设置引导层的显示与消失监听 | 引导层 |
| setOnPageChangedListener    |  设置引导页切换监听 | 引导层 |
| alwaysShow    |  是否每次都显示引导层，默认false，只显示一次 | 引导层 |
| alwaysShow    |  是否每次都显示引导层，默认false，只显示一次 | 引导层 |
| addHighLight    |  添加引导页高亮的view | 引导页 |
| setLayoutRes    |  引导页布局，第二个可变参数为点击跳转下一页或者消失引导层的控件id | 引导页 |
| setEveryWhereCancelable    |  是否点击任意地方跳转下一页或者消失引导层，默认true | 引导页 |
| fullScreen    |  是否全屏，即是否包含状态栏，默认false，设置为true的前提是Activity设置为全屏或者沉浸式状态栏 | 引导页 |
| setBackgroundColor    |  设置引导页背景色，建议使用有透明度的颜色，默认背景色为：0xb2000000 | 引导页 |
| asPage    |  保存此方法前所有引导页的配置为GuidePage，并新建一个GuidePage用于设置下一张引导页的参数，如果只有一页或者最后一页可以省略 | 引导页 |
| build    |  生成Controller对象，控制引导层的显示，隐藏等操作 | 引导层 |
| show    |  直接显示引导层，内部是调用Controller的show方法 | 引导层 |


### GuidePage

引导页对象，包含一张引导页的信息，如高亮的view，布局，跳转控件id，是否全屏，背景色等。

### Controller

通过Builder.build()方法返回，用于控制引导层的显示，隐藏等操作

| 方法    |  含义    |
| ------- | :--------:|
| show    |  显示引导层的第一页 |
| resetLabel    |  设置此引导层从没有显示过 |
| remove    |  移除引导层 |


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
