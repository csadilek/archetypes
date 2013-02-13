#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.local;

import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import ${package}.client.shared.Member;
import ${package}.client.shared.MemberService;
import ${package}.client.shared.New;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point for the Errai Kitchen Sink application. The {@code @EntryPoint}
 * annotation tells the Errai framework that this class should be
 * instantiated inside the web browser when the web page is first loaded.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@Templated @EntryPoint
public class KitchenSinkApp extends Composite {

  /**
   * This is the client-side proxy to the Errai service implemented by
   * MemberServiceImpl. The proxy is generated at build time, and injected into
   * this field when the page loads. You can create additional Errai services by
   * following this same pattern; just be sure that the client-side class you
   * inject the Caller into is created by the client-side bean manager and not by
   * {@code new MyType()}.
   */
  @Inject private Caller<MemberService> memberService;

  /**
   * A nested custom templated widget. Because this field is annotated with
   * {@code @Inject} and {@code @DataField}, ErraiUI will create a new instance
   * of MemberForm and replace the {@code <div data-field=memberForm>} element
   * with that new MemberForm widget's UI.
   */
  @Inject private @DataField MemberForm memberForm;

  /**
   * A nested custom templated widget. Because this field is annotated with
   * {@code @Inject} and {@code @DataField}, ErraiUI will create a new instance
   * of MembersTable and replace the {@code <div data-field=membersTable>} element
   * with that new MembersTable widget's UI.
   */
  @Inject private @DataField MembersTable membersTable;

  /**
   * Builds the UI and populates the member list by making an RPC call to the server.
   * <p>
   * Note that because this method performs an RPC call to the server, it is annotated
   * with AfterInitialization rather than PostConstruct: the contract of PostConstruct
   * only guarantees that all of <em>this</em> bean's dependencies have been injected,
   * but it does not guarantee that the entire runtime environment has completed its
   * bootstrapping routine. Methods annotated with the Errai-specific AfterInitialization
   * are only called once everything is up and running, including the communication
   * channel to the server.
   */
  @AfterInitialization
  public void createUI() {
    RootPanel.get("kitchensink").add(this);
    membersTable.setTableStatusMessage("Fetching member list...");
    fetchMemberList();
  }

  /**
   * Responds to the CDI event that's fired (from the server) every time a new
   * member is added to the database. The new member could have been added by
   * any user, including ourselves.
   *
   * @param newMember
   *          The member that was just added to the database on the server.
   */
  public void onMemberAdded(@Observes @New Member newMember) {
    membersTable.addMember(newMember);
  }

  /**
   * Fetches the member list from the server, adding each member to the table in the UI.
   */
  private void fetchMemberList() {

    // note that GWT.log messages only show up in development mode.
    // They have no effect (and no cost) in production mode.
    GWT.log("Requesting member list...");

    memberService.call(new RemoteCallback<List<Member>>() {
      @Override
      public void callback(List<Member> response) {
        GWT.log("Got member list. Size: " + response.size());
        membersTable.setMembers(response);
      }
    },
    new ErrorCallback() {
      @Override
      public boolean error(Message message, Throwable throwable) {
        throwable.printStackTrace();
        memberForm.setGeneralErrorMessage("Failed to retrieve list of members: " + throwable.getMessage());
        return false;
      }
    }).retrieveAllMembersOrderedByName();
  }

}
