package com.bsuir.verghel.basketball.model.service.impl;

import com.bsuir.verghel.basketball.model.dao.BallDao;
import com.bsuir.verghel.basketball.model.dao.StockDao;
import com.bsuir.verghel.basketball.model.dao.impl.JdbcBallDao;
import com.bsuir.verghel.basketball.model.dao.impl.JdbcStockDao;
import com.bsuir.verghel.basketball.model.entities.cart.Cart;
import com.bsuir.verghel.basketball.model.entities.cart.CartItem;
import com.bsuir.verghel.basketball.model.entities.ball.Ball;
import com.bsuir.verghel.basketball.model.exceptions.DaoException;
import com.bsuir.verghel.basketball.model.exceptions.OutOfStockException;
import com.bsuir.verghel.basketball.model.exceptions.ServiceException;
import com.bsuir.verghel.basketball.model.service.CartService;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service to work with cart
 *
 * @author Melekit
 * @version 1.0
 */
public class HttpSessionCartService implements CartService {
    /**
     * Attribute of cart in session
     */
    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";
    /**
     * Attribute of cart in request attribute
     */
    private static final String CART_ATTRIBUTE = "cart";
    /**
     * Instance of HttpSessionCartService
     */
    private static volatile HttpSessionCartService instance;
    /**
     * Instance of BallDao
     */
    private BallDao ballDao;
    /**
     * Instance of StockDao
     */
    private StockDao stockDao;

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of HttpSessionCartServiece
     */

    public static HttpSessionCartService getInstance() {
        if (instance == null) {
            synchronized (HttpSessionCartService.class) {
                if (instance == null) {
                    instance = new HttpSessionCartService();
                }
            }
        }
        return instance;
    }

    /**
     * Constructor of HttpSessionCartService
     */
    private HttpSessionCartService() {
        ballDao = JdbcBallDao.getInstance();
        stockDao = JdbcStockDao.getInstance();
    }

    /**
     * Get cart from session
     *
     * @param currentSession session with cart
     * @return cart from session
     */
    @Override
    public Cart getCart(HttpSession currentSession) {
        synchronized (currentSession) {
            Cart cart = (Cart) currentSession.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                currentSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            if (cart.getTotalCost() == null) {
                cart.setTotalCost(BigDecimal.ZERO);
            }
            return cart;
        }
    }

    /**
     * Add Ball to cart
     *
     * @param cart           cart to adding
     * @param productId      productId of ball to add
     * @param quantity       quantity of ball to add
     * @param currentSession session with cart
     * @throws OutOfStockException throws when ball outOfStock
     * @throws ServiceException    throws when there is some errors during service method execution
     */
    @Override
    public void add(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException {
        Optional<CartItem> productMatch;
        synchronized (currentSession) {
            Ball ball;
            try {
                ball = ballDao.get(productId).orElse(null);
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
            if (ball != null) {
                if (countingQuantityIncludingCart(cart, ball) < quantity) {
                    throw new OutOfStockException(ball, quantity, countingQuantityIncludingCart(cart, ball));
                }
                if ((productMatch = getCartItemMatch(cart, ball)).isPresent()) {
                    cart.getItems().
                            get(cart.getItems().indexOf(productMatch.get())).
                            setQuantity(productMatch.get().getQuantity() + quantity);
                } else {
                    cart.getItems().add(new CartItem(ball, quantity));
                    currentSession.setAttribute(CART_ATTRIBUTE, cart);
                }
                reCalculateCart(cart);
            }
        }
    }

    /**
     * Calculate quantity of ball with cart
     *
     * @param cart  cart with balls to recalculate
     * @param ball ball to recalculate
     * @return available quantity of ball minus quantity of ball in cart
     */
    private int countingQuantityIncludingCart(Cart cart, Ball ball) throws ServiceException {
        int result = 0;
        try {
            result = stockDao.availableStock(ball.getId());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
        Integer quantityInCart = cart.getItems().stream()
                .filter(currProduct -> currProduct.getBall().equals(ball))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0);
        result -= quantityInCart;
        return result;
    }

    /**
     * Update quantity of ball in cart
     *
     * @param cart           cart to update
     * @param productId      id of ball to update
     * @param quantity       quantity of ball to update
     * @param currentSession session with cart
     * @throws OutOfStockException throws when ball quantity out of stock during updating
     * @throws ServiceException    throws when there is some errors during service method execution
     */
    @Override
    public void update(Cart cart, Long productId, int quantity, HttpSession currentSession) throws OutOfStockException, ServiceException {
        synchronized (currentSession) {
            Ball ball;
            try {
                ball = ballDao.get(productId).orElse(null);
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage());
            }
            if (ball != null) {
                int availableStock = 0;
                try {
                    availableStock = stockDao.availableStock(ball.getId());
                } catch (DaoException e) {
                    throw new ServiceException(e.getMessage());
                }
                if (quantity > availableStock) {
                    throw new OutOfStockException(ball, quantity, availableStock);
                }
                getCartItemMatch(cart, ball).ifPresent(cartItem -> cart.getItems().
                        get(cart.getItems().indexOf(cartItem)).
                        setQuantity(quantity));
                reCalculateCart(cart);
            }
        }
    }

    /**
     * Delete item from cart
     *
     * @param cart           cart to delete
     * @param productId      id of ball to delete
     * @param currentSession session with cart
     */
    @Override
    public void delete(Cart cart, Long productId, HttpSession currentSession) {
        synchronized (currentSession) {
            cart.getItems().removeIf(item -> productId.equals(item.getBall().getId()));
            reCalculateCart(cart);
        }
    }

    /**
     * Recalculate cart
     *
     * @param cartToRecalculate cat to recalculate
     */
    @Override
    public void reCalculateCart(Cart cartToRecalculate) {
        BigDecimal totalCost = BigDecimal.ZERO;
        cartToRecalculate.setTotalItems(
                cartToRecalculate.getItems().stream().
                        map(CartItem::getQuantity).
                        mapToInt(q -> q).
                        sum()
        );
        for (CartItem item : cartToRecalculate.getItems()) {
            totalCost = totalCost.add(
                    item.getBall().getPrice().
                            multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        cartToRecalculate.setTotalCost(totalCost);
    }

    /**
     * Find cart item in cart
     *
     * @param cart    cart in witch we find
     * @param product product to find
     * @return cartItem
     */
    private Optional<CartItem> getCartItemMatch(Cart cart, Ball product) {
        return cart.getItems().stream().
                filter(currProduct -> currProduct.getBall().getId().equals(product.getId())).
                findAny();
    }

    /**
     * Clear cart in request
     *
     * @param currentSession session with cart
     */
    @Override
    public void clear(HttpSession currentSession) {
        Cart cart = getCart(currentSession);
        cart.getItems().clear();
        reCalculateCart(cart);
    }

    /**
     * Remove item from cart
     *
     * @param currentSession session with cart
     * @param ballId        id of ball to remove
     */
    @Override
    public void remove(HttpSession currentSession, Long ballId) {
        Cart cart = getCart(currentSession);
        cart.getItems().removeIf(item -> ballId.equals(item.getBall().getId()));
        reCalculateCart(cart);
    }
}
