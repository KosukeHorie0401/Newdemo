package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Client;
import com.example.demo.entity.User;
import com.example.demo.service.ClientService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id).orElseThrow(() -> new RuntimeException("Client not found with id " + id));
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    @PostMapping("/saveWithUsers")
    public ResponseEntity<?> saveClientWithUsers(@RequestBody Client client) {
        try {
            Client savedClient = clientService.saveClientWithUsers(client);
            return ResponseEntity.ok(savedClient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody Client client) {
        try {
            if (client.getUsers() != null && !client.getUsers().isEmpty()) {
                for (User user : client.getUsers()) {
                    user.setClient(client);
                }
            }
            Client savedClient = clientService.createClient(client);
            return ResponseEntity.ok(savedClient);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        Client updatedClient = clientService.updateClient(id, clientDetails);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}