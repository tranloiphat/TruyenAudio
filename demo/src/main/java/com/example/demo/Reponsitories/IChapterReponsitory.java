package com.example.demo.Reponsitories;

import com.example.demo.Entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChapterReponsitory extends JpaRepository<Chapter, Integer> {

    @Query("""
        select distinct c
        from Chapter c
        join c.chiTietBoTruyens ct
        where ct.boTruyen.BoTruyenId = :boTruyenId
    """)
    List<Chapter> findByChapterId(@Param("chapterId") Integer chapterId);

    // Hoặc dùng tên method theo path (dài nhưng không cần @Query):
}
