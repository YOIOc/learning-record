# Git

## 1. Git

- 用于管理多人协同开发项目的技术

## [2. 版本控制]

- #### 1.1 什么是版本控制

  - 版本控制是一种在开发的过程中用于 **管理项目的修改历史**、**查看项目的更改记录**、**备份以便恢复以前的版本**的软件工程技术。

- #### 1.2 版本控制的好处

  - 实现跨区域多人协同开发
  - 追踪和记载一个或者多个文件的历史记录

- #### 1.3 版本控制的分类

  - **本地版本控制**

    ![image-20240322095552853](https://github.com/YOIOc/learning-record/blob/main/image/本地版本控制.png)

    ​                                               **在每次更新时，拷贝一份当前最新版本，并在这份拷贝文件上修改更新**

  - **集中版本控制** - **SVN**

    优点：

    1. 可以进行访问控制
    2. 便于集中管理

    缺点：

    1. 如果用户不联网，就看不到历史版本
    2. 如果服务器损坏，将丢失所有数据

    ![image-20240322100437560](https://github.com/YOIOc/learning-record/blob/main/image/集中版本控制.png)

    ​                                  **所有的版本数据都保存在服务器上，协同开发者从服务器上同步更新或上传自己的修改**

  - **分布式版本控制** - **Git**

    优点：

    1. 开发者可在不联网的情况下开发
    2. 解即使某个开发者出现了问题，也不会影响版本库的完整性

    缺点：

    1. 空间占用大
    2. 不利于进行访问控制

    ![image-20240322101023424](https://github.com/YOIOc/learning-record/blob/main/image/分布式版本控制.png)

    ​                                                                      **所有版本信息仓库全部同步到本地的每个用户**

## 3. 安装Git 

- #### 3.1 前往镜像网站，下载安装包http://npm.taobao.org/mirrors/git-for-windows/

- #### 3.2 反安装

  - 删除path环境中，有关git的环境变量
  - 在控制面板的卸载程序中，卸载Git应用

- #### 3.3 使用安装包安装，修改安装路径后，无脑next

## 4. Git程序说明

- **Git Bash**：Unix与Linux风格的命令行【使用最多】
- **Git CMD**：Windows风格的命令行
- **Git GUI**：图形界面的Git

## [5. 常用Linux命令]

- 路径有关

  ```bash
  移动到指定路径：cd 路径
  回退到上级目录：cd ..
  显示当前所在的目录路径：pwd
  ```

- 目录、文件

  ```bash
  列出当前目录中的所有文件：ls(ll)    // (ll内容更详细)
  
  新建文件：touch 文件名
  新建目录：mkdir 目录名
  
  删除文件：rm 文件名
  删除目录：rm -r 目录名
  
  将文件移动到目录中：mv 文件名 目录名 // (文件和目标目录在同一目录下)
  ```

- 功能

  ```bash
  初始化终端：reset
  清屏：clear
  查看命令历史：history
  帮助：命令 help
  退出：exit
  ```

- `#`  注释

## 6. Git 环境配置

- #### 6.1 查看配置 `git config -l`

  - 查看系统级配置

    ```bash
    # 对应目录：D:\Git\etc\gitconfig
    git config --system --list
    ```

  - 查看用户级配置

    ```bash
    # 对应目录：C:\Users\YOIO\.gitconfig
    git config --global --list
    ```

- #### 6.2 配置用户信息

  - 开发者在每次提交数据时，都会将个人信息嵌入进去

  - ```bash
    # 配置前，先清空用户级文件中的内容
    git config --global user.name "YOIO" #名称
    git config --global user.email zhaoang.YOIO@gmail.com #邮箱
    ```

## 7. Git 的工作原理

- #### 7.1 工作域

  - Workspace：工作区（本地编写代码的地方）
  - Stage：暂存区（是一个文件，用于准备和组织即将提交的更改）
  - Repository：仓库区（本地仓库，包含提交的所有版本数据）
  - Remote：远程仓库（中央仓库）

  ![image-20240322151515303](https://github.com/YOIOc/learning-record/blob/main/image/工作域.png)

- #### 7.2 工作流程

  1. 在工作区中添加、修改文件                    【已修改-modified】
  2. 将需要进行版本管理的文件放入暂存区 【以暂存-staged】
  3. 将暂存区中的文件提交到Git仓库           【已提交-committed】

## 8. Git 项目搭建

- #### 8.1 本地搭建仓库

  - 在想要创建Git仓库的目录中，鼠标右键选择`Open Git Bash here`
  - 在命令行中输入`git init`，初始化项目，生成 .git 文件

- #### 8.2 克隆远程仓库

  - 在想要创建Git仓库的目录中，鼠标右键选择`Open Git Bash here`
  - 在命令行中输入`git clone url`（填写远程仓库的url），初始化项目，生成 .git 文件

## 9. Git 文件操作

- #### 9.1 文件的四种状态

  ![image-20240322190313959](https://github.com/YOIOc/learning-record/blob/main/image/文件的四种状态.png)

- #### 9.2 查看文件状态

  ```bash
  git status [文件名]
  ```

- #### 9.3 忽略文件

  - 什么是忽略文件：不想纳入版本控制的文件

  - 如何设置忽略文件：在主目录（.git所在的目录）下建立".gitignore"文件，并在其中配置

  - 配置规则：

    1. 注释：#
    2. 通配符：*[任意多个] / ?[一个] ......等
    3. 不忽略：!
    4. 此目录下文件：目录名/
    5. 此目录中文件：/目录名

    ```python
    *.txt       #忽略以.txt结尾的文件
    !lib.txt    #不忽略lib.txt文件
    /temp       #忽略根目录下的temp目录("/"代表根)
    build/      #忽略build目录下的所有文件
    doc/*.txt   #忽略doc目录下以.txt结尾的文件
    ```

## 10. Git分支

- #### 10.1 什么是分支

  - 多个分支相当于多个平行宇宙，开发者可以在不同的分支上进行不同的开发和修改，而不会影响其他分支
  - 例如：我们使用 "main" 主分支存储稳定的代码版本。使用"dev"分支进行开发和测试，完成后，再将"dev"合并回 "main"

![image-20240324135047653](https://github.com/YOIOc/learning-record/blob/main/image/分支.png)

- #### 10.2 Git中的分支操作

  - 查看所有分支

    ```bash
    # 查看本地
    git branch
    
    # 查看远程
    git branch -r
    ```

  - 新建分支

    ```bash
    git branch [分支名]
    ```

  - 切换分支

    ```bash
    git checkout [分支名]
    
    # 新建并切换
    git checkout -b [分支名]
    ```

  - 合并指定分支到当前分支

    ```bash
    git merge [分支名]
    ```

  - 删除分支

    ```bash
    git branch -d [分支名]
    
    # 删除远程分支
    git branch -dr [分支名]
    ```

  - 重命名分支

    ```bash
    git branch -m [原名称] [新名称]
    ```

- #### 10.3 分支冲突

  - 产生冲突的原因：提交者的版本库 < 远程仓库的版本
  - 解决措施：
    - 情况一 → 非部门共享代码发生冲突（非交叉）
      - `git pull` 将本地仓库同步
      - `git add .`  → `git commit -m [提交信息]` →  `git push` 将本地仓库提交
    - 情况一 → 部门共享代码发生冲突（交叉）
      - `git pull` 将本地仓库同步
      - 与其他部门协商修改公共代码的保留情况
      - `git add .`  → `git commit -m [提交信息]` →  `git push` 将本地仓库提交

## 11. Git中的一些概念

- #### 11.1 HEAD

  - HEAD 是一个特殊的指针，它指向当前所在的分支的最新提交（HEAD 代表了你当前的工作状态）

- #### 11.2 分支继承

  - 创建的新分支会继承当前所在分支的所有提交与修改，当前分支又叫新分支的 "基"（Git仓库创建好后，默认在main分支下）

- #### 11.3 签出

  - 从当前分支变更到指定分支

- #### 11.3 回滚

  - 

- #### 11.4 合并 与 变基

  - 合并：
  - 变基：

- #### 11.5 提取 与 拉取

  - 提取：从远程仓库获取所有你还没有的提交
  - 拉取：提取 + 合并

## 11. 在Github上配置远程仓库

- #### 11.1 配置SSH公钥，实现免登录

  - 打开 `Git Bash` 输入 `ssh-keygen -t rsa`  生成公钥
  - 打开生成的公钥文件（C:\Users\YOIO\.ssh\id_rsa.pub），全选复制
  - 登录个人Github进入设置（setting），点击左边栏  `SSH and GPG keys `，然后选择 `New SSH key`
  - 将复制好的公钥内容粘贴到 `key` 中，并为该公钥设置`title`，点击`Add SHH key`

- #### 11.2 创建远程仓库

  - 登录个人Github后，点击右上角 "+" 号，选择 `New repository` 
  - 在 `Repository name` 上设置仓库名字
  - 仓库的其他设置
    - 可在 `Description` 中添加仓库描述
    - 可选择仓库的访问权限 `Public` / `Private`
    - 可添加仓库的详细描述文件 - `Add a README dile` 
    - 可为仓库添加忽略文件类型 -  `Add.gitignore` 
    - 可为仓库添加许可 -  `Choose a license` 
  - 点击 `Create repository`
  - 在跳转页面的蓝色区域中，选择仓库地址的类型 `HTTP` / `SSH` （这里我们选择SSH）

- #### 10.3 关联远程仓库

  - 复制仓库的 `HTTP` / `SSH` 地址
  - 在本地仓库目录下打开`Git Bash` ，输入关联命令 `git remote add origin[仓库别名] URL[仓库地址]`  

## 12. 在IDEA中为项目构建Git仓库

- #### 12.1 创建项目

  - 为项目创建Git仓库
    - 新建一个项目
    - 打开IDEA顶部的 `VSC`  → `创建Git仓库`，仓库位置就设置在项目的根目录下（此时图标由 `VSC` 变成 `Git`）

  - 为项目关联远程仓库
    - 打开 `Git` → `管理远程` → `+` ，粘贴远程仓库的URL

- #### 12.2 克隆项目（自动将本地仓库与远程仓库关联）

  - 在IDEA首页中选择  `从VSC获取`
    - 粘贴远程仓库的URL
    - 克隆个人账号下的远程仓库

## 13. IDEA中的Git操作

- #### 13.1 创建分支

  - 选择IDEA顶部的 `Git` → `分支` 
  - 选择一个分支作为基，设置新分支的名称 → 创建

- #### 13.2 文件状态

  - 红色：没有进行版本控制
  - 绿色：加入，未提交
  - 蓝色：加入，已提交，有改动
  - 白色：加入，已提交，无改动
  - 灰色：git忽略文件文件，不加入版本控制（配置在.gitignore中的文件）

- #### 13.3 常用操作

  - 更新（add）：IDEA右上角`Git` 区域中的 `↓`
  - 提交（commit）：IDEA右上角`Git` 区域中的 `√` / 右键文件 `Git` → `提交文件` / 快捷键 `Ctrl + K`
    - 关于提交窗口
  - 推送（push）：IDEA右上角`Git` 区域中的 `↑`（若远程仓库没有当前分支，远程仓库会新建）

- #### 13.4 Git控制台的使用

# 实战

## → 使项目可以版本控制

- 版本控制中的几种状态
  - 添加：将文件放入暂存区，准备提交
  - 提交：将暂存区的文件保存到本地仓库
  - 推送：将本地仓库的更改上传到远程仓库

![为项目创建Git仓库](https://github.com/YOIOc/learning-record/blob/main/image/为项目创建Git仓库1.png)

​                                                                                         **方式：将项目初始化为一个 Git 仓库**

![为项目创建Git仓库2](https://github.com/YOIOc/learning-record/blob/main/image/为项目创建Git仓库2.png)

​                                                                                                     **选择项目根目录创建**

## 1. 拉取远程仓库

- 方式一：适用于从头开始获取整个项目的所有内容

![拉取远程仓库中的项目](https://github.com/YOIOc/learning-record/blob/main/image/拉取远程仓库中的项目.png)

![拉取远程仓库中的项目2](https://github.com/YOIOc/learning-record/blob/main/image/拉取远程仓库中的项目2.png)

​                                                                                               输入远程仓库的URL

- 方式二：适用于已经有本地仓库，只需要同步特定分支的最新更改

![拉取远程仓库中某一分支的内容](https://github.com/YOIOc/learning-record/blob/main/image/拉取远程仓库中某一分支的内容.png)

![拉取远程仓库中某一分支中的内容2](https://github.com/YOIOc/learning-record/blob/main/image/拉取远程仓库中某一分支中的内容2.png)

​                                                                                           拉取了msater分支的最新内容

## 2. 提交修改到本地仓库

- 若项目没有被添加到Git暂存区，先将项目添加到Git暂存区（意味着项目将被标记为准备提交到Git仓库中）

![将项目添加到Git暂存区](https://github.com/YOIOc/learning-record/blob/main/image/将项目添加到Git暂存区.png)

- 将项目提交到Git仓库（意味着将暂存区中的内容提交到本地仓库）

![将项目提交到Git仓库](https://github.com/YOIOc/learning-record/blob/main/image/将项目提交到Git仓库.png)

![将项目提交到Git仓库2](https://github.com/YOIOc/learning-record/blob/main/image/将项目提交到Git仓库2.png)

​                                                             注意：每次提交修改，都应有详细的注释告知此次提交做了那些修改

## 3. 推送修改到远程仓库

![将项目推送到远程仓库](https://github.com/YOIOc/learning-record/blob/main/image/将项目推送到远程仓库.png)

- 输入远程仓库的URL（以Gitee仓库为例）

![将项目推送到远程仓库2](https://github.com/YOIOc/learning-record/blob/main/image/将项目推送到远程仓库2.png)

- 登录Gitee账号

![将项目推送到远程仓库3](https://github.com/YOIOc/learning-record/blob/main/image/将项目推送到远程仓库3.png)

- 完成项目推送

![将项目推送到远程仓库4](https://github.com/YOIOc/learning-record/blob/main/image/将项目推送到远程仓库4.png)

## 分支的创建与合并

- 公司中的项目通常存在多个分支（主干）
  - dev：开发分支
  - test：测试分支
  - pre：预发分支
  - master：生产分支

- **分支创建**
  - 通常都是从**主干分支**创建新的**子分支**（相当于创建了N个主干分支的副本），基于**子分支**完成新的开发需求
  - 每次新分支创建完成后，应立刻将新分支推送到远程仓库

![新建Git分支](https://github.com/YOIOc/learning-record/blob/main/image/新建Git分支.png)

![新建Git分支2](https://github.com/YOIOc/learning-record/blob/main/image/新建Git分支2.png)

- **分支合并**

  情景：开发人员从dev创建子分支，在子分支上完成代码编写后，将子分支合并到dev分支，此时就需要项目主管将dev分支合并到test分支，以供测试人员测试

  注意：每次分支合并完成后，应立刻将合并后的分支推送到远程仓库

![合并分支](https://github.com/YOIOc/learning-record/blob/main/image/合并分支.png)

​                                         项目主管在测试分支下，点击开发分支中的 “将dev合并到test” 选项完成分支合并

- 合并冲突

  情景：两名开发人员都从dev分支拉取了最新的代码，此时两人本地的开发版本认为是V1，成员一在完成了开发后推送修改，此时远程仓库的开发版本认为是V2，但成员二使用的仍然是自己本地的V1版本，当成员二完成开发并推送修改，若两名成员都对同一处代码进行了修改，此时就会发生分支冲突

​                                                                        **发生推送被拒，就意味着存在合并冲突问题**

![发生合并冲突，所以推送被拒](https://github.com/YOIOc/learning-record/blob/main/image/发生合并冲突，所以推送被拒.png)

![发生合并冲突，所以推送被拒2](https://github.com/YOIOc/learning-record/blob/main/image/发生合并冲突，所以推送被拒2.png)

解决措施：

- 找到发生代码冲突的位置，联系提交修改的开发人员，协商应该保留内容
- 应用协商好的修改后，会推送最新的本地仓库版本

![如何解决合并冲突](https://github.com/YOIOc/learning-record/blob/main/image/如何解决合并冲突.png)

## 实际开发流程

以开发人员为例，在实际开发中每个开发人员通常会从**dev**新建一个**dev-Xxx**分支，在完成了公司所分配下来的任务后，将**dev-Xxx**分支合并到**dev**这个主干分支中

## 问题

- 推送修改时出现403错误：用户没有向远程仓库推送修改的权限，需要向管理员申请权限
