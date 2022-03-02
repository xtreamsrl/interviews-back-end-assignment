package it.ms.backendassignment.service;

import it.ms.backendassignment.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SearchService {
    List<Post> searchPostsByKeyword(Integer pageNo, Integer pageSize, String keyword);
}
