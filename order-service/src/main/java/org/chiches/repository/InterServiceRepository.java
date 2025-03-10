package org.chiches.repository;

import org.apache.catalina.User;
import org.chiches.dto.DishItemDTO;
import org.chiches.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InterServiceRepository {
    private final RestTemplate restTemplate;


    public InterServiceRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public UserDTO getUserById(Long id) {
        return restTemplate.getForObject("http://identity-service/users/id/" + id, UserDTO.class);
    }
    public UserDTO getUserByUsername(String username) {
        return restTemplate.getForObject("http://identity-service/users/name/" + username, UserDTO.class);
    }
    public DishItemDTO getDishItemById(Long id) {
        return restTemplate.getForObject("http://product-service/dish-items/id/" + id, DishItemDTO.class);
    }
    public List<DishItemDTO> getDishItemsByIds(List<Long> ids) {
        return restTemplate.postForObject("http://product-service/dish-items/ids", ids, List.class);
    }
}
