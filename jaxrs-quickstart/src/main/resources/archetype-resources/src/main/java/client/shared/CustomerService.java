package ${package}.client.shared;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * JAX-RS service interface
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
@Path("customers")
public interface CustomerService {
  @GET
  @Produces("application/json")
  public List<Customer> listAllCustomers();

  @POST
  @Consumes("application/json")
  @Produces("text/plain")
  public long createCustomer(Customer customer);

  @PUT
  @Path("/{id}")
  @Consumes("application/json")
  @Produces("application/json")
  public Customer updateCustomer(@PathParam("id") long id, Customer customer);

  @DELETE
  @Path("/{id}")
  public void deleteCustomer(@PathParam("id") long id);

  @GET
  @Path("/{id}")
  @Produces("application/json")
  public Customer retrieveCustomerById(@PathParam("id") long id);
}
