package com.Maktab101.SpringProject.utils.sortFilterable;

import com.Maktab101.SpringProject.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CustomerSortFilterable implements UserSortFilterable<Customer> {
    @Override
    public List<Customer> sort(EntityManager entityManager, List<String> sortingFields) {
        log.info("Sorting Customers by these values [{}]", sortingFields);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);

        // Get the default orderMap
        Map<String, Order> orderMap = createDefualtOrderMap(cb, root);

        // Retrieve the orders
        List<Order> orders = getOrders(sortingFields, orderMap);
        query.orderBy(orders);

        return entityManager.createQuery(query).getResultList();
    }


}
