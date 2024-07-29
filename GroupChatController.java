package com.mom_management.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mom_management.model.GroupChat;

import com.mom_management.service.GroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupChatController {

	@Autowired
    private GroupService service;

    @PostMapping
    public ResponseEntity<GroupChat> create(@RequestBody GroupChat entity) {
    	GroupChat savedEntity = service.save(entity);
        return ResponseEntity.ok(savedEntity);
    }

    @GetMapping
    public ResponseEntity<List<GroupChat>> getAll() {
        List<GroupChat> entities = service.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupChat> getById(@PathVariable Long id) {
        Optional<GroupChat> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<GroupChat>> getByName(@PathVariable String name) {
        List<GroupChat> entities = service.findByName(name);
        return ResponseEntity.ok(entities);
    }
    
    @GetMapping("/messages/{name}")
    public ResponseEntity<List<String>> getMessagesByGroupName(@PathVariable String name) {
        List<String> messages = service.findMessagesByGroupName(name);
        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(messages);
    }
    
    @GetMapping("members/{members}")
    public ResponseEntity<List<GroupChat>> getMemberMessages(@PathVariable Long members) {
        // Retrieve all messages for the specified member from the database
        List<GroupChat> messages = service.findMemberMessages(members);
        
        // Return the messages as the API response
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupChat> update(@PathVariable Long id, @RequestBody GroupChat entity) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        entity.setId(id);
        GroupChat updatedEntity = service.save(entity);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{groupId}/message")
    public ResponseEntity<GroupChat> sendMessage(@PathVariable Long groupId, @RequestBody String message) {
        Optional<GroupChat> groupChatOptional = service.findById(groupId);
        if (!groupChatOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        GroupChat groupChat = groupChatOptional.get();
        groupChat.setMessage(message);
        groupChat.setDate(LocalDateTime.now()); // Update the timestamp

        GroupChat updatedGroupChat = service.save(groupChat);
        return ResponseEntity.ok(updatedGroupChat);
    }
    
    @PostMapping("/{groupName}/message")
    public ResponseEntity<GroupChat> sendMessageByName(@PathVariable String groupName, @RequestBody String message) {
        List<GroupChat> groupChats = service.findByName(groupName);
        if (groupChats.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("groupChats"+groupChats);
        // Assuming one group for a given name
        GroupChat groupChat = groupChats.get(0);
        List<Long> members = groupChat.getMembers();
        System.out.println("groupChat"+groupChat);
        groupChat.setMembers(members);
        groupChat.setMessage(message);
        groupChat.setDate(LocalDateTime.now()); // Update the timestamp

        GroupChat updatedGroupChat = service.save(groupChat);
        return ResponseEntity.ok(updatedGroupChat);
    }

//    @PostMapping
//    public GroupDao createGroup(@RequestBody GroupDaoDTO request) {
//        return groupService.createGroup(request.getName(), request.getMemberIds(), request.getSenderId(), request.getMessages(), request.getStatus());
//    }

//    @PostMapping("/{groupId}/members/{userId}")
//    public GroupDao addMemberToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
//        return groupService.addMemberToGroup(groupId, userId);
//    }

//    @GetMapping
//    public List<GroupDao> getAllGroups() {
//        return groupService.getAllGroups();
//    }
//
//    @GetMapping("/{groupId}")
//    public Optional<GroupDao> getGroupById(@PathVariable Long groupId) {
//        return groupService.getGroupById(groupId);
//    }
//
//    @GetMapping("/name/{name}")
//    public List<GroupDao> getGroupByName(@PathVariable String name) {
//        return groupService.getGroupByName(name);
//    }
//
//    @GetMapping("/{groupId}/messages")
//    public List<String> getMessagesByGroupId(@PathVariable Long groupId) {
//        return groupService.getMessagesByGroupId(groupId);
//    }
//
//    @GetMapping("/name/{name}/messages")
//    public List<String> getMessagesByGroupName(@PathVariable String name) {
//        return groupService.getMessagesByGroupName(name);
//    }
}

