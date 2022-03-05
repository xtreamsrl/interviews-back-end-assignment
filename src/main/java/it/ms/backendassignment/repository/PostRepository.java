package it.ms.backendassignment.repository;

import it.ms.backendassignment.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    Page<Post> findByTitleContainingOrBodyContainingAllIgnoreCase(String title, String body, Pageable pageable);

    Page<Post> findByUser_Username(String username, Pageable pageable);
}
