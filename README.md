# NewbieGuide
Android 快速实现新手引导层的库

这是一款可以通过简洁链式调用，一行代码实现引导层的显示，自动判断首次显示，当然也可以通过参数配置来满足不同的显示逻辑和需求。
通过自定义layout.xml实现文本及image的添加，非常方便位置的调整，避免代码调整各种不好控制的情况：实验5，6次才最终确定文字等的位置。

# 效果

![sample](https://github.com/huburt-Hu/NewbieGuide/raw/master/screenshoot/device-2017-08-09-161703.png)  

# 导入

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
	  compile 'com.github.huburt-Hu:NewbieGuide:v1.0.3'
	}
 ```

# 使用
 
 ## 基本使用：
 ```
NewbieGuide.with(this)//传入activity
                .setLabel("guide1")//设置引导层标示，用于区分不同引导层，必传！否则报错
                .addHighLight(textView, HighLight.Type.RECTANGLE)//添加需要高亮的view
                .setLayoutRes(R.layout.view_guide)//自定义的提示layout，不要添加背景色，引导层背景色通过setBackgroundColor()设置
                .show();//显示引导层
 ```
## 参数配置
```
Controller controller = NewbieGuide.with(this)
                .setOnGuideChangedListener(new OnGuideChangedListener() {//设置监听
                    @Override
                    public void onShowed(Controller controller) {
                        //引导层显示
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        //引导层消失
                    }
                })
                .setBackgroundColor(Color.BLACK)//设置引导层背景色，建议有透明度，默认背景色为：0xb2000000
                .setEveryWhereCancelable(false)//设置点击任何区域消失，默认为true
                .setLayoutRes(R.layout.view_guide, R.id.textView)//自定义的提示layout,第二个可变参数为点击隐藏引导层view的id
                .alwaysShow(true)//是否每次都显示引导层，默认false
                .build();//构建引导层的控制器
        controller.resetLabel("guide1");
        controller.remove();//移除引导层
        controller.show();//显示引导层
```

# License

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
