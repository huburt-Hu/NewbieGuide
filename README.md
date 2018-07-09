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
compileOnly 'com.android.support:appcompat-v7:25.3.1'
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
	  compile 'com.github.huburt-Hu:NewbieGuide:v2.4.0'
	}
```

**确保你的项目中已经依赖了appcompat-v7**


## 使用

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

### 添加高亮

#### 高亮view

addHighLight方法有多个重写，完整参数如下：

```
    /**
     * 添加需要高亮的view
     *
     * @param view          需要高亮的view
     * @param shape         高亮形状{@link com.app.hubert.guide.model.HighLight.Shape}
     * @param round         圆角尺寸，单位dp，仅{@link com.app.hubert.guide.model.HighLight.Shape#ROUND_RECTANGLE}有效
     * @param padding       高亮相对view的padding,单位px
     * @param relativeGuide 相对于高亮的引导布局
     */
    public GuidePage addHighLight(View view, HighLight.Shape shape, int round, int padding, @Nullable RelativeGuide relativeGuide)

```
#### 高亮区域（v2.3.0新增）

有些情况可能不太容易获得高亮view的引用，那么此时可以用添加高亮区域的方式来代替，
计算出需要高亮的view在anchor中位置，将获得的rectF传入addHighLight方法

```
    /**
     * 添加高亮区域
     *
     * @param rectF         高亮区域，相对于anchor view（默认是android.R.id.content）
     * @param shape         高亮形状{@link com.app.hubert.guide.model.HighLight.Shape}
     * @param round         圆角尺寸，单位dp，仅{@link com.app.hubert.guide.model.HighLight.Shape#ROUND_RECTANGLE}有效
     * @param relativeGuide 相对于高亮的引导布局
     */
    public GuidePage addHighLight(RectF rectF, HighLight.Shape shape, int round, @Nullable RelativeGuide relativeGuide)
```

### 高亮区域点击事件（v2.4.0新增）

之前也是issues中提到需要这个功能，希望能够开放此api，因此在2.4版本中增加了。
由于目前高亮view的相关参数过多，因此将一些新增的配置都放入了HighlightOptions中，HighlightOptions可以通过内部Builder对象构建：

```
HighlightOptions options = new HighlightOptions.Builder()
         .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                       Toast.makeText(FirstActivity.this, "highlight click", Toast.LENGTH_SHORT).show();
                }
         })
         .build();
GuidePage page = GuidePage.newInstance().addHighLightWithOptions(btnRelative, options);
NewbieGuide.with(FirstActivity.this)
         .setLabel("relative")
         .alwaysShow(true)//总是显示，调试时可以打开
         .addGuidePage(page)
         .show();
```

### 自定义高亮区域绘制内容（v2.4.0新增）

该功能主要是为了满足[issue51](https://github.com/huburt-Hu/NewbieGuide/issues/51)提出的需求。

首先构建一个HighlightOptions，并设置OnHighlightDrewListener：

```
HighlightOptions options = new HighlightOptions.Builder()
        .setOnHighlightDrewListener(new OnHighlightDrewListener() {
            @Override
            public void onHighlightDrew(Canvas canvas, RectF rectF) {
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(10);
                paint.setPathEffect(new DashPathEffect(new float[]{20, 20}, 0));
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2 + 10, paint);
            }
        })
        .build();
```

onHighlightDrew方法会在引导层绘制高亮之后马上回调，可通过canvas，以及给定的高亮区域rectF，绘制想要任何视图，例如上述示例中完成的issue提出的虚线。

然后与高亮区域点击事件类似，传入highlight以及option到addHighLightWithOptions方法中：

```
GuidePage page = GuidePage.newInstance().addHighLightWithOptions(btnRelative, options);
```


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


### 引导布局

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
        public void onLayoutInflated(View view, Controller controller) {
            TextView tv = view.findViewById(R.id.tv_text);
        }
    })
```

该方法还有一个可变参数`setLayoutRes(@LayoutRes int resId, int... id)`，传入id数组表示在布局中点击让引导页消失或者进入下一页的View（例如，Button ok的id）。
`setOnLayoutInflatedListener`设置布局填充完成的监听，当传入的xml(`R.layout.view_guide_dialog`)填充完成时会回答调用该监听，用于初始化自定布局的元素。

#### 相对高亮位置的引导布局（v2.3.0新增）

鉴于好多人提出上面的方法对于箭头指向的引导控制起来比较麻烦，在不用的手机屏幕尺寸上会有位置差异。
因此v2.3版本新增在高亮相对位置添加引导布局的方法。扩展addHighLight方法的重载，新增参数RelativeGuide：
```
.addGuidePage(
        GuidePage.newInstance()
                .addHighLight(btnRelative, new RelativeGuide(R.layout.view_relative_guide,
                        Gravity.RIGHT, 100))
)
```

RelativeGuide构造需要2个必传参数：
+ 布局layout的res id；
+ gravity 目前仅支持 Gravity.LEFT Gravity.TOP Gravity.RIGHT Gravity.BOTTOM

还有一个可选参数padding，表示与高亮view的padding

引导层本质是一个FrameLayout，RelativeGuide与setLayoutRes都会成为FrameLayout的子view，两者可以共存，
setLayoutRes在下层，多个RelativeGuide按照添加顺序依次添加。

各个方向的对齐方式如下图所示：

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/relative_default_gravity.png)

如Gravity.LEFT 的top与高亮view的top对齐，如果想改变，可以通过在传入布局的根布局添加marginTop。
或者还可以继承RelativeGuide并复写offsetMargin方法修改位置，具体细节可查看RelativeGuide类。

### 引导页控制（v2.2.1版本新增）

v2.2.1版本Controller新增两个方法用于控制引导页的回退，可以在OnLayoutInflatedListener接口的回调方法中获取到controller对象，执行相应的操作。

```
.setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
             @Override
             public void onLayoutInflated(View view, final Controller controller) {
                        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  controller.showPage(0);
                              }
                        });
             }
})
```

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

引导层其实是一个FrameLayout，设置anchor之后，引导层的大小就与anchor所占的位置相同。默认是android.R.id.content。setLayoutRes方法设置的说明布局则会添加到引导层的FrameLayout中。

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
            public void onLayoutInflated(View view, Controller controller) {
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





## [Q&A](https://github.com/huburt-Hu/NewbieGuide/wiki/Q&A)

遇到问题可以看查看Q&A

## 关于我

Github：https://github.com/huburt-Hu

CSDN：http://blog.csdn.net/Hubert_bing

简书：https://www.jianshu.com/u/002f99a0df6b

掘金：https://juejin.im/user/57bb1fdcc4c971006152d7b0/posts


**本人正在考虑新的工作机会，如果有上海的公司，欢迎内推我，联系邮箱654360340@qq.com或者微信h139x726845**


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

