package org.example.helloworld.controller;

import org.example.helloworld.dto.DishDTO;
import org.example.helloworld.dto.MenuDTO;
import org.example.helloworld.rmi.CafeteriaRemote;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final CafeteriaRemote cafeteriaRemoteClient;

    public AdminController(@Qualifier("cafeteriaRemoteClient") CafeteriaRemote cafeteriaRemoteClient) {
        this.cafeteriaRemoteClient = cafeteriaRemoteClient;
    }

    @GetMapping
    public String adminPage(Model model) throws RemoteException {

        model.addAttribute("dishes", cafeteriaRemoteClient.getAllDishes());
        model.addAttribute("menus", cafeteriaRemoteClient.getAllMenus());
        model.addAttribute("options", cafeteriaRemoteClient.getAllMenuComponentOptions());
        return "admin"; // src/main/resources/templates/admin.html
    }

    @PostMapping("/api/dishes")
    @ResponseBody
    public void createDish(@RequestBody DishDTO dish) throws RemoteException {
        cafeteriaRemoteClient.createDish(dish);
    }

    @DeleteMapping("/api/dishes/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDish(@PathVariable Long id){
        try {
            cafeteriaRemoteClient.deleteDish(id);

            return ResponseEntity.ok("Deleted dish id=" + id);
        } catch (DataIntegrityViolationException | RemoteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cannot delete dish. It is used in a menu and/or an order.");
        }
    }

    @PostMapping("/api/menus")
    @ResponseBody
    public void createMenu(@RequestBody Map<String, Object> payload) throws RemoteException {
        String name = (String) payload.getOrDefault("name", "");
        String description = (String) payload.getOrDefault("description", "");
        List<Number> idsRaw = (List<Number>) payload.getOrDefault("componentIds", List.of());
        List<Long> componentIds = idsRaw.stream().map(Number::longValue).toList();
        cafeteriaRemoteClient.createMenu(name, description, componentIds);
    }

    @DeleteMapping("/api/menus/{id}")
    @ResponseBody
    public void deleteMenu(@PathVariable Long id) throws RemoteException {
        cafeteriaRemoteClient.deleteMenu(id);
    }
}