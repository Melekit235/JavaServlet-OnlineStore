package com.bsuir.verghel.basketball.web.commands.commandImpl;

import com.bsuir.verghel.basketball.model.service.CartService;
import com.bsuir.verghel.basketball.model.service.impl.HttpSessionCartService;
import com.bsuir.verghel.basketball.web.commands.CommandHelper;
import com.bsuir.verghel.basketball.web.commands.ICommand;
import com.bsuir.verghel.basketball.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Melekit
 * @version 1.0
 * Command to delete cart item
 */
public class CartDeleteCommand implements ICommand {
    private CartService cartService = HttpSessionCartService.getInstance();

    /**
     * Delete cart item and return cart jsp
     *
     * @param request http request
     * @return cart jsp
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Object lang = request.getSession().getAttribute("lang");
        if (lang == null) {
            lang = "en";
        }
        Locale locale = new Locale(lang.toString());
        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        int ballId = Integer.parseInt(request.getParameter("id"));
        cartService.delete(cartService.getCart(request.getSession()), (long) ballId, request.getSession());
        request.setAttribute("successMessage", rb.getString("delete_success"));
        return CommandHelper.getInstance().getCommand("cart").execute(request);
    }
}
