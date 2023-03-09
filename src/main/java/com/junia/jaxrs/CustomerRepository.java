package com.junia.jaxrs;


import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;


@ApplicationScoped
public class CustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Customer> findAll() {
        System.out.println("======================\nfindAll\n======================");
        return entityManager.createNamedQuery("Customers.findAll", Customer.class).getResultList();
    }

    public Customer findCustomerById(Long id) {
        Customer customer;
        try {
            customer = entityManager.find(Customer.class, id);
            if (customer == null) {
                throw new NotFoundException("Customer not found");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("An error occurred while processing the request");
        }
        return customer;
    }
    
    @Transactional
    public void updateCustomer(Customer customer) {
        Customer customerToUpdate = findCustomerById(customer.getId());
        customerToUpdate.setName(customer.getName());
        customerToUpdate.setSurname(customer.getSurname());
    }
    
    @Transactional
    public void createCustomer(Customer customer) {
        System.out.println("Nouveau customer");
        entityManager.persist(customer);
    }
    
    @Transactional
    public void deleteCustomer(Long customerId) {
        System.out.println("Suppression customer");
        Customer c = findCustomerById(customerId);
        entityManager.remove(c);
    }
}
