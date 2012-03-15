package ${package}.client.local;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.MessageCallback;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.framework.MessageBus;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

@EntryPoint
public class HelloWorldClient extends VerticalPanel {

  @Inject
  private MessageBus bus;

  private final Label responseLabel = new Label();
  private final Button button = new Button("Send");
  private final TextBox message = new TextBox();

  @PostConstruct
  public void buildUI() {

      button.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent event) {
              System.out.println("Handling click event!");
              sendMessage();
          }
      });

      HorizontalPanel horizontalPanel = new HorizontalPanel();
      horizontalPanel.add(message);
      horizontalPanel.add(button);
      horizontalPanel.add(responseLabel);

      RootPanel.get().add(horizontalPanel);
      
      System.out.println("UI Constructed!");
  }
  
  /**
   * Sends the text {@code "Hello, There!"} to HelloWorldService on the bus.
   * Displays the response, if any, in the responseLabel div.
   * <p>
   * This method is exposed as package-private so it can be called from tests.
   */
  void sendMessage() {
    MessageBuilder.createMessage()
        .toSubject("HelloWorldService")
        .withValue("Hello, There!")
        .done()
        .repliesTo(new MessageCallback() {
          public void callback(Message message) {
            System.out.println("Got a Response!");
            responseLabel.setText("Message from Server: " + message.get(String.class, MessageParts.Value));
          }
        })
        .sendNowWith(bus);
  }

  /**
   * Returns the GWT responseLabel object (a div in the DOM).
   * <p>
   * This is exposed so the test suite can access it and check for updates.
   */
  Label getResponseLabel() {
    return responseLabel;
  }
}