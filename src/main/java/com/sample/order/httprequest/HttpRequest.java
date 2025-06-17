package com.sample.order.httprequest;

import com.sample.order.dto.AddressDTO;
import com.sample.order.dto.CartDTO;
import com.sample.order.dto.UserDTO;
import com.sample.order.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpRequest {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    @Value("${user.get-address.url}")
    private String getAddressUrl;

    @Value("${cart.get-cart-by-user.url}")
    private String getCartUrl;

    @Value("${user.get-user.url}")
    private String getUserUrl;

    @Value("${cart.empty-cart.url}")
    private String emptyCartUrl;

    public HttpRequest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDTO getUserById(Long id) throws ApplicationException {
        logger.info("Fetching user with id: {} in getUserById method", id);
        try {
            String url = getUserUrl.replace("{id}", String.valueOf(id));
            UserDTO userDTO =  restTemplate.getForObject(url, UserDTO.class);
            logger.info("User fetched successfully with id: {}", id);
            return userDTO;

        } catch (HttpClientErrorException.NotFound ex) {
            logger.error("User with id {} not found: {}", id, ex.getMessage());
            throw new UserNotFoundException("User with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        catch (HttpClientErrorException ex) {
            logger.error("Client error while fetching user with id {}: {}", id, ex.getMessage());
            throw new UserAccessException("Client error fetching user", HttpStatus.BAD_REQUEST);
        }catch (HttpServerErrorException e) {
            logger.error("Server error while fetching user with id {}: {}", id, e.getMessage());
            throw new UserAccessException("Server error while fetching user", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (ResourceAccessException e){
            logger.error("Resource access error while fetching user with id {}: {}", id, e.getMessage());
            throw new UserAccessException("User Service is not available", HttpStatus.SERVICE_UNAVAILABLE);
        }catch (Exception e) {
            logger.error("Unexpected error while fetching user with id {}: {}", id, e.getMessage());
            throw new ApplicationException("Unexpected error while fetching user with id " + id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AddressDTO getAddressByUserIdAndAddressId(Long userId, Long addressId) throws ApplicationException {
        logger.info("Fetching address for userId: {}, addressId: {}", userId, addressId);
        try {
            String url = getAddressUrl.replace("{userId}", String.valueOf(userId))
                                      .replace("{addressId}", String.valueOf(addressId));

            AddressDTO addressDTO = restTemplate.getForObject(url, AddressDTO.class);
            logger.info("Address fetched successfully for userId: {}, addressId: {}", userId, addressId);
            return addressDTO;

        }catch (HttpClientErrorException.NotFound e){
            logger.error("Address with userId {} and addressId {} not found: {}", userId, addressId, e.getMessage());
            throw new AddressNotFoundException("Address with userId " + userId + " and addressId " + addressId + " not found", HttpStatus.NOT_FOUND);
        }catch (HttpClientErrorException e){
            throw new AddressAccessException("Client error while fetching address", HttpStatus.BAD_REQUEST);
        }
        catch (HttpServerErrorException e) {
            logger.error("Server error while fetching address with userId {} and addressId {}: {}", userId, addressId, e.getMessage());
            throw new AddressAccessException("Server error while fetching address", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (ResourceAccessException e){
            logger.error("Resource access error while fetching address with userId {} and addressId {}: {}", userId, addressId, e.getMessage());
            throw new AddressAccessException("Address Service is not available", HttpStatus.SERVICE_UNAVAILABLE);
        }catch (Exception e) {
            logger.error("Unexpected error while fetching address with userId {} and addressId {}: {}", userId, addressId, e.getMessage());
            throw new ApplicationException("Unexpected error while fetching address for userId " + userId + " and addressId " + addressId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public CartDTO getCartByUserId(Long userId) throws ApplicationException {
        logger.info("Fetching cart for userId: {}", userId);
        try {
            String url = getCartUrl.replace("{userId}", String.valueOf(userId));
            logger.info("Fetching cart for userId: {}", userId);
            CartDTO cart = restTemplate.getForObject(url,CartDTO.class);
            logger.info("Cart fetched successfully for userId: {}", userId);
            return cart;
        }catch (HttpClientErrorException.NotFound e){
           throw new CartNotFoundException("Cart for userId " + userId + " not found", HttpStatus.NOT_FOUND);
        }catch (HttpClientErrorException e) {
            throw new CartAccessException("Client error while fetching cart", HttpStatus.BAD_REQUEST);
        }catch (HttpServerErrorException e) {
            logger.error("Server error while fetching cart for userId {}: {}", userId, e.getMessage());
            throw new CartAccessException("Server error while fetching cart", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (ResourceAccessException e){
            logger.error("Resource access error while fetching cart for userId {}: {}", userId, e.getMessage());
            throw new CartAccessException("Cart Service is not available", HttpStatus.SERVICE_UNAVAILABLE);
        }catch (Exception e) {
            logger.error("Unexpected error while fetching cart for userId {}: {}", userId, e.getMessage());
            throw new ApplicationException("Unexpected error while fetching cart for userId " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public void emptyCart(Long userId, Long cartId) throws ApplicationException {
            logger.info("Emptying cart for userId: {}, cartId: {}", userId, cartId);
            String url = emptyCartUrl.replace("{userId}", String.valueOf(userId))
                                      .replace("{cartId}", String.valueOf(cartId));

            try {
                restTemplate.delete(url);
                logger.info("Cart emptied successfully for userId: {}, cartId: {}", userId, cartId);
            }catch (HttpClientErrorException.NotFound e) {
                logger.error("Cart with userId {} and cartId {} not found: {}", userId, cartId, e.getMessage());
                throw new CartNotFoundException("Cart with userId " + userId + " and cartId " + cartId + " not found", HttpStatus.NOT_FOUND);
            }catch (HttpClientErrorException e) {
                throw new CartAccessException("Client error while emptying cart", HttpStatus.BAD_REQUEST);
            } catch (HttpServerErrorException e) {
                logger.error("Server error while emptying cart with userId {} and cartId {}: {}", userId, cartId, e.getMessage());
                throw new CartAccessException("Server error while emptying cart", HttpStatus.INTERNAL_SERVER_ERROR);
            }catch (ResourceAccessException e) {
                logger.error("Resource access error while emptying cart with userId {} and cartId {}: {}", userId, cartId, e.getMessage());
                throw new CartAccessException("Cart Service is not available", HttpStatus.SERVICE_UNAVAILABLE);
            }catch (Exception e) {
                logger.error("Unexpected error while emptying cart with userId {} and cartId {}: {}", userId, cartId, e.getMessage());
                throw new ApplicationException("Unexpected error while emptying cart for userId " + userId + " and cartId " + cartId, HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }


}

