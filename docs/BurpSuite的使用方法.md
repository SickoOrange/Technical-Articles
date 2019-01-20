# BurpSuite的使用方法	

使用BurpSuite软件之前，需要配置浏览器代理。这里简介一种浏览器的配置方式，其他浏览器同理。如下图所示打开工具——代理设置——添加新代理、使用BurpSuite软件之前，需要配置浏览器代理。



![img](https://ask.qcloudimg.com/http-save/developer-news/hdp8bg6t6s.jpeg)

在图示位置中勾选刚刚添加的代理，启用代理.



![img](https://ask.qcloudimg.com/http-save/developer-news/67z3940osh.jpeg)

打开“burpsuite_free_v1.7.24.jar”，选择【I Accept】，如下图所示 。



![img](https://ask.qcloudimg.com/http-save/developer-news/zv9cu02ium.jpeg)

建立Burp Suite临时工程，选择【Temporaryproject】，单击【Next】→【Start Burp】。如下图所示。



![img](https://ask.qcloudimg.com/http-save/developer-news/br10e891ce.jpeg)

创建“Temporary project”完成后，选择【Proxy】→【Options】。如下图所示。默认BurpSuite设置为图示红框所示，若不同请更改与图片一致



![img](https://ask.qcloudimg.com/http-save/developer-news/b41mdo5swb.jpeg)

打开Burp Suite界面，点击【Intercept is off】使其切换回“Intercept is on”的状态，即拦截开启的状态，如下图所示。



![img](https://ask.qcloudimg.com/http-save/developer-news/nwlfthh75d.jpeg)

在浏览器中输入需要抓包地址，此时由于BurpSuite拦截了流量，页面应处于无法打开的状态，打开BurpSuite软件，点击Forward，即可打开界面。



![img](https://ask.qcloudimg.com/http-save/developer-news/mbben34o71.jpeg)

在登陆界面中，用户名为username，密码为password，点击登录，如下图所示。



![img](https://ask.qcloudimg.com/http-save/developer-news/hqx28qkn2x.jpeg)

打开BurpSuite界面，可以看到，和刚才打开界面一样，软件拦截了登录请求包，这样就可以对数据包进行分析了。



![img](https://ask.qcloudimg.com/http-save/developer-news/tzlye6h9vo.jpeg)

软件集成了很多模块，包括重发请求，攻击模块，爬虫模块，渲染模块等等，我们可以根据需要对于这个数据包进行任意操作，比如把发送的账号密码更改，然后Forward发送，这样别人接受到的就是错误的账号密码了