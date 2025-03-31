package com.jade.detect.repository;

import com.jade.detect.model.DetectedObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectedObjectRepository extends JpaRepository<DetectedObject, Long> {
}
