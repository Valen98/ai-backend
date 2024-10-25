package com.booleanuk.controller;

import com.booleanuk.model.Todo;
import com.booleanuk.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.saveOrUpdate(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent()) {
            Todo updatedTodo = todo.get();
            updatedTodo.setTitle(todoDetails.getTitle());
            updatedTodo.setComplete(todoDetails.isComplete());
            return ResponseEntity.ok(todoService.saveOrUpdate(updatedTodo));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable Long id) {
        if (todoService.getTodoById(id).isPresent()) {
            todoService.deleteTodoById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}