package com.monocampusconnect.controller.postgres;

import com.monocampusconnect.model.postgres.Room;
import com.monocampusconnect.service.postgres.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody Room room) {
        return new ResponseEntity<>(roomService.create(room), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAll() {
        return ResponseEntity.ok(roomService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getById(id));
    }

    @GetMapping("/room-number/{roomNumber}")
    public ResponseEntity<Room> getByRoomNumber(@PathVariable String roomNumber) {
        return ResponseEntity.ok(roomService.getByRoomNumber(roomNumber));
    }

    @GetMapping("/building/{building}")
    public ResponseEntity<List<Room>> getByBuilding(@PathVariable String building) {
        return ResponseEntity.ok(roomService.getByBuilding(building));
    }

    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<Room>> getByRoomType(@PathVariable String roomType) {
        return ResponseEntity.ok(roomService.getByRoomType(roomType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id, @RequestBody Room room) {
        return ResponseEntity.ok(roomService.update(id, room));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

