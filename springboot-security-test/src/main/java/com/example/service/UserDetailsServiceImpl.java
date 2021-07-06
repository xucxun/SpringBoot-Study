package com.example.service;

import com.example.domain.Authority;
import com.example.domain.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //获取数据库用户基本信息
        User dbUser = userService.getUser(s);
        //获取数据库用户权限信息
        List<Authority> authorities = userService.getUserAuthority(s);
        //将信息转换为UserDetails对象
        return changeToUserDetails(dbUser,authorities);
    }
    private UserDetails changeToUserDetails(User dbUser,List<Authority> authorities) throws UsernameNotFoundException{
        if(dbUser!=null) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            for (Authority authority : authorities) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
                authorityList.add(grantedAuthority);
            }
            //创建UserDetails对象，设置用户名、密码和权限
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(dbUser.getUsername(),
                    dbUser.getPassword(), authorityList);
            return userDetails;
        }else {
            // 如果查询的用户不存在（用户名不存在），必须抛出此异常
            throw new UsernameNotFoundException("当前用户不存在！");
        }
    }

}
