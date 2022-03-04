package it.ms.backendassignment.repository;

import it.ms.backendassignment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Pageable pageable, Long postId);

    List<Comment> findByUser_Username(String username);
}
