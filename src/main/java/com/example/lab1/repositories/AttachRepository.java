package com.example.lab1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lab1.models.Attachment;

@Repository
public interface AttachRepository extends JpaRepository<Attachment, Long> {

}