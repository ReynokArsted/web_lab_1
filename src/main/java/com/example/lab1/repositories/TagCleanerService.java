package com.example.lab1.repositories;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TagCleanerService {
    private final TagRepository tagRepository;
    
    public TagCleanerService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }
    
    public void cleanupOrphanTags() {
        long orphansCount = tagRepository.countOrphanTags();
        if (orphansCount > 0) {
            tagRepository.deleteOrphanTags();
        }
    }
}

