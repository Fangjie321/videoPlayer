package com.video.service;

import com.video.entity.User;
import java.util.Map;

public interface UserService {
    Map<String, Object> login(String username, String password);
    void register(String username, String password, String nickname);
    User getUserInfo(Long userId);
    void updateUser(User user);
}