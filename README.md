# hy.common.android


* [引入aar的方法](#引入aar的方法)
* [通用开发包 hy.common.android.aar](#通用开发包)
* [二维码开发包 google.zxing.android.aar](#二维码开发包)



引入aar的方法
------
以Android Studio为例

    1. 将hy.common.android.aar拷贝到工程的lib目录中。

    2. 在build.gradle添加repositories节点
```
    android {
        ...
        repositories {
            flatDir {
                dirs 'libs'
            }
        }
    }
    dependencies {
        ...
    }
```

    3. 在build.gradle中的节点添加编译的aar包
```
    dependencies {
        compile(name: 'hy.common.android', ext: 'aar')
        compile(name: 'google.zxing.android', ext: 'aar')
    }
```

    4. 同步更新配置到工程
        [Tools] -> [Android] -> [Sync Project with Gradle Files]



通用开发包
------
[hy.common.android.aar下载](hy.common.android.aar)


二维码开发包
------
[google.zxing.android.aar下载](google.zxing.android.aar) 基本ZXing V3.3.1

![image](images/Demo-2D.jpg)

![image](images/Demo-1D.jpg)