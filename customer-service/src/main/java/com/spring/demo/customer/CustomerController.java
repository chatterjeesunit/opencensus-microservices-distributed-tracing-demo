package com.spring.demo.customer;

import com.spring.demo.common.ErrorMessage;
import com.spring.demo.customer.domain.Customer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer")
@Log4j2
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomerDetail(@PathVariable(name = "id") String customerId,
                                               @RequestParam(value = "fetchByGuid", required = false, defaultValue = "true") boolean fetchByGuid) {
        try {
            if(fetchByGuid) {
                Customer customer = customerService.getCustomerByGuid(UUID.fromString(customerId))
                        .orElseThrow(()->new RuntimeException("Customer does not exists"));
                return ResponseEntity.ok(customer);
            }else {
                Long customerIdLong = Long.valueOf(customerId);
                Customer customer = customerService.getCustomerById(customerIdLong)
                        .orElseThrow(()->new RuntimeException("Customer does not exists"));
                return ResponseEntity.ok(customer);
            }
        }catch(Exception ex) {
            return handleException(ex);
        }
    }


    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCustomers(
            @RequestParam("pageNum") String pageNumber,
            @RequestParam("pageSize") String pageSize,
            @RequestHeader Map<String, String> headers) {
        try {
            log.info("Request Headers : {}", headers);
            Integer pageNumberLong = Integer.valueOf(pageNumber);
            Integer pageSizeLong = Integer.valueOf(pageSize);
            //Create a new paginated search request.
            PageRequest pageRequest = PageRequest.of(pageNumberLong, pageSizeLong);
            List<Customer> customers = customerService.findAll(pageRequest);
            return ResponseEntity.ok(customers);
        }catch(Exception ex) {
            return handleException(ex);
        }
    }

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.create(customer);
            return ResponseEntity.created(new URI("/customer/" + createdCustomer.getId())).body(customer);
        }catch(Exception ex) {
            return handleException(ex);
        }
    }


    @PutMapping (path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCustomer(@PathVariable(name = "id") String customerId, @Valid @RequestBody Customer customer) {
        try {
            customer.setId(Long.valueOf(customerId));
            Customer updatedCustomer = customerService.update(customer);
            return ResponseEntity.ok(updatedCustomer);
        }catch(Exception ex) {
            return handleException(ex);
        }
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(name = "id") String customerId) {
        try {
            customerService.deleteCustomer(Long.valueOf(customerId));
            return ResponseEntity.noContent().build();
        }catch(Exception ex) {
            return handleException(ex);
        }
    }

    private ResponseEntity<ErrorMessage> handleException(Exception ex) {
        ex.printStackTrace();
        ErrorMessage error = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
