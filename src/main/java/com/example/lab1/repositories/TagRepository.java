package com.example.lab1.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.lab1.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTitle(String title);
    
    @Modifying
    @Query("DELETE FROM Tag t WHERE t.notes IS EMPTY OR SIZE(t.notes) = 0")
    void deleteOrphanTags();
    
    @Query("SELECT COUNT(t) FROM Tag t WHERE t.notes IS EMPTY OR SIZE(t.notes) = 0")
    long countOrphanTags();
}