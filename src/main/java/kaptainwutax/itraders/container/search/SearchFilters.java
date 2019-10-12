package kaptainwutax.itraders.container.search;

import kaptainwutax.itraders.container.search.filter.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchFilters {

    public static final FilterLiteral LITERAL_FILTER = new FilterLiteral();
    private static final Map<String, SearchFilter> FILTERS = new HashMap<>();
    private static final Pattern SEARCH_QUERY_PATTERN = Pattern.compile("^@(?<filtername>.*?) .*$");

    static {
        addFilter(new FilterStartsWith());
        addFilter(new FilterEndsWith());
        addFilter(new FilterMonths());
        addFilter(new FilterMonthsMin());
        addFilter(new FilterMonthsMax());
    }

    private static void addFilter(SearchFilter filter) {
        FILTERS.put(filter.name, filter);
    }

    public static SearchFilter getFilterFor(String searchQuery) {
        Matcher matcher = SEARCH_QUERY_PATTERN.matcher(searchQuery);

        return matcher.matches()
                ? FILTERS.getOrDefault(matcher.group("filtername"), LITERAL_FILTER)
                : LITERAL_FILTER;
    }

}
