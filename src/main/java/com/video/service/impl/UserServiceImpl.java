package com.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.video.entity.User;
import com.video.mapper.UserMapper;
import com.video.service.UserService;
import com.video.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);

        if (user == null) throw new RuntimeException("用户名或密码错误");
        if (user.getStatus() == 0) throw new RuntimeException("账号已被禁用");

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) throw new RuntimeException("用户名或密码错误");

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        user.setPassword(null);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    @Override
    public void register(String username, String password, String nickname) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        if (userMapper.selectCount(wrapper) > 0) throw new RuntimeException("用户名已存在");

        User user = new User();
        user.setUsername(username);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        user.setNickname(nickname != null ? nickname : username);
        user.setLevel(1);
        user.setCoin(0);
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public User getUserInfo(Long userId) { return userMapper.selectById(userId); }

    @Override
    public void updateUser(User user) { userMapper.updateById(user); }
}