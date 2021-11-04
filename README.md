# BDD Test Doc Intellij Plugin

[Introducing BDD](https://dannorth.net/introducing-bdd/?source=:em:nw:mt::::RC_WWMK200429P00043C0038:NSL400181916)

Inspired by [AgileDox/TestDox](http://agiledox.sourceforge.net/).

BddTestDocPlugin creates simple documentation from the method names in JUnit test cases.

For Example, a test class (`FooTests.java`):

```java
public class FooTests {
    public void testIsASingleton() {
    }

    public void testAReallyLongNameIsAGoodThing() {
    }

    public void shouldTestSomethingUseful() {
    }
}
```

would generate the following :

```
# Tests Found:

##  FooTests
   * is a singleton
   * a really long name is a good thing
   * test something useful
```

To run ArcTestDox:

1. Download `bddtestdox-0.0.3-SNAPSHOT.jar`
2. Run `java -jar bddtestdox-0.0.3-SNAPSHOT.jar <test-directory> <output filename>`


