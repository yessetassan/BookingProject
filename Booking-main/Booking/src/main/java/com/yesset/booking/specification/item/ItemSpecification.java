package com.yesset.booking.specification.item;
import com.yesset.booking.dto.filter.AttributeFilterDto;
import com.yesset.booking.dto.item.ItemRequestDto;
import com.yesset.booking.entity.Item;
import com.yesset.booking.entity.Reason;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;
import java.util.*;


public class ItemSpecification {

    public static Specification<Item> filterItems(ItemRequestDto request, String status) {

        return (Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deleted"), Boolean.FALSE));
            switch (status) {
                case "available": {
                    predicates.add(cb.equal(root.get("status"), "ACTIVE"));
                } break;
                case "approved": {
                    predicates.add(cb.equal(root.get("approved"), true));
                } break;
                case "rejected": {
                    predicates.add(cb.equal(root.get("rejected"), true));
                } break;
                case "checkIn": {
                    predicates.add(cb.equal(root.get("approved"), false));
                    predicates.add(cb.equal(root.get("rejected"), false));
                }break;
                case "conflicted": {
                    query.distinct(true);

                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<Reason> reasonRoot = subquery.from(Reason.class);

                    subquery.select(cb.literal(1L));
                    subquery.where(
                            cb.equal(reasonRoot.get("item").get("id"), root.get("id")),
                            cb.isTrue(reasonRoot.get("isActive"))
                    );

                    predicates.add(cb.exists(subquery));
                } break;
            }

            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }

            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), request.getCategoryId()));
            }

            if (request.getOwnerId() != null) {
                predicates.add(cb.equal(root.get("ownerId"), request.getOwnerId()));
            }

            if (request.getFilterAttributes() != null && !request.getFilterAttributes().isEmpty()) {
                for (AttributeFilterDto filter : request.getFilterAttributes()) {
                    Map<String, Object> parsedOptions = filter.getParsedOptions();
                    String attributeName = filter.getName();

                    if ("multi-select".equals(filter.getType())) {
                        List<String> values = (List<String>) parsedOptions.get("values");

                        List<Predicate> valuePredicates = new ArrayList<>();
                        for (String value : values) {
                            valuePredicates.add(
                                    cb.like(
                                            cb.lower(
                                                    cb.function("jsonb_extract_path_text", String.class, root.get("attributes"), cb.literal(attributeName))
                                            ),
                                            "%" + value.toLowerCase() + "%"
                                    )
                            );
                        }

                        predicates.add(cb.or(valuePredicates.toArray(new Predicate[0])));
                    }

                    if ("range".equals(filter.getType())) {
                        Integer min = ((Double) parsedOptions.get("min")).intValue();
                        Integer max = ((Double) parsedOptions.get("max")).intValue();

                        predicates.add(
                                cb.between(
                                        cb.function("jsonb_extract_path_text", Integer.class, root.get("attributes"), cb.literal(attributeName)).as(Integer.class),
                                        min, max
                                )
                        );
                    }
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<Item> recommendedFirst(List<Long> recommendedIds) {
        return (root, query, cb) -> {
            if (Item.class.equals(query.getResultType()) && recommendedIds != null && !recommendedIds.isEmpty()) {
                Expression<Object> orderExpr = cb.selectCase()
                        .when(root.get("id").in(recommendedIds), 0)
                        .otherwise(1);
                query.orderBy(cb.asc(orderExpr));
            }
            return cb.conjunction();
        };
    }

}
