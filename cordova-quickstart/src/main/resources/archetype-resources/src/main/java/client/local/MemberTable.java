#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client.local;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import ${package}.client.shared.Member;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;

@Templated("Table.html${symbol_pound}table")
public class MemberTable extends Composite {

  @Inject
  Instance<MemberRow> rowInstance;

  @DataField("data")
  HTMLPanel rows = new HTMLPanel("");

  public void add(Member member) {
    createRow(member);
  }

  public void set(List<Member> members) {
    rows.clear();
    for (Member member : members) {
      createRow(member);
    }
  }

  private void createRow(Member member) {
    MemberRow memberRow = rowInstance.get();
    memberRow.set(member);
    rows.add(memberRow);
  }

}
