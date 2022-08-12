# JitPack 步骤

步骤一 项目根目录 build.gradle 添加

```
classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1'
```

------------------------------------------------------------------------------------------------------------------------

步骤二 项目module目录 build.gradle 添加

```
plugins {
    //...
    id 'maven-publish'
}
```

```
afterEvaluate {
    publishing {
        publications {
            release(MavenPublication) {
            from components.release
            groupId = 'ejiayou.com.ren.lib.http'
            artifactId = 'http'
            version = '1.0.0'
            }
        }
    }
}
```

一、编译范围
# implementation

Gradle 会将依赖项添加到编译类路径，并将依赖项打包到构建输出。不过，当您的模块配置 implementation 依赖项时，会让 Gradle 了解您不希望该模块在编译时将该依赖项泄露给其他模块。也就是说，其他模块只有在运行时才能使用该依赖项。
使用此依赖项配置代替 api 或 compile（已弃用）可以显著缩短构建时间，因为这样可以减少构建系统需要重新编译的模块数。例如，如果 implementation 依赖项更改了其 API，Gradle 只会重新编译该依赖项以及直接依赖于它的模块。大多数应用和测试模块都应使用此配置。

# api 
Gradle 会将依赖项添加到编译类路径和构建输出。当一个模块包含 api 依赖项时，会让 Gradle 了解该模块要以传递方式将该依赖项导出到其他模块，以便这些模块在运行时和编译时都可以使用该依赖项。
此配置的行为类似于 compile（现已弃用），但使用它时应格外小心，只能对您需要以传递方式导出到其他上游消费者的依赖项使用它。 这是因为，如果 api 依赖项更改了其外部 API，Gradle 会在编译时重新编译所有有权访问该依赖项的模块。 因此，拥有大量的 api 依赖项会显著增加构建时间。除非要将依赖项的 API 公开给单独的模块，否则库模块应改用 implementation 依赖项。


二、依赖传递
加入我们项目中有A、B、C、D
第一种情况：
A implementation B
B implementation C
A可以直接访问B中资源，但不能直接访问C
第二种情况：
A implementation B
B api C
A 可以直接访问B和C中的资源
第三种情况：
A implementation B
B implementation C
C api D
B可以直接访问C和D中资源
而A只能访问B中资源

https://www.jianshu.com/p/8962d6ba936e