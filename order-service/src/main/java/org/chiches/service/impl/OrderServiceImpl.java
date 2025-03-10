package org.chiches.service.impl;

import jakarta.persistence.PersistenceException;
import org.chiches.dto.OrderContentDTO;
import org.chiches.dto.OrderDTO;
import org.chiches.entity.OrderEntity;
import org.chiches.entity.OrderStatus;
import org.chiches.exception.DatabaseException;
import org.chiches.exception.ResourceNotFoundException;
import org.chiches.repository.InterServiceRepository;
import org.chiches.repository.OrderRepository;
import org.chiches.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final InterServiceRepository interServiceRepository;
    // Remove direct DishItemRepository dependency. Instead, rely on a REST client for catalog info if needed.
    //private final UserRepository userRepository; // Alternatively, use a REST client to the Identity Service.
    //private final NotificationService notificationService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ModelMapper modelMapper, InterServiceRepository interServiceRepository
                            //,
                            //UserRepository userRepository,
                            //NotificationService notificationService
                             ) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
//        this.userRepository = userRepository;
//        this.notificationService = notificationService;
        this.interServiceRepository = interServiceRepository;
    }

//    @Override
    @Transactional
    public ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO, Principal principal) {
        //TODO: user to be added
        Long userId = 1L;
        OrderEntity orderEntity = new OrderEntity(userId);

        orderEntity.changeStatus(OrderStatus.CREATED);

        for (OrderContentDTO orderContentDTO: orderDTO.getOrderContentDTOs()){
            orderContentDTO.setDishItemPrice(
                    interServiceRepository
                            .getDishItemById(
                                    orderContentDTO
                                            .getDishItemId()

                            ).getPrice());
        }

        for (OrderContentDTO oc : orderDTO.getOrderContentDTOs()) {
            // In a microservices world, order service does not access dish item details directly.
            // Instead, it stores the dishItemId (fetched from the Catalog service via REST, if needed)
            Long dishItemId = oc.getDishItemId();
            // Assume oc carries a price snapshot (populated by the client or through a prior catalog lookup)
            orderEntity.addOrderContent(dishItemId, oc.getDishItemPrice(), oc.getQuantity());
        }
        try {
            OrderEntity savedEntity = orderRepository.save(orderEntity);
            OrderDTO savedDTO = convert(savedEntity);
            ResponseEntity<OrderDTO> responseEntity = ResponseEntity.ok(savedDTO);
            notifyWebSocketClients();
            return responseEntity;
        } catch (DataAccessException | PersistenceException e) {
            throw new DatabaseException("Order creation failed due to DB issues");
        }
//        // Instead of retrieving user entity from local repository, consider using token info or a REST call
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserEntity userEntity = userRepository.findByLogin(userDetails.getUsername())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        OrderEntity orderEntity = new OrderEntity(userEntity.getId()); // only store user ID
//
//        // For each order content, store dish item ID and a snapshot price
//        for (OrderContentDTO oc : orderDTO.getOrderContentDTOs()) {
//            // In a microservices world, order service does not access dish item details directly.
//            // Instead, it stores the dishItemId (fetched from the Catalog service via REST, if needed)
//            Long dishItemId = oc.getDishItemDTO().getId();
//            // Assume oc carries a price snapshot (populated by the client or through a prior catalog lookup)
//            orderEntity.addOrderContent(dishItemId, oc.getDishItemDTO().getPrice(), oc.getQuantity());
//        }
//        try {
//            OrderEntity savedEntity = orderRepository.save(orderEntity);
//            OrderDTO savedDTO = convert(savedEntity);
//            ResponseEntity<OrderDTO> responseEntity = ResponseEntity.ok(savedDTO);
//            notifyWebSocketClients();
//            return responseEntity;
//        } catch (DataAccessException | PersistenceException e) {
//            throw new DatabaseException("Order creation failed due to DB issues");
//        }

    }

    @Override
    public ResponseEntity<OrderDTO> findById(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderDTO orderDTO = convert(orderEntity);
        return ResponseEntity.ok(orderDTO);
    }

    @Override
    public ResponseEntity<?> findAllActive() {
        return null;
    }

    @Override
    public ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO) {
        return null;
    }

    @Override
//    @Transactional
    public ResponseEntity<OrderDTO> updateOrderStatus(Long id, OrderStatus orderStatus) {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        orderEntity.changeStatus(orderStatus);
        try {
            OrderEntity savedEntity = orderRepository.save(orderEntity);
            OrderDTO savedDTO = convert(savedEntity);
            ResponseEntity<OrderDTO> responseEntity = ResponseEntity.ok(savedDTO);
            notifyWebSocketClients();
            return responseEntity;
        } catch (DataAccessException | PersistenceException e) {
            throw new DatabaseException("Order update failed due to DB issues");
        }
    }

    private OrderDTO convert(OrderEntity orderEntity) {
        // Convert the order entity to a DTO. Optionally enrich the order DTO
        // by fetching dish details from the Catalog service via REST if needed.
        OrderDTO orderDTO = modelMapper.map(orderEntity, OrderDTO.class);
        // For each order content, the dishItemDTO may contain only the ID and snapshot price.
        List<OrderContentDTO> contents = orderEntity.getOrderContents().stream()
                .map(oc -> {
                    OrderContentDTO dto = modelMapper.map(oc, OrderContentDTO.class);
                    // Do not set a full DishItemDTO; only include the dishItemId and any snapshots.
                    return dto;
                })
                .collect(Collectors.toList());
        orderDTO.setOrderContentDTOs(contents);
        return orderDTO;
    }

    private void notifyWebSocketClients() {
//        List<OrderDTO> activeOrders = orderRepository.findByStatus(OrderStatus.CREATED).stream()
//                .map(this::convert)
//                .collect(Collectors.toList());
//        notificationService.sentToAll(PublicDestination.ORDER_UPDATE, ResponseEntity.ok(activeOrders));
    }
}
