package com.Maktab101.SpringProject.utils.sorter;


import com.Maktab101.SpringProject.model.User;
import com.Maktab101.SpringProject.utils.exceptions.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserSorter<T extends User> {
    List<T> sort(EntityManager entityManager, List<String> sortingFields);

    default List<Order> getOrders(List<String> sortingFields, Map<String, Order> orderMap) {

        List<Order> orderList = new ArrayList<>();

        if (orderMap.isEmpty()) {
            throw new CustomException("Order map is empty");
        }

        // Get orders based on fields
        for (String field : sortingFields) {
            Order order = orderMap.get(field);
            if (order == null) {
                throw new CustomException("Invalid sorting field: " + field);
            }
            orderList.add(order);
        }

        return orderList;
    }

    default Map<String, Order> createDefualtOrderMap(CriteriaBuilder cb, Root<T> root) {
        Map<String, Order> orderMap = new HashMap<>();

        orderMap.put("byFirstnameAsc", cb.asc(root.get("firstname")));
        orderMap.put("byLastnameAsc", cb.asc(root.get("lastname")));
        orderMap.put("byEmailAsc", cb.asc(root.get("email")));
        orderMap.put("byBalanceAsc", cb.asc(root.get("balance")));

        orderMap.put("byFirstnameDesc", cb.desc(root.get("firstname")));
        orderMap.put("byLastnameDesc", cb.desc(root.get("lastname")));
        orderMap.put("byEmailDesc", cb.desc(root.get("email")));
        orderMap.put("byBalanceDesc", cb.desc(root.get("balance")));

        return orderMap;
    }
}
