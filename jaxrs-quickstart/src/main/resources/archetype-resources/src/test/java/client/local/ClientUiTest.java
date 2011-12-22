package ${package}.client.local;

import org.jboss.errai.enterprise.client.jaxrs.test.AbstractErraiJaxrsTest;
import static org.junit.Assert.*;
import org.junit.Test;

import com.google.gwt.user.client.Timer;

public class ClientUiTest extends AbstractErraiJaxrsTest {

  @Override
  public String getModuleName() {
    return "${package}.App";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    setJaxRsApplicationRoot("/");
  }

  public native void setJaxRsApplicationRoot(String path) /*-{
    $wnd.erraiJaxRsApplicationRoot = path;
  }-*/;

  @Test
  public void testPopulateCustomersTable() {
    ErraiIocTestHelper.afterIocInitialized(new Runnable() {
      @Override
      public void run() {
        final App client = ErraiIocTestHelper.instance.client;
        client.populateCustomersTable();

        new Timer() {
          @Override
          public void run() {
            if (client.rows.isEmpty()) {
              System.out.println("No data in the UI yet. Will check again later...");
              schedule(500);
            }

            // Assertions go here!
            assertEquals(3, client.rows.size());
            finishTest();
          }
        }.schedule(500);
      }
    });
    delayTestFinish(20000);
  }
}
