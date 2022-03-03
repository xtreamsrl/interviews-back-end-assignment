package it.ms.backendassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    @NonNull
    private String username;

    @Column(columnDefinition = "TEXT")
    @NonNull
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @ToString.Exclude
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @ToString.Exclude
    private Set<Comment> comments;

    public void addPost(Post post) {
        post.setUser(this);

        if (Objects.isNull(this.posts)) {
            this.posts = new HashSet<>();
        }

        this.posts.add(post);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
        post.setUser(null);
    }

    public void addComment(Comment comment) {
        comment.setUser(this);

        if (Objects.isNull(this.posts)) {
            this.comments = new HashSet<>();
        }

        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
