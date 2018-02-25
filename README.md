[![](https://jitpack.io/v/huburt-Hu/NewbieGuide.svg)](https://jitpack.io/#huburt-Hu/NewbieGuide)


# NewbieGuide
Android 快速实现新手引导层的库

这是一款可以通过简洁链式调用，一行代码实现引导层的显示，自动判断首次显示，当然也可以通过参数配置来满足不同的显示逻辑和需求。
通过自定义layout.xml实现文本及image的添加，非常方便位置的调整，避免代码调整各种不好控制的情况：实验5，6次才最终确定文字等的位置。

## 更新日志

[更新日志](https://github.com/huburt-Hu/NewbieGuide/wiki)


## 效果

改变高亮view的尺寸，并不用调整显示引导层的代码

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-08-09-161703.png)  
![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/change_size.png)

引导层的xml可以完全自定义，像怎样显示就怎样显示

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-11-03-151550.png)


## 此库依赖


`
compile 'com.android.support:appcompat-v7:25.3.1'
`

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
	  compile 'com.github.huburt-Hu:NewbieGuide:v2.0.0'
	}
```

如果你的项目中使用了appcompat-v7，可以排除此库对v7的引用，避免版本混淆

```
 dependencies {
	  compile ('com.github.huburt-Hu:NewbieGuide:v2.0.0') {
            exclude group: 'com.android.support'
      }
 }
```

## 使用

* [v1.x.x版本使用](https://github.com/huburt-Hu/NewbieGuide/wiki)

* v2.x版本使用

改动看似很大，实际上只是把Page单独抽离出来，通过addGuidePage方法添加一页代替之前的asPage，使其符合面对对象。

```
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
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .addHighLight(button)//添加高亮的view
                                .addHighLight(tvBottom, HighLight.Shape.RECTANGLE)
                                .setLayoutRes(R.layout.view_guide)//设置引导页布局
                                .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                    @Override
                                    public void onLayoutInflated(View view) {
                                        //引导页布局填充后回调，用于初始化
                                        TextView tv = view.findViewById(R.id.textView2);
                                        tv.setText("我是动态设置的文本");
                                    }
                                })
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .addGuidePage(
                        GuidePage.newInstance()
                                .addHighLight(tvBottom, HighLight.Shape.RECTANGLE,20)
                                .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件id
                                .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
                                .setBackgroundColor(getResources().getColor(R.color.testColor))//设置背景色，建议使用有透明度的颜色
                                .setEnterAnimation(enterAnimation)//进入动画
                                .setExitAnimation(exitAnimation)//退出动画
                )
                .show();//显示引导层(至少需要一页引导页才能显示)
```


## 流程控制

### NewbieGuide

`NewbieGuide.with()` 为入口方法，返回Builder对象，用于构建引导层

### Builder

引导层的建造者对象，一个引导层可以包含多个引导页。

| 方法    |  含义    |
| ------- | :--------:|
| setLabel    |  设置引导层标示区分不同引导层，必传！否则报错 |
| setOnGuideChangedListener    |  设置引导层的显示与消失监听 |
| setOnPageChangedListener    |  设置引导页切换监听 |
| alwaysShow    |  是否每次都显示引导层，默认false，只显示一次 |
| addGuidePage    |  添加一页引导页 |
| build    |  生成Controller对象，控制引导层的显示，隐藏等操作 |
| show    |  直接显示引导层，内部是调用Controller的show方法 |

### GuidePage v1.2.0版本新增

引导页对象，包含一张引导页的信息，如高亮的view，布局，跳转控件id，背景色等。

| 方法    |  含义    |
| ------- | :--------:|
| addHighLight    |  添加引导页高亮的view |
| setLayoutRes    |  引导页布局，第二个可变参数为点击跳转下一页或者消失引导层的控件id |
| setEverywhereCancelable    |  是否点击任意地方跳转下一页或者消失引导层，默认true |
| setBackgroundColor    |  设置引导页背景色，建议使用有透明度的颜色，默认背景色为：0xb2000000 |
| setOnLayoutInflatedListener    |  设置自定义layout填充监听，用于自定义layout初始化 |
| setEnterAnimation    |  设置进入动画 |
| setExitAnimation    |  设置退出动画 |


### Controller

通过Builder.build()方法返回，用于控制引导层的显示，隐藏等操作

| 方法    |  含义    |
| ------- | :--------:|
| show    |  显示引导层的第一页 |
| resetLabel    |  设置此引导层从没有显示过 |
| remove    |  移除引导层 |


## 关于我

Github：https://github.com/huburt-Hu

CSDN：http://blog.csdn.net/Hubert_bing

简书：https://www.jianshu.com/u/002f99a0df6b

掘金：https://juejin.im/user/57bb1fdcc4c971006152d7b0/posts


## [Q&A](https://github.com/huburt-Hu/NewbieGuide/wiki/Q&A)


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

