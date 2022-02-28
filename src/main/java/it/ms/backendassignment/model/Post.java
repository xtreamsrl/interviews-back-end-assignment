package it.ms.backendassignment.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate creationDate;

    @NonNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate updateDate;

    @OneToMany(mappedBy = "post")
    @ToString.Exclude
    private Set<Comment> comments;

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
