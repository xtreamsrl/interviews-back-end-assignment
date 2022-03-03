package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.PostDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SearchService {
    List<PostDto> searchPostsByKeyword(Integer pageNo, Integer pageSize, String keyword);
}
