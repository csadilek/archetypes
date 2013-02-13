#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.local;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.WrappedPortable;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.api.InitialState;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import ${package}.client.shared.Member;
import ${package}.client.shared.MemberService;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * A custom ErraiUI templated component that allows creation of new Member instances.
 * <p>
 * Note on software architecture: the embedded event handler in this class
 * communicates directly with the server. Although this makes a small project
 * such as this one easier to understand at a glance, it is not a great approach
 * for long-term success. If you are planning to extend this example into a
 * large application, read up on the <a href=
 * "http://code.google.com/webtoolkit/doc/latest/DevGuideMvpActivitiesAndPlaces.html"
 * >MVP pattern</a>, which is recommended for larger apps.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@Templated("KitchenSinkApp.html${symbol_pound}memberForm")
public class MemberForm extends Composite {

  @Inject
  private Caller<MemberService> memberService;

  // This DataBinder is injected with @AutoBound, which means it will
  // be configured automatically to bind properties of Member to
  // @Bound @DataField widgets with the corresponding name.
  // For example, the value of Member.name is bound to the name TextBox.
  @Inject private @AutoBound DataBinder<Member> memberBinder;

  // The following widgets are all wired to DOM elements by ErraiUI
  // (because they have been annotated with @DataField)

  @Inject private @DataField Label generalErrorLabel;

  @Inject private @Bound @DataField TextBox name;
  @Inject private @DataField InlineLabel nameValidationErr;

  @Inject private @Bound @DataField TextBox email;
  @Inject private @DataField InlineLabel emailValidationErr;

  @Inject private @Bound @DataField TextBox phoneNumber;
  @Inject private @DataField InlineLabel phoneNumberValidationErr;

  @Inject private @DataField Button registerButton;
  @Inject private @DataField InlineLabel registerConfirmMessage;

  /**
   * This event handler method watches for the enter key being pressed, calling
   * the {@link ${symbol_pound}onRegisterButtonClick(ClickEvent)} method when that happens.
   * This means the form can be submitted by pressing enter.
   */
  @EventHandler
  private void onKeyUp(KeyUpEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      onRegisterButtonClick(null);
    }
  }

  /**
   * Validates the new member data and sends it to the server if validation
   * passes. Displays validation messages if validation fails.
   *
   * @param event The click event (ignored, can be null)
   */
  @EventHandler("registerButton")
  private void onRegisterButtonClick(ClickEvent event) {
    Member newMember = (Member) ((WrappedPortable) memberBinder.getModel()).unwrap();

    nameValidationErr.setText("");
    emailValidationErr.setText("");
    phoneNumberValidationErr.setText("");
    registerConfirmMessage.setText("");
    registerConfirmMessage.setStyleName("errorMessage");

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<Member>> violations = validator.validate(newMember);

    for (ConstraintViolation<Member> cv : violations) {
      String prop = cv.getPropertyPath().toString();
      if (prop.equals("name")) {
        nameValidationErr.setText(cv.getMessage());
      } else if (prop.equals("email")) {
        emailValidationErr.setText(cv.getMessage());
      } else if (prop.equals("phoneNumber")) {
        phoneNumberValidationErr.setText(cv.getMessage());
      } else {
        registerConfirmMessage.setText(cv.getMessage());
      }
    }

    if (!violations.isEmpty()) return;

    memberService.call(
            new RemoteCallback<Void>() {
              @Override
              public void callback(Void response) {
                registerConfirmMessage.setText("Registration Complete!");
                registerConfirmMessage.setStyleName("successMessage");

                // this will clear the form
                memberBinder.setModel(new Member(), InitialState.FROM_MODEL);
                name.setFocus(true);

                // the server will also broadcast a @New Member CDI event, which causes the table to update
                // so we don't have to do that here.
              }
            },
            new ErrorCallback() {
              @Override
              public boolean error(Message message, Throwable throwable) {
                registerConfirmMessage.setText("Member registration failed: " + throwable.getMessage());
                return false;
              }
            }).register(newMember);
  }

  /**
   * Sets the general error message that appears near the top of the form.
   */
  public void setGeneralErrorMessage(String string) {
    generalErrorLabel.setText(string);
  }

}
