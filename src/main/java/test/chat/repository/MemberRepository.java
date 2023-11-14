package test.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.chat.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByUsername(String username);
}
