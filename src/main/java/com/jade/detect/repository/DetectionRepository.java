package com.jade.detect.repository;

import com.jade.detect.model.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetectionRepository extends JpaRepository<Detection, Long> {
    List<Detection> findByDeviceId(Long deviceId);
}

