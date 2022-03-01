package it.ms.backendassignment.repository;

import it.ms.backendassignment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
