#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.shared;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.gwt.regexp.shared.RegExp;

/**
 * Implements the same validation semantics as the Hibernate email validator,
 * but uses the GWT regular expressions API so the validation can succeed on the
 * client as well as the server.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class GwtCompatibleEmailValidator implements ConstraintValidator<Email, String> {

  private static String ATOM = "[a-z0-9!${symbol_pound}${symbol_dollar}%&'*+/=?^_`{|}~-]";
  private static String DOMAIN = "(" + ATOM + "+(${symbol_escape}${symbol_escape}." + ATOM + "+)*";
  private static String IP_DOMAIN = "${symbol_escape}${symbol_escape}[[0-9]{1,3}${symbol_escape}${symbol_escape}.[0-9]{1,3}${symbol_escape}${symbol_escape}.[0-9]{1,3}${symbol_escape}${symbol_escape}.[0-9]{1,3}${symbol_escape}${symbol_escape}]";

  private RegExp pattern = RegExp.compile(
      "^" + ATOM + "+(${symbol_escape}${symbol_escape}." + ATOM + "+)*@"
          + DOMAIN
          + "|"
          + IP_DOMAIN
          + ")${symbol_dollar}",
          "i"
      );

  @Override
  public void initialize(Email constraintAnnotation) {
    // no op
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if ( value == null || value.length() == 0 ) {
      return true;
    }
    return pattern.test( value );
  }

}
