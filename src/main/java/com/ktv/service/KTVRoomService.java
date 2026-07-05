package com.ktv.service;
import com.ktv.dao.KTVRoomDAO;
import com.ktv.entity.KTVRoom;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KTVRoomService {
    @Resource
    private KTVRoomDAO dao;

    public List<KTVRoom> listRooms() {
        return dao.getAllRooms();
    }


    public List<KTVRoom> list() {
        return dao.getRooms();
    }


    public boolean saveRoom(KTVRoom r) {
        return
                dao.addRoom(r);
    }
    public boolean deleteRoom(String id) {
        return dao.deleteRoom(id);
    }
    public boolean deleteBatchRoom(List<String> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return dao.executeUpdate("DELETE FROM KTV房间 WHERE 房间号 IN (" + qs + ")", ids.toArray()) > 0;
    }
    public List<KTVRoom> listByStatus() {
        return dao.getRoomsByStatus();
    }


    public KTVRoom getRoomDetail(String roomNo) {
        return dao.getRoomByNo(roomNo);
    }
}