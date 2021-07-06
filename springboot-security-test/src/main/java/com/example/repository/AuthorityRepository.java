package com.example.repository;

import com.example.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户权限接口，通过用户名查询用户对应的权限
 * @author: xcx
 * @since: 2021/6/16
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    @Query(value = "select a.* from t_user c,t_authority a,t_user_authority ca where ca.user_id=c.id and ca" +
            ".authority_id=a.id and c.username=?1", nativeQuery = true)
    List<Authority> findAuthoritiesByUsername(String username);
}
