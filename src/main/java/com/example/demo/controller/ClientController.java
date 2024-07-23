package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ClientDTO;
import com.example.demo.entity.Client;
import com.example.demo.entity.User;
import com.example.demo.service.ClientService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // @Autowired
    // private UserService userService;

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
    public ResponseEntity<?> saveClientWithUsers(@RequestBody ClientDTO clientDTO, HttpSession session, Authentication authentication) {
        System.out.println("Received ClientDTO: " + clientDTO);
        System.out.println("Address: " + clientDTO.getAddress());
        System.out.println("Session ID: " + session.getId());
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        System.out.println("Received request for saveClientWithUsers");
        System.out.println("Session ID: " + session.getId());
        System.out.println("User ID from session: " + session.getAttribute("userId"));
        System.out.println("User Role from session: " + session.getAttribute("userRole"));
        if (authentication != null) {
            System.out.println("Authenticated user: " + authentication.getName());
            System.out.println("User authorities: " + authentication.getAuthorities());
        } else {
            System.out.println("No authentication found");
        }
        try {
            Client savedClient = clientService.saveClientWithUsers(clientDTO);
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