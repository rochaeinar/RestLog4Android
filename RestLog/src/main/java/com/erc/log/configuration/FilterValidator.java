package com.erc.log.configuration;

import com.erc.log.AppContext;
import com.erc.log.containers.LOG;
import com.erc.log.helpers.ReflectionHelper;

import java.util.ArrayList;

public class FilterValidator {
    public static boolean isValidFilter(LOG log) {
        ArrayList<Filter> filters = LogConfiguration.getInstance(AppContext.getContext()).getFilters();

        for (Filter filter : filters) {
            if (validateField(log, filter)) {
                return true;
            }
        }

        return filters.size() == 0;
    }

    private static boolean validateField(LOG log, Filter filter) {
        Object value = ReflectionHelper.getFieldValue(log, filter.getField());
        boolean valid = false;
        if (value != null) {
            if (value instanceof Integer) {
                valid = validateNumber((Long.parseLong(value.toString())), filter);
            } else if (value instanceof Long) {
                valid = validateNumber((long) value, filter);
            } else {
                valid = validateString(value.toString(), filter);
            }
        }
        return valid;

    }

    private static boolean validateString(String value, Filter filter) {

        switch (filter.getFilterOperator()) {
            case CONTAINS: {
                return value.toString().toLowerCase().indexOf(filter.getValue().toLowerCase()) >= 0;
            }
            case EQUALS: {
                return value.equals(filter.getValue());
            }
            case NOT_EQUAL_TO: {
                return !value.equals(filter.getValue());
            }
            default: {
                return true;
            }
        }
    }

    private static boolean validateNumber(long value, Filter filter) {

        switch (filter.getFilterOperator()) {
            case EQUALS: {
                return value == Long.parseLong(filter.getValue());
            }
            case NOT_EQUAL_TO: {
                return value != Long.parseLong(filter.getValue());
            }
            case GREATER_THAN: {
                return value > Long.parseLong(filter.getValue());
            }
            case LESS_THAN_OR_EQUAL_TO:
            case NOT_GREATER_THAN: {
                return value <= Long.parseLong(filter.getValue());
            }
            case LESS_THAN: {
                return value < Long.parseLong(filter.getValue());
            }
            case GREATER_THAN_OR_EQUAL_TO:
            case NOT_LESS_THAN: {
                return value >= Long.parseLong(filter.getValue());
            }
            default: {
                return true;
            }
        }
    }
}
