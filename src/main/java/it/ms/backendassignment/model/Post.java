package it.ms.backendassignment.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NonNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NonNull
    private String body;

    @NonNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @NonNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "post")
    @ToString.Exclude
    @JsonManagedReference
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public void addComment(Comment comment) {
        comment.setPost(this);

        if (Objects.isNull(this.comments)) {
            this.comments = new HashSet<>();
        }

        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPost(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Post post = (Post) o;
        return id != null && Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
