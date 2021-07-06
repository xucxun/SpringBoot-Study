package com.example.service;

import com.example.domain.Authority;
import com.example.domain.User;
import com.example.repository.AuthorityRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * 用来通过用户名查询用户信息、权限
 * @author: xcx
 * @since: 2021/6/16
 */
@Service
public class UserService {
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    private String cacheName = "user::";
    //    用户名查询用户
    public User getUser(String username) {
        User user = null;
        Object o = redisTemplate.opsForValue().get(cacheName + "user_" + username);
        if (o != null) {
            user = (User) o;
        } else {
            user = userRepository.findByUsername(username);
            if (user != null) {
                redisTemplate.opsForValue().set(cacheName + "user_" + username, user);
            }
        }
        return user;
    }
    public List<Authority> getUserAuthority(String s) {
        List<Authority> authorityList = null;
        Object o = redisTemplate.opsForValue().get(cacheName + "authorities_" + s);
        if (o != null) {
            authorityList = (List<Authority>) o;
        } else {
            authorityList = authorityRepository.findAuthoritiesByUsername(s);
            if (authorityList.size() > 0) {
                redisTemplate.opsForValue().set(cacheName + "authorities_" + s, authorityList);
            }
        }
        return authorityList;
    }

//    /**
//     *  模拟使用唯一用户名查询用户基本信息
//     */
//    public User findByUsername(String username){
//        User user=new User();
//        user.setUsername(username);
//        user.setPassword(encoder.encode("123456"));
//        return user;
//    }
//    /**
//     * 模拟使用唯一用户名查询用户权限信息
//     */
//    public List<Authority> findAuthoritiesByUsername(String
//                                                             username){
//        List<Authority> authorities=new ArrayList<>();
//        Authority authority1=new Authority();
//        authority1.setAuthority("ROLE_common");
//        Authority authority2=new Authority();
//        authority2.setAuthority("ROLE_vip");
//        authorities.add(authority1);
//        authorities.add(authority2);
//        return authorities;
//    }
}
