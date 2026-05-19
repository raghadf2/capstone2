package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space,Integer> {
    Space findSpaceById(Integer id);
    Space findSpaceByName(String name);

    @Query("""
select distinct s
from Space s
where s.id in (
    select a.spaceId
    from Availability a
    where a.day = :day and a.isAvailable = true
)
and s.capacity >= :capacity
""")
    List<Space> findAvailableSpacesByDay(@Param("day") String day,@Param("capacity") Integer capacity);

    @Query("select s from Space s where s.city = :city and s.pricePerHour between :min and :max and s.status = 'ACTIVE'")
    List<Space> filterAdvanced(@Param("city") String city,@Param("min") Double min,@Param("max") Double max);

    @Query("""
select s
from Space s
where s.id in (
    select r.spaceId
    from Review r
    group by r.spaceId
    having avg(r.rating) >= 4
)
""")
    List<Space> topRatedSpaces();

    @Query("select b.spaceId from Booking b group by b.spaceId order by count(b.id) desc")
    List<Integer> mostBookedSpaces();
}
