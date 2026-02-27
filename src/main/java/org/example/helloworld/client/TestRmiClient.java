package org.example.helloworld.client;

import org.example.helloworld.dto.DishDTO;
import org.example.helloworld.dto.MenuComponentOptionDTO;
import org.example.helloworld.dto.MenuDTO;
import org.example.helloworld.dto.OrderDTO;
import org.example.helloworld.rmi.CafeteriaRemote;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestRmiClient {

    public static void main(String[] args) throws Exception {
        CafeteriaRemote remote = (CafeteriaRemote) Naming.lookup("rmi://localhost:1099/CafeteriaService");
        System.out.println("Connected to RMI service: rmi://localhost:1099/CafeteriaService");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> listDishes(remote);
                    case "2" -> getMenuById(remote, scanner);
                    case "3" -> createDish(remote, scanner);
                    case "4" -> deleteDish(remote, scanner);
                    case "5" -> createMenu(remote, scanner);
                    case "6" -> listMenus(remote);
                    case "7" -> deleteMenu(remote, scanner);
                    case "8" -> listOrders(remote);
                    case "9" -> deleteOrder(remote, scanner);
                    case "0" -> {
                        System.out.println("Bye.");
                        return;
                    }
                    default -> System.out.println("Unknown option. Try again.");
                }

                System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("==== Cafeteria RMI Admin Client ====");
        System.out.println("1 List all dishes");
        System.out.println("2 Get menu by id");
        System.out.println("3 Create dish");
        System.out.println("4 Delete dish");
        System.out.println("5 Create menu (select dishes/menus)");
        System.out.println("6 List all menus");
        System.out.println("7 Delete menu");
        System.out.println("8 List all orders");
        System.out.println("9 Delete order");
        System.out.println("0 Exit");
        System.out.print("Choose: ");
    }

    private static void listDishes(CafeteriaRemote remote) throws Exception {
        List<DishDTO> dishes = remote.getAllDishes();
        if (dishes.isEmpty()) {
            System.out.println("(no dishes)");
            return;
        }

        for (DishDTO d : dishes) {
            System.out.println(d.getId() + " - " + d.getName() + " : " + d.getPrice());
        }
    }

    private static void getMenuById(CafeteriaRemote remote, Scanner scanner) throws Exception {
        System.out.print("Menu id: ");
        Long id = readLong(scanner);

        MenuDTO menu = remote.getMenuById(id);
        if (menu == null) {
            System.out.println("Menu not found.");
            return;
        }

        System.out.println("Menu: " + menu.getName() + " (total price = " + menu.getTotalPrice() + ")");
        if (menu.getDishes() == null || menu.getDishes().isEmpty()) {
            System.out.println("(no dishes in this menu)");
            return;
        }

        for (DishDTO dish : menu.getDishes()) {
            System.out.println(" - " + dish.getName() + " : " + dish.getPrice());
        }
    }

    private static void createDish(CafeteriaRemote remote, Scanner scanner) throws Exception {
        System.out.print("Dish name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Dish description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Dish price: ");
        Double price = readDouble(scanner);

        DishDTO created = remote.createDish(new DishDTO(null, name, description, price));
        System.out.println("Created dish: " + created.getId() + " - " + created.getName() + " : " + created.getPrice());
    }

    private static void deleteDish(CafeteriaRemote remote, Scanner scanner) {
        try {
            System.out.print("Dish id to delete: ");
            Long id = readLong(scanner);

            remote.deleteDish(id);
            System.out.println("Deleted dish id=" + id);
        }catch (Exception e){
            System.out.println("Dish is being used in a menu. Cannot delete.");
        }
    }

    private static void createMenu(CafeteriaRemote remote, Scanner scanner) throws Exception {
        System.out.print("Menu name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Menu description: ");
        String description = scanner.nextLine().trim();

        List<MenuComponentOptionDTO> options = remote.getAllMenuComponentOptions();
        if (options.isEmpty()) {
            System.out.println("No available components to add (all dishes/menus already belong to a parent menu).");
            System.out.println("Creating an empty menu...");
            MenuDTO created = remote.createMenu(name, description, List.of());
            System.out.println("Created menu: id=" + created.getId() + ", name=" + created.getName());
            return;
        }

        System.out.println("\nAvailable components (not already in a menu):");
        for (MenuComponentOptionDTO o : options) {
            System.out.println(" - " + o.getId() + " [" + o.getType() + "] " + o.getName());
        }

        System.out.println("\nEnter component IDs separated by comma (example: 1,2,5).");
        System.out.print("Selected IDs (empty = none): ");
        String line = scanner.nextLine().trim();

        List<Long> selectedIds = parseIds(line);

        MenuDTO created = remote.createMenu(name, description, selectedIds);
        System.out.println("Created menu: id=" + created.getId() + ", name=" + created.getName()
                + ", totalPrice=" + created.getTotalPrice());
    }

    private static void deleteMenu(CafeteriaRemote remote, Scanner scanner) throws Exception {
        try {
            System.out.print("Menu id to delete: ");
            Long id = readLong(scanner);

            remote.deleteMenu(id);
            System.out.println("Deleted menu id=" + id);
        } catch (Exception e) {
            System.out.println("Cannot delete menu: " + e.getMessage());
        }
    }

    private static List<Long> parseIds(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }

        String[] parts = input.split(",");
        List<Long> ids = new ArrayList<>();

        for (String p : parts) {
            String trimmed = p.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            ids.add(Long.parseLong(trimmed));
        }
        return ids;
    }

    private static void listMenus(CafeteriaRemote remote) throws Exception {
        List<MenuDTO> menus = remote.getAllMenus();
        if (menus.isEmpty()) {
            System.out.println("(no menus)");
            return;
        }

        for (MenuDTO m : menus) {
            System.out.println(m.getId() + " - " + m.getName() + " (total = " + m.getTotalPrice() + ")");
        }
    }

    private static void listOrders(CafeteriaRemote remote) throws Exception {
        List<OrderDTO> orders = remote.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("(no orders)");
            return;
        }

        for (OrderDTO o : orders) {
            System.out.println(o.getId()
                    + " | user=" + o.getUsername()
                    + " | status=" + o.getStatus()
                    + " | total=" + o.getTotalPrice());
        }
    }

    private static void deleteOrder(CafeteriaRemote remote, Scanner scanner) {
        try {
            System.out.print("Order id to delete: ");
            Long id = readLong(scanner);

            remote.deleteOrder(id);
            System.out.println("Deleted order id=" + id);
        } catch (Exception e) {
            // Simple UI message (server sends RemoteException message)
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    private static Long readLong(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    private static Double readDouble(Scanner scanner) {
        while (true) {
            String s = scanner.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}