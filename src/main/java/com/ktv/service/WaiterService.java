package com.ktv.service;
import com.ktv.dao.WaiterDAO;
import com.ktv.entity.Waiter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaiterService {
    @Resource
    private WaiterDAO dao;

    public List<Waiter> listAll() {
        return dao.getAll();
    }
    public boolean saveWaiter(Waiter w) {
        return dao.getById(w.getWaiterId()) == null ?
                dao.insert(w) : dao.update(w);
    }
    public boolean deleteWaiter(String id) {
        return dao.delete(id);
    }
    public boolean deleteBatchWaiter(List<String> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return dao.executeUpdate("DELETE FROM 服务员 WHERE 工号 IN (" + qs + ")", ids.toArray()) > 0;
    }

    public Waiter getContact(String waiterId) {
        return dao.getById(waiterId);
    }
}