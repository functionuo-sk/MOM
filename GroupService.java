package com.mom_management.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mom_management.model.GroupChat;

import com.mom_management.repository.GroupRepository;

@Service
public class GroupService {


	@Autowired
    private GroupRepository repository;

	public GroupChat save(GroupChat entity) {
        return repository.save(entity);
    }

    public List<GroupChat> findAll() {
        return repository.findAll();
    }

    public Optional<GroupChat> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<GroupChat> findByName(String name) {
        return repository.findByName(name);
    }
    
    public List<String> findMessagesByGroupName(String name) {
        List<String> messages = repository.findMessageByGroupName(name);
        return messages != null ? messages : Collections.emptyList();
    }
    
    public List<GroupChat> findMemberMessages(Long members) {
        // Query the database to find messages for the specified member
        List<GroupChat> groupChats = repository.findByMembers(members);
        
        // Extract messages from the retrieved group chats
        List<String> messages = new ArrayList<>();
        for (GroupChat groupChat : groupChats) {
            messages.add(groupChat.getMessage());
        }
        
        return groupChats;
    }
	    
//	    GroupDao group = new GroupDao();

//	    @Transactional
//	    public GroupDao createGroup(String name, List<Long> memberIds, Long senderId, String messages, Status status) {
//	    	//GroupDao group = new GroupDao();
//	        group.setName(name);
//	        group.setSenderId(userDaoRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found")));
//	        group.setMessages(messages);
//	        group.setStatus(status);
//
//	        group = groupRepository.save(group);
//
//	        List<GroupMember> members = memberIds.stream()
//	            .map(memberId -> {
//	                UserDao user = userDaoRepository.findById(memberId).orElseThrow(() -> new RuntimeException("User not found"));
//	                GroupMember member = new GroupMember();
//	                member.setGroup(group);
//	                member.setUser(user);
//	                member.setGroupName(group);
//	                return member;
//	            })
//	            .collect(Collectors.toList());
//
//	        groupMemberRepository.saveAll(members);
//
//	        group.setMembers(members);
//
//	        return group;
//	    }

//	    public GroupDao createGroup(String name,List<Long> memberIds, Long senderId, String messages, Status status) {
//	        GroupDao group = new GroupDao();
//	        group.setName(name);
//	        group.setStatus(status);
//	        group.setMessages(messages);
//	        System.out.println("name"+name);
//	        List<UserDao> members = userDaoRepository.findAllById(memberIds);
//	        group.setMembers(members);
//
//	        UserDao sender = userDaoRepository.findById(senderId).orElseThrow(() -> new RuntimeException("User not found"));
//	        group.setSenderId(sender);
//
//	        return groupRepository.save(group);
//	    }

//	    public GroupDao addMemberToGroup(Long groupId, Long userId) {
//	        GroupDao group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
//	        UserDao user = userDaoRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//	        
//	        group.getMembers().add(user);
//	        return groupRepository.save(group);
//	    }

//	    public List<GroupDao> getAllGroups() {
//	        return groupRepository.findAll();
//	    }
//
//	    public Optional<GroupDao> getGroupById(Long groupId) {
//	        return groupRepository.findById(groupId);
//	    }
//
//	    public List<GroupDao> getGroupByName(String name) {
//	        return groupRepository.findByName(name);
//	    }
//
//	    public List<String> getMessagesByGroupId(Long groupId) {
//	        Optional<GroupDao> group = groupRepository.findById(groupId);
//	        return group.map(g -> List.of(g.getMessages().split("\n"))).orElseThrow(() -> new RuntimeException("Group not found"));
//	    }
//
//	    public List<String> getMessagesByGroupName(String name) {
//	        List<GroupDao> groups = groupRepository.findByName(name);
//	        return groups.stream()
//	                .flatMap(g -> List.of(g.getMessages().split("\n")).stream())
//	                .collect(Collectors.toList());
//	    }
}

