package org.example.helloworld.rmi;
import org.example.helloworld.dto.*;
import org.example.helloworld.model.*;
import org.example.helloworld.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CafeteriaRemoteImpl implements CafeteriaRemote {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final MenuComponentRepository menuComponentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CafeteriaRemoteImpl(MenuRepository menuRepository,
                                DishRepository dishRepository,
                                MenuComponentRepository menuComponentRepository,
                                UserRepository userRepository,
                               OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.menuComponentRepository = menuComponentRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }


    @Override
    public MenuDTO getMenuById(Long id) throws RemoteException {
        Optional<Menu> opt = menuRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }

        Menu menu = opt.get();

        List<DishDTO> dishDTOs = menu.getComponents().stream()
                .filter(c -> c instanceof Dish)
                .map(c -> (Dish) c)
                .map(d -> new DishDTO(
                        d.getId(),
                        d.getName(),
                        d.getDescription(),
                        d.getPrice()
                ))
                .toList();

        Double totalPrice = menu.computePrice();

        return new MenuDTO(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                totalPrice,
                dishDTOs
        );
    }

    @Override
    public List<DishDTO> getAllDishes() throws RemoteException {
        return dishRepository.findAll().stream()
                .map(this::toDishDTO)
                .collect(Collectors.toList());
    }

    private DishDTO toDishDTO(Dish d) {
        return new DishDTO(
                d.getId(),
                d.getName(),
                d.getDescription(),
                d.getPrice()
        );
    }
    @Override
    @Transactional
    public DishDTO createDish(DishDTO dish) throws RemoteException {
        Dish entity = new Dish();
        entity.setName(dish.getName());
        entity.setDescription(dish.getDescription());
        entity.setPrice(dish.getPrice());

        Dish saved = dishRepository.save(entity);
        return toDishDTO(saved);
    }

    @Override
    @Transactional
    public void deleteDish(Long id) throws RemoteException {
        dishRepository.deleteById(id);
    }

    @Override
    public List<MenuDTO> getAllMenus() throws RemoteException {
        return menuRepository.findAll().stream()
                .map(m -> new MenuDTO(
                        m.getId(),
                        m.getName(),
                        m.getDescription(),
                        m.computePrice(),
                        List.of()
                ))
                .toList();
    }

    @Override
    public List<MenuComponentOptionDTO> getAllMenuComponentOptions() throws RemoteException {
        Set<Long> componentIdsThatAlreadyHaveAParent = new HashSet<>();


        for (Menu m : menuRepository.findAll()) {
            for (MenuComponent child : m.getComponents()) {
                if (child.getId() != null) {
                    componentIdsThatAlreadyHaveAParent.add(child.getId());
                }
            }
        }

        return menuComponentRepository.findAll().stream()
                .filter(c -> c.getId() != null && !componentIdsThatAlreadyHaveAParent.contains(c.getId()))
                .map(c -> {
                    MenuComponentOptionDTO.Type type =
                            (c instanceof Menu) ? MenuComponentOptionDTO.Type.MENU : MenuComponentOptionDTO.Type.DISH;
                    return new MenuComponentOptionDTO(c.getId(), c.getName(), type);
                })
                .toList();
    }

    @Override
    @Transactional
    public MenuDTO createMenu(String name, String description, List<Long> componentIds) throws RemoteException {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setDescription(description);

        setComponentsOnMenu(menu, componentIds);

        Menu saved = menuRepository.save(menu);

        return new MenuDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.computePrice(),
                List.of()
        );
    }

    @Override
    @Transactional
    public MenuDTO setMenuComponents(Long menuId, List<Long> componentIds) throws RemoteException {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RemoteException("Menu not found: " + menuId));

        menu.getComponents().clear();
        setComponentsOnMenu(menu, componentIds);

        Menu saved = menuRepository.save(menu);

        return new MenuDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.computePrice(),
                List.of()
        );
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) throws RemoteException {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RemoteException("Menu not found: " + id));

        boolean usedInOrder = orderRepository.findAll().stream()
                .anyMatch(o -> o.getItems().stream().anyMatch(c -> c.getId() != null && c.getId().equals(id)));
        if (usedInOrder) {
            throw new RemoteException("Cannot delete menu. It is used in an order.");
        }

        boolean usedInFavorites = userRepository.findAll().stream()
                .anyMatch(u -> u.getFavoriteItems().stream().anyMatch(c -> c.getId() != null && c.getId().equals(id)));
        if (usedInFavorites) {
            throw new RemoteException("Cannot delete menu. It is in user favorites.");
        }

        menu.getComponents().clear();
        menuRepository.save(menu);

        menuRepository.delete(menu);
    }

    private void setComponentsOnMenu(Menu menu, List<Long> componentIds) throws RemoteException {
        if (componentIds == null || componentIds.isEmpty()) {
            return;
        }

        for (Long componentId : componentIds) {
            if (menu.getId() != null && menu.getId().equals(componentId)) {
                throw new RemoteException("Invalid menu structure: menu cannot contain itself (id=" + componentId + ")");
            }

            MenuComponent component = menuComponentRepository.findById(componentId)
                    .orElseThrow(() -> new RemoteException("MenuComponent not found: " + componentId));

            menu.addComponent(component);
        }
    }

    @Override
    public MenuComponentDTO getMenuTreeById(Long id) throws RemoteException {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RemoteException("Menu not found: " + id));

        return toMenuComponentTree(menu, new HashSet<>());
    }

    private MenuComponentDTO toMenuComponentTree(MenuComponent component, Set<Long> visited) throws RemoteException {
        if (component.getId() == null) {
            throw new RemoteException("Cannot build DTO tree for component without id");
        }

        if (!visited.add(component.getId())) {
            // cycle detected → stop expanding
            return new MenuComponentDTO(
                    component.getId(),
                    component.getName(),
                    component.getDescription(),
                    component.getPrice(),
                    MenuComponentDTO.Type.MENU
            );
        }

        if (component instanceof Dish dish) {
            return new MenuComponentDTO(
                    dish.getId(),
                    dish.getName(),
                    dish.getDescription(),
                    dish.getPrice(),
                    MenuComponentDTO.Type.DISH
            );
        }

        if (component instanceof Menu menu) {
            MenuComponentDTO dto = new MenuComponentDTO(
                    menu.getId(),
                    menu.getName(),
                    menu.getDescription(),
                    menu.getPrice(),
                    MenuComponentDTO.Type.MENU
            );

            List<MenuComponentDTO> children = menu.getComponents().stream()
                    .map(child -> {
                        try {
                            return toMenuComponentTree(child, visited);
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            dto.setChildren(children);
            return dto;
        }

        throw new RemoteException("Unknown MenuComponent type: " + component.getClass());
    }

    @Override
    public List<OrderDTO> getAllOrders() throws java.rmi.RemoteException {
        return orderRepository.findAll().stream()
                .map(this::toOrderDTO)
                .toList();
    }

    private OrderDTO toOrderDTO(Order order) {
        User u = order.getUser();
        Long userId = (u != null) ? u.getId() : null;
        String username = (u != null) ? u.getUsername() : null;

        return new OrderDTO(
                order.getId(),
                userId,
                username,
                order.getStatus(),
                order.getTotalPrice()
        );
    }

    @Override
    @Transactional
    public void deleteOrder(java.lang.Long orderId) throws java.rmi.RemoteException {
        if (orderId == null) {
            throw new RemoteException("Order id is required");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RemoteException("Order not found: " + orderId));

        if (order.getStatus() == OrderStatus.PENDING) {
            throw new RemoteException("Cannot delete a PENDING order. Only COMPLETED orders can be deleted.");
        }

        orderRepository.delete(order);
    }

}
