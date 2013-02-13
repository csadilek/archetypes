#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import ${package}.client.shared.Member;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * This is an ErraiUI custom widget that gets injected into the page by
 * {@link KitchenSinkApp} at page load time. The layout of this widget is
 * declared by the {@code <div data-field=tableBody>} in the companion file
 * KitchenSinkApp.html, which you will find in the same source directory.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@Templated("KitchenSinkApp.html${symbol_pound}membersTable")
public class MembersTable extends Composite {

  /**
   * The list of members. Add to this list via the
   * {@link ${symbol_pound}addMember(Member)} method to ensure the update is visible to
   * the user.
   */
  private final List<Member> members = new ArrayList<Member>();

  // The following fields are all injected by ErraiUI during instance construction

  @Inject private @DataField Label tableEmptyMessage;

  @Inject private @DataField ListWidget<Member, MemberRow> tableBody;

  /**
   * Adds the given member into the local Registered Members CellTable. Does not
   * communicate with the server.
   *
   * @param m
   *          The member to add to the CellTable being displayed in the web
   *          page.
   */
  public void addMember(Member m) {
    members.add(m);
    Collections.sort(members);
    tableBody.setItems(members);
    setTableStatusMessage("");
  }

  /**
   * Replaces the displayed list of members with the given list of members.
   *
   * @param members The list of members to display on the web page. Not null.
   */
  public void setMembers(List<Member> members) {
    this.members.clear();
    this.members.addAll(members);
    tableBody.setItems(this.members);
    if (members.isEmpty()) {
      setTableStatusMessage("No members registered yet.");
    } else {
      setTableStatusMessage("");
    }
  }

  /**
   * Sets the message that appears underneath the Registered Members table.
   */
  public void setTableStatusMessage(String message) {
    tableEmptyMessage.setText(message);
  }

}
