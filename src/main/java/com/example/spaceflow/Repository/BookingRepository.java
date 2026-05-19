package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {
    Booking findBookingById(Integer id);
    List<Booking> findBookingBySpaceId(Integer spaceId);
    List<Booking> findBookingByUserId(Integer userId);

    @Query("select b from Booking b where b.userId = :userId order by b.bookingDate desc")
    List<Booking> findUserBookingsSorted(@Param("userId") Integer userId);

    @Query("select b from Booking b where b.spaceId = :spaceId and b.bookingDate = :date and b.startTime < :endTime and b.endTime > :startTime")
    List<Booking> findOverlappingBookings(@Param("spaceId") Integer spaceId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
