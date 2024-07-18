// package com.example.demo.service;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.demo.entity.Assignment;
// import com.example.demo.entity.Client;
// import com.example.demo.entity.Staff;
// import com.example.demo.repository.AssignmentRepository;
// import com.example.demo.repository.ClientRepository;
// import com.example.demo.repository.StaffRepository;

// @Service
// public class AssignmentService {

//     @Autowired
//     private AssignmentRepository assignmentRepository;

//     @Autowired
//     private StaffRepository staffRepository;

//     @Autowired
//     private ClientRepository clientRepository;

//     public Assignment saveAssignment(Assignment assignment) {
//         Staff staff = staffRepository.findById(assignment.getStaff().getId())
//             .orElseThrow(() -> new RuntimeException("Staff not found with id: " + assignment.getStaff().getId()));
//         // Client client = clientRepository.findById(assignment.getClient().getId())
//             .orElseThrow(() -> new RuntimeException("Client not found with id: " + assignment.getClient().getId()));

//         assignment.setStaff(staff);
//         assignment.setClient(client);

//         return assignmentRepository.save(assignment);
//     }

//     public List<Assignment> getAllAssignments() {
//         return assignmentRepository.findAll();
//     }

//     public Assignment getAssignmentById(Long id) {
//         return assignmentRepository.findById(id).orElseThrow();
//     }

//     public Assignment updateAssignment(Long id, Assignment assignmentDetails) {
//         Assignment assignment = assignmentRepository.findById(id).orElseThrow();

//         Staff staff = staffRepository.findById(assignmentDetails.getStaff().getId()).orElseThrow();
//         Client client = clientRepository.findById(assignmentDetails.getClient().getId()).orElseThrow();

//         assignment.setStaff(staff);
//         assignment.setClient(client);
//         assignment.setCreatedAt(assignmentDetails.getCreatedAt());
//         assignment.setUpdatedAt(assignmentDetails.getUpdatedAt());

//         return assignmentRepository.save(assignment);
//     }

//     public void deleteAssignment(Long id) {
//         Assignment assignment = assignmentRepository.findById(id).orElseThrow();
//         assignmentRepository.delete(assignment);
//     }
// }
