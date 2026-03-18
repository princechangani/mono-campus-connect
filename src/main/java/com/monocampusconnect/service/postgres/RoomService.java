package com.monocampusconnect.service.postgres;

import com.monocampusconnect.exception.ApiException;
import com.monocampusconnect.model.postgres.Room;
import com.monocampusconnect.repository.postgres.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Create a new room
     */
    public Room create(Room room) {
        if (room.getRoomPublicId() == null) {
            room.setRoomPublicId(UUID.randomUUID());
        }
        return roomRepository.save(room);
    }

    /**
     * Get all rooms
     */
    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    /**
     * Get room by ID
     */
    public Room getById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Room not found", 404));
    }

    /**
     * Get room by room number
     */
    public Room getByRoomNumber(String roomNumber) {
        return roomRepository.findAll().stream()
                .filter(r -> roomNumber.equals(r.getRoomNumber()))
                .findFirst()
                .orElseThrow(() -> new ApiException("Room not found", 404));
    }

    /**
     * Update a room
     */
    public Room update(Long id, Room room) {
        Room existing = getById(id);
        if (room.getBuilding() != null)    existing.setBuilding(room.getBuilding());
        if (room.getFloor() != null)       existing.setFloor(room.getFloor());
        if (room.getRoomNumber() != null)  existing.setRoomNumber(room.getRoomNumber());
        if (room.getName() != null)        existing.setName(room.getName());
        if (room.getCapacity() != null)    existing.setCapacity(room.getCapacity());
        if (room.getRoomType() != null)    existing.setRoomType(room.getRoomType());
        return roomRepository.save(existing);
    }

    /**
     * Delete a room
     */
    public void delete(Long id) {
        Room room = getById(id);
        roomRepository.delete(room);
    }

    /**
     * Get rooms by building
     */
    public List<Room> getByBuilding(String buildingName) {
        return roomRepository.findAll().stream()
                .filter(r -> buildingName.equals(r.getBuilding()))
                .toList();
    }

    /**
     * Get rooms by type
     */
    public List<Room> getByRoomType(String roomType) {
        return roomRepository.findAll().stream()
                .filter(r -> roomType.equals(r.getRoomType()))
                .toList();
    }
}

