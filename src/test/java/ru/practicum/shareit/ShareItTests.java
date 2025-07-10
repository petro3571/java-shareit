package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemRepositoryImpl;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.user.*;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ShareItTests {
	UserController userController;
	ItemController itemController;

	@BeforeEach
	public void beaforeEach() {
		UserRepository userRepository = new UserRepositoryImpl();
		userController = new UserController(new UserServiceImpl(userRepository));
		itemController = new ItemController(new ItemServiceImpl(userRepository, new ItemRepositoryImpl()));
	}

	@Test
	void contextLoads() {
	}

	@Test
	void shouldRunWorkWithUsers() {
		NewUserRequest user = new NewUserRequest();
		user.setName("test");
		user.setEmail("test@example.com");
		userController.saveNewUser(user);
		assertEquals("test", user.getName());

		UpdateUserRequest updateUser = new UpdateUserRequest();
		updateUser.setName("Petro");
		updateUser.setEmail("dimapetro357@gmail.com");
		long userId = 1;
		userController.updateUser(updateUser, userId);
		assertEquals("dimapetro357@gmail.com", userController.getUser(userId).getEmail());

		UpdateUserRequest newUser = new UpdateUserRequest();
		newUser.setName("test1");
		newUser.setEmail("test1@gmail.com");
		long notFoundId = 999L;
		assertThrows(IndexOutOfBoundsException.class, () -> userController.updateUser(newUser, notFoundId));

		userController.deleteUser(userId);
		assertEquals(new ArrayList<>(), userController.getAllUsers());
	}

	@Test
	void shouldRunWorkWithItems() {
		NewUserRequest user = new NewUserRequest();
		user.setName("test");
		user.setEmail("test@example.com");
		UserDto userDto = userController.saveNewUser(user);
		assertEquals("test", userController.getAllUsers().stream().findFirst().get().getName());

		NewItemRequest newItem = new NewItemRequest();
		newItem.setName("Test");
		newItem.setDescription("TestDescription");
		newItem.setAvailable(true);
		ItemDto itemDto = itemController.postItem(userDto.getId(), newItem);

		assertEquals("Test", itemController.getAllUserItems(userDto.getId()).stream().findFirst().get().getName());

		UpdateItemRequest updItem = new UpdateItemRequest();
		updItem.setName("Drel");
		updItem.setDescription("DrelDiscription");
		updItem.setAvailable(true);
		itemController.patchItem(userDto.getId(), itemDto.getId(), updItem);
		assertEquals("Drel", itemController.getUserItem(userDto.getId(),itemDto.getId()).getName());

		List<ItemDto> searchItems = itemController.searchItems(userDto.getId(), "DREL");

		assertEquals(1, searchItems.size());
	}
}