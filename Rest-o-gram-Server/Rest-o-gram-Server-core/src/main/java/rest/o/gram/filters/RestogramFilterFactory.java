package rest.o.gram.filters;

/**
 * Created with IntelliJ IDEA.
 * User: Itay
 * Date: 21/04/13
 */
public class RestogramFilterFactory {

    public static RestogramFilter createFilter(RestogramFilterType filterType) {

        if (filterType == RestogramFilterType.Simple)
            return new HashtagRestogramFilter();
        else if (filterType == RestogramFilterType.Complex)
            return new HashtagRestogramFilter();
        else

        return null;
    }
}
