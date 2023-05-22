package com.fcai.SoftwareTesting;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.fcai.SoftwareTesting.todo.Todo;
import com.fcai.SoftwareTesting.todo.TodoService;
import com.fcai.SoftwareTesting.todo.TodoController;
import com.fcai.SoftwareTesting.todo.TodoServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SoftwareTestingApplicationTests {

	TodoController todoController = new TodoController();

    TodoServiceImpl todoServiceImpl = new TodoServiceImpl();

	// the mock object that mocks the functions
	TodoService todoServiceMock = mock(TodoService.class);

	// used in delete and update testing functions
	// the object that used mocked funtion (read)
	TodoServiceImpl todoServiceImplMockedInjected = new TodoServiceImpl(todoServiceMock);


	// the mock object that mocks the functions (call mocked function)
	// used in the testing controller functions
	TodoServiceImpl todoServiceImplMock = mock(TodoServiceImpl.class);


	// used in the testing controller functions
	// call the controller real functions
	TodoController todoControllerMockedInjected = new TodoController(todoServiceImplMock);

	
	@Test
	void contextLoads() {
	}

	// Testing Todo Service Functions
	@Test
	public void read_null_id_testing() {
		String id = null;
		assertThrows(IllegalArgumentException.class,()->todoServiceImpl.read(id));
	}

	@Test
	public void read_empty_id_testing() {
		String id = "";
		assertThrows(IllegalArgumentException.class,()->todoServiceImpl.read(id));
	}

	@Test
	public void read_empty_todos_testing() {
		todoServiceImpl.todos = new ArrayList<Todo>();

		String id = "1";		
		assertThrows(IllegalArgumentException.class,()->todoServiceImpl.read(id));
	}

	@Test
	public void read_not_found_todo_testing() {
		todoServiceImpl.todos = new ArrayList<Todo>();

		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);

		todoServiceImpl.todos.add(todo1);
		todoServiceImpl.todos.add(todo2);

		String id = "3";		
		assertThrows(IllegalArgumentException.class,()->todoServiceImpl.read(id));
	}
	
	@Test
	public void read_happy_senario_testing() {
		todoServiceImpl.todos = new ArrayList<Todo>();

		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);

		todoServiceImpl.todos.add(todo1);
		todoServiceImpl.todos.add(todo2);

		String id = "2";

		assertEquals(todo2, todoServiceImpl.read(id));
	}

	@Test
	public void delete_happy_senario_testing() {
		Todo todo = new Todo();
        todo.setId("1");

		todoServiceImplMockedInjected.todos = new ArrayList<Todo>();
		todoServiceImplMockedInjected.todos.add(todo);
		
		int size = todoServiceImplMockedInjected.todos.size();

		when(todoServiceMock.read("1")).thenReturn(todo);

		todoServiceImplMockedInjected.delete("1");

		assertEquals(size - 1 , todoServiceImplMockedInjected.todos.size());
		
		verify(todoServiceMock).read("1");
    }

    @Test
    public void delete_throwing_exception_testing() {
        when(todoServiceMock.read("111")).thenThrow(IllegalArgumentException.class);

		assertThrows(IllegalArgumentException.class, () -> todoServiceImplMockedInjected.delete("111"));

		verify(todoServiceMock).read("111");
    }

	@Test
	public void update_happy_senario_testing() {
		Todo todo = new Todo();
        todo.setId("1");

		todoServiceImplMockedInjected.todos = new ArrayList<Todo>();
		todoServiceImplMockedInjected.todos.add(todo);
		
		Boolean completed = true; 

		when(todoServiceMock.read("1")).thenReturn(todo);

		assertEquals(completed, todoServiceImplMockedInjected.update("1", completed).isCompleted());
		
		verify(todoServiceMock).read("1");
    }

    @Test
    public void update_throwing_exception_testing() {
        when(todoServiceMock.read("111")).thenThrow(IllegalArgumentException.class);

		assertThrows(IllegalArgumentException.class, () -> todoServiceImplMockedInjected.update("111", true));

		verify(todoServiceMock).read("111");
    }

	
	// Testing Todo Controller Functions

	@Test
	public void read_controller_happy_senario_testing(){
		Todo todo = new Todo();
        todo.setId("1");

		when(todoServiceImplMock.read("1")).thenReturn(todo);

		assertEquals(HttpStatus.OK, todoControllerMockedInjected.read("1").getStatusCode());

		verify(todoServiceImplMock).read("1");
	}

	@Test
	public void read_controller_exception_testing(){
		when(todoServiceImplMock.read("1")).thenThrow(IllegalArgumentException.class);

		assertEquals(HttpStatus.BAD_REQUEST, todoControllerMockedInjected.read("1").getStatusCode());

		verify(todoServiceImplMock).read("1");
	}

	@Test
	public void delete_controller_happy_senario_testing(){		
		doNothing().when(todoServiceImplMock).delete("1");

		assertEquals(HttpStatus.OK, todoControllerMockedInjected.delete("1").getStatusCode());
		
		verify(todoServiceImplMock).delete("1");
	}

	@Test
	public void delete_controller_exception_testing(){
		doThrow(IllegalArgumentException.class).when(todoServiceImplMock).delete("1");

		assertEquals(HttpStatus.BAD_REQUEST, todoControllerMockedInjected.delete("1").getStatusCode());

		verify(todoServiceImplMock).delete("1");
	}

	@Test
	public void update_controller_happy_senario_testing(){
		Todo todo = new Todo();
        todo.setId("1");

		when(todoServiceImplMock.update("1", true)).thenReturn(todo);

		assertEquals(HttpStatus.OK, todoControllerMockedInjected.update("1", true).getStatusCode());

		verify(todoServiceImplMock).update("1", true);
	}

	@Test
	public void update_controller_exception_testing(){
		when(todoServiceImplMock.update("1", true)).thenThrow(IllegalArgumentException.class);

		assertEquals(HttpStatus.BAD_REQUEST, todoControllerMockedInjected.update("1", true).getStatusCode());

		verify(todoServiceImplMock).update("1", true);
	}

}
