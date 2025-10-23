package com.example.TruyenAudio.Reponsitories;

import com.example.TruyenAudio.Entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChapterReponsitory extends JpaRepository<Chapter, Integer> {
    @Query ("""
select c from Chapter c join fetch c.BoTruyen b order by c.ChapterId desc""")
    List<Chapter> findLatestWithBoTruyen(org.springframework.data.domain.Pageable pageable);
}
