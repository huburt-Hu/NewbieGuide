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

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/shape_dialog.png)


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
	  compile 'com.github.huburt-Hu:NewbieGuide:v2.1.0'
	}
```

如果你的项目中使用了appcompat-v7，可以排除此库对v7的引用，避免版本混淆

```
 dependencies {
	  compile ('com.github.huburt-Hu:NewbieGuide:v2.1.0') {
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

### 简单使用

```
NewbieGuide.with(activity)
        .setLabel("guide1")
        .addGuidePage(GuidePage.newInstance()
            .addHighLight(btnSimple)
            .setLayoutRes(R.layout.view_guide_simple))
        .show();
```

通过链式调用，一行代码即可实现引导层的显示，来看下效果：

![simple use](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/simple_use.png)

其中：

+ `with`方法可以传入Activity或者Fragment，获取引导页的依附者。Fragment中使用建议传入fragment，内部会添加监听，当依附的Fragment销毁时，引导层自动消失。
+ `setLabel`方法用于设置引导页的标签，区别不同的引导页，该方法**必须**调用设置，否则会抛出异常。内部使用该label控制引导页的显示次数。
+ `addGuidePage`方法添加一页引导页，这里的引导层可以有多个引导页，但至少需要一页。
+ `GuidePage`即为引导页对象，表示一页引导页，可以通过`.newInstance()`创建对象。并通过`addHighLight`添加一个或多个需要高亮的view，该方法有多个重载，可以设置高亮的形状，以及padding等（默认是矩形）。`setLayoutRes`方法用于引导页说明布局，就是上图的说明文字的布局。
+ `show`方法直接显示引导层，如果不想马上显示可以使用`build`方法返回一个Controller对象，完成构建。需要显示得时候再次调用Controller对象的show方法进行显示。


### 显示次数控制

通常情况下引导页只在用户首次打开app的时候显示，第二次进入时不显示，因此默认只显示一次。当然你也可以通过`.setShowCounts(3)`自定义显示的次数，调试的时候可以使用`.alwaysShow(true)`设置每次都显示。

```
NewbieGuide.with(activity)
        .setLabel("guide1")
        //.setShowCounts(3)//控制次数
        .alwaysShow(true)//总是显示，调试时可以打开
        .addGuidePage(GuidePage.newInstance()
            .addHighLight(btnSimple)
            .setLayoutRes(R.layout.view_guide_simple))
        .show();
```

就算设置了`.alwaysShow(true)`，内部还是会记录显示得次数，之后改会`setShowCounts(3)`可能实际记录的次数早已超过限制，因此不会再次显示。使用Controller对象的`resetLabel`方法重置次数。（或者清除应用缓存也能重置次数）


### 自定义说明布局

着重说明一下setLayoutRes方法，通常其他的类似的库都是通过代码参数来控制说明内容展示在高亮view相对的位置，如下方。经常需要多次运行才能找到满意的位置的参数。大多说明内容只能出现在高亮的上下左右，需要库的支持，自定义的程度不是很高。

我所采用的方式是将说明内容通过xml的方式，自定义摆放位置。使得说明内容高度自定义，不管你是简单的图片，还是对话框类型的都可以。

![like dialog](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/shape_dialog.png)

```
GuidePage.newInstance()
    .addHighLight(btnDialog)
    .setEverywhereCancelable(false)//是否点击任意位置消失引导页，默认true
    .setLayoutRes(R.layout.view_guide_dialog, R.id.btn_ok)
    .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
        @Override
        public void onLayoutInflated(View view) {
            TextView tv = view.findViewById(R.id.tv_text);
        }
    })
```

该方法还有一个可变参数`setLayoutRes(@LayoutRes int resId, int... id)`，传入id数组表示在布局中点击让引导页消失或者进入下一页的View（例如，Button ok的id）。
`setOnLayoutInflatedListener`设置布局填充完成的监听，当传入的xml(`R.layout.view_guide_dialog`)填充完成时会回答调用该监听，用于初始化自定布局的元素。

### 背景色

引导页的背景色不要在xml中设置，通过`GuidePage.setBackgroundColor()`设置引导页的背景色，不同引导页可以有不同背景色，默认是0xb2000000，建议设置有透明度的背景色。

### anchor

默认的话引导层是添加在DecorView中的，我借鉴了[Highlight](https://github.com/hongyangAndroid/Highlight)的anchor概念，可以改变引导层添加到的view，实现局部引导层的显示。通过调用`.anchor(view)`传入anchorView，即为引导层的根布局。
```
final View anchorView = findViewById(R.id.ll_anchor);

NewbieGuide.with(FirstActivity.this)
        .setLabel("anchor")                        .anchor(anchorView)
        .alwaysShow(true)//总是显示，调试时可以打开
        .addGuidePage(GuidePage.newInstance()                                .addHighLight(btnAnchor, HighLight.Shape.CIRCLE, 5)
                .setLayoutRes(R.layout.view_guide_anchor))
        .show();
```
这里实现了具体显示引导层。

![anchor](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/anchor.png)

引导层其实是一个FrameLayout，设置anchor之后，引导层的大小就与anchor所占的位置相同。默认是DecorView，即全屏。setLayoutRes方法设置的说明布局则会添加到引导层的FrameLayout中。

### 引导层显示隐藏监听

setOnGuideChangedListener添加引导层的显示隐藏监听，一个label表示一个引导层，一个引导层可以有多个引导页，引导页切换不会触发该监听。

```
NewbieGuide.with(FirstActivity.this)
    .setLabel("listener")
    .alwaysShow(true)//总是显示，调试时可以打开
    .setOnGuideChangedListener(new OnGuideChangedListener() {
        @Override
        public void onShowed(Controller controller) {
            Toast.makeText(FirstActivity.this, "引导层显示", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemoved(Controller controller) {
            Toast.makeText(FirstActivity.this, "引导层消失", Toast.LENGTH_SHORT).show();
        }
    })
    .addGuidePage(GuidePage.newInstance().addHighLight(btnListener))
    .show();
```

### 多页引导页与监听

```
.setOnPageChangedListener(new OnPageChangedListener() {
    @Override
    public void onPageChanged(int page) {
         //引导页切换，page为当前页位置，从0开始
        Toast.makeText(MainActivity.this, "引导页切换：" + page, Toast.LENGTH_SHORT).show();
    }
})
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
        .addHighLight(tvBottom, HighLight.Shape.RECTANGLE, 20)
        .setLayoutRes(R.layout.view_guide_custom, R.id.iv)//引导页布局，点击跳转下一页或者消失引导层的控件id
       .setEverywhereCancelable(false)//是否点击任意地方跳转下一页或者消失引导层，默认true
        .setBackgroundColor(getResources().getColor(R.color.testColor))//设置背景色，建议使用有透明度的颜色
        .setEnterAnimation(enterAnimation)//进入动画
        .setExitAnimation(exitAnimation)//退出动画
    )
```

### 引导页切换动画

如上面的例子所示，还可以添加引导页的切换动画

```
Animation enterAnimation = new AlphaAnimation(0f, 1f);
enterAnimation.setDuration(600);
enterAnimation.setFillAfter(true);

Animation exitAnimation = new AlphaAnimation(1f, 0f);
exitAnimation.setDuration(600);
exitAnimation.setFillAfter(true);

GuidePage.setEnterAnimation(enterAnimation)//进入动画
GuidePage.setExitAnimation(exitAnimation)//退出动画
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
| anchor | 引导层显示的锚点，即根布局，不设置的话默认是decorView（v2.1.0版本添加） |
| setShowCounts | 引导层的显示次数，默认是1次。（v2.1.0版本添加）|

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

