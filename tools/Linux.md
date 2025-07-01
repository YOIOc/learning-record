# Liux

附：[老杜的官方语雀笔记](https://www.yuque.com/dujubin/ltckqu/neea1g?#puEPb)

## 1. 安装虚拟机VMware

- 什么是VMware
  - VMware是一款模拟计算机系统运行环境的工具

## 2. 安装操作系统CentOS Stream

- 什么是CentOS Stream
  - CentOS Stream是Linux操作系统的一个版本
- 如何登录超级管理员root（其他用户同理）
  - 虚拟机启动后，在登陆界面选择 `未列出` ，输入用户名、密码

## 3. Xshell的安装与使用

- #### 1. 什么是Xshell

  - Xshell是一款连接远程服务器的工具

- #### 2. 安装Xshell

  - 修改安装路径，无脑下一步

- #### 3. Xshell的使用

  - **创建会话**（一个会话，就是一个远程连接）
    - 点击软件左上方的![](https://github.com/YOIOc/learning-record/blob/main/image/创建会话.png)[新建]按钮，打开会话创建窗口
    - 在 `连接` 中配置远程服务器
      - 输入连接名称（通常配置为远程服务器IP）
      - 选择连接协议（这里采用SSH）
      - 设置主机[服务器]IP（在Linux终端中使用 `ifconfig` 命令查看服务器IP）
    - 在 `用户身份验证` 中配置远程服务器的登录信息
      - 用户名
      - 登陆密码
  - **连接会话**
    - 双击会话管理器中想要连接的会话
  - **操作服务器**
    - 在主窗口中操控远程服务器

## 4. Xftp的安装与使用

- #### 1. 什么是Xftp

  - Xftp是一款文件传输工具

- #### 2. 安装Xftp

  - 修改安装路径，无脑下一步

- #### 3. Xftp的使用

  - **创建会话**（一个会话，就是一个远程连接）
    - 点击软件左上方的![](https://github.com/YOIOc/learning-record/blob/main/image/创建会话.png)[新建]按钮，打开会话创建窗口
    - 在 `站点` 中配置远程服务器
      - 输入连接名称（通常配置为远程服务器IP）
      - 选择连接协议（这里采用SFTP）
      - 设置主机[服务器]IP（在Linux终端中使用 `ifconfig` 命令查看服务器IP）
    - 在 `登录` 中配置远程服务器的登录信息
      - 用户名
      - 登陆密码
  - **连接会话**
    - 点击软件左上方的![](https://github.com/YOIOc/learning-record/blob/main/image/连接会话.png)[打开]按钮，打开会话窗口
    - 双击会话窗口中想要连接的会话
  - **文件传输**
    - 选中想要传输的文件，拖动到另一端

## 5. 磁盘管理

- #### 5.1 Linux 与 Windows系统在资源管理上的区别

  - Windows：采用不同的**磁盘分区**组织所有资源
  - Linux：采用**文档树**组织所有资源，树的根目录为 `/`

- #### 5.2 相关命令

  **pwd**

  - 功能：查看当前所在的位置

  **cd 路径**

  - 功能：切换目录
  - 技巧：
    - cd .. 回到上级目录
    - cd /  回到根
  - 关于 绝对/相对 路径
    - 绝对路径：以"/"开始
    - 相对路径：以"资源名"开始，相对当前所在位置而言（可理解为将当前路径拼接到相对路径前）

  **ls [参数] 路径**

  - 功能：列出资源
  - 参数：
    - -a：列出全部，包含隐藏资源(Linux中的隐藏资源，文件名已"."开头)
    - -l：展示资源详细信息
  - 技巧：
    - ll【等同于ls -l】

## 6. 文件详细信息及其含义

-    d **|** rwxr-xr-x. **| **2 **|** yoio **|** yoio **|** 6 **|** 3月 **|** 25 **|** 10:38 **|** 公共
     -    文件类型
          -    `-`：普通文件
          -    `d`：目录文件
          -    `l`：软连接（windows中的快捷方式）
     -    文件个数
          -    对文件：硬链接个数
          -    对目录：其下子目录的个数
     -    文件所有人
     -    文件所有组
     -    文件大小
          -    对文件：文件大小
          -    对目录：目录中子文件元数据的大小
     -    文件修改时间
     -    文件名


## 7. Vi 和 Vim

- #### 7.1 新建文件

  - `touch [文件名]`
  - `vi 文件名`  新建文件，并打开(命令行模式保存后，文件才会新建)

- #### 7.2 什么是Vi

  - Vi是Linux操作系统提供的一个文本编辑器

- #### 7.3 Vi的模式

  - 两个模式
    - 命令行模式：此模式下输入的都是命令【文件打开后所处的模式】
    - 编辑模式：此模式下输入的内容都会写入文件
  - 模式切换
    - `i`命令：命令行 ===> 编辑模式
    - `esc`键：编辑模式 ===> 命令行

- #### 7.3 Vi的使用

  - 使用文本编辑器打开文件 `vi 文件路径`
  - 在两个模式下，编辑文件
  - 命令行模式下保存并退出

- #### 7.4 Vim与Vi的区别

  - Vim是Vi的升级版，功能更加强大
  - Vim更适合coding（编码开发）；Vi适合编辑普通文件

- #### 7.5 一些命令

  - `i`  切换为插入模式
  - `esc `切换为命令模式

  ****

  - `:q` 退出（`:q!` 不保存，强制退出）
  - `:w` 保存（`:wq` 保存并退出）

  ****

  - `o` 在下一行插入
  - `dd` 删除光标所在行 / `x` 删除光标悬浮所在处的字符
  - `yy` 复制光标所在行 / `p` 粘贴到光标所在的下一行

  ****

  - `gg` 光标移到文件第一行 / `GG ` 光标移到文件最后一行
  - `^ ` 光标移到当前行首 / `$ ` 光标移到当前行首

  ****

  - `/关键字` 关键字搜索，按n键向后查找

## 8. 文件管理

- #### 8.1 新建

  - 文件：`touch 文件名... `（多个文件间用空格隔开）
  - 目录：`mkdir 目录名` 
    -  `-p` 参数创建多层目录：`mkdir -p 父/子/...`

- #### 8.2 删除

  - 文件：`rm 文件名...` （多个文件间用空格隔开）
    - `-f`参数强行删除文件（不再询问）
    - 也可使用模糊匹配：`rm -f *.java`
  - 目录：`rm -rf 目录名...`（递归删除）

- #### 8.3 复制

  - 文件：`cp 源文件路径 新文件路径` 
  - 目录：`cp -rf 源目录路径 新目录路径`

- #### 8.4 剪切

  - `mv 源文件/目录路径 新文件/目录路径`

## 9. 软链接 与 硬链接

- #### 9.1 文件inode号

  - 什么是inode号
    - index node（索引节点号）
  - inode号的作用
    - 所文件都有一个自己的inode号，Linux依靠inode号区分文件（windows依靠文件名）
  - 查看inode号
    - 使用`ls` 的 `-i` 参数查看文件的inode号

- #### 9.1 软链接

  - 概念：[Linux]软连接 == 快捷方式[Windows]
  - 本质：存储目标文件的路径，inode号与目标文件不一致（目标文件被删除，软连接失效）
  - 作用：方便操作
  - 创建：`ln -s 目标文件路径 软连接文件路径`

- #### 9.2 硬连接

  - 概念：硬连接 == 热备份
  - 本质：存储目标文件的inode号（目标文件被删除，硬连接依然有效）
  - 作用：资源备份
  - 创建：`ln 目标文件路径 硬连接文件路径`

## 10. 系统命令

- 显示当前系统时间 `date`

- 切换用户 `su 用户名`

- 查看系统进程 `ps [参数]`

  - `-e` 显示所有
  - `-f` 显示UID、PPID、C与STIME栏位信息

  ![image-20240326164406348](https://github.com/YOIOc/learning-record/blob/main/image/查看系统进程.png)

- 关闭进程 `kill 进程ID`

  - `-9` 强行终止

- 重启 `reboot`

- 关机 `shutdown`

  - `-h` 关闭系统后关闭电源

****

 **其他**

- 清屏 `clear`
- 查看命令的具体用法 
  - `man 命令名` 【"q"键退出查看，“空格”翻页】
  - `命令名 --help` 【更为常用】
- 查看文件内容 `cat 文件路径`
- 搜索指定内容 `grep 搜索内容 搜索源`

## 11. 网络通讯

- #### 11.1 ifconfig

  - 查看网卡IP

- #### 11.2 ping

  - 查看计算机之间是否可以正常通信
  - 语法：`ping ip/域名`

- #### 11.3 curl (很少用)

  - 模拟用户访问，模拟浏览器行为
  - 语法：`curl ip/域名`

- #### 11.4 wget

  - 下载资源
  - 语法：`wget 资源地址`

## 12. 管道和重定向

- #### 12.1 管道  `|`

  - 概念：将前面命令的输出作为后面命令的输入
  - 例如：`ps -ef | grep java` （查找与java有关的进程）

- #### 12.2 重定向 `>`

  - 两种方式
    - `>`：将原文件内容清空，再写入
    - `>>`：在原文件末尾追加写入
  - 例如：
    - `ifconfig > a.text ` （将本机的网络配置写入a.text文件）

## 13. 压缩 与 解压缩

- #### 13.1 tar命令

  - tar命令是在Linux系统中完成压缩和解压缩的命令（压缩后的文件又被称为**归档文件**）

- #### 13.2 tar命令参数详解

  - 执行功能（以下三个参数不能共存）
    - c：压缩
    - x：解压缩
    - t：查看压缩文件中的文件
  - 压缩/解压缩 方式
    - z：采用gzip方式（扩展名通常为 **tar.gz**，速度快） -- 常用
    - j：再用bzip2方式（扩展名通常为 **tar.bz2**，体积小）
  - 其他
    - v：展示被压缩的文件
    - f：指定文件名（该参数后为文件名，故该参数后不能再跟其他参数）
    - C：指定解压到的具体目录（大写C）

- #### 13.3 压缩

  - `tar -zcvf 压缩文件路径 被压缩文件路径...`

- #### 13.4 解压缩

  - `tar -zxvf 压缩文件路径 [-C 解压到指定路径]`

- #### 13.5 查看压缩文件

  - `tar -tf 压缩文件路径`

## 14. 文件内容查看

- #### 14.1 cat命令

```shell
用法：
	cat [参数]... [文件]...
	
功能：
	连接所有指定文件并将结果写到标准输出，如果没有指定文件，则从标准输入读取

常用参数：
	-n，行编号 / -b，行编号（不包含空白行）
	-t，将制表符(tab)显示为^I
	-e, 每行结束显示"$"
	-s, 连续空白行数量大于1时，合并为一行
	
原理：
	cat命令的作用是，将读取到的内容输出到某个地方
		默认读取：标准输入流
		默认输出：终端
```

**cat命令会一次性将文件的完整内容全部显示出来，不适合大文件**

- 技巧

  - 新建文件

  ```shell
  cat > test.txt
  ```

  - 合并文件

  ```shell
  cat a.txt b.txt > c.txt
  ```

  - 清空文件内容

  ```shell
  cat /dev/null > text.txt
  ```

****

- #### 14.2 tac命令

```shell
用法：cat [参数]... [文件]...
将cat的输出内容倒序
```

****

- #### 14.3 more命令

```shell
用法：
	more [选项] <文件>...
	
功能：
	与cat类似
	
与cat命令的异同：
	相同点：more和cat在读取文件的时候，都是一次性将文件的全部内容装载到缓存中
	不同点：cat是一次性全部打印；more是部分打印

常用参数：
	-u：禁止显示下划线和粗体
	-<number>：每屏行数
	+<number>：从行号开始显示文件
	+/<pattern>：模糊匹配
	-p：以清除原内容的方式翻页
	
常用操作：
	回车键			【显示下一行】
	空格键			【显示下一屏】
	ctrl + b	  【显示上一屏】
	=			  【显示行号】
	:f			  【显示文件名的同时显示行号】
	q			  【退出more命令】
```

- 技巧

  - 从文件中查找"java"字符串的行

  ```shell
  more +/java log.txt
  ```

  - 查看进程，每5条为一屏，翻屏时清空原内容

  ```shell
  ps -ef | more -5 -p 
  ```

****

- #### 14.4 less命令

```shell
用法：
	less [选项] <文件>...
	
功能：
	与more类似，但功能更为强大

常用参数：
	-g：只标识第一个搜索到的行
	-I：忽略搜索时的大小写
	-m：显示类似more命令的百分比
	-N：显示每行的行号
	+num：从第num行开始显示
	+F：实时监控文件动态变化(之监控由机器自动写入/重定向写入的修改)
	
常用操作：
	/字符串		【向下搜索“字符串”】
	?字符串		【向上搜索“字符串”】
	
	n			  【重复前一个搜索】
	N			  【反向重复前一个搜索】
	
	y			  【向前翻一行】
	回车键			【向后翻一行】
	u			  【向前翻半页】
	d			  【向后翻半页】
	b / ↑		  【向前翻一页】
	空格键	/ ↓		【向后翻一页】
	
	g			  【移动到第一行】
	ng			  【移动到第n行】
	G			  【移动到最后一行】
	
	=			  【显示详细信息】
	v		      【使用vim经行编辑】
	q		      【退出】
	
	:n			  【跳转到下一个文件】
	:p			  【跳转到上一个文件】
	
	m + 标记名称   【添加标记】
	' + 标记名称   【回到标记】
```

****

- #### 14.5 head和tail命令

```shell
head命令
用法：
	head [选项]... [文件]...
	
作用：
	显示文件头部的内容
	
参数：
	-c：输出前几个字节
	-n：指定输出行数（默认10行）
	-q：不显示文件名
	
tail命令
用法：
	tail [选项]... [文件]...
	
作用：
	显示文件尾部的内容
	
参数：
	-c：输出最后几个字节
	-n：指定输出行数（默认10行）
	-f：实时监控文件
	-q：不显示文件名
```

****

- #### 14.6 nl命令

```shell
用法：
	nl [选项]... [文件]...
	
作用：
	为输出内容添加行号（比-n参数更加高级）
	
参数：
	-b a：所有行添加行号（空白行默认不添加）
	-n ln：栏位左侧显示
	-n rn：栏位右侧显示
	-n rz：栏位自动补0
	-w:设置行号栏的占用位数
```

****

- #### 14.7 tailf命令

```shell
用法：
	第一步：cd命令切换到/user/local目录下，新建tail.c文件，将下方代码粘贴进文件中，保存并退出
	第二步：在/usr/local目录下，然后编译c语言程序：gcc -o /usr/bin/tailf tailf.c
	第三步：当第一次执行时，会提示下载c语言环境，一路 "y"+回车 即可
	第四步：测试tailf命令是否可用，输入命令：tailf，显示：Usage: tailf logfile 即为成功
	第五步：使用tailf命令：tailf 文件名
	
作用：
	实时监控文件（与tail -f不同，如果文件不增长则不会访问磁盘文件，适合用于跟踪日志文件，因为省电）
```

```c
/* tailf.c -- tail a log file and then follow it 
 * Created: Tue Jan  9 15:49:21 1996 by faith@acm.org 
 * Copyright 1996, 2003 Rickard E. Faith (faith@acm.org) 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions: 
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR 
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE. 
 *  
 * less -F and tail -f cause a disk access every five seconds.  This 
 * program avoids this problem by waiting for the file size to change. 
 * Hence, the file is not accessed, and the access time does not need to be 
 * flushed back to disk.  This is sort of a "stealth" tail. 
 */  
  
#include <stdio.h>  
#include <stdlib.h>  
#include <unistd.h>  
#include <malloc.h>  
#include <sys/stat.h>  
//#include "nls.h"  
#define _(s) s  
  
static size_t filesize(const char *filename)  
{  
    struct stat sb;  
  
    if (!stat(filename, &sb)) return sb.st_size;  
    return 0;  
}  
  
static void tailf(const char *filename, int lines)  
{  
    char **buffer;  
    int  head = 0;  
    int  tail = 0;  
    FILE *str;  
    int  i;  
  
    if (!(str = fopen(filename, "r"))) {  
    fprintf(stderr, _("Cannot open \"%s\" for read\n"), filename);  
    perror("");  
    exit(1);  
    }  
  
    buffer = malloc(lines * sizeof(*buffer));  
    for (i = 0; i < lines; i++) buffer[i] = malloc(BUFSIZ + 1);  
  
    while (fgets(buffer[tail], BUFSIZ, str)) {  
    if (++tail >= lines) {  
        tail = 0;  
        head = 1;  
    }  
    }  
  
    if (head) {  
    for (i = tail; i < lines; i++) fputs(buffer[i], stdout);  
    for (i = 0; i < tail; i++)     fputs(buffer[i], stdout);  
    } else {  
    for (i = head; i < tail; i++)  fputs(buffer[i], stdout);  
    }  
    fflush(stdout);  
  
    for (i = 0; i < lines; i++) free(buffer[i]);  
    free(buffer);  
}  
  
int main(int argc, char **argv)  
{  
    char       buffer[BUFSIZ];  
    size_t     osize, nsize;  
    FILE       *str;  
    const char *filename;  
    int        count;  
  
    //setlocale(LC_ALL, "");  
    //bindtextdomain(PACKAGE, LOCALEDIR);  
    //textdomain(PACKAGE);  
  
    if (argc != 2) {  
    fprintf(stderr, _("Usage: tailf logfile\n"));  
    exit(1);  
    }  
  
    filename = argv[1];  
  
    tailf(filename, 10);  
  
    for (osize = filesize(filename);;) {  
    nsize = filesize(filename);  
    if (nsize != osize) {  
        if (!(str = fopen(filename, "r"))) {  
        fprintf(stderr, _("Cannot open \"%s\" for read\n"), filename);  
        perror(argv[0]);  
        exit(1);  
        }  
        if (!fseek(str, osize, SEEK_SET))  
                while ((count = fread(buffer, 1, sizeof(buffer), str)) > 0)  
                    fwrite(buffer, 1, count, stdout);  
        fflush(stdout);  
        fclose(str);  
        osize = nsize;  
    }  
    usleep(250000);     /* 250mS */  
    }  
    return 0;  
}
```

## 15. 用户管理

- #### 15.1 关于Linux中的用户

  - Linux系统中的超级用户是root，可通过root创建其他普通用户
  - Linux系统中每一个用户都有 一个用户名+一个口令(密码)，登陆成功后就可以进入自己的主目录

- #### 15.2 用户管路

  - 用户组的管理

    概念：用户组的管理，实际上就是对/etc/group文件的更新

    信息：组名:密码标识:GID:该用户组中的用户列表

    - 查看

    ```shell
    # 查看用户组目录
    cat /etc/group 
    
    #查看当前用户属于哪一用户组
    groups
    
    # 查看用户属于哪一组
    groups 用户名
    ```

    - 添加

    ```shell
    语法：
    	groupadd [参数] 组名
    	
    参数：
    	-g：指定组标识（GID）
    ```

    - 修改

    ```shell
    用法：
    	groupmod [参数] 组名
    	
    参数：
    	-g：修改GID
    	-n：修改组名
    ```

    - 删除

    ```shell
    用法：
    	groupdel 组名
    ```

  - 用户的管理

    概念：用户的管理，实际上就是对/etc/passwd、/etc/group等文件的更新

    信息：用户名:密码:用户ID:组ID:用户备注:主目录:shell程序路径

    - 添加

    ```shell
    语法：
    	useradd [参数] 用户名  #-添加用户
    	passwd 用户名         #-设置密码
    	
    参数：
    	-d：指定主目录位置(默认/home/用户名)
    	-g：指定所在主组(默认在新建的通过名用户组)
    	-G：指定所在副组
    ```

    - 切换

    ```shell
    #root → 普通用户(无需密码) / 普通用户 → root(需要密码)
    su 用户名
    ```

    - 修改

    ```shell
    语法：
    	usermod [参数] 用户名
    	
    参数：
    	-l：修改用户名
    	-d：修改主目录
    	-m：当修改的主目录不存在时新建
    	-g：修改主组
    	-G：修改副组
    	-L：锁定用户
    	-U：解锁用户
    ```

    - 删除

    ```shell
    语法：
    	userdel [参数] 用户名 
    	
    参数：
    	-r：连同主目录一起删除
    ```

  - 给用户主目录之外的目录授权

    - 第一步：创建目录

    ```shell
    mkdir /java
    ```

    - 第二步：给目录授权

    ```shell
    # -R表示递归设置权限
    chmod -R 775 /java
    ```

    - 第三步：创建组

    ```shell
    groupadd dev
    ```

    - 第四步：把目录赋予组

    ```shell
    chgrp -R dev /java
    ```

    - 第五步：创建用户

    ```shell
    useradd xioaming
    ```

    - 第六步：设置密码

    ```shell
    passwd xiaoming
    ```

    - 第七步：给用户添加附加组

    ```shell
    usermod -G dev xiaoming
    ```

## 16. 文件权限

- #### 16.1文件权限概述

  - Linux中文件的权限包含以下三种
    - 读（Read→r）
    - 写（Writer→w）
    - 执行（eXecute→x）
  - Linux中包含以下三类用户
    - 创建者（User→U）
    - 创建者的同组用户（Group→G）
    - 创建者的其他组用户（Other→O）
  - 文件的拥有者以及root用户，可以为文件设置权限

- #### 16.2 查看文件权限

  - 采用`ls -l`命令查看文件权限

  ![image-20240328193933959](https://github.com/YOIOc/learning-record/blob/main/image/查看文件权限.png)

- #### 16.3 基于UGO设置文件权限

  ```shell
  语法：
  	chmod u/g/o[用户类型] +/-[添加or删除] r/w/x[权限种类] 文件名
  	
  案例：
  	chmod u+x g+w o-r apache-tomcat-10.1.20.tar.gz
  ```

- #### 16.4 基于421设置文件权限

  ```shell
  语法：
  	chmod xxx 文件名
  	
  采用数字来表示不同的权限：
  	4 读
  	2 写
  	1 执行
  	
  案例：
  	chmod 755 HelloWorld.java # U-rwx G-rx O-rx
  ```

## 17. CentOS中软件的安装

- #### 17.1 安装方式概述

  - 源码安装
  - rpm安装（二进制安装）
  - yum安装（在线安装- 最为常用）

- #### 17.2 yum的相关命令

  - 安装/升级：yum -y install SoftwareName
  - 升级所有：yum -y install
  - 卸载：yum remove SoftwareName
  - 列出可安装：yum list
  - 列出已安装：yum list installed
  - 查询安装包：yum list SoftwareName
  - 关键字查询安装包：yum search Keyword
  - 清楚缓存：yum clean all

- #### 17.3 JDK的安装与配置

  - **安装**
    1. 从yum仓库中搜索jdk：yum search jdk
    2. 安装jdk：yum -y install java-17-openjdk-devel.x86_64
  - **配置**（使用yum方式安装，无需配置环境）
    1. 看看jdk的安装目录：
       - which java                          【java ==> /usr/bin/java】
       - ls -l /usr/bin/java               【/usr/bin/java ==> /etc/alternatives/java】
       - ls -l /etc/alternatives/java【/etc/alternatives/java ==> /usr/lib/jvm/java-17-openjdk-17.0.6.0.9-0.4.ea.el9.x86_64】
    2. 在/etc/profile文件中配置环境变量：
       - export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.6.0.9-0.4.ea.el9.x86_64
       - export CLASSPATH=.:$JAVA_HOME/lib
       - export PATH=$PATH:$JAVA_HOME/bin
    3. 生效：source /etc/profile

- #### 17.4 MySQL的安装与配置

  - **安装**

    1. 更新系统软件包 `dnf update`（dnf命令是yum命令的升级版本，它在性能、依赖关系处理和可用插件等方面都更加优秀）
    2. 添加MySQL Yum存储库 `dnf install https://dev.mysql.com/get/mysql80-community-release-el8-1.noarch.rpm`
    3. 导入mysql公钥 `rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023` （随数据库版本升级会发生变化）
    4. 安装MySQL 8`dnf install mysql-community-server`

  - **配置**

    1. 启动MySQL服务 `systemctl start mysqld`

    2. 设置MySQL开机自启 `systemctl enable mysqld`

    3. 查看root账户的临时密码 `grep 'temporary password' /var/log/mysqld.log`

       ![image-20240329115908808](https://github.com/YOIOc/learning-record/blob/main/image/查看临时密码.png)

    4. 配置MySQL安全设置 `mysql_secure_installation` 【y→y→y→n→y→y】

    5. 登录MySQL `mysql -u root -p密码`

  - **设置字符集**

    1. 查看MySQL字符集 `show variables like '%character%';`

    2. 如果字符集不是utf-8，可在/etc/my.cnf文件中的[mysqld]下面添加如下内容进行配置（/etc/my.cnf是mysql的配置文件）

       ```text
       [mysqld]
       character-set-server=utf8mb4
       collation-server=utf8mb4_general_ci
       ```

    3. 保存退出，并重启MySQL服务 `systemctl restart mysqld`

  - **创建MySQL用户**

    1. 首先，以root用户身份登录MySQL `mysql -u root -p密码`

    2. 创建数据库 `create database oa;`

    3. 创建用户并设置密码 

       ```sql
       # USER 用户名@主机名('%'表示任何主机 - 允许远程登陆)
       # IDENTIFIED BY 口令
       CREATE USER 'java_dev'@'%' IDENTIFIED BY 'java_DEV123';
       ```

    4. 为用户授权

       ```sql
       # 权限
       # ON 数据库()
       # TO 用户名@主机名
       GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER, INDEX, REFERENCES ON oa.* TO 'java_dev'@'%';
       ```

    5. 刷新权限 `FLUSH PRIVILEGES;`

    6. 关闭防火墙 `systemctl stop firewalld`

- #### 17.5 Tomcat的安装与配置

  - **安装前提**：已经安装并配置了Java环境
  - **安装**
    1. 进入Tomcat官网，下载Tomcat
    2. 使用Xftp传输到远程Linux中
    3. 解压 `tar -xvf apache-tomcat-10.1.20.tar.gz`
    4. 第三方软件通常存放在/usr/local目录下 `mv ./apache-tomcat-10.1.20 /usr/local`
    5. 为了方便配置环境变量，将目录重命名 `mv apache-tomcat-10.1.20 tomcat10`
  - **配置**
    1. 配置环境变量
       - export CATALINA_HOME=/usr/localtomcat10
       - 为PATH添加 `$CATALINA_HOME/bin`
    2. 生效环境变量 `source /etc/profile`

## 18. 部署Web应用

- #### 18.1 部署前提

  - 确保JDK17、Tomcat10、MySQL8已经安装部署好

- #### 18.2 部署流程

  1. 初始化数据
     - 使用Navicat工具，连接Linux服务器上的MySQL数据库，创建项目数据库（通常与项目同名）
     - 执行sql脚本完成数据初始化
  2. 将项目中连接数据库的信息进行修改，例如：url、username、password等信息
  3. 使用maven将项目以war的形式打包
  4. 使用Xftp将war包上传到Tomcat的webapps目录下
  5. 启动Tomcat服务器，war包会被自动解压生成web应用文件

- #### 18.3 实时查看Tomcat服务器的后台日志

  - 切换到CATALINA_HOME/logs目录下，执行以下命令，实时查看Tomcat服务器日志

  ```shell
  tail -f catalina.out
  ```

## 19. 查看网络连状态、监听端口

在实际开发中，netstat最常用于查看网络连接状态，使用netstat排查网络问题和监控系统状态

```shell
语法：
	netstat 参数
	
连接状态：
	LISTEN：表示端口已占用，正在监听，连接未建立，等待连接
	ESTABLISHED：表示两台机器正在通讯(在进行安全检查时，尤其注意这种连接状态)
	TIME_WAIT：表示连接已终止(说明端口之前被访问过，但是访问已经结束)
	
参数：
	-a：查看所有的网络连接状态
	-l：查看所有处于监听状态的连接 / 	-o：查看所有处于TIME_WAIT状态的连接
	-t：查看所有TCP连接状态      /  -u：查看所有UDP连接状态
	
案例 - 查看指定端口的网络连接状态
	netstat -an | grep 端口号
```

## 20. 文件搜索相关

- #### 20.1 搜索文件

  ```shell
  find 指定目录 -name "文件名"
  ```

- #### 20.2 搜索字符串

  ```shell
  grep "字符串内容" 输入源
  ```

- #### 20.3 搜索命令

  ```shell
  whereis/which 命令名
  ```

## 21. 将Web应用部署到阿里云

- #### 21.1 购买阿里云服务器

  - 注册阿里云账号→[阿里云登录页 (aliyun.com)](https://account.aliyun.com/)
  - 完成账号的实名认证
  - 购买服务器
  - 重置服务器的实例密码

  ![image-20240329172131060](https://github.com/YOIOc/learning-record/blob/main/image/使用阿里云服务器.png)

- #### 21.2 Xshell的连接与宝塔面板的安装

  - 连接 - 开启"密码验证"

    1. 在阿里云服务器实例中找到VNC连接

       ![image-20240329193439450](https://github.com/YOIOc/learning-record/blob/main/image/连接阿里云服务器.png)

    2. 点击后输入用户名[root]和密码，登陆成功后找到 `/etc/ssh/sshd_config` 文件

    3. 使用vim编辑 `sshd_config` 文件中的 “passwordAuthcation”，将no修改为yes

    4. 重启sshd服务：`service sshd restart` （此时Xshell等软件就可以采用密码的方式连接阿里云服务器了）

  - 安装宝塔面板

    1. 前往[宝塔官网](https://www.bt.cn/new/index.html)复制安装脚本，并在Xshell上执行脚本

       ```shell
       yum install -y wget && wget -O install.sh https://download.bt.cn/install/install_6.0.sh && sh install.sh ed8484bec
       ```

    2. 安装成功后会有如下信息

       ```text
       ========================面板账户登录信息==========================
       
        【云服务器】请在安全组放行 18243 端口
        外网面板地址: https://8.137.97.69:18243/60f4ef51
        内网面板地址: https://172.20.132.165:18243/60f4ef51
        username: pxyudbie
        password: 928472d0
       
        宝塔面板交流QQ群：633748484
       ==================================================================
       ```

    3. 在阿里云服务器上放行宝塔面板的端口（配置安全组）

       ![image-20240329194857201](https://github.com/YOIOc/learning-record/blob/main/image/配置安全组1.png)

       ![image-20240329200201103](https://github.com/YOIOc/learning-record/blob/main/image/配置安全组2.png)

    4. 访问宝塔的外网面板地址（若网址显示危险，则忽略点击“高级” → “继续前往”）

    5. 输入 “宝塔面板账户登录信息” 中的用户名和密码（如果没有宝塔账户，先注册一个宝塔账户）

- #### 21.3 使用宝塔面板安装并配置JDK Tomcat MySQL

  - JDK

    - 安装Tomcat会自动安装JDK

  - Tmocat

    - 应用商店搜索"Tomcat"并安装
    - 阿里云 - 添加放行8080端口（阿里云控制台的安全组中）
    - 宝塔面板 - 添加放行8080端口（宝塔面板的安全中）

  - MySQL

    - 应用商店搜索"MySQL"并安装

    - 阿里云 - 添加放行3306端口（阿里云控制台的安全组中）

    - 宝塔面板 - 添加放行3306端口（宝塔面板的安全中）

    - 软件商店搜索”nginx“、”php 7.4“、”phpMyAdmin5.0“并安装（当前无法远程登录root账户）

    - 在宝塔面板左侧”数据库“中，点击”root密码“修改管理员密码

    - 在宝塔面板左侧”数据库“中，点击”phpMyAdmin“然后选择通过面板访问，在弹出窗口中登录root账号

    - 在左侧找到mysql.user表并打开，找到用户名为”root“的数据，将主机(host)设置为”%“

      ![image-20240330001355125](https://github.com/YOIOc/learning-record/blob/main/image/宝塔面板.png)

    - 重启MySQL（可在宝塔面板中重启，重启后即可在远程登录root账户）

  **注意：阿里云服务器有两个防火墙，一个是阿里云服务器自带的防火墙，在安全组中进行放行设置。阿里云服务器中安装的centos操作系统也会有一个防火墙，要访问tomcat服务器的话，centos操作系统中的防火墙也要放行8080端口：这个需要在宝塔中进行配置**

- #### 21.4 将Web应用部署到云端

  - 详见**18.2 部署流程**（宝塔面板中打开Tomcat软件的安装位置，打开webapps目录，直接将打包好的war包拖拽进来就行，无需Xftp传输）

## 22. shell编程

#### 22.1 Linux系统结构

- 内核层 ←→ shell层 ←→ 应用程序

  - 内核层负责控制管理硬件设备
  - 应用程序负责执行特有功能
  - shell层负责连接内核层与应用层

  ![image-20240331150426904](https://github.com/YOIOc/learning-record/blob/main/image/系统结构.png)

#### 22.2 shell的种类

shell是一种用于与操作系统进行交互的命令行解释器

在Linux系统中，常见的Shell语言包括以下几种：

- sh、**bash**[CentOS默认]、csh、tcsh、ksh、zsh

如何切换shell（只有拥有超级权限的用户才可以更改）：

1. 查看已安装的Shell

   ```shell
   # 打印系统上已安装的shell列表
   cat /etc/shells
   ```

2. 更改Shell

   ```shell
   # -s参数后为更改的Shell路径
   chsh -s /bin/zsh
   ```

3. 输入用户密码

4. 检查是否更改

   ```shell
   # 打印当前Shell路径
   echo $SHELL
   ```

#### 22.3 注释

- 单行注释

  ```shell
  # 注释内容
  ```

- 多行注释

  ```bash
  : '
  注释内容
  '
  ```

#### 22.4 变量

- 环境变量 - 所有会话共享

  - 查看环境变量

    ```shell
    printenv
    # 或
    env
    ```

  - 设置环境变量

    ```shell
     export 变量名=变量值
    ```

  - 使用环境变量

    ```shell
    echo $变量名
    ```

- 本地变量 - 仅当前会话可用

  - 设置本地变量

    ```shell
    变量名=变量值
    ```

  - 使用本地变量

    ```shell
    echo $变量名
    ```

- 特殊变量

  - `$0` ：当前脚本文件名
  - `$1, $2...` ：脚本参数列表中的第1个、第2个（例：./first.sh a b，执行的first命令时，第1个参数是a。第2个参数是b）
  - `$#` ：脚本参数数量
  - `$*` ：脚本参数列表
  - `$@` ：脚本参数列表（作为独立字符串）
  - `$$` ：当前脚本的进程ID
  - `$?` ：上一个命令的退出状态，一个数值

  ```shell
  # 例如
  echo "The name of this script is: $0"
  echo "The first parameter is: $1"
  echo "The total number of parameters is: $#"
  echo "All parameters are: $@"
  echo "The current process ID is: $$"
  echo "The last exit status was: $?"
  ```

#### 22.5 控制语句

- 注意：

  在shell编程中 `$((...))` 被称为算术扩展运算符，做数学运算的，并且将运算结果返回。`$(...)`运算符会将结果直接返回

  - $((j+1))，如果j是5的话，结果就会返回6 （注意，使用这个运算符的时候，括号里面不能有空格）
  - $(echo "hello world")，会将"hello world"打印，然后再将"hello world"字符串返回。

- **条件编写** **- [ 布尔值 ]**

  - 比较操作符（比较两个值的大小或判断是否相等）
    - `-eq` / `-ne` ：是否相等，例如 `[ $a -eq $b ]`
    - `-lt` / `-gt` ：是否小于/大于，例如 `[ $a -lt $b ]`
    - `-le` / `-ge` ：是否小于等于/大于等于，例如 `[ $a -le $b ]`
  - 测试表达式（测试某个表达式是否成立）- 变量用双引号括起来
    - `-f`：某个文件是否存在，且是一个常规文件，例如`[ -f file.txt ]`
    - `-d`：某个目录是否存在，且是一个常规目录，例如`[ -d dir ]`
    - `-z`：某个字符串是否为空，例如`[ -z "$str" ]`
    - `-n`：某个字符串是否非空，例如`[ -n "$str" ]`
    - `-e`：某个文件或目录是否存在，例如`[ -e file.txt ]`

- **条件控制**

  ```shell
  # if语句自上而下如果条件成立，则执行对应的语句块，执行完后跳出if语句
  # condition是要检查的条件 then表示条件成立后要执行的语句块
  
  if condition 
  then
    command1
    command2
    ...
  elif condition2 
  then
    command3
    command4
    ...
  else
    command5
    command6
    ...
  fi
  ```

- **循环控制**

  - for循环 - 用于遍历指定列表或值得集合

  ```shell
  # var是临时变量名，用于存储当前循环的值 list是一个值或者多个带有空格或换行符分隔的值组成的列表
  for var in list
  do
    command1
    command2
    ...
  done
  ```

  - while循环 - 满足条件继续

  ```shell
  # condition是循环条件，如果条件为真，则执行do语句块中的命令
  while condition
  do
    command1
    command2
    ...
  done
  ```

  - until循环 - 满足条件停止

  ```shell
  # condition是循环条件，如果条件为真，则跳出循环
  until condition
  do
    command1
    command2
    ...
  done
  ```

- **break和continue语句**

  - break 跳出当前循环
  - continue 跳过本次循环

#### 22.6 函数

- 函数定义

```shell
# 定义了一个函数，名为say_hello，执行结果是输出Hello, world!字符串
function say_hello() {
  echo "Hello, world!"
}
```

- 函数调用

```shell
say_hello
```

- 函数传参

```shell
# 定义了一个名为greet的函数，它输入参数$1和$2，并把这些参数用于输出字符串Hello, $1 $2
function greet() {
  echo "Hello, $1 $2"
}

greet "John" "Doe"
```

#### 22.7 输出重定向

- 标准输出 终端 → 文件

  - 语法：以将查找到的文件写入files.txt文件为例

  ```shell
  ls > files.txt  # 清空写入
  ls >> files.txt # 追加写入
  ```

- 错误输出 终端 → 文件

  - 语法：以当查找的文件不存在时，将错误信息写入error.txt文件为例

  ```shell
  ls a.txt 2> error.txt  # 清空写入
  ls a.txt 2>> error.txt # 追加写入
  ```

- 管道

  - 概念：将一个命令的输出传递给另一个命令
  - 语法：以查询带有file字符的文件或目录为例

  ```shell
  ls | grep file
  ```

  

#### 22.8 输入重定向

- 概念：文件(输入内容) → 内存

- 语法：以将文件内容排序为例

  ```shell
  sort < file.txt
  ```

  

## 23. Shell案例 - 数据库自动备份

- 以下是使用Shell编写的自动备份数据库的脚本

```shell
#!/bin/bash

# 设置备份目录
backupDir="/home/backup"

# 设置需要备份的数据库名称和用户名、密码
dbUser="root"
dbPass="123456"
dbName="database_name"

# 设置备份文件名，包括日期和时间
backupFile="$backupDir/${dbName}_$(date +%Y%m%d_%H%M%S).sql"

# 执行备份命令
mysqldump -u$dbUser -p$dbPass $dbName > $backupFile

# 压缩备份文件
gzip -f $backupFile

# 删除7天前的备份文件
find $backupDir -mtime +7 -type f -name "${dbName}*.sql.gz" -delete
```

将这个脚本保存到一个文件中，如backup_db.sh，并添加权限：

```shell
chmod +x backup_db.sh
```

通过编辑crontab文件，可以将这个脚本设置为定期自动运行

```shell
crontab -e
```

在文件末尾添加如下内容

```text
# 表示会在每天12:00执行一次脚本，时间后为脚本路径
0 12 * * * ~/backup_db.sh
```

- 脚本的实现流程如下：
  1.  首先设置备份目录和要备份的数据库的用户名、密码和名称。 
  2.  然后，设置备份文件名，这里使用了当前日期和时间来命名备份文件。 
  3.  接下来，使用mysqldump命令备份数据库并将结果重定向到备份文件中。 
  4.  备份完成后，使用gzip命令将备份文件压缩。 
  5.  最后，使用find命令删除7天前的备份文件，以避免占用过多磁盘空间。 

# 实战

## 1.使用Xshell登录虚拟机

- 打开Xshell软件新建会话

![新建Xshell会话](https://github.com/YOIOc/learning-record/blob/main/image/新建Xshell会话.png)

![新建Xshell会话2](https://github.com/YOIOc/learning-record/blob/main/image/新建Xshell会话2.png)

![新建Xshell会话3](https://github.com/YOIOc/learning-record/blob/main/image/新建Xshell会话3.png)

## 2.Linux的常用命令

- 网络
  - 检查网络连接：**`ping 网址`**
- 磁盘管理
  - 切换目录：**`cd 路径`** ( `/` 为回到根路径； `..` 为回到上一级)
  - 查看当前所在位置：**`pwd`**
  - 列出资源：**`ls`** ( `ll` 为列出资源的详细信息)

​	   关于绝对路径和相对路径：（绝对路径：路径以"/"开始；相对路径：路径以”资源名“开始，相对当前所在位置而言）

- 文件管理
  - 新建文件：**`touch 文件名`**
  - 新建目录：**`mkdir 目录名`**
  - 删除：**`rm [-r] 文件/目录名`**
- 其他
  - 清屏：**`clear`**
  - 查看文件内容：**`cat 文件路径`**
  - 查看IP地址：**`ifconfig`**

![查看ip地址](https://github.com/YOIOc/learning-record/blob/main/image/查看ip地址.png)

- **vim相关**
  - 打开文件：**`vim 文件路径`**
  - 进入编辑模式：**`i键`**
  - 退出编辑模式：**`esc键`**
  - 退出vim：**`:wq`**

## 3.Linux上部署Java程序

### 打包项目

- 使用Maven的clean命令将项目上次构建生成的文件清理删除
- 使用Maven的package命令将项目打包
- 打包的jar包在，项目的target目录下

### 配置Linux运行环境

- 安装/更新yum：**`yum install`** / **`yum update`** 

- 安装JDK8：**`yum -y install java-1.8.0-openjdk.x86_64`**

- 安装mysql8：

  - **安装**

    1. 更新系统软件包 `dnf update`（dnf命令是yum命令的升级版本，它在性能、依赖关系处理和可用插件等方面都更加优秀）
    2. 添加MySQL Yum存储库 `yum install https://dev.mysql.com/get/mysql80-community-release-el8-1.noarch.rpm`
    3. 导入mysql公钥 `rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2023` （随数据库版本升级会发生变化）
    4. 安装MySQL 8`yum install mysql-community-server`

  - **配置**

    1. 启动MySQL服务 `systemctl start mysqld`

    2. 设置MySQL开机自启 `systemctl enable mysqld`

    3. 查看root账户的临时密码 `grep 'temporary password' /var/log/mysqld.log`

       ![Linux上配置mysql](https://github.com/YOIOc/learning-record/blob/main/image/Linux上配置mysql.png)

    4. 配置MySQL安全设置 `mysql_secure_installation` 【y→y→y→n→y→y】

    5. 登录MySQL `mysql -u root -p密码`

  - **设置字符集**

    1. 查看MySQL字符集 `show variables like '%character%';`

    2. 如果字符集不是utf-8，可在/etc/my.cnf文件中的[mysqld]下面添加如下内容进行配置（/etc/my.cnf是mysql的配置文件）

       ```text
       [mysqld]
       character-set-server=utf8mb4
       collation-server=utf8mb4_general_ci
       ```

    3. 保存退出，并重启MySQL服务 `systemctl restart mysqld`

  - **创建MySQL用户**

    1. 首先，以root用户身份登录MySQL `mysql -u root -p密码`

    2. 创建数据库 `create database oa;`

    3. 创建用户并设置密码 

       ```sql
       # USER 用户名@主机名('%'表示任何主机 - 允许远程登陆)
       # IDENTIFIED BY 口令
       CREATE USER 'java_dev'@'%' IDENTIFIED BY 'java_DEV123';
       ```

    4. 为用户授权

       ```sql
       # 权限
       # ON 数据库()
       # TO 用户名@主机名
       GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, ALTER, INDEX, REFERENCES ON oa.* TO 'java_dev'@'%';
       ```

    5. 刷新权限 `FLUSH PRIVILEGES;`

    6. 关闭防火墙 `systemctl stop firewalld`

- 安装lrzsz：**`yum install lrzsz`** (一款便于在linux和windows之间上传下载文件的工具)

### 部署项目

- 鼠标选中打包好的jar包，将其拖动到连接了远程Linux的Xshell窗口中

![上传jar包到linux](https://github.com/YOIOc/learning-record/blob/main/image/上传jar包到linux.png)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  

**启动程序**

- 关闭虚拟机防火墙
  - `systemctl stop firewalld.service`

- 启动程序[后台]：**`nohup java -jar [包全名] > [输出路径] 2>&1 $`**

![Linux后台启动java程序](https://github.com/YOIOc/learning-record/blob/main/image/Linux后台启动java程序.png)

- 使用PostMan测试：`IP+端口+接口`

**关闭程序**

- 找与 Java 相关的进程
  - **`ps -aux | grep java`**

![查看java进程](https://github.com/YOIOc/learning-record/blob/main/image/查看java进程.png)

- 根据pid关闭指定进程
  - **`kill -9 [pid]`**

![根据指定pid关闭进程](https://github.com/YOIOc/learning-record/blob/main/image/根据指定pid关闭进程.png)

## 4.如何创建、查看日志

### 创建日志

- **为项目添加日志框架**

  首先添加相关依赖

  ```xml
  <!--lombok依赖(日志框架)-->
  <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
  </dependency>
  ```

  在需要记录日志的类上添加 **`@slf4j`** 标签

  ![lombok日志的标签使用](https://github.com/YOIOc/learning-record/blob/main/image/lombok日志的标签使用.png)

  在合适位置添加合适级别的日志输出

  ​               **项目上线运行后，通常只会记录error级别的日志，记录的日志太多，会存在长时间不清理日志，系统卡崩的风险**

  ![如何记录日志](https://github.com/YOIOc/learning-record/blob/main/image/如何记录日志.png)

  日志记录的信息最终会输出到指定的日志文件中

### 查看日志

- **热查看**（实时显示程序的日志输出）
  -  **`tail -f [日志文件名]`**

![热查看日志](https://github.com/YOIOc/learning-record/blob/main/image/热查看日志.png)

- **冷查看**（通常用于修改bug）
  - **`view [日志文件名]`**
- **搜索日志中的指定内容**
  - 冷查看日志文件
  - 使用 **`/[查找内容]`** 命令查找，点击 **`N`** 键光标移动到下一个匹配位置

![搜索内容](https://github.com/YOIOc/learning-record/blob/main/image/搜索内容.png)

​                                                                                                 以搜索 ‘port’ 为例

- **退出查看**：**`:q`**
