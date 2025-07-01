# 1. 功能分区

- 集合区：一组请求的集合，可以用文件夹细分
- 请求区：请求输入和响应返回
- 环境：设置公共变量方便拼装请求

![postman功能分区](https://github.com/YOIOc/learning-record/blob/main/image/postman功能分区.png)

# 2. 文件区

- 创建集合

  > 集合：区分业务大类

![postman创建集合](https://github.com/YOIOc/learning-record/blob/main/image/postman创建集合.png)

- 创建文件夹

  > 文件夹：用于细分请求

![post创建文件夹](https://github.com/YOIOc/learning-record/blob/main/image/post创建文件夹.png)

- 创建请求

  > 请求：基本元素

![postman创建请求](https://github.com/YOIOc/learning-record/blob/main/image/postman创建请求.png)

# 3. 工作区

- 说明
  - 在请求体中可以设置请求数据的格式

![postman工作区](https://github.com/YOIOc/learning-record/blob/main/image/postman工作区.png)

- 如何发送带有JSON数据的请求

![postman发送带有json数据的请求](https://github.com/YOIOc/learning-record/blob/main/image/postman发送带有json数据的请求.png)

# 4. 环境变量

为什么需要设置环境变量？

​	由于在实际开发中主机随时变化，测试全流程跟进会遇到测试环境off、开发环境dev、线上沙盒pre环境、线上环境pro，切换不同的ip成本太高，所以创建环境变量是必要的。

**变量使用**：工作区中使用{{VARIABLE}}来引用变量

![postman环境](https://github.com/YOIOc/learning-record/blob/main/image/postman环境.png)

**全局变量global：**使用全局变量可以保证全局变量唯一，修改只需要修改全局变量即可。特别注意的是，全局变量优先级会低于环境变量，如果全局和自定义环境都有host变量，那么范围采用自定义环境，如果用全局变量最好保证 No Environment

![postman全局环境](https://github.com/YOIOc/learning-record/blob/main/image/postman全局环境.png)

# 5. 常用功能

## 5.1 code

- **各种语音引用**：比如curl，直接引用在linux测试机执行查看返回结果
- **接口自动化**：选择python-requests，可以直接生成request模块的语言格式

![postman的code](https://github.com/YOIOc/learning-record/blob/main/image/postman的code.png)

## 5.2 Mock Servers

**适用场景**：

- 后端代码还未开发完，前端代码需要调用后端接口进行调试，该怎么办?
- 无法控制第三方系统某接口的返回，返回的数据不满足要求?
- 需要跟第三方联调但对方还未开发完成，如何提早测试自己的代码呢？

**什么是Mock**

> ​	以可控的方式模拟真实对象行为的假的对象 ,可以根据自己的实际需求 返回想要的数据
>
> 使用前提：要有完善的接口文档，URL、请求方式、请求参数、返回参数、错误码，这样才能根据实际业务需求造不同的返回数据

**如何使用Mock Server**

- 创建Mock Server

![postman创建MockServer](https://github.com/YOIOc/learning-record/blob/main/image/postman创建MockServer.png)

- 点击Collections 点击MockTest，创建了两个接口，并自动生成了examples

![postman使用MockServer](https://github.com/YOIOc/learning-record/blob/main/image/postman使用MockServer.png)

- 若要修改之前填写的接口信息，展开请求后点击default，编辑修改
  - 新增一个传入参数 fgdel
  - 新增一个返回字段 addComent

![postman修改MockServer配置信息](https://github.com/YOIOc/learning-record/blob/main/image/postman修改MockServer配置信息.png)

- 复制修改后default文件上的URL，重新创建一个request输入修改后的URL，send即可获取到最新的response
  - URL有2种访问方式：
    1. {{url}}//orderlist?status=1&fgdel=0：url为postman右侧选中的环境变量
    2. https://321435da-e5cf-4370-bc75-0d46cf224db2.mock.pstmn.io//orderlist?status=1&fgdel=0：Copy mock url + 路径，可以直接在浏览器访问

![postman修改MockServer配置信息2](https://github.com/YOIOc/learning-record/blob/main/image/postman修改MockServer配置信息2.png)

**注意**：每个request都可以有多个example（default就相当于每个request的初始example），每个example创建之后都可以修改，MockServer只会采用最后创建的example，保存example。一定要记得先设置对应的example并且保存后在运行新创建的请求，否则返回结果会报错

![postman创建新的example](https://github.com/YOIOc/learning-record/blob/main/image/postman创建新的example.png)

## 5.3 导入导出

- 导出：推荐使用v2.1版本，点击Export，本地生成json文件

![postman导出](https://github.com/YOIOc/learning-record/blob/main/image/postman导出.png)

![postman导出2](https://github.com/YOIOc/learning-record/blob/main/image/postman导出2.png)

- 导入方式1：选择文件或拖入文件导入

![postman导入](https://github.com/YOIOc/learning-record/blob/main/image/postman导入.png)

- 导入方式2：通过URL文本导入某个请求

![postman导入2](https://github.com/YOIOc/learning-record/blob/main/image/postman导入2.png)

## 5.4 历史记录

- 对于忘记保存或者想要查看最近调用的某个接口，可以通过history搜索关键词搜出来

![postman历史记录](https://github.com/YOIOc/learning-record/blob/main/image/postman历史记录.png)
