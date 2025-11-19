package com.nixo.ejb.impl;

import com.nixo.entity.SharerRequests;
import java.util.List;
import jakarta.ejb.Local;

@Local
public interface AdminBeanLocal {

    List<SharerRequests> getPendingRequests();

    void approveRequest(int requestId);

    void rejectRequest(int requestId);

    void changeUserRole(int userId, int newRoleId);
}
