package com.bcsd.community.repository;

import com.bcsd.community.entity.Board;
import com.bcsd.community.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardName(String email);
}
