package it.ms.backendassignment.service;

import it.ms.backendassignment.domain.Comment;
import it.ms.backendassignment.domain.Post;
import it.ms.backendassignment.dto.CommentDto;
import it.ms.backendassignment.dto.PostDto;
import it.ms.backendassignment.repository.CommentRepository;
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

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public List<PostDto> searchPostsByKeyword(Integer pageNo, Integer pageSize, String keyword) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());
        keyword = StringUtils.trim(keyword);
        Page<Post> posts = postRepository.findByTitleContainingOrBodyContainingAllIgnoreCase(keyword, keyword, pagination);
        List<PostDto> out = new ArrayList<>();
        return fromPostPageToDtoList(posts, out);
    }

    @Override
    @Transactional
    public List<PostDto> searchPostsByUser(Integer pageNo, Integer pageSize, String username) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());
        username = StringUtils.trim(username);
        Page<Post> posts = postRepository.findByUser_Username(username, pagination);
        List<PostDto> out = new ArrayList<>();
        return fromPostPageToDtoList(posts, out);
    }

    @Override
    @Transactional
    public List<CommentDto> searchCommentsByUser(Integer pageNo, Integer pageSize, String username) {
        Pageable pagination = PageRequest.of(pageNo, pageSize, Sort.by("updateDate").descending());
        username = StringUtils.trim(username);
        Page<Comment> comments = commentRepository.findByUser_Username(username, pagination);
        List<CommentDto> out = new ArrayList<>();
        if (comments.hasContent()) {
            out = comments.getContent()
                    .stream()
                    .map(CommentDto::new)
                    .collect(Collectors.toList());
        }
        return out;
    }


    private List<PostDto> fromPostPageToDtoList(Page<Post> posts, List<PostDto> out) {
        if (posts.hasContent()) {
            out = posts.getContent()
                    .stream()
                    .map(PostDto::new)
                    .collect(Collectors.toList());
        }
        return out;
    }
}
