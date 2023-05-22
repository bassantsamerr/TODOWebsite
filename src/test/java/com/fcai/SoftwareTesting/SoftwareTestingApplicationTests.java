package com.fcai.SoftwareTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.fcai.SoftwareTesting.todo.Todo;
import com.fcai.SoftwareTesting.todo.TodoController;
import com.fcai.SoftwareTesting.todo.TodoService;
import com.fcai.SoftwareTesting.todo.TodoServiceImpl;

@SpringBootTest
class SoftwareTestingApplicationTests {
	TodoController todoController = new TodoController();

	
    TodoServiceImpl todoServiceImpl = new TodoServiceImpl();
	
	@Test
	void contextLoads() {
	}

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

		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);

		todoServiceImpl.todos.add(todo1);
		todoServiceImpl.todos.add(todo2);

		String id = "2";

		assertEquals(todo2, todoServiceImpl.read(id));
	}

	@Test
	public void delete_happy_senario_testing() {

		TodoService todoServiceMock = mock(TodoService.class);
		todoServiceImpl = new TodoServiceImpl(todoServiceMock);

		Todo todo = new Todo();
        todo.setId("1");

		todoServiceImpl.todos = new ArrayList<Todo>();
		todoServiceImpl.todos.add(todo);
		int size = todoServiceImpl.todos.size();

		when(todoServiceMock.read("1")).thenReturn(todo);

		todoServiceImpl.delete("1");

		assertEquals(size - 1 , todoServiceImpl.todos.size());
		
		verify(todoServiceMock).read("1");
    }

    @Test
    public void delete_null_exception_testing() {

		TodoService todoServiceMock = mock(TodoService.class);
		todoServiceImpl = new TodoServiceImpl(todoServiceMock);
		
        when(todoServiceMock.read("111")).thenThrow(new IllegalArgumentException("Todo id cannot be null"));

		assertThrows(IllegalArgumentException.class, () -> todoServiceImpl.delete("111"));

		verify(todoServiceMock).read("111");
    }

	@Test
    public void delete_empty_exception_testing() {

		TodoService todoServiceMock = mock(TodoService.class);
		todoServiceImpl = new TodoServiceImpl(todoServiceMock);
		
        when(todoServiceMock.read("111")).thenThrow(new IllegalArgumentException("Todo id cannot be empty"));

		assertThrows(IllegalArgumentException.class, () -> todoServiceImpl.delete("111"));

		verify(todoServiceMock).read("111");
    }

	@Test
    public void delete_not_found_exception_testing() {

		TodoService todoServiceMock = mock(TodoService.class);
		todoServiceImpl = new TodoServiceImpl(todoServiceMock);
		
        when(todoServiceMock.read("111")).thenThrow(new IllegalArgumentException("Todo not found"));

		assertThrows(IllegalArgumentException.class, () -> todoServiceImpl.delete("111"));

		verify(todoServiceMock).read("111");
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

		todoServiceImpl.todos = new ArrayList<Todo>();

		String id = "1";
		assertNull(todoController.delete(id).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.delete(id).getStatusCode());
	}

	/*@Test
	public void delete_happy_senario_testing() {

		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);
		

		todoServiceImpl.todos.add(todo1);
		todoServiceImpl.todos.add(todo2);

		int size = todoServiceImpl.todos.size();

		String id = "2";
		
		assertEquals(HttpStatus.OK, todoController.delete(id).getStatusCode());

		assertEquals(size - 1, todoServiceImpl.todos.size());
	}
*/


	@Test
	public void update_null_id_testing() {

		String id = null;
		assertNull(todoController.update(id, true).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.update(id, true).getStatusCode());
	}
 
	@Test
	public void update_empty_id_testing() {

		String id = "";
		assertNull(todoController.update(id, true).getBody());
		assertEquals(HttpStatus.BAD_REQUEST, todoController.update(id, true).getStatusCode());
	}

	@Test
	public void update_id_not_found_testing() {

		todoServiceImpl.todos = new ArrayList<Todo>();
		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);
		

		todoServiceImpl.todos.add(todo1);
		todoServiceImpl.todos.add(todo2);

		String id = "3";

		assertNull(todoController.update(id, true).getBody());
		
		assertEquals(HttpStatus.BAD_REQUEST, todoController.update(id, true).getStatusCode());

		
	}

	@Test
	public void update_happy_senario_todos_testing() {

		todoServiceImpl.todos = new ArrayList<Todo>();
		Todo todo1 = new Todo("1", "title1", "description1", true);
		Todo todo2 = new Todo("2", "title2", "description2", true);
		todoServiceImpl.todos.add(todo1);
		todoServiceImpl.todos.add(todo2);

		String id = "1";
		boolean completed = false;
		assertEquals(HttpStatus.OK, todoController.update(id, completed).getStatusCode());
	}

}
