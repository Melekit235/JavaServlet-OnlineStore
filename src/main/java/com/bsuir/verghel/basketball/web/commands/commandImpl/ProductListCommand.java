package com.bsuir.verghel.basketball.web.commands.commandImpl;

import com.bsuir.verghel.basketball.model.dao.BallDao;
import com.bsuir.verghel.basketball.model.dao.impl.JdbcBallDao;
import com.bsuir.verghel.basketball.model.enums.SortField;
import com.bsuir.verghel.basketball.model.enums.SortOrder;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;
import com.bsuir.verghel.basketball.web.JspPageName;
import com.bsuir.verghel.basketball.web.commands.ICommand;
import com.bsuir.verghel.basketball.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * @author Melekit
 * @version 1.0
 * Command to show list of products
 */
public class ProductListCommand implements ICommand {
    private final BallDao ballDao = JdbcBallDao.getInstance();
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_PARAMETER = "sort";
    private static final String ORDER_PARAMETER = "order";
    private static final String BALLS_ATTRIBUTE = "balls";
    private static final String PAGE_PARAMETER = "page";
    private static final String PAGE_ATTRIBUTE = "numberOfPages";
    private static final int BALLS_ON_PAGE = 10;

    /**
     * Get product list and return product list jsp
     * @param request http request
     * @return product list jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String pageNumber = request.getParameter(PAGE_PARAMETER);
        Long number;
        try {
            request.setAttribute(BALLS_ATTRIBUTE, ballDao.findAll(((pageNumber == null ? 1 : Integer.parseInt(pageNumber)) - 1) * BALLS_ON_PAGE, BALLS_ON_PAGE,
                    Optional.ofNullable(request.getParameter(SORT_PARAMETER)).map(SortField::valueOf).orElse(null),
                    Optional.ofNullable(request.getParameter(ORDER_PARAMETER)).map(SortOrder::valueOf).orElse(null), request.getParameter(QUERY_PARAMETER)));
            number = ballDao.numberByQuery(request.getParameter(QUERY_PARAMETER));
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        request.setAttribute(PAGE_ATTRIBUTE, (number + BALLS_ON_PAGE - 1) / BALLS_ON_PAGE);
        return JspPageName.PRODUCT_LIST_JSP;
    }
}
