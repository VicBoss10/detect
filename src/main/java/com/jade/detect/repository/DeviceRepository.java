package com.jade.detect.repository;


import com.jade.detect.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findByName(String name);
    List<Device> findByLocation(String location);

}