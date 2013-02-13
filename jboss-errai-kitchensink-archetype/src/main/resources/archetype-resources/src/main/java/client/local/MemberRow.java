#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.local;

import javax.inject.Inject;

import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.api.InitialState;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import ${package}.client.shared.Member;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

/**
 * A custom widget backed by an ErraiUI HTML5 template. Each instance of this
 * widget represents one Member object.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@Templated("KitchenSinkApp.html${symbol_pound}memberRow")
public class MemberRow extends Composite implements HasModel<Member> {

  // This DataBinder is injected with @AutoBound, which means it will
  // be configured automatically to bind properties of Member to
  // @Bound @DataField widgets with the corresponding name.
  // For example, the value of Member.name is bound to the name Label.
  @Inject @AutoBound DataBinder<Member> memberBinder;

  // These two bindings work well with Errai's default auto-bindings
  @Inject @Bound @DataField Label name;
  @Inject @Bound @DataField Label email;

  // this auto-binding shows what to do if the template data-field attribute
  // differs from the Java source code name (the template has a <div data-field="phone">)
  @Inject @Bound(property="phoneNumber") @DataField("phone") Label phoneNumber;

  // this data field can't be bound automatically for two reasons:
  // 1. there is no corresponding "restUrl" property on the Member class;
  // 2. We have to set the text AND the href on the Anchor widget
  // Instead, we just inject the Anchor element without @AutoBound and set it up manually in setModel().
  @Inject @DataField Anchor restUrl;

  /**
   * Returns the Member object that this row displays. The returned member is
   * <i>data bound</i> to this widget. That means changes to the returned Member
   * object (for example, {@code memberRow.getModel().setName("foo");}) will
   * cause an immediate corresponding update in the UI.
   */
  @Override
  public Member getModel() {
    return memberBinder.getModel();
  }

  /**
   * Binds the given Member to this widget. The magical data binding effects
   * described in {@link ${symbol_pound}getModel()} do not apply to the exact instance passed
   * in here. If you want to be able to update the Member object, first retrieve
   * the proxied instance by calling {@code getMember()}.
   * <p>
   * Example:
   * <pre>
   *   Member member = new Member();
   *   MemberRow row = IOC.getBeanManager().lookupBean(MemberRow.class).newInstance();
   *   row.setModel(member);
   *   member.setName("this update on the original raw member object will be missed");
   *   member = row.getModel();
   *   member.setName("this update will show up immediately in the UI");
   * </pre>
   */
  @Override
  public void setModel(Member model) {
    memberBinder.setModel(model, InitialState.FROM_MODEL);

    String href = "rest/members/" + model.getId();
    restUrl.setText(href);
    restUrl.setHref(href);
  }

}
