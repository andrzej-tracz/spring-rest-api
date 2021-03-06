package agency.http.controllers;


import agency.repositories.CustomerRepository;
import agency.repositories.PoliciesRepository;
import agency.repositories.ProductRepository;
import agency.services.policies.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PoliciesRepository policiesRepository;

    @Autowired
    private PolicyService policiesService;

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> indexAction() {

        Map<String, Object> response = new HashMap<>();

        response.put("customers", this.customerRepository.getActiveCustomerCount());
        response.put("products", this.productRepository.getActiveProductsCount());
        response.put("policies", this.policiesRepository.getActivePoliciesCount());

        Object history = this.policiesService.getRecentPoliciesHistory();
        response.put("history", history);

        return response;
    }
}
