package com.ktv.service;
import com.ktv.dao.CustomerDAO;
import com.ktv.entity.Customer;
import com.ktv.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Resource
    private CustomerDAO dao;

    public boolean register(Customer c) {
        if (!validate(c)) return false;
        if (dao.getCustomerById(c.getAccount()) != null)
            throw new BizException("Customer already exists");
        return dao.addCustomer(c);
    }

    public Customer getProfile(String customerId) {
        return dao.getCustomerById(customerId);
    }

    public boolean updatePhone(String customerId, String newPhone) {
        Customer c = dao.getCustomerById(customerId);
        if (c == null) throw new BizException("Customer not found");
        c.setPhone(newPhone);
        return dao.updateCustomer(c);
    }

    private boolean validate(Customer c) {
        if (c == null || c.getAccount() == null || c.getName() == null) return false;
        // 只认中文
        if (!"男".equals(c.getGender()) && !"女".equals(c.getGender()))
        if (c.getPhone() != null && !c.getPhone().matches("^1[3-9]\\d{9}$"))
            throw new BizException("Invalid phone");
        return true;
    }


    /* 新增 or 修改（主键存在即改） */
    public boolean saveCustomer(Customer c) {
        return dao.getCustomerById(c.getAccount()) == null
                ? dao.addCustomer(c)
                : dao.updateCustomer(c);
    }
    /* 单删 */
    public boolean deleteCustomer(String account) {
        return dao.deleteCustomer(account);
    }
    /* 批删 */
    public boolean deleteBatchCustomer(List<String> ids) {
        if (ids.isEmpty()) return true;
        String qs = ids.stream().map(i -> "?").collect(Collectors.joining(","));
        return dao.executeUpdate("DELETE FROM 顾客 WHERE 顾客账号 IN (" + qs + ")", ids.toArray()) > 0;
    }
}