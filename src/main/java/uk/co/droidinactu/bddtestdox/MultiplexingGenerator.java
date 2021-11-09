package uk.co.droidinactu.bddtestdox;

import java.util.ArrayList;
import java.util.List;

/**
 * Output's the test results to multiple {@link DocumentGenerator}.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox
 * project.</a>
 */
public class MultiplexingGenerator implements DocumentGenerator {

  /** the list of DocumentGenerator that will be used to generate output documents. */
  private List<DocumentGenerator> generators = new ArrayList<>();

  /**
   * add a generator to the list.
   *
   * @param generator the DocumentGenerator to add
   */
  public void addGenerator(DocumentGenerator generator) {
    generators.add(generator);
  }

  /**
   * remove a generator of the given class from the list.
   *
   * @param generator the DocumentGenerator class to remove
   */
  public void removeGenerator(Class generator) {
    List<DocumentGenerator> newGenerators = new ArrayList<>();
    generators.forEach(
        gen -> {
          if (!gen.getClass().equals(generator)) {
            newGenerators.add(gen);
          }
        });
    generators = newGenerators;
  }

  /** {@inheritDoc} */
  public void startClass(String name) {
    generators.forEach(gen -> gen.startClass(name));
  }

  /** {@inheritDoc} */
  public void onTest(String name) {
    generators.forEach(gen -> gen.onTest(name));
  }

  /** {@inheritDoc} */
  public void endClass(String name) {
    generators.forEach(gen -> gen.endClass(name));
  }
}
