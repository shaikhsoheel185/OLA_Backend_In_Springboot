package com.app.repository;

import com.app.modal.Driver;
import com.app.modal.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Component
public interface DriverRepository extends JpaRepository<Driver, Integer> {

    public Driver findByEmail(String email);
    @Query("SELECT R FROM Ride R WHERE R.status=REQUESTED AND R.driver.id=:driverId")
    public List<Ride> getAllocatedRides(@Param("driverId") Integer driverId);


    @Query("SELECT R FROM Ride R where R.status=COMPLETED AND R.driver.Id=:driverId")
    public List<Ride> getCompletedRides(@Param("driverId")Integer driverId);

}
