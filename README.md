# hy.common.android


* [引入aar的方法](#引入aar的方法)
* [hy.common.android.aar通用包](#通用开发包hy.common.android.aar)



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
    }
```

    4. 同步更新配置到工程
        [Tools] -> [Android] -> [Sync Project with Gradle Files]



通用开发包hy.common.android.aar
------