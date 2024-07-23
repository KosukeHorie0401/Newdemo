package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.UserDTO;
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
    private PasswordEncoder passwordEncoder;

    // @Autowired
    // private UserService userService;

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
    public Client saveClientWithUsers(ClientDTO clientDTO) {
        Client client = convertToEntity(clientDTO);
        Client savedClient = clientRepository.save(client);
        
        if (clientDTO.getUsers() != null) {
            for (UserDTO userDTO : clientDTO.getUsers()) {
                User user = convertToUserEntity(userDTO);
                user.setClient(savedClient);
                userRepository.save(user);
            }
        }
        
        return savedClient;
    }

    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setCompanyName(clientDTO.getCompanyName());
        client.setRepresentativeName(clientDTO.getRepresentativeName());
        client.setContactPersonName(clientDTO.getContactPersonName());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        client.setEmail(clientDTO.getEmail());
        client.setWebsiteUrl(clientDTO.getWebsiteUrl());
        client.setContractStartDate(clientDTO.getContractStartDate());
        client.setMeetingHistory(clientDTO.getMeetingHistory());
        client.setReferralRecord(clientDTO.getReferralRecord());
        client.setPriority(Client.Priority.valueOf(clientDTO.getPriority()));
        return client;
    }

    private User convertToUserEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // パスワードをエンコード
        user.setRole(userDTO.getRole());
        return user;
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