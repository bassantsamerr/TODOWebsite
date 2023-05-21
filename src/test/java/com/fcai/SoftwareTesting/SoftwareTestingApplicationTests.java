package com.fcai.SoftwareTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.fcai.SoftwareTesting.todo.Todo;
import com.fcai.SoftwareTesting.todo.TodoController;
import com.fcai.SoftwareTesting.todo.TodoServiceImpl;

@SpringBootTest
class SoftwareTestingApplicationTests {
	TodoController todoController = new TodoController();

	@Test
	void contextLoads() {
	}

	@Test
	public void read_null_id_testing() {

		String id = null;
		assertNull(todoController.read(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.read(id).getStatusCode());
	}

	@Test
	public void read_empty_id_testing() {

		String id = "";
		assertNull(todoController.read(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.read(id).getStatusCode());
	}

	@Test
	public void read_empty_todos_testing() {

		TodoServiceImpl.todos = new ArrayList<Todo>();

		String id = "1";
		assertNull(todoController.read(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.read(id).getStatusCode());
	}

	@Test
	public void read_happy_senario_testing() {

		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);

		TodoServiceImpl.todos.add(todo1);
		TodoServiceImpl.todos.add(todo2);

		String id = "2";

		assertEquals(todo2, todoController.read(id).getBody());

		assertEquals(HttpStatus.OK, todoController.read(id).getStatusCode());
	}

	@Test
	public void delete_null_id_testing() {

		String id = null;
		assertNull(todoController.delete(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.delete(id).getStatusCode());
	}
 
	@Test
	public void delete_empty_id_testing() {

		String id = "";
		assertNull(todoController.delete(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.delete(id).getStatusCode());
	}

	@Test
	public void delete_empty_todos_testing() {

		TodoServiceImpl.todos = new ArrayList<Todo>();

		String id = "1";
		assertNull(todoController.delete(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.delete(id).getStatusCode());
	}

	@Test
	public void delete_happy_senario_testing() {

		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);
		

		TodoServiceImpl.todos.add(todo1);
		TodoServiceImpl.todos.add(todo2);

		int size = TodoServiceImpl.todos.size();

		String id = "2";
		
		assertEquals(HttpStatus.OK, todoController.delete(id).getStatusCode());

		assertEquals(size - 1, TodoServiceImpl.todos.size());
	}


}
