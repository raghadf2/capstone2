package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Integer> {
    List<WaitingList> findBySpaceIdAndDateOrderByPositionAsc(Integer spaceId, LocalDate date);
}
