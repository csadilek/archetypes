#set($symbol_pound='#')
    #set($symbol_dollar='$')
    #set($symbol_escape='\' )
/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${package}.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import ${package}.client.shared.Customer;
import ${package}.client.shared.CustomerService;

/**
 * Simple mock based service implementation.
 * 
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class CustomerServiceImpl implements CustomerService {

  private static AtomicInteger id = new AtomicInteger(3);
  
  private static Map<Long, Customer> customers = new ConcurrentHashMap<Long, Customer>() {
    {
      put(1l, new Customer(1, "Christian", "Sadilek", "A1B2C3"));
      put(2l, new Customer(2, "Mike", "Brock", "A1B2C3"));
      put(3l, new Customer(3, "Jonathan", "Fuerth", "A1B2C3"));
    }
  };

  @Override
  public long createCustomer(Customer customer) {
    customer.setId(id.incrementAndGet());
    customers.put(customer.getId(), customer);
    return customer.getId();
  }

  @Override
  public Customer updateCustomer(long id, Customer customer) {
    customers.put(id, customer);
    customer.setLastChanged(new Date());
    return customer;
  }

  @Override
  public void deleteCustomer(long id) {
    customers.remove(id);
  }

  @Override
  public Customer retrieveCustomerById(long id) {
    return customers.get(id);
  }

  @Override
  public List<Customer> listAllCustomers() {
    List<Customer> customers = new ArrayList<Customer>(CustomerServiceImpl.customers.values());
    Collections.sort(customers);
    return customers;
  }
}