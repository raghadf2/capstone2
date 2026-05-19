package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.Availability;
import com.example.spaceflow.Model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability,Integer> {
    Availability findAvailabilityById(Integer id);
    Availability findAvailabilityBySpaceIdAndDay(Integer spaceId,String day);

}
