### Java空指针异常与防护措施



空指针异常算是平时最常见到的异常，也是最不容易处理的异常，大多数情况下，为了防止空指针异常，程序员不得不在代码里面编写大量的非空判断。

Java中引入Null是一个非常让人不舒服的事情，因为Null在很多场景下没有明确的定义，最典型的例子比如在某个map中通过get(key)来获取某个对象，返回值为null的情况。这里有两种情况，第一种情况就是通过key获取到的对象就是null，或者本身在map中不存在这样的key导致获取的对象为null。所以这里还需要程序员去进一步判断到底是因为什么导致返回值为null！


为了避免这种情况发生，Java8 引入了一个新的类，叫做Optional，用于避免空指针的出现。 Optinal类的本质其实是一个包装的容器，用它来包裹着可能会出现空指针的类。 

简单的来说，Optional允许使用一个不确定是否为null的对象，同时在对象为null的时候才去一系列错误去避免程序去抛出空指针异常！

* 使用Optional可以简化大量的

  ```java
  if(obj==null){
      ...
  }
  ```

  类似上述的判空代码

* 降低函数的复杂度，增加可读性追外，它还是一种非常傻瓜式的防护措施。因为它会强制对使用它的人考虑对象如果为null的情况下应该采取的措施。 



但是不假思索直接就使用Optional，就是简单的觉得OPtional的出现就是Java官方优雅的解决NullPointException的问题就不对了。



1. ##### 创建 Optinal 实例

* Optional.of(Object object), object不能为空，否则在创建的时候会抛出空指针异常

  ```java
  Optional<Integer> ofOptional=Optional.of(4);
  // Exception in thread "main" java.lang.NullPointerException
  Optional<Integer> ofOptional=Optional.of(null);
  ```

* Optional.ofNullable(Object object)，object可以为空，如果不为空，就创建一个对应类型的Optinal实例，如果object为空的话，就创建一个值为null的Optional实例

  ```java
  Optional<Integer> nullableOptional=Optional.ofNullable(null);
  Optional<Integer> nullableOptional=Optional.ofNullable(9);
  ```

* Optional.empty()，创建一个空的Optional实例，对于当前创建的Optional不代表任何值！

  ```java
  Optional<Integer> emptyOptional=Optional.empty(); 
  ```

2. ##### 基础用法

   当我们使用Optional的时候我们必须要先对它进行判断，是否当前Optional包含一个非Null的引用，如果存在返回true，如果不存在就返回false。如果存在的话我们必须要用get()方法获取对应的非空对象。 如果Optional包裹的对象为null的话，那么Optional的get()方法将抛出一个空指针异常。

   ```java
   Optional<Integer> nullableOptional=Optional.ofNullable(null);
   if (nullableOptional.isPresent()) {
       //optional包含非null实例，可以用get获取对应的对象
       Integer value = nullableOptional.get();
       //做具体的业务逻辑
   }else {
       //如果Optional包含一个null对象，get会抛出一个空指针异常
       //所以当isPresent为false时候，不要在使用get方法
       nullableOptional.get();
   }
   ```

    我们可以发现对于Optional的使用，我们首先必须要对Optional调用isPresent()方法来判断当前Optional是否包含非null的对象，那对于传统的空指针处理办法

   ```java
   Object obj=.....;
   if (obj!=null) {
       //做具体的业务逻辑
   }else {
      // null的处理
   }
   ```

   如果我们只会用isPresent()方法去判断，然后get方法去获取实际对象，这跟我们传统的判空处理是没有任何的区别的，所以使用的姿势是非常正确的。本人主要也是谈一谈使用Optional的正确姿势。

   当我们已经有一个Optional实例的适合，我们应该避免使用下列的代码:

   ```java
   if(optional.isPresent()){
       //something
   }else{
       //something
   }
   ```


* 如果Optional包含一个非null的实例，直接获取当前实例，如果不存在就手动提供一个默认值

  ```java
  Optional<Integer> nullableOptional=Optional.ofNullable(null);
  Integer integer = nullableOptional.orElse(3);
  ```

* 如果Optional包含一个非null的实例就直接返回当前实例，不存在就由一个指定的函数来生成返回的对象

  ```java
  Optional<Integer> nullableOptional=Optional.ofNullable(null);
  Integer integer = nullableOptional.orElseGet(()-> 3);
  ```

* 如果当前Optional包含一个非null的实例的话就做一些事情，请避免使用if else语句来判断

  ```java
  Optional<Integer> nullableOptional=Optional.ofNullable(null);
  nullableOptional.ifPresent(System.out::println);
  ```

* 如果当前Optional包含一个非null的实例就直接返回当前实例，不存在就抛出一个指定的异常！

  ```java
  Optional<Integer> nullableOptional=Optional.ofNullable(null);
  nullableOptional.orElseThrow(()->new NullPointerException("hello world!"));
  ```


3. 高级用法

* filter在Optional中的用法主要是对当前Optional包裹的对象进行过滤。当创建的Optional对象的值满足filter中的条件的时候，则返回包含该值的OPtional的对象，否则返回一个空的Optional对象。

  ```java
  Optional<Integer> optional=Optional.ofNullable(10);
  //如果满足条件就返回一个Optional<10>，如果不满足就返回一个empty optional
  Optional<Integer> optional = optional.filter(o -> o > 20);
  
  //与orElse，orElseGet，orElseThrow连用
  Optional<Integer> optional1 = Optional.of(optional.filter(o -> o > 4).orElse(5));
  ```


* map

  https://blog.csdn.net/hao134838/article/details/83346555

  https://blog.csdn.net/zknxx/article/details/78586799

  https://blog.csdn.net/shewmi/article/details/79797555

  https://blog.csdn.net/boling_cavalry/article/details/77610629