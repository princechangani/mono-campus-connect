package com.monocampusconnect.controller.mongo;

import com.monocampusconnect.model.mongo.ChatMessageDocument;
import com.monocampusconnect.service.mongo.ChatMessageDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents/chat-messages")
public class ChatMessageDocumentController {

    private final ChatMessageDocumentService chatMessageDocumentService;

    public ChatMessageDocumentController(ChatMessageDocumentService chatMessageDocumentService) {
        this.chatMessageDocumentService = chatMessageDocumentService;
    }

    @PostMapping
    public ResponseEntity<ChatMessageDocument> create(@RequestBody ChatMessageDocument message) {
        return new ResponseEntity<>(chatMessageDocumentService.create(message), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ChatMessageDocument>> getAll() {
        return ResponseEntity.ok(chatMessageDocumentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatMessageDocument> getById(@PathVariable String id) {
        return ResponseEntity.ok(chatMessageDocumentService.getById(id));
    }

    @GetMapping("/public-id")
    public ResponseEntity<ChatMessageDocument> getByPublicId(
            @RequestParam String tenantId,
            @RequestParam String publicId) {
        return ResponseEntity.ok(chatMessageDocumentService.getByPublicId(tenantId, publicId));
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<ChatMessageDocument>> getByChannelId(@PathVariable String channelId) {
        return ResponseEntity.ok(chatMessageDocumentService.getByChannelId(channelId));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<ChatMessageDocument>> getBySenderId(@PathVariable Long senderId) {
        return ResponseEntity.ok(chatMessageDocumentService.getBySenderId(senderId));
    }

    @GetMapping("/tenant-channel")
    public ResponseEntity<List<ChatMessageDocument>> getByTenantAndChannel(
            @RequestParam String tenantId,
            @RequestParam String channelId) {
        return ResponseEntity.ok(chatMessageDocumentService.getByTenantAndChannel(tenantId, channelId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatMessageDocument> update(@PathVariable String id, @RequestBody ChatMessageDocument message) {
        return ResponseEntity.ok(chatMessageDocumentService.update(id, message));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        chatMessageDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

