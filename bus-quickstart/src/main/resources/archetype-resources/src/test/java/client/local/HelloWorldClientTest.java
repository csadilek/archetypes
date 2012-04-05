package ${package}.client.local;

import org.jboss.errai.ioc.client.Container;
import org.jboss.errai.bus.client.tests.AbstractErraiTest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

public class HelloWorldClientTest extends AbstractErraiTest {

  @Override
  public String getModuleName() {
    return "${package}.App";
  }

  @Override
  protected void gwtSetUp() throws Exception {
    super.gwtSetUp();
    
    // We need to bootstrap the IoC container manually because GWTTestCase
    // doesn't call onModuleLoad() for us.
    new Container().onModuleLoad();
  }
  
  public void testSendMessage() throws Exception {
    ErraiIocTestHelper.afterBusInitialized(new Runnable() {
      @Override
      public void run() {
        final HelloWorldClient client = ErraiIocTestHelper.instance.client;
        assertNotNull(client);
        
        // send a message using the bus (it is now initialized)
        client.sendMessage();
        System.out.println("Sent message");
        
        // wait a few seconds, then check that the server response caused a DOM update
        new Timer() {
          @Override
          public void run() {
            System.out.println("Checking for update");
            
            String labelText = client.getResponseLabel().getText();
            
            assertTrue("Unexpected label contents after pressing button: \"" + labelText + "\"",
                labelText.startsWith("Message from Server: Hello, World! The server's time is now"));
            finishTest();
          }
        }.schedule(2000);

      }
    });
    delayTestFinish(20000);
  }
}
