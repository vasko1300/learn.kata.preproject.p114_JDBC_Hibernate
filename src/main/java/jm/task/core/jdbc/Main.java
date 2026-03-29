package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        Stream<User> users = Stream.of(
                new User("Bill", "Gates", (byte) 99),
                new User("Steve", "Ballmer", (byte) 69),
                new User("Timur", "Belokobylskiy", (byte) 35),
                new User("Vasilii", "Chapaev", (byte) 117)
        );
        users.forEach(user -> {userService.saveUser(
                user.getName(),
                user.getLastName(),
                user.getAge()
        );
            System.out.println("User с именем " + user.getName() + " добавлен в базу данных");
        });
        System.out.println();

        userService.getAllUsers().forEach(System.out::println);

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
