package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Client;
import com.example.demo.entity.User;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        System.out.println("Received client data: " + client);
        Client savedClient = clientRepository.save(client);
        System.out.println("Saved client: " + savedClient);
        return savedClient;
    }

    @Transactional
    public Client saveClientWithUsers(Client client) {
        Client savedClient = createClient(client);
        if (client.getUsers() != null) {
            for (User user : client.getUsers()) {
                user.setClient(savedClient);
                userService.createUser(user);
            }
        }
        return savedClient;
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found with id " + id));
        client.setCompanyName(clientDetails.getCompanyName());
        client.setRepresentativeName(clientDetails.getRepresentativeName());
        client.setContactPersonName(clientDetails.getContactPersonName());
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}