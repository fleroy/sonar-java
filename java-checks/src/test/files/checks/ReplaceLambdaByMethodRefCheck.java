import java.util.*;
import java.util.stream.Collectors;
class A {
  void fun() {
    IntStream.range(1, 5)
        .map((x) -> x * x)
        .map(x -> square(x)) // Noncompliant [[sc=16;ec=18]] {{Replace this lambda with a method reference.}}
        .map(x -> { // Noncompliant
          return square(x);
        })
        .map(this::square) //Compliant
        .forEach(System.out::println);
    IntStream.range(1, 5).forEach(x -> System.out.println(x)); // Noncompliant
    IntStream.range(1, 5).forEach(x -> { // Noncompliant
          System.out.println(x);
        });
    IntStream.range(1, 5).forEach(x -> {return;}); // Compliant

    Arrays.asList("bar").stream().filter(string -> string.startsWith("b")); // Compliant
    Arrays.asList(new A()).stream().filter(a -> a.coolerThan(0, a)); // Compliant
    foo((x, y) -> x * y);
    foo((x, y) -> { ; });
    foo((x, y) -> { ;; });
  }

  void foo(List<String> list, String a) {
    list.stream().map(String::toLowerCase).close();

    list.stream().map(s -> s.toLowerCase()).close(); // Noncompliant
    list.stream().map(s -> { return s.toLowerCase(); }).close(); // Noncompliant

    list.stream().map(s -> new String()).close(); // Compliant
    list.stream().map(s -> a.toLowerCase()).close(); // Compliant
    list.stream().map(s -> s.toLowerCase().toUpperCase()).close(); // Compliant
    list.stream().forEach(s -> fun()); // Compliant
    list.stream().reduce((x, y) -> x.toLowerCase()); // Compliant
  }

  int square(int x) {
    return x * x;
  }
  
  boolean coolerThan(int i, A a) {
    return true;
  }

  Collection<Number> values = transform(
    input -> getValueProvider().apply(input).getValue() //cannot be replaced by  a method reference.
  );

  Collection<Number> values2 = transform2((input, input2) -> getValueProvider().apply(input)); //cannot be replaced by  a method reference.

  A getValueProvider() {
    return null;
  }
  A getValue() {
    return null;
  }
  A apply(A a) {

  }

  Collection transform(F f) {return null;}
  Collection transform2(F2 f) {return null;}
  interface F2 {
    A apply(A a1, A a2);
  }
  interface F {
    A apply(A a1);
  }

  public interface Query extends Unwrappable {
    Collection<String> keys();
    default String get(String name) {
    }

    default Map<String, String> keyValues() {
      return keys().stream().collect(Collectors.toMap(key -> key, key -> get(key))); // Noncompliant
    }

    void process(String s1, String s2, int i){}
    void fun2(){
      IntStream.range(1, 5).forEach(i -> { process("foo", "bar", i); });
      foo((x, y) -> myMethod(x , y)); // Noncompliant
      foo((x, y) -> myMethod(y , x));
      foo((x, y) -> myMethod(y));
      foo((x,y) -> new ClassTree(x, y)); // Noncompliant
      foo((x,y) -> new ClassTree(y, x));
      foo((x,y) -> new ClassTree(x, y) {
        //can get some capture
      });
      foo(() -> myMethod()); // Noncompliant
    }
  }

  void nullChecks(List<String> strings, String s2) {
    strings.stream().filter(s -> s != null); // Noncompliant {{Replace this lambda with method reference 'Objects::nonNull'.}}
    strings.stream().filter(s -> { return s != null; }); // Noncompliant {{Replace this lambda with method reference 'Objects::nonNull'.}}
    strings.stream().filter(s -> (s) == null); // Noncompliant {{Replace this lambda with method reference 'Objects::isNull'.}}
    strings.stream().filter(s -> null == s); // Noncompliant {{Replace this lambda with method reference 'Objects::isNull'.}}
    strings.stream().filter(s -> (((s == null)))); // Noncompliant {{Replace this lambda with method reference 'Objects::isNull'.}}

    strings.stream().filter(Objects::nonNull); // Compliant
    strings.stream().filter(Objects::isNull); // Compliant

    strings.stream().filter(s -> (((s == s2)))); // Compliant
    strings.stream().filter(s -> (((s2 == s)))); // Compliant
    strings.stream().filter(s -> (((s2 == null)))); // Compliant
    strings.stream().filter(s -> (((null == null)))); // Compliant
  }
}
