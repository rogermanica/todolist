package com.todolist.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class TodoListController {

    private List<Task> todoList = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(TodoListController.class, args);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok(todoList);
    }

    @PostMapping("/tasks")
    public ResponseEntity<Task> criarTask(@RequestBody Task task) {
        todoList.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> atualizarTask(@PathVariable Long id, @RequestBody Task task) {
        Task taskExist = findTaskById(id);
        if (taskExist != null) {
            taskExist.setDescricao(task.getDescricao());
            taskExist.setConcluida(task.isConcluida());
            return ResponseEntity.ok(taskExist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> excluirTask(@PathVariable Long id) {
        Task task = findTaskById(id);
        if (task != null) {
            todoList.remove(task);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Task findTaskById(Long id) {
        return todoList.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}