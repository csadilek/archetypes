#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.local;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ${package}.client.shared.Member;
import ${package}.client.shared.MemberService;
import ${package}.client.shared.New;
import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.cdi.api.CDI;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point for the Errai Kitchen Sink application. The {@code @EntryPoint}
 * annotation indicates to the Errai framework that this class should be
 * instantiated inside the web browser when the web page is first loaded.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@EntryPoint
public class KitchenSinkApp {

  /**
   * This is the client-side proxy to the Errai service implemented by
   * MemberServiceImpl. The proxy is generated at build time, and injected into
   * this field when the page loads. You can create additional Errai services by
   * following this same pattern; just be sure that the client-side class you
   * inject the Caller into is an injectable class (client-side injectable
   * classes are annotated with {@code @EntryPoint}, {@code @ApplicationScoped},
   * or {@code @Singleton}).
   */
  @Inject
  private Caller<MemberService> memberService;

  private KitchenSinkClient kitchenSinkUi;

  @PostConstruct
  public void createUI() {
    kitchenSinkUi = new KitchenSinkClient(memberService);
    kitchenSinkUi.setTableStatusMessage("Fetching member list...");

    RootPanel.get("kitchensink").add(kitchenSinkUi);

    // Can't call RPC methods yet, because CDI may not have found all the remote services yet.
    // The CDI post init tasks run after the server-side services have been dicovered.
    CDI.addPostInitTask(new Runnable() {
      @Override
      public void run() {
        fetchMemberList();
      }
    });
  }

  /**
   * Responds to the CDI event that's fired every time a new member is added to
   * the database.
   *
   * @param newMember The member that was just added to the database.
   */
  public void onMemberAdded(@Observes @New Member newMember) {
    kitchenSinkUi.addDisplayedMember(newMember);
  }

  /**
   * Fetches the member list from the server, adding each member to the table in the UI.
   */
  private void fetchMemberList() {

    // note that GWT.log messages only show up in development mode. They have no effect in production mode.
    GWT.log("Requesting member list...");

    memberService.call(new RemoteCallback<List<Member>>() {
      @Override
      public void callback(List<Member> response) {
        GWT.log("Got member list. Size: " + response.size());
        kitchenSinkUi.setDisplayedMembers(response);
      }
    },
    new ErrorCallback() {
      @Override
      public boolean error(Message message, Throwable throwable) {
        throwable.printStackTrace();
        kitchenSinkUi.setGeneralErrorMessage("Failed to retrieve list of members: " + throwable.getMessage());
        return false;
      }
    }).retrieveAllMembersOrderedByName();
  }

}
