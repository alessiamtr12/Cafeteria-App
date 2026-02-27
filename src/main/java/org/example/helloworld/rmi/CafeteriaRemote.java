package org.example.helloworld.rmi;

import org.example.helloworld.dto.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CafeteriaRemote extends Remote {
    MenuDTO getMenuById(Long id) throws RemoteException;

    List<DishDTO> getAllDishes() throws RemoteException;

    DishDTO createDish(DishDTO dish) throws RemoteException;

    void deleteDish(Long id) throws RemoteException;

    List<MenuDTO> getAllMenus() throws RemoteException;

    List<MenuComponentOptionDTO> getAllMenuComponentOptions() throws RemoteException;

    MenuDTO createMenu(String name, String description, List<Long> componentIds) throws RemoteException;

    MenuDTO setMenuComponents(Long menuId, List<Long> componentIds) throws RemoteException;

    void deleteMenu(Long id) throws RemoteException;
    MenuComponentDTO getMenuTreeById(Long id) throws RemoteException;

    List<OrderDTO> getAllOrders() throws RemoteException;

    void deleteOrder(java.lang.Long orderId) throws RemoteException;
}
