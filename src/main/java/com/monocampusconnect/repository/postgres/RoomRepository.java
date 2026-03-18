package com.monocampusconnect.repository.postgres;

import com.monocampusconnect.model.postgres.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}

