package it.ms.backendassignment.service;

import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.model.Post;
import it.ms.backendassignment.repository.PostRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private PostRepository postRepository;

    @Override
    @Transactional
    public List<PostDto> searchPostsByKeyword(Integer pageNo, Integer pageSize, String keyword) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());
        keyword = StringUtils.trim(keyword);
        Page<Post> posts = postRepository.findByTitleContainingOrBodyContainingAllIgnoreCase(keyword, keyword, pagination);
        List<PostDto> out = new ArrayList<>();
        if (posts.hasContent()){
            out = posts.getContent()
                    .stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
        }
        return out;
    }
}
