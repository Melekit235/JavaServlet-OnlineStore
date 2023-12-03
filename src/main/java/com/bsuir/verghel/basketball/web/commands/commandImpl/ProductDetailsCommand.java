package com.bsuir.verghel.basketball.web.commands.commandImpl;

import com.bsuir.verghel.basketball.model.dao.BallDao;
import com.bsuir.verghel.basketball.model.dao.impl.JdbcBallDao;
import com.bsuir.verghel.basketball.model.entities.ball.Ball;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;
import com.bsuir.verghel.basketball.web.JspPageName;
import com.bsuir.verghel.basketball.web.commands.ICommand;
import com.bsuir.verghel.basketball.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Melekit
 * @version 1.0
 * Command to get product details page
 */
public class ProductDetailsCommand implements ICommand {
    private final BallDao ballDao = JdbcBallDao.getInstance();
    private static final String BALL_ATTRIBUTE = "ball";

    /**
     * Return product details page of current ball
     * @param request http request
     * @return product details page jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Ball ball;
        try {
            if (request.getParameter("ball_id") == null){
                ball = ballDao.get(Long.parseLong(request.getAttribute("ball_id").toString())).orElse(null);
            } else {
                ball = ballDao.get(Long.valueOf(request.getParameter("ball_id"))).orElse(null);
            }
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        if (ball != null) {
            request.setAttribute(BALL_ATTRIBUTE, ball);
            return JspPageName.PRODUCT_PAGE;
        }
        else{
            return JspPageName.PRODUCT_NOT_FOUND_PAGE;
        }
    }
}
