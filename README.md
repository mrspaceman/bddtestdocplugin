# BDD Test Doc Intellij Plugin

[Introducing BDD](https://dannorth.net/introducing-bdd/)

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

1. Install plugin from [Intellij Marketplace](https://plugins.jetbrains.com/plugin/17953-bdd-test-dox)
2. Select `Tools` -> `Generate Test Dox`

[comment]: <> (<iframe frameborder="none" width="384px" height="319px" src="https://plugins.jetbrains.com/embeddable/card/17953"></iframe>)

[comment]: <> (<br/>)

[comment]: <> (<iframe frameborder="none" width="245px" height="48px" src="https://plugins.jetbrains.com/embeddable/install/17953"></iframe>)

[comment]: <> (<br/>)

[comment]: <> (<script src="https://plugins.jetbrains.com/assets/scripts/mp-widget.js"></script>)

[comment]: <> (<script>)

[comment]: <> (  // Please, replace #yourelement with a real element id on your webpage)

[comment]: <> (  MarketplaceWidget.setupMarketplaceWidget&#40;'card', 17953, "#yourelement"&#41;;)

[comment]: <> (</script>)

