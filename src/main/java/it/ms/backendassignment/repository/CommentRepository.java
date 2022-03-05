package it.ms.backendassignment.repository;

import it.ms.backendassignment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);

    Page<Comment> findByUser_Username(String username, Pageable pageable);
}
