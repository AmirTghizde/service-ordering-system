package com.Maktab101.SpringProject.utils.sorter;

import com.Maktab101.SpringProject.model.SubServices;
import com.Maktab101.SpringProject.model.Technician;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TechnicianSorter implements UserSorter<Technician> {

    @Override
    public List<Technician> sort(EntityManager entityManager, List<String> sortingFields) {
        log.info("Sorting technicians by these values [{}]", sortingFields);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Technician> query = cb.createQuery(Technician.class);
        Root<Technician> root = query.from(Technician.class);
        Join<Technician, SubServices> subServicesJoin = root.join("subServices");

        // Add custom orders
        Map<String, Order> orderMap = createDefualtOrderMap(cb, root);
        orderMap.put("bySubServiceAsc", cb.asc(subServicesJoin.get("name")));
        orderMap.put("bySubServiceDesc", cb.desc(subServicesJoin.get("name")));

        // Retrieve the orders
        List<Order> orders = getOrders(sortingFields, orderMap);
        log.info("Order list contains {}",orders);
        query.orderBy(orders);

        List<Technician> resultList = entityManager.createQuery(query).getResultList();
        log.info("End result is {}",resultList);
        return resultList;
    }
}
