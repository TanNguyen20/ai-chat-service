package com.tannguyen.ai.spec;

import com.tannguyen.ai.model.third.base.FPTBase;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class FPTSpecifications {
    /**
     * Generic "contains ignore case" for any String field
     */
    public static <T extends FPTBase> Specification<T> containsIgnoreCase(String field, String value) {
        if (value == null || value.isBlank()) return null;
        String like = "%" + value.toLowerCase() + "%";
        return (root, q, cb) -> cb.like(cb.lower(root.get(field)), like);
    }

    /**
     * Apply list filter: WHERE field IN (list)
     */
    private static <T extends FPTBase> Specification<T> inIfPresent(String field, List<String> values) {
        if (values == null || values.isEmpty()) return null;
        return (root, q, cb) -> root.get(field).in(values);
    }

    /**
     * Main filter for facets
     */
    public static <T extends FPTBase> Specification<T> filter(
            List<String> odataType,
            List<String> displayName,
            List<String> givenName,
            List<String> surname,
            List<String> jobTitle,
            List<String> mail,
            List<String> mobilePhone,
            List<String> userPrincipalName,
            List<String> officeLocation,
            List<String> preferredLanguage
    ) {
        return Specification.<T>where(inIfPresent("odataType", odataType))
                .and(inIfPresent("displayName", displayName))
                .and(inIfPresent("givenName", givenName))
                .and(inIfPresent("surname", surname))
                .and(inIfPresent("jobTitle", jobTitle))
                .and(inIfPresent("mail", mail))
                .and(inIfPresent("mobilePhone", mobilePhone))
                .and(inIfPresent("userPrincipalName", userPrincipalName))
                .and(inIfPresent("officeLocation", officeLocation))
                .and(inIfPresent("preferredLanguage", preferredLanguage));
    }

    /**
     * Free-text search over multiple fields (same pattern as StudentSpecifications)
     */
    public static <T extends FPTBase> Specification<T> freeText(String query) {
        if (query == null || query.isBlank()) return null;
        final String[] tokens = query.trim().toLowerCase().split("\\s+");

        return (root, q, cb) -> {

            var odataType          = cb.lower(root.get("odataType"));
            var displayName        = cb.lower(root.get("displayName"));
            var givenName          = cb.lower(root.get("givenName"));
            var surname            = cb.lower(root.get("surname"));
            var jobTitle           = cb.lower(root.get("jobTitle"));
            var mail               = cb.lower(root.get("mail"));
            var mobilePhone        = cb.lower(root.get("mobilePhone"));
            var userPrincipalName  = cb.lower(root.get("userPrincipalName"));
            var officeLocation     = cb.lower(root.get("officeLocation"));
            var preferredLanguage  = cb.lower(root.get("preferredLanguage"));

            Predicate andAllTokens = cb.conjunction();

            for (String t : tokens) {
                String like = "%" + t + "%";

                Predicate tokenMatches = cb.or(
                        cb.like(odataType, like),
                        cb.like(displayName, like),
                        cb.like(givenName, like),
                        cb.like(surname, like),
                        cb.like(jobTitle, like),
                        cb.like(mail, like),
                        cb.like(mobilePhone, like),
                        cb.like(userPrincipalName, like),
                        cb.like(officeLocation, like),
                        cb.like(preferredLanguage, like)
                );

                andAllTokens = cb.and(andAllTokens, tokenMatches);
            }

            return andAllTokens;
        };
    }
}
