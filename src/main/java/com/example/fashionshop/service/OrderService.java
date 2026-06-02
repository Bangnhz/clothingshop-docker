package com.example.fashionshop.service;

//
import com.example.fashionshop.dto.*;
import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.model.*;
import com.example.fashionshop.common.OrderStatus;
import com.example.fashionshop.common.PaymentMethod;
import com.example.fashionshop.common.PaymentStatus;
import com.example.fashionshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> request = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {

            if (order == null)
                continue;

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setIsGuest(order.getIsGuest());
            orderDTO.setEmail(order.getEmail());
            orderDTO.setPhone(order.getPhone());
            orderDTO.setSubtotal(order.getSubtotal());
            orderDTO.setDiscountAmount(order.getDiscountAmount());
            orderDTO.setShippingFee(order.getShippingFee());
            orderDTO.setTotalPrice(order.getTotalPrice());
            orderDTO.setVoucherCode(order.getVoucherCode());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setPaymentMethod(order.getPaymentMethod());
            orderDTO.setPaymentStatus(order.getPaymentStatus());
            orderDTO.setCreatedAt(order.getCreatedAt());

            // USER
            User user = order.getUser();
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFullName(user.getFullName());
                userDTO.setEmail(user.getEmail());
                userDTO.setPhone(user.getPhone());

                Address address = user.getAddress();
                if (address != null) {
                    AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setWard(address.getWard());
                    addressDTO.setCity(address.getCity());
                    addressDTO.setDistrict(address.getDistrict());
                    addressDTO.setAddressLine(address.getAddressLine());
                    userDTO.setAddress(addressDTO);
                }

                orderDTO.setUser(userDTO);
            }

            // ITEMS
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
            if (order.getItems() != null) {
                for (OrderItem orderItem : order.getItems()) {

                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setQuantity(orderItem.getQuantity());
                    dto.setPrice(orderItem.getPrice());

                    ProductVariant variant = orderItem.getProductVariant();
                    if (variant != null) {
                        Size size = variant.getSize();
                        if (size != null) {
                            SizeResponse sizeRes = new SizeResponse();
                            sizeRes.setName(size.getName());

                            ProductVariantResponse variantRes = new ProductVariantResponse();
                            variantRes.setSize(sizeRes);

                            dto.setVariant(variantRes);
                        }
                    }

                    orderItemDTOS.add(dto);
                }
            }

            orderDTO.setOrderItems(orderItemDTOS);
            request.add(orderDTO);
        }

        return request;
    }

    public OrderDTO getOrderById(long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return null;

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setIsGuest(order.getIsGuest());
        orderDTO.setEmail(order.getEmail());
        orderDTO.setPhone(order.getPhone());
        orderDTO.setSubtotal(order.getSubtotal());
        orderDTO.setDiscountAmount(order.getDiscountAmount());
        orderDTO.setShippingFee(order.getShippingFee());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setVoucherCode(order.getVoucherCode());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setPaymentStatus(order.getPaymentStatus());
        orderDTO.setCreatedAt(order.getCreatedAt());

        // USER
        User user = order.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());

            Address address = user.getAddress();
            if (address != null) {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setWard(address.getWard());
                addressDTO.setCity(address.getCity());
                addressDTO.setDistrict(address.getDistrict());
                addressDTO.setAddressLine(address.getAddressLine());
                userDTO.setAddress(addressDTO);
            }

            orderDTO.setUser(userDTO);
        }

        // ITEMS
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {

                OrderItemDTO dto = new OrderItemDTO();
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getPrice());

                orderItemDTOS.add(dto);
            }
        }

        orderDTO.setOrderItems(orderItemDTOS);

        return orderDTO;
    }

    // huy don hang
    public boolean cancelOrder(long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return false;

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return true;
    }

    // cap nhat trang thai don hang
    public boolean updateStatusOrder(long id, String status) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return false;

        try {
            order.setStatus(OrderStatus.valueOf(status));
        } catch (Exception e) {
            return false;
        }

        orderRepository.save(order);
        return true;
    }

    // hien thi don hang (user)
    public List<OrderDTO> getOrdersByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if (user == null)
            return new ArrayList<>();

        List<Order> orders = orderRepository.findByUserId(user.getId());
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(mapToOrderDTO(order));
        }

        return result;
    }

    // hien thi don hang theo status (user)
    public List<OrderDTO> getOrdersByUsernameAndStatus(String username, OrderStatus status) {

        User user = userRepository.findByUsername(username);
        if (user == null)
            return new ArrayList<>();

        List<Order> orders = orderRepository.findByUserIdAndStatus(user.getId(), status);
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(mapToOrderDTO(order));
        }

        return result;
    }

    // xem chi tiêt don hang (user)
    public OrderDTO getOrderDetailForUser(long id, String username) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return null;

        if (order.getUser() != null &&
                !order.getUser().getUsername().equals(username)) {
            return null;
        }

        return buildOrderDetail(order);
    }

    // huy don hang (user)
    public boolean cancelOrderByUser(long id, String username) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return false;

        if (order.getUser() != null &&
                !order.getUser().getUsername().equals(username)) {
            return false;
        }

        if (order.getStatus() != OrderStatus.PENDING)
            return false;

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return true;
    }

    // hien thi don hang (guest)
    public List<OrderDTO> getOrdersByEmail(String email) {

        List<Order> orders = orderRepository.findByEmail(email);
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(mapToOrderDTO(order));
        }

        return result;
    }

    // xem chi tiet don hang (guest)
    public OrderDTO getOrderDetailForGuest(long id, String email) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return null;

        if (!email.equals(order.getEmail()))
            return null;

        return buildOrderDetail(order);
    }

    // huy don hang (guest)
    public boolean cancelOrderByGuest(long id, String email) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null)
            return false;

        if (!email.equals(order.getEmail()))
            return false;

        if (order.getStatus() != OrderStatus.PENDING)
            return false;

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return true;
    }

    // dat hang
    @Transactional
    public boolean placeOrder(OrderDTO request, String username) {

        User user = null;
        boolean isGuest = true;

        if (username != null) {
            user = userRepository.findByUsername(username);
            if (user != null)
                isGuest = false;
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        order.setIsGuest(isGuest);
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());

        if (request.getVoucherCode() != null) {
            order.setVoucherCode(request.getVoucherCode());
        }
        order.setCreatedAt(java.time.LocalDateTime.now());

        // Set payment method from request, default to COD
        PaymentMethod paymentMethod = PaymentMethod.COD;
        if (request.getPaymentMethod() != null) {
            paymentMethod = request.getPaymentMethod();
        }
        order.setPaymentMethod(paymentMethod);

        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            return false;
        }

        for (OrderItemDTO dto : request.getOrderItems()) {

            if (dto.getVariant() == null || dto.getVariant().getId() == null)
                continue;

            ProductVariant variant = productVariantRepository
                    .findById(dto.getVariant().getId())
                    .orElse(null);

            if (variant == null || variant.getProduct() == null)
                continue;

            double price = variant.getProduct().getPrice();

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductVariant(variant);
            item.setQuantity(dto.getQuantity());
            item.setPrice(price);

            total += price * dto.getQuantity();
            items.add(item);
        }

        order.setSubtotal(total);

        double discount = request.getDiscountAmount() != null ? request.getDiscountAmount() : 0;
        if (discount == 0) {
            if (!isGuest && user != null && user.getDiscount_percent() != null) {
                discount = total * user.getDiscount_percent() / 100;
            } else if (isGuest && request.getVoucherCode() != null
                    && request.getVoucherCode().equalsIgnoreCase("LUXE5")) {
                discount = total * 0.05;
            }
        }
        order.setDiscountAmount(discount);

        double shippingFee = request.getShippingFee() != null ? request.getShippingFee() : 0.0;
        order.setShippingFee(shippingFee);

        order.setItems(items);
        order.setTotalPrice(total - discount + shippingFee);

        orderRepository.save(order);

        return true;
    }

    private OrderDTO mapToOrderDTO(Order order) {

        OrderDTO dto = new OrderDTO();

        dto.setId(order.getId());
        dto.setIsGuest(order.getIsGuest());
        dto.setEmail(order.getEmail());
        dto.setPhone(order.getPhone());
        dto.setSubtotal(order.getSubtotal());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setShippingFee(order.getShippingFee());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setVoucherCode(order.getVoucherCode());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setCreatedAt(order.getCreatedAt());

        // Map user to display on UI
        User user = order.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            Address address = user.getAddress();
            if (address != null) {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setAddressLine(address.getAddressLine());
                addressDTO.setWard(address.getWard());
                addressDTO.setDistrict(address.getDistrict());
                addressDTO.setCity(address.getCity());
                userDTO.setAddress(addressDTO);
            }
            dto.setUser(userDTO);
        }

        return dto;
    }

    private OrderDTO buildOrderDetail(Order order) {

        if (order == null)
            return null;

        OrderDTO dto = mapToOrderDTO(order);

        List<OrderItemDTO> itemDTOS = new ArrayList<>();
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());

        for (OrderItem item : items) {

            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());

            ProductVariant variant = item.getProductVariant();
            if (variant == null)
                continue;

            ProductVariantResponse variantRes = new ProductVariantResponse();

            Product product = variant.getProduct();
            if (product != null) {

                ProductsResponse productRes = new ProductsResponse();
                productRes.setName(product.getName());
                productRes.setDescription(product.getDescription());
                productRes.setPrice(product.getPrice());

                List<ProductImageResponse> images = new ArrayList<>();
                for (ProductImage img : productImageRepository.findByProductId(product.getId())) {
                    ProductImageResponse imgRes = new ProductImageResponse();
                    imgRes.setImageUrl(img.getImageUrl());
                    images.add(imgRes);
                }

                productRes.setImages(images);
                variantRes.setProductResponse(productRes);
            }

            if (variant.getSize() != null) {
                SizeResponse size = new SizeResponse();
                size.setName(variant.getSize().getName());
                variantRes.setSize(size);
            }

            itemDTO.setVariant(variantRes);
            itemDTOS.add(itemDTO);
        }

        dto.setOrderItems(itemDTOS);
        return dto;
    }
}
