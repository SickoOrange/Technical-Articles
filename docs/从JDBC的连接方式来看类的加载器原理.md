### 从mysql如何将自己注册进jdbc来了解java中的类的加载器原理

用过Mysql的同学都知道，在使用传统的jdbc来连接mysql数据库有一段非常经典的代码：

```java
Class.forName(“com.mysql.jdbc.Driver”);
String url = “jdbc:mysql://localhost:1234/test?user=root&password=111111″;
Connection con = DriverManager.getConnection(url);
Statement statement = con.createStatement();
```

在这里主要使用了Class.forName()来加载了mysql 中的驱动类Driver。但是为什么我们只需要仅仅去加载这个Driver类，甚至都不用初始化Driver就可以获取对应的Connection呢？其实道理很简单，秘密也同样隐藏在Driver这个驱动类中。因为在Driver驱动类中有如下一段静态代码：

```java
    static {
        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
```



也就是说在我们调用Class.forName()的时候，回去加载com.mysql.jdbc.Driver这个类，然后就会执行static静态代码块，这个时候会调用jdbc中的DriverManager去讲自己注册到jdbc的驱动管理器中。所以我们只需要加载这个类便可以完成mysql的驱动注册。

#### 1. 类的 Class 对象

首先要了解类的加载器跟类的加载机制，我们需要了解java中Class类的相关知识！

通俗来说java在运行的时候虚拟机会对所有的对象进行标识，这个信息记录了每一个对象所属于的类的信息！保存这些类型的信息就是Class类。当虚拟机加载类的时候，Class类型的想象就会自动的创建。

Class这个类是没有public的构造方法的。因为Class这个对象是在加载类的时候由java虚拟机通过调用类加载器ClassLoader中的方法来构造的。因此我们是没有办法自己去创建一个Class对象！

获取Class对象一般由三种方法：

1. 直接通过Object类的getClass()就可以获取到当前类的Class对象，这是最简单的获取方法

2. 使用Class类的forName()方法。这个是一个静态的方法，接受一个Stirng类型的参数代表类的名字，该方法还有有一个重载的方法如下：

   ```java
   forName(String name, boolean initialize, ClassLoader loader)
   ```

   我们使用forName(String className)的时候，该方法内部实际调用的是：

   ```java
   Class.forName(className,true, this.getClass().getClassLoader())
   ```

   true表示初始化该类，loader表示使用当前类的加载器来初始化！

3. 第三种方法是最简单，直接在类的后面用.class来表示当前类的Class对象！



#### 2. Java虚拟机中的ClassLoader

ClassLoader简称类的加载器，以下简称类加载器！ 从名字上来看，类的加载器就是用来加载Class文件的。它最大的用处就是将Class的字节码形式转换为内存中的Class对象(也就是上文所指的Class对象)。所谓的class的字节码可是是经过编译生成的 .class文件，也可以是我们通常使用的jar中的那些.class文件，甚至是一段字节流，因为这里所谓的字节码文件或者.class文件从本质上来说就是一段byte类型的数组而已！只不过这个byte类型的数组被特殊的格式所约束以便能够让虚拟机识别这个class文件! 

所谓的字节码跟Class对象，还有类的加载器之间的关系如下：

> byte[]类型的字节码 ———————————>  Class对象
>
> ​					      （类的加载器）



```java
public final class Class<T>{
    ...
/*
     * Private constructor. Only the Java Virtual Machine creates Class objects.
     * This constructor is not used and prevents the default constructor being
     * generated.
     */
    private Class(ClassLoader loader) {
        // Initialize final field for classLoader.  The initialization value of non-null
        // prevents future JIT optimizations from assuming this final field is null.
        classLoader = loader;
    }
    ...
}

```

由Class类的构造器这段代码我们可以看出来：

每一个Class对象的内部都自己维护了一个classLoader对象，这个classLoader表示了当前这个Class对象是由哪个类构造器所加载的！

我们再来看看ClassLoader的部分源码：

```java
public abstract class ClassLoader {
    ...
// The classes loaded by this class loader. The only purpose of this table
    // is to keep the classes from being GC'ed until the loader is GC'ed.
    private final Vector<Class<?>> classes = new Vector<>();
    ...
}
```

ClassLoader就像是一个容器，里面装载了很多经过这个ClassLoader生成的Class对象！

通常一个java程序在启动的时候会有很多很多的类需要加载，但是JVM不会一次性把所有需要的类都加载一边，而是在程序运行的时候按照实际的需要来加载，这就是所谓的延迟加载策略，这种策略在Spring框架跟很多数据库持久层框架如Hibernate中都有体现！这个时候在实际运行中需要加载那个类，JVM就会嗲用对应的ClassLoadder来加载这些类！，加载之后这类被加载的类的Class对象就会保存在classes这个集合中。

在这里需要注意以下类中被static修饰的字段，因为被static修饰的字段已经跟类的实例对象没有关系了，它是行为是跟持有它的类相关的！我们在调用某一个类的静态方法的时候，首先是确保这个类已经被加载过，但是这个加载的过程不会跟类中的普通字段有关(没有被static修饰的)！



#### 3. 虚拟机中ClassLoader的分类

在虚拟中主要有三种不同类型的ClassLoader：

> 1. BootstrapClassLoader
> 2. ExtensionClassLoader
> 3. AppClassLoader

他们之间主要的区别就是加载字节码的来源不一样！通俗来说不同ClassLoader可以从不同的目录中去加载class文件，甚至可以从网络上下载字节码文件进行加载！

* BootstrapClassLoader

主要是加载JVM在运行时候所需要的一些核心类，核心代码是由C++写的，  是最顶层的类装载器,用来加载jre/lib/rt.jar下的类

* ExtensionClassLoader

主要是加载JVM的拓展类，常见的如Swing，AWT等位于javax包中，用来加载jre/lib/ext/*.jar的所有类

* AppClassLoader

这个加载器跟我们程序员的关系最为紧密，它主要加载ClassPath环境变量路径中的所有的jar包跟所有目录的class文件！ClassLoader这个类提供了一个静态方法getStystemClassLoader()，这个方法就可以获得一个AppClassLoader，我们平时写的代码其实就是由它加载的。

* URLClassLoader跟自定义类加载器

jdk其实还有一个加载器叫做URLClassLoader，顾名思义这个加载器主要需要给定一个URL，然后从这个URL中获取指定的class字节码文件来加载Class对象，这个URL可以是一个网络class字节码资源，同样也可以是一个本地路径中的class字节码资源。

所谓的自定义加载器就是继承 java.lang.ClassLoader类实现里面的方法,自定义加载类的方法



#### 4. ClassLoader的双亲委派机制

在上面我们已经说过常用的JVM中的ClassLoader通常由三种，遇到一个还没有被加载的类的时候，JVM如何去判断选择使用哪一个ClassLoader来加载当前需要加载的未知的类呢？ 在经过查阅资料以后，我发现JVM的加载策略就是这个未知的类是被谁调用的，那么就使用调用者的Class对象的ClassLoader来加载这个类。因为每一个Class对象内部都维护了一个classLoader，指明这个Class对象是被谁加载的，这样一来 JVM虚拟机就可以确定到底该使用哪一个ClassLoader来加载了。



所谓的双亲委派机制就是当AppClassLoader需要去加载一个类的时候，首先它本身比较懒不会立马就去加载这个类，而是将这个加载的请求转发给他的父类加载器ExtensionClassLoader，而这个ExtensionClassLoader也是比较懒，它也不会自己去加载，而是将请求委派给父类加载器BootstrapClassLoader去完成。如果BootstrapClassLoader就会让ExtensionClassLoader去加载，如果ExtensionClassLoader也加载失败了，就会让AppClassLoader去加载，如果连AppClassLoader都加载不了的话，就会去让自定义类加载器去加载，最后都不能加载成功，就只能抛出一个异常，这个异常及时我们熟悉的ClassNotFoundException异常，这就是所谓的双亲委派机制！

这三个 ClassLoader 之间形成了级联的父子关系，每个 ClassLoader 都很懒，尽量把工作交给父亲做，父亲干不了了自己才会干。每个 ClassLoader 对象内部都会有一个 parent 属性指向它的父加载器。

```java
class ClassLoader {
  ...
  private final ClassLoader parent;
  ...
}
```



当 parent 字段是 null 时就表示它的父加载器是「根加载器」。如果某个 Class 对象的 classLoader 属性值是 null，那么就表示这个类也是「根加载器」加载的

当ClassLoader的parent为null 时，ClassLoader的parent就是bootstrap classloader，所以在ClassLoader的最顶层就是bootstrap classloader，因此最终委托到bootstrap classloader的时候，bootstrap classloader就会返的对应的Class。